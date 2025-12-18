package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.adverse.entity.AdverseEvent;
import org.jeecg.modules.adverse.mapper.AdverseEventMapper;
import org.jeecg.modules.adverse.entity.AdverseEventRectify;
import org.jeecg.modules.adverse.service.IAdverseEventFlowService;
import org.jeecg.modules.adverse.service.IAdverseEventRectifyService;
import org.jeecg.modules.adverse.service.IAdverseEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 不良事件主表 服务实现类
 * <p>
 * 实现不良事件的核心业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Service
public class AdverseEventServiceImpl extends ServiceImpl<AdverseEventMapper, AdverseEvent> implements IAdverseEventService {

    /**
     * 事件编号前缀
     */
    private static final String EVENT_CODE_PREFIX = "AE";

    /**
     * 事件状态常量
     */
    private static final String STATUS_DRAFT = "draft";
    private static final String STATUS_PENDING_AUDIT = "pending_audit";
    private static final String STATUS_RETURNED = "returned";
    private static final String STATUS_PENDING_PROCESS = "pending_process";
    private static final String STATUS_PENDING_RECTIFY = "pending_rectify";
    private static final String STATUS_RECTIFYING = "rectifying";
    private static final String STATUS_CLOSED = "closed";

    /**
     * 整改状态常量
     */
    private static final String RECTIFY_STATUS_SUBMITTED = "submitted";

    @Autowired
    private IAdverseEventFlowService flowService;

    @Autowired
    private IAdverseEventRectifyService rectifyService;

    @Override
    public String generateEventCode() {
        // 获取当前日期，格式：yyyyMMdd
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = EVENT_CODE_PREFIX + dateStr;

        // 获取今日最大序号
        Integer maxSeq = baseMapper.getMaxEventCodeSeq(prefix);
        int nextSeq = (maxSeq == null) ? 1 : maxSeq + 1;

        // 生成事件编号：AE + 日期 + 4位序号
        return String.format("%s%04d", prefix, nextSeq);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitEvent(String id) {
        // 1. 获取事件
        AdverseEvent event = this.getById(id);
        if (event == null) {
            log.error("事件不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态（仅 draft 和 returned 状态可提交）
        String currentStatus = event.getStatus();
        if (!STATUS_DRAFT.equals(currentStatus) && !STATUS_RETURNED.equals(currentStatus)) {
            log.error("事件状态不允许提交，当前状态: {}", currentStatus);
            return false;
        }

        // 3. 校验必填字段
        if (!validateRequiredFields(event)) {
            log.error("事件必填字段校验失败，ID: {}", id);
            return false;
        }

        // 4. 更新事件状态
        event.setStatus(STATUS_PENDING_AUDIT);
        event.setSubmitTime(new Date());
        this.updateById(event);

        // 5. 记录流转日志
        flowService.logFlow(id, currentStatus, STATUS_PENDING_AUDIT, "submit", "提交上报");

        log.info("事件提交成功，ID: {}, 编号: {}", id, event.getEventCode());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdverseEvent saveDraft(AdverseEvent event) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        if (oConvertUtils.isEmpty(event.getId())) {
            // 新增草稿
            event.setEventCode(generateEventCode());
            event.setStatus(STATUS_DRAFT);
            event.setReporterId(loginUser.getId());
            event.setReporterName(loginUser.getRealname());
            event.setDepartmentId(loginUser.getOrgCode());
            event.setCreateBy(loginUser.getUsername());
            event.setCreateTime(new Date());
            this.save(event);
            log.info("新建事件草稿，ID: {}, 编号: {}", event.getId(), event.getEventCode());
        } else {
            // 更新草稿
            AdverseEvent existEvent = this.getById(event.getId());
            if (existEvent != null && canEdit(event.getId())) {
                event.setUpdateBy(loginUser.getUsername());
                event.setUpdateTime(new Date());
                this.updateById(event);
                log.info("更新事件草稿，ID: {}", event.getId());
            }
        }

        return event;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEvent(String id) {
        AdverseEvent event = this.getById(id);
        if (event == null) {
            log.error("事件不存在，ID: {}", id);
            return false;
        }

        // 仅草稿状态可删除
        if (!STATUS_DRAFT.equals(event.getStatus())) {
            log.error("仅草稿状态可删除，当前状态: {}", event.getStatus());
            return false;
        }

        // 逻辑删除
        return this.removeById(id);
    }

    @Override
    public boolean canEdit(String id) {
        AdverseEvent event = this.getById(id);
        if (event == null) {
            return false;
        }
        String status = event.getStatus();
        return STATUS_DRAFT.equals(status) || STATUS_RETURNED.equals(status);
    }

    /**
     * 校验必填字段
     *
     * @param event 事件信息
     * @return 是否校验通过
     */
    private boolean validateRequiredFields(AdverseEvent event) {
        // 校验事件标题
        if (oConvertUtils.isEmpty(event.getTitle())) {
            log.warn("事件标题不能为空");
            return false;
        }
        // 校验事件分类
        if (oConvertUtils.isEmpty(event.getCategoryId())) {
            log.warn("事件分类不能为空");
            return false;
        }
        // 校验事件级别
        if (oConvertUtils.isEmpty(event.getLevel())) {
            log.warn("事件级别不能为空");
            return false;
        }
        // 校验发生时间
        if (event.getOccurTime() == null) {
            log.warn("发生时间不能为空");
            return false;
        }
        // 校验事件经过
        if (oConvertUtils.isEmpty(event.getDescription())) {
            log.warn("事件经过不能为空");
            return false;
        }
        return true;
    }

    // ==================== 科室审核相关方法实现 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditPass(String id, String comment) {
        // 1. 获取事件
        AdverseEvent event = this.getById(id);
        if (event == null) {
            log.error("事件不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态（仅待审核状态可审核）
        String currentStatus = event.getStatus();
        if (!STATUS_PENDING_AUDIT.equals(currentStatus)) {
            log.error("事件状态不允许审核，当前状态: {}", currentStatus);
            return false;
        }

        // 3. 更新事件状态为待处理
        event.setStatus(STATUS_PENDING_PROCESS);
        event.setUpdateTime(new Date());
        // 获取当前登录用户设置更新人
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser != null) {
            event.setUpdateBy(loginUser.getUsername());
        }
        this.updateById(event);

        // 4. 记录流转日志
        String auditComment = oConvertUtils.isEmpty(comment) ? "审核通过" : comment;
        flowService.logFlow(id, currentStatus, STATUS_PENDING_PROCESS, "approve", auditComment);

        log.info("事件审核通过，ID: {}, 编号: {}", id, event.getEventCode());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditReject(String id, String comment) {
        // 1. 获取事件
        AdverseEvent event = this.getById(id);
        if (event == null) {
            log.error("事件不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态（仅待审核状态可审核）
        String currentStatus = event.getStatus();
        if (!STATUS_PENDING_AUDIT.equals(currentStatus)) {
            log.error("事件状态不允许审核，当前状态: {}", currentStatus);
            return false;
        }

        // 3. 校验退回原因（必填）
        if (oConvertUtils.isEmpty(comment)) {
            log.error("退回原因不能为空");
            return false;
        }

        // 4. 更新事件状态为已退回
        event.setStatus(STATUS_RETURNED);
        event.setUpdateTime(new Date());
        // 获取当前登录用户设置更新人
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser != null) {
            event.setUpdateBy(loginUser.getUsername());
        }
        this.updateById(event);

        // 5. 记录流转日志
        flowService.logFlow(id, currentStatus, STATUS_RETURNED, "reject", comment);

        log.info("事件审核退回，ID: {}, 编号: {}, 原因: {}", id, event.getEventCode(), comment);
        return true;
    }

    @Override
    public boolean canAudit(String id) {
        AdverseEvent event = this.getById(id);
        if (event == null) {
            return false;
        }
        return STATUS_PENDING_AUDIT.equals(event.getStatus());
    }

    // ==================== 职能处理相关方法实现 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean requireRectify(String id, String requirement, Date deadline) {
        // 1. 获取事件
        AdverseEvent event = this.getById(id);
        if (event == null) {
            log.error("事件不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态（仅待处理状态可要求整改）
        String currentStatus = event.getStatus();
        if (!STATUS_PENDING_PROCESS.equals(currentStatus)) {
            log.error("事件状态不允许要求整改，当前状态: {}", currentStatus);
            return false;
        }

        // 3. 校验必填参数
        if (oConvertUtils.isEmpty(requirement)) {
            log.error("整改要求不能为空");
            return false;
        }
        if (deadline == null) {
            log.error("整改期限不能为空");
            return false;
        }

        // 4. 创建整改记录
        rectifyService.createRectifyRequirement(id, requirement, deadline);

        // 5. 更新事件状态为待整改
        event.setStatus(STATUS_PENDING_RECTIFY);
        event.setUpdateTime(new Date());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser != null) {
            event.setUpdateBy(loginUser.getUsername());
            event.setCurrentHandlerId(loginUser.getId());
            event.setCurrentHandlerDept(loginUser.getOrgCode());
        }
        this.updateById(event);

        // 6. 记录流转日志
        flowService.logFlow(id, currentStatus, STATUS_PENDING_RECTIFY, "require_rectify", "要求整改: " + requirement);

        log.info("要求整改，事件ID: {}, 编号: {}", id, event.getEventCode());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean closeDirectly(String id, String comment) {
        // 1. 获取事件
        AdverseEvent event = this.getById(id);
        if (event == null) {
            log.error("事件不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态（仅待处理状态可直接结案）
        String currentStatus = event.getStatus();
        if (!STATUS_PENDING_PROCESS.equals(currentStatus)) {
            log.error("事件状态不允许直接结案，当前状态: {}", currentStatus);
            return false;
        }

        // 3. 更新事件状态为已结案
        event.setStatus(STATUS_CLOSED);
        event.setCloseTime(new Date());
        event.setUpdateTime(new Date());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser != null) {
            event.setUpdateBy(loginUser.getUsername());
            event.setCurrentHandlerId(loginUser.getId());
            event.setCurrentHandlerDept(loginUser.getOrgCode());
        }
        this.updateById(event);

        // 4. 记录流转日志
        String closeComment = oConvertUtils.isEmpty(comment) ? "直接结案" : comment;
        flowService.logFlow(id, currentStatus, STATUS_CLOSED, "close_direct", closeComment);

        log.info("直接结案，事件ID: {}, 编号: {}", id, event.getEventCode());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmRectify(String id, String comment) {
        // 1. 获取事件
        AdverseEvent event = this.getById(id);
        if (event == null) {
            log.error("事件不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态（仅整改中状态可确认）
        String currentStatus = event.getStatus();
        if (!STATUS_RECTIFYING.equals(currentStatus)) {
            log.error("事件状态不允许确认整改，当前状态: {}", currentStatus);
            return false;
        }

        // 3. 获取当前整改记录并审核通过
        AdverseEventRectify rectify = rectifyService.getCurrentRectify(id);
        if (rectify == null || !RECTIFY_STATUS_SUBMITTED.equals(rectify.getStatus())) {
            log.error("没有待审核的整改记录");
            return false;
        }
        rectifyService.approveRectify(rectify.getId(), comment);

        // 4. 更新事件状态为已结案
        event.setStatus(STATUS_CLOSED);
        event.setCloseTime(new Date());
        event.setUpdateTime(new Date());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser != null) {
            event.setUpdateBy(loginUser.getUsername());
        }
        this.updateById(event);

        // 5. 记录流转日志
        String confirmComment = oConvertUtils.isEmpty(comment) ? "整改确认通过" : comment;
        flowService.logFlow(id, currentStatus, STATUS_CLOSED, "confirm_rectify", confirmComment);

        log.info("确认整改完成，事件ID: {}, 编号: {}", id, event.getEventCode());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectRectifyResult(String id, String comment) {
        // 1. 获取事件
        AdverseEvent event = this.getById(id);
        if (event == null) {
            log.error("事件不存在，ID: {}", id);
            return false;
        }

        // 2. 校验状态（仅整改中状态可退回）
        String currentStatus = event.getStatus();
        if (!STATUS_RECTIFYING.equals(currentStatus)) {
            log.error("事件状态不允许退回整改，当前状态: {}", currentStatus);
            return false;
        }

        // 3. 校验退回原因（必填）
        if (oConvertUtils.isEmpty(comment)) {
            log.error("退回原因不能为空");
            return false;
        }

        // 4. 获取当前整改记录并退回
        AdverseEventRectify rectify = rectifyService.getCurrentRectify(id);
        if (rectify == null || !RECTIFY_STATUS_SUBMITTED.equals(rectify.getStatus())) {
            log.error("没有待审核的整改记录");
            return false;
        }
        rectifyService.rejectRectify(rectify.getId(), comment);

        // 5. 更新事件状态为待整改（需重新整改）
        event.setStatus(STATUS_PENDING_RECTIFY);
        event.setUpdateTime(new Date());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser != null) {
            event.setUpdateBy(loginUser.getUsername());
        }
        this.updateById(event);

        // 6. 记录流转日志
        flowService.logFlow(id, currentStatus, STATUS_PENDING_RECTIFY, "reject_rectify", comment);

        log.info("退回整改，事件ID: {}, 编号: {}, 原因: {}", id, event.getEventCode(), comment);
        return true;
    }

    @Override
    public boolean canProcess(String id) {
        AdverseEvent event = this.getById(id);
        if (event == null) {
            return false;
        }
        return STATUS_PENDING_PROCESS.equals(event.getStatus());
    }

    @Override
    public boolean canConfirmRectify(String id) {
        AdverseEvent event = this.getById(id);
        if (event == null) {
            return false;
        }
        // 检查事件状态
        if (!STATUS_RECTIFYING.equals(event.getStatus())) {
            return false;
        }
        // 检查是否有已提交的整改记录
        AdverseEventRectify rectify = rectifyService.getCurrentRectify(id);
        return rectify != null && RECTIFY_STATUS_SUBMITTED.equals(rectify.getStatus());
    }
}
