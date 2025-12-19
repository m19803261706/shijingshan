package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.adverse.entity.DeviceAdverseFlow;
import org.jeecg.modules.adverse.entity.DeviceAdverseReport;
import org.jeecg.modules.adverse.mapper.DeviceAdverseReportMapper;
import org.jeecg.modules.adverse.service.IDeviceAdverseFlowService;
import org.jeecg.modules.adverse.service.IDeviceAdverseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 医疗器械不良事件报告主表 服务实现类
 * <p>
 * 实现医疗器械不良事件报告的核心业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/61">Issue #61</a>
 */
@Slf4j
@Service
public class DeviceAdverseReportServiceImpl extends ServiceImpl<DeviceAdverseReportMapper, DeviceAdverseReport>
        implements IDeviceAdverseReportService {

    /**
     * 报告编号前缀（Medical Device Event）
     */
    private static final String REPORT_CODE_PREFIX = "MDE";

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
     * 流转操作类型常量
     */
    private static final String ACTION_SUBMIT = "submit";
    private static final String ACTION_AUDIT_PASS = "audit_pass";
    private static final String ACTION_AUDIT_REJECT = "audit_reject";
    private static final String ACTION_RESUBMIT = "resubmit";

    @Autowired(required = false)
    private IDeviceAdverseFlowService flowService;

    @Override
    public String generateReportCode() {
        // 获取当前日期，格式：yyyyMMdd
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = REPORT_CODE_PREFIX + dateStr;

        // 获取今日最大序号
        Integer maxSeq = baseMapper.getMaxReportCodeSeq(prefix);
        int nextSeq = (maxSeq == null) ? 1 : maxSeq + 1;

        // 生成报告编号：MDE + 日期 + 4位序号
        return String.format("%s%04d", prefix, nextSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceAdverseReport saveDraft(DeviceAdverseReport report) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Date now = new Date();

        if (oConvertUtils.isEmpty(report.getId())) {
            // 新建
            report.setReportCode(generateReportCode());
            report.setStatus(STATUS_DRAFT);
            report.setCreateBy(loginUser != null ? loginUser.getUsername() : null);
            report.setCreateTime(now);
            report.setDepartmentId(loginUser != null ? loginUser.getOrgCode() : null);
            this.save(report);
            log.info("新建医疗器械不良事件报告草稿，ID: {}, 编号: {}", report.getId(), report.getReportCode());
        } else {
            // 更新
            report.setUpdateBy(loginUser != null ? loginUser.getUsername() : null);
            report.setUpdateTime(now);
            this.updateById(report);
            log.info("更新医疗器械不良事件报告草稿，ID: {}", report.getId());
        }

        return report;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitReport(String id) {
        DeviceAdverseReport report = this.getById(id);
        if (report == null) {
            log.warn("提交失败：报告不存在，ID: {}", id);
            return false;
        }

        // 校验状态
        String currentStatus = report.getStatus();
        if (!STATUS_DRAFT.equals(currentStatus) && !STATUS_RETURNED.equals(currentStatus)) {
            log.warn("提交失败：报告状态不允许提交，ID: {}, 当前状态: {}", id, currentStatus);
            return false;
        }

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Date now = new Date();

        // 更新状态
        report.setStatus(STATUS_PENDING_AUDIT);
        report.setUpdateBy(loginUser != null ? loginUser.getUsername() : null);
        report.setUpdateTime(now);
        this.updateById(report);

        // 记录流转历史
        String action = STATUS_DRAFT.equals(currentStatus) ? ACTION_SUBMIT : ACTION_RESUBMIT;
        recordFlow(id, action, currentStatus, STATUS_PENDING_AUDIT, loginUser, null);

        log.info("医疗器械不良事件报告提交成功，ID: {}", id);
        return true;
    }

    @Override
    public boolean canEdit(String id) {
        DeviceAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        String status = report.getStatus();
        return STATUS_DRAFT.equals(status) || STATUS_RETURNED.equals(status);
    }

    @Override
    public boolean canAudit(String id) {
        DeviceAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        return STATUS_PENDING_AUDIT.equals(report.getStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditPass(String id, String auditUserId, String auditUserName, String comment) {
        DeviceAdverseReport report = this.getById(id);
        if (report == null) {
            log.warn("审核通过失败：报告不存在，ID: {}", id);
            return false;
        }

        if (!STATUS_PENDING_AUDIT.equals(report.getStatus())) {
            log.warn("审核通过失败：报告状态不允许审核，ID: {}, 当前状态: {}", id, report.getStatus());
            return false;
        }

        Date now = new Date();
        String fromStatus = report.getStatus();

        // 更新报告状态和审核信息
        report.setStatus(STATUS_PENDING_PROCESS);
        report.setAuditUserId(auditUserId);
        report.setAuditUserName(auditUserName);
        report.setAuditTime(now);
        report.setAuditComment(comment);
        report.setUpdateBy(auditUserName);
        report.setUpdateTime(now);
        this.updateById(report);

        // 记录流转历史
        LoginUser loginUser = new LoginUser();
        loginUser.setId(auditUserId);
        loginUser.setRealname(auditUserName);
        recordFlow(id, ACTION_AUDIT_PASS, fromStatus, STATUS_PENDING_PROCESS, loginUser, comment);

        log.info("医疗器械不良事件报告审核通过，ID: {}, 审核人: {}", id, auditUserName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditReject(String id, String auditUserId, String auditUserName, String comment) {
        DeviceAdverseReport report = this.getById(id);
        if (report == null) {
            log.warn("审核退回失败：报告不存在，ID: {}", id);
            return false;
        }

        if (!STATUS_PENDING_AUDIT.equals(report.getStatus())) {
            log.warn("审核退回失败：报告状态不允许审核，ID: {}, 当前状态: {}", id, report.getStatus());
            return false;
        }

        if (oConvertUtils.isEmpty(comment)) {
            log.warn("审核退回失败：退回原因不能为空，ID: {}", id);
            return false;
        }

        Date now = new Date();
        String fromStatus = report.getStatus();

        // 更新报告状态和审核信息
        report.setStatus(STATUS_RETURNED);
        report.setAuditUserId(auditUserId);
        report.setAuditUserName(auditUserName);
        report.setAuditTime(now);
        report.setAuditComment(comment);
        report.setUpdateBy(auditUserName);
        report.setUpdateTime(now);
        this.updateById(report);

        // 记录流转历史
        LoginUser loginUser = new LoginUser();
        loginUser.setId(auditUserId);
        loginUser.setRealname(auditUserName);
        recordFlow(id, ACTION_AUDIT_REJECT, fromStatus, STATUS_RETURNED, loginUser, comment);

        log.info("医疗器械不良事件报告审核退回，ID: {}, 审核人: {}, 原因: {}", id, auditUserName, comment);
        return true;
    }

    @Override
    public boolean canProcess(String id) {
        DeviceAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        return STATUS_PENDING_PROCESS.equals(report.getStatus());
    }

    @Override
    public boolean canSubmitRectify(String id) {
        DeviceAdverseReport report = this.getById(id);
        if (report == null) {
            return false;
        }
        String status = report.getStatus();
        return STATUS_PENDING_RECTIFY.equals(status) || STATUS_RECTIFYING.equals(status);
    }

    /**
     * 记录流转历史
     *
     * @param reportId   报告ID
     * @param action     操作类型
     * @param fromStatus 操作前状态
     * @param toStatus   操作后状态
     * @param loginUser  操作人
     * @param comment    备注
     */
    private void recordFlow(String reportId, String action, String fromStatus, String toStatus,
                            LoginUser loginUser, String comment) {
        if (flowService == null) {
            log.debug("DeviceAdverseFlowService 未注入，跳过流转记录");
            return;
        }

        DeviceAdverseFlow flow = new DeviceAdverseFlow();
        flow.setReportId(reportId);
        flow.setAction(action);
        flow.setFromStatus(fromStatus);
        flow.setToStatus(toStatus);
        flow.setOperatorId(loginUser != null ? loginUser.getId() : null);
        flow.setOperatorName(loginUser != null ? loginUser.getRealname() : null);
        flow.setComment(comment);
        flow.setCreateTime(new Date());

        flowService.save(flow);
    }
}
