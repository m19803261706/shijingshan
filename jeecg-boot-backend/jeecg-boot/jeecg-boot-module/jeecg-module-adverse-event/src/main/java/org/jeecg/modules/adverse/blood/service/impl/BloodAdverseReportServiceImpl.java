package org.jeecg.modules.adverse.blood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseRectify;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseReport;
import org.jeecg.modules.adverse.blood.mapper.BloodAdverseReportMapper;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseFlowService;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseRectifyService;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 输血使用不良事件报告主表 服务实现类
 * <p>
 * 实现输血使用不良事件报告的核心业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/79">Issue #79</a>
 */
@Slf4j
@Service
public class BloodAdverseReportServiceImpl extends ServiceImpl<BloodAdverseReportMapper, BloodAdverseReport>
        implements IBloodAdverseReportService {

    /**
     * 报告编号前缀
     */
    private static final String REPORT_CODE_PREFIX = "BLD";

    /**
     * 报告状态常量
     */
    private static final String STATUS_DRAFT = "draft";
    private static final String STATUS_PENDING_AUDIT = "pending_audit";
    private static final String STATUS_PENDING_PROCESS = "pending_process";
    private static final String STATUS_PENDING_RECTIFY = "pending_rectify";
    private static final String STATUS_RECTIFYING = "rectifying";
    private static final String STATUS_RETURNED = "returned";
    private static final String STATUS_CLOSED = "closed";

    /**
     * 结案方式常量
     */
    private static final String CLOSE_TYPE_DIRECT = "direct";
    private static final String CLOSE_TYPE_RECTIFY = "rectify";

    /**
     * 流转操作类型常量
     */
    private static final String ACTION_SUBMIT = "submit";
    private static final String ACTION_AUDIT_PASS = "audit_pass";
    private static final String ACTION_AUDIT_REJECT = "audit_reject";
    private static final String ACTION_CLOSE_DIRECT = "close_direct";
    private static final String ACTION_REQUIRE_RECTIFY = "require_rectify";
    private static final String ACTION_SUBMIT_RECTIFY = "submit_rectify";
    private static final String ACTION_CONFIRM_APPROVE = "confirm_approve";
    private static final String ACTION_CONFIRM_REJECT = "confirm_reject";

    @Autowired
    private IBloodAdverseFlowService flowService;

    @Autowired
    private IBloodAdverseRectifyService rectifyService;

    // ==================== 报告编号 ====================

    @Override
    public String generateReportCode() {
        // 获取当前日期，格式：yyyyMMdd
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = REPORT_CODE_PREFIX + dateStr;

        // 获取今日最大序号
        Integer maxSeq = baseMapper.getMaxReportCodeSeq(prefix);
        int nextSeq = (maxSeq == null) ? 1 : maxSeq + 1;

        // 生成报告编号：BLD + 日期 + 4位序号
        return String.format("%s%04d", prefix, nextSeq);
    }

    // ==================== CRUD 方法 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BloodAdverseReport saveReport(BloodAdverseReport report) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Date now = new Date();

        // 生成报告编号
        report.setReportCode(generateReportCode());
        report.setStatus(STATUS_DRAFT);
        report.setCreateBy(loginUser != null ? loginUser.getUsername() : null);
        report.setCreateTime(now);
        report.setSysOrgCode(loginUser != null ? loginUser.getOrgCode() : null);
        report.setDelFlag(0);

        // 保存报告
        this.save(report);

        log.info("新建输血使用不良事件报告，ID: {}, 编号: {}", report.getId(), report.getReportCode());
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BloodAdverseReport updateReport(BloodAdverseReport report) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        report.setUpdateBy(loginUser != null ? loginUser.getUsername() : null);
        report.setUpdateTime(new Date());

        // 更新报告
        this.updateById(report);

        log.info("更新输血使用不良事件报告，ID: {}", report.getId());
        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BloodAdverseReport saveDraft(BloodAdverseReport report) {
        if (report == null) {
            log.error("保存草稿失败：report 参数为空");
            throw new IllegalArgumentException("报告数据不能为空");
        }

        if (oConvertUtils.isEmpty(report.getId())) {
            // 新增草稿
            return saveReport(report);
        } else {
            // 更新草稿
            if (canEdit(report.getId())) {
                return updateReport(report);
            } else {
                log.error("报告不可编辑，ID: {}", report.getId());
                throw new IllegalStateException("报告当前状态不可编辑");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReport(String id) {
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            log.error("报告不存在，ID: {}", id);
            return false;
        }

        // 仅草稿状态可删除
        if (!STATUS_DRAFT.equals(report.getStatus())) {
            log.error("仅草稿状态可删除，当前状态: {}", report.getStatus());
            return false;
        }

        // 删除报告（逻辑删除）
        return this.removeById(id);
    }

    @Override
    public boolean canEdit(String id) {
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        String status = report.getStatus();
        return STATUS_DRAFT.equals(status) || STATUS_RETURNED.equals(status);
    }

    // ==================== 提交相关方法 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitReport(String id) {
        // 1. 获取报告
        BloodAdverseReport report = this.getById(id);
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
        Date now = new Date();
        report.setStatus(STATUS_PENDING_AUDIT);
        report.setUpdateTime(now);

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser != null) {
            report.setUpdateBy(loginUser.getUsername());
        }

        this.updateById(report);

        // 5. 记录流转
        String operatorId = loginUser != null ? loginUser.getId() : null;
        String operatorName = loginUser != null ? loginUser.getRealname() : null;
        flowService.recordFlow(id, ACTION_SUBMIT, currentStatus, STATUS_PENDING_AUDIT,
                operatorId, operatorName, "提交报告");

        log.info("输血使用不良事件报告提交成功，ID: {}, 编号: {}", id, report.getReportCode());
        return true;
    }

    /**
     * 校验必填字段
     *
     * @param report 报告信息
     * @return 是否校验通过
     */
    private boolean validateRequiredFields(BloodAdverseReport report) {
        // 校验患者姓名
        if (oConvertUtils.isEmpty(report.getPatientName())) {
            log.warn("患者姓名不能为空");
            return false;
        }
        // 校验不良事件名称
        if (oConvertUtils.isEmpty(report.getEventName())) {
            log.warn("不良事件名称不能为空");
            return false;
        }
        // 校验事件描述
        if (oConvertUtils.isEmpty(report.getEventDescription())) {
            log.warn("事件描述不能为空");
            return false;
        }
        return true;
    }

    // ==================== 审核相关方法 ====================

    @Override
    public boolean canAudit(String id) {
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        return STATUS_PENDING_AUDIT.equals(report.getStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditPass(String id, String auditUserId, String auditUserName, String comment) {
        // 1. 获取报告
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            log.error("报告不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态
        String oldStatus = report.getStatus();
        if (!STATUS_PENDING_AUDIT.equals(oldStatus)) {
            log.error("报告状态不允许审核，当前状态: {}", oldStatus);
            return false;
        }

        // 3. 更新报告状态和审核信息
        Date now = new Date();
        report.setStatus(STATUS_PENDING_PROCESS);
        report.setAuditUserId(auditUserId);
        report.setAuditUserName(auditUserName);
        report.setAuditTime(now);
        report.setAuditComment(comment);
        report.setUpdateBy(auditUserName);
        report.setUpdateTime(now);

        this.updateById(report);

        // 4. 记录流转历史
        flowService.recordFlow(id, ACTION_AUDIT_PASS, oldStatus, STATUS_PENDING_PROCESS,
                auditUserId, auditUserName, comment);

        log.info("输血使用不良事件报告审核通过，ID: {}, 编号: {}, 审核人: {}",
                id, report.getReportCode(), auditUserName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditReject(String id, String auditUserId, String auditUserName, String comment) {
        // 1. 获取报告
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            log.error("报告不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态
        String oldStatus = report.getStatus();
        if (!STATUS_PENDING_AUDIT.equals(oldStatus)) {
            log.error("报告状态不允许审核，当前状态: {}", oldStatus);
            return false;
        }

        // 3. 更新报告状态和审核信息
        Date now = new Date();
        report.setStatus(STATUS_RETURNED);
        report.setAuditUserId(auditUserId);
        report.setAuditUserName(auditUserName);
        report.setAuditTime(now);
        report.setAuditComment(comment);
        report.setUpdateBy(auditUserName);
        report.setUpdateTime(now);

        this.updateById(report);

        // 4. 记录流转历史
        flowService.recordFlow(id, ACTION_AUDIT_REJECT, oldStatus, STATUS_RETURNED,
                auditUserId, auditUserName, comment);

        log.info("输血使用不良事件报告审核退回，ID: {}, 编号: {}, 审核人: {}, 原因: {}",
                id, report.getReportCode(), auditUserName, comment);
        return true;
    }

    // ==================== 处理相关方法 ====================

    @Override
    public boolean canProcess(String id) {
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        return STATUS_PENDING_PROCESS.equals(report.getStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeDirect(String id, String closeUserId, String closeUserName, String comment) {
        // 1. 获取报告
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            log.error("报告不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态
        String oldStatus = report.getStatus();
        if (!STATUS_PENDING_PROCESS.equals(oldStatus)) {
            log.error("报告状态不允许直接结案，当前状态: {}", oldStatus);
            return false;
        }

        // 3. 更新报告状态和结案信息
        Date now = new Date();
        report.setStatus(STATUS_CLOSED);
        report.setCloseType(CLOSE_TYPE_DIRECT);
        report.setCloseUserId(closeUserId);
        report.setCloseUserName(closeUserName);
        report.setCloseTime(now);
        report.setCloseComment(comment);
        report.setUpdateBy(closeUserName);
        report.setUpdateTime(now);

        this.updateById(report);

        // 4. 记录流转历史
        flowService.recordFlow(id, ACTION_CLOSE_DIRECT, oldStatus, STATUS_CLOSED,
                closeUserId, closeUserName, comment);

        log.info("输血使用不良事件报告直接结案，ID: {}, 编号: {}, 结案人: {}",
                id, report.getReportCode(), closeUserName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean requireRectify(String id, String requirement, Date deadline,
                                   String operatorId, String operatorName) {
        // 1. 获取报告
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            log.error("报告不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态
        String oldStatus = report.getStatus();
        if (!STATUS_PENDING_PROCESS.equals(oldStatus)) {
            log.error("报告状态不允许要求整改，当前状态: {}", oldStatus);
            return false;
        }

        // 3. 创建整改记录
        rectifyService.createRectify(id, requirement, deadline, operatorId, operatorName);

        // 4. 更新报告状态
        Date now = new Date();
        report.setStatus(STATUS_PENDING_RECTIFY);
        report.setUpdateBy(operatorName);
        report.setUpdateTime(now);

        this.updateById(report);

        // 5. 记录流转历史
        flowService.recordFlow(id, ACTION_REQUIRE_RECTIFY, oldStatus, STATUS_PENDING_RECTIFY,
                operatorId, operatorName, requirement);

        log.info("输血使用不良事件报告要求整改，ID: {}, 编号: {}, 操作人: {}",
                id, report.getReportCode(), operatorName);
        return true;
    }

    // ==================== 整改相关方法 ====================

    @Override
    public boolean canSubmitRectify(String id) {
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        String status = report.getStatus();
        // 待整改或整改中状态，且有待整改的整改记录
        if (STATUS_PENDING_RECTIFY.equals(status) || STATUS_RECTIFYING.equals(status)) {
            BloodAdverseRectify latestRectify = rectifyService.getLatestByReportId(id);
            return latestRectify != null && IBloodAdverseRectifyService.STATUS_PENDING.equals(latestRectify.getStatus());
        }
        return false;
    }

    @Override
    public boolean canConfirmRectify(String id) {
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        // 整改中状态，且有已提交的整改记录
        if (STATUS_RECTIFYING.equals(report.getStatus())) {
            BloodAdverseRectify latestRectify = rectifyService.getLatestByReportId(id);
            return latestRectify != null && IBloodAdverseRectifyService.STATUS_SUBMITTED.equals(latestRectify.getStatus());
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmRectifyAndClose(String id, String confirmUserId, String confirmUserName, String confirmComment) {
        // 1. 获取报告
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            log.error("报告不存在，ID: {}", id);
            return false;
        }

        // 2. 获取最新整改记录
        BloodAdverseRectify latestRectify = rectifyService.getLatestByReportId(id);
        if (latestRectify == null) {
            log.error("未找到整改记录，报告ID: {}", id);
            return false;
        }

        // 3. 确认整改通过
        if (!rectifyService.confirmApprove(latestRectify.getId(), confirmUserId, confirmUserName, confirmComment)) {
            return false;
        }

        // 4. 更新报告状态为已结案
        String oldStatus = report.getStatus();
        Date now = new Date();
        report.setStatus(STATUS_CLOSED);
        report.setCloseType(CLOSE_TYPE_RECTIFY);
        report.setCloseUserId(confirmUserId);
        report.setCloseUserName(confirmUserName);
        report.setCloseTime(now);
        report.setCloseComment(confirmComment);
        report.setUpdateBy(confirmUserName);
        report.setUpdateTime(now);

        this.updateById(report);

        // 5. 记录流转历史
        flowService.recordFlow(id, ACTION_CONFIRM_APPROVE, oldStatus, STATUS_CLOSED,
                confirmUserId, confirmUserName, confirmComment);

        log.info("输血使用不良事件报告整改确认通过并结案，ID: {}, 编号: {}, 确认人: {}",
                id, report.getReportCode(), confirmUserName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmRectifyReject(String id, String confirmUserId, String confirmUserName, String confirmComment) {
        // 1. 获取报告
        BloodAdverseReport report = this.getById(id);
        if (report == null) {
            log.error("报告不存在，ID: {}", id);
            return false;
        }

        // 2. 获取最新整改记录
        BloodAdverseRectify latestRectify = rectifyService.getLatestByReportId(id);
        if (latestRectify == null) {
            log.error("未找到整改记录，报告ID: {}", id);
            return false;
        }

        // 3. 退回整改（会自动创建下一轮整改记录）
        if (!rectifyService.confirmReject(latestRectify.getId(), confirmUserId, confirmUserName, confirmComment, null)) {
            return false;
        }

        // 4. 更新报告状态为待整改（重新开始整改流程）
        String oldStatus = report.getStatus();
        Date now = new Date();
        report.setStatus(STATUS_PENDING_RECTIFY);
        report.setUpdateBy(confirmUserName);
        report.setUpdateTime(now);

        this.updateById(report);

        // 5. 记录流转历史
        flowService.recordFlow(id, ACTION_CONFIRM_REJECT, oldStatus, STATUS_PENDING_RECTIFY,
                confirmUserId, confirmUserName, confirmComment);

        log.info("输血使用不良事件报告整改被退回，ID: {}, 编号: {}, 确认人: {}, 原因: {}",
                id, report.getReportCode(), confirmUserName, confirmComment);
        return true;
    }
}
