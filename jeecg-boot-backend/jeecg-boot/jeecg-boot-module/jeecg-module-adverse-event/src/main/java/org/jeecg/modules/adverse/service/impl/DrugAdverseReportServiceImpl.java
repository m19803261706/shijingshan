package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.adverse.entity.DrugAdverseConcomitantDrug;
import org.jeecg.modules.adverse.entity.DrugAdverseReport;
import org.jeecg.modules.adverse.entity.DrugAdverseSuspectDrug;
import org.jeecg.modules.adverse.mapper.DrugAdverseReportMapper;
import org.jeecg.modules.adverse.service.IDrugAdverseConcomitantDrugService;
import org.jeecg.modules.adverse.service.IDrugAdverseReportService;
import org.jeecg.modules.adverse.service.IDrugAdverseSuspectDrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 药品不良反应报告主表 服务实现类
 * <p>
 * 实现药品不良反应报告的核心业务逻辑，支持主子表联动
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Slf4j
@Service
public class DrugAdverseReportServiceImpl extends ServiceImpl<DrugAdverseReportMapper, DrugAdverseReport>
        implements IDrugAdverseReportService {

    /**
     * 报告编号前缀
     */
    private static final String REPORT_CODE_PREFIX = "ADR";

    /**
     * 报告状态常量
     */
    private static final String STATUS_DRAFT = "draft";
    private static final String STATUS_PENDING_AUDIT = "pending_audit";
    private static final String STATUS_RETURNED = "returned";

    @Autowired
    private IDrugAdverseSuspectDrugService suspectDrugService;

    @Autowired
    private IDrugAdverseConcomitantDrugService concomitantDrugService;

    @Override
    public String generateReportCode() {
        // 获取当前日期，格式：yyyyMMdd
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = REPORT_CODE_PREFIX + dateStr;

        // 获取今日最大序号
        Integer maxSeq = baseMapper.getMaxReportCodeSeq(prefix);
        int nextSeq = (maxSeq == null) ? 1 : maxSeq + 1;

        // 生成报告编号：ADR + 日期 + 4位序号
        return String.format("%s%04d", prefix, nextSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DrugAdverseReport saveReportWithDetails(DrugAdverseReport report,
                                                    List<DrugAdverseSuspectDrug> suspectDrugs,
                                                    List<DrugAdverseConcomitantDrug> concomitantDrugs) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Date now = new Date();

        // 生成报告编号
        report.setReportCode(generateReportCode());
        report.setStatus(STATUS_DRAFT);
        report.setCreateBy(loginUser != null ? loginUser.getUsername() : null);
        report.setCreateTime(now);
        report.setDepartmentId(loginUser != null ? loginUser.getOrgCode() : null);

        // 保存主表
        this.save(report);
        String reportId = report.getId();

        // 保存怀疑药品子表
        suspectDrugService.saveBatchByReportId(reportId, suspectDrugs);

        // 保存并用药品子表
        concomitantDrugService.saveBatchByReportId(reportId, concomitantDrugs);

        log.info("新建药品不良反应报告，ID: {}, 编号: {}", reportId, report.getReportCode());
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DrugAdverseReport updateReportWithDetails(DrugAdverseReport report,
                                                      List<DrugAdverseSuspectDrug> suspectDrugs,
                                                      List<DrugAdverseConcomitantDrug> concomitantDrugs) {
        String reportId = report.getId();

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        report.setUpdateBy(loginUser != null ? loginUser.getUsername() : null);
        report.setUpdateTime(new Date());

        // 更新主表
        this.updateById(report);

        // 删除原有子表数据
        suspectDrugService.deleteByReportId(reportId);
        concomitantDrugService.deleteByReportId(reportId);

        // 重新保存子表数据
        suspectDrugService.saveBatchByReportId(reportId, suspectDrugs);
        concomitantDrugService.saveBatchByReportId(reportId, concomitantDrugs);

        log.info("更新药品不良反应报告，ID: {}", reportId);
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DrugAdverseReport saveDraft(DrugAdverseReport report,
                                        List<DrugAdverseSuspectDrug> suspectDrugs,
                                        List<DrugAdverseConcomitantDrug> concomitantDrugs) {
        if (oConvertUtils.isEmpty(report.getId())) {
            // 新增草稿
            return saveReportWithDetails(report, suspectDrugs, concomitantDrugs);
        } else {
            // 更新草稿
            if (canEdit(report.getId())) {
                return updateReportWithDetails(report, suspectDrugs, concomitantDrugs);
            } else {
                log.error("报告不可编辑，ID: {}", report.getId());
                return null;
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitReport(String id) {
        // 1. 获取报告
        DrugAdverseReport report = this.getById(id);
        if (report == null) {
            log.error("报告不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态（仅 draft 和 returned 状态可提交）
        String currentStatus = report.getStatus();
        if (!STATUS_DRAFT.equals(currentStatus) && !STATUS_RETURNED.equals(currentStatus)) {
            log.error("报告状态不允许提交，当前状态: {}", currentStatus);
            return false;
        }

        // 3. 校验必填字段
        if (!validateRequiredFields(report)) {
            log.error("报告必填字段校验失败，ID: {}", id);
            return false;
        }

        // 4. 更新报告状态
        report.setStatus(STATUS_PENDING_AUDIT);
        report.setUpdateTime(new Date());

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser != null) {
            report.setUpdateBy(loginUser.getUsername());
        }

        this.updateById(report);

        log.info("药品不良反应报告提交成功，ID: {}, 编号: {}", id, report.getReportCode());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReportWithDetails(String id) {
        DrugAdverseReport report = this.getById(id);
        if (report == null) {
            log.error("报告不存在，ID: {}", id);
            return false;
        }

        // 仅草稿状态可删除
        if (!STATUS_DRAFT.equals(report.getStatus())) {
            log.error("仅草稿状态可删除，当前状态: {}", report.getStatus());
            return false;
        }

        // 删除子表数据（由于设置了外键级联删除，实际上会自动删除）
        suspectDrugService.deleteByReportId(id);
        concomitantDrugService.deleteByReportId(id);

        // 删除主表（逻辑删除）
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatchWithDetails(List<String> ids) {
        int successCount = 0;
        for (String id : ids) {
            if (deleteReportWithDetails(id)) {
                successCount++;
            }
        }
        return successCount;
    }

    @Override
    public boolean canEdit(String id) {
        DrugAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        String status = report.getStatus();
        return STATUS_DRAFT.equals(status) || STATUS_RETURNED.equals(status);
    }

    @Override
    public DrugAdverseReport getReportWithDetails(String id) {
        // 这里返回主表数据，子表数据通过单独的方法获取
        // 前端可以通过 suspectDrugs 和 concomitantDrugs 两个接口获取子表数据
        return this.getById(id);
    }

    @Override
    public List<DrugAdverseSuspectDrug> getSuspectDrugsByReportId(String reportId) {
        return suspectDrugService.selectByReportId(reportId);
    }

    @Override
    public List<DrugAdverseConcomitantDrug> getConcomitantDrugsByReportId(String reportId) {
        return concomitantDrugService.selectByReportId(reportId);
    }

    /**
     * 校验必填字段
     *
     * @param report 报告信息
     * @return 是否校验通过
     */
    private boolean validateRequiredFields(DrugAdverseReport report) {
        // 校验报告类型
        if (oConvertUtils.isEmpty(report.getReportType())) {
            log.warn("报告类型不能为空");
            return false;
        }
        // 校验严重程度
        if (oConvertUtils.isEmpty(report.getSeverityType())) {
            log.warn("严重程度不能为空");
            return false;
        }
        // 校验不良反应名称
        if (oConvertUtils.isEmpty(report.getReactionName())) {
            log.warn("不良反应名称不能为空");
            return false;
        }
        // 校验不良反应发生时间
        if (report.getReactionTime() == null) {
            log.warn("不良反应发生时间不能为空");
            return false;
        }
        return true;
    }
}
