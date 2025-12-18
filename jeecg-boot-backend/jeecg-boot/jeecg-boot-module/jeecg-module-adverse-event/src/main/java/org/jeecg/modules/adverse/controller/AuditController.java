package org.jeecg.modules.adverse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.adverse.entity.AdverseEvent;
import org.jeecg.modules.adverse.entity.AdverseEventFlow;
import org.jeecg.modules.adverse.service.IAdverseEventFlowService;
import org.jeecg.modules.adverse.service.IAdverseEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科室审核 Controller
 * <p>
 * 提供科室审核功能的 API 接口，包括：
 * - 待审核列表查询
 * - 已审核列表查询
 * - 审核通过
 * - 审核退回
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Tag(name = "科室审核管理")
@RestController
@RequestMapping("/adverse/audit")
public class AuditController {

    /**
     * 事件状态常量
     */
    private static final String STATUS_PENDING_AUDIT = "pending_audit";
    private static final String STATUS_PENDING_PROCESS = "pending_process";
    private static final String STATUS_RETURNED = "returned";

    @Autowired
    private IAdverseEventService adverseEventService;

    @Autowired
    private IAdverseEventFlowService flowService;

    /**
     * 获取待审核事件列表
     * <p>
     * 查询当前登录用户所在科室的待审核事件
     * </p>
     *
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param title    事件标题（模糊查询）
     * @param level    事件级别
     * @return 分页结果
     */
    @AutoLog(value = "科室审核-待审核列表")
    @Operation(summary = "获取待审核事件列表")
    @GetMapping(value = "/pendingList")
    public Result<IPage<AdverseEvent>> getPendingList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "level", required = false) String level) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询待审核状态
        queryWrapper.eq(AdverseEvent::getStatus, STATUS_PENDING_AUDIT);
        // 只查询当前用户所在科室的事件
        queryWrapper.eq(AdverseEvent::getDepartmentId, loginUser.getOrgCode());
        // 标题模糊查询
        if (oConvertUtils.isNotEmpty(title)) {
            queryWrapper.like(AdverseEvent::getTitle, title);
        }
        // 级别筛选
        if (oConvertUtils.isNotEmpty(level)) {
            queryWrapper.eq(AdverseEvent::getLevel, level);
        }
        // 按提交时间倒序
        queryWrapper.orderByDesc(AdverseEvent::getSubmitTime);

        // 分页查询
        Page<AdverseEvent> page = new Page<>(pageNo, pageSize);
        IPage<AdverseEvent> pageList = adverseEventService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取已审核事件列表
     * <p>
     * 查询当前登录用户所在科室的已审核事件（已通过或已退回）
     * </p>
     *
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param title    事件标题（模糊查询）
     * @param status   事件状态（pending_process-已通过，returned-已退回）
     * @return 分页结果
     */
    @AutoLog(value = "科室审核-已审核列表")
    @Operation(summary = "获取已审核事件列表")
    @GetMapping(value = "/completedList")
    public Result<IPage<AdverseEvent>> getCompletedList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "status", required = false) String status) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询已审核状态（通过或退回）
        if (oConvertUtils.isNotEmpty(status)) {
            queryWrapper.eq(AdverseEvent::getStatus, status);
        } else {
            queryWrapper.in(AdverseEvent::getStatus, STATUS_PENDING_PROCESS, STATUS_RETURNED);
        }
        // 只查询当前用户所在科室的事件
        queryWrapper.eq(AdverseEvent::getDepartmentId, loginUser.getOrgCode());
        // 标题模糊查询
        if (oConvertUtils.isNotEmpty(title)) {
            queryWrapper.like(AdverseEvent::getTitle, title);
        }
        // 按更新时间倒序
        queryWrapper.orderByDesc(AdverseEvent::getUpdateTime);

        // 分页查询
        Page<AdverseEvent> page = new Page<>(pageNo, pageSize);
        IPage<AdverseEvent> pageList = adverseEventService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 审核通过
     * <p>
     * 将事件状态从待审核改为待处理，流转至职能科室
     * </p>
     *
     * @param id      事件ID
     * @param comment 审核意见（选填）
     * @return 操作结果
     */
    @AutoLog(value = "科室审核-审核通过")
    @Operation(summary = "审核通过")
    @PostMapping(value = "/pass")
    public Result<String> auditPass(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment", required = false) String comment) {

        // 校验事件是否可审核
        if (!adverseEventService.canAudit(id)) {
            return Result.error("该事件当前状态不允许审核");
        }

        // 执行审核通过
        boolean success = adverseEventService.auditPass(id, comment);
        if (success) {
            return Result.OK("审核通过");
        } else {
            return Result.error("审核失败，请稍后重试");
        }
    }

    /**
     * 审核退回
     * <p>
     * 将事件状态从待审核改为已退回，需填写退回原因
     * </p>
     *
     * @param id      事件ID
     * @param comment 退回原因（必填）
     * @return 操作结果
     */
    @AutoLog(value = "科室审核-审核退回")
    @Operation(summary = "审核退回")
    @PostMapping(value = "/reject")
    public Result<String> auditReject(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment") String comment) {

        // 校验退回原因
        if (oConvertUtils.isEmpty(comment)) {
            return Result.error("退回原因不能为空");
        }

        // 校验事件是否可审核
        if (!adverseEventService.canAudit(id)) {
            return Result.error("该事件当前状态不允许审核");
        }

        // 执行审核退回
        boolean success = adverseEventService.auditReject(id, comment);
        if (success) {
            return Result.OK("已退回");
        } else {
            return Result.error("退回失败，请稍后重试");
        }
    }

    /**
     * 获取事件审核详情
     * <p>
     * 获取事件信息及流转历史，用于审核页面展示
     * </p>
     *
     * @param id 事件ID
     * @return 事件详情
     */
    @AutoLog(value = "科室审核-获取详情")
    @Operation(summary = "获取事件审核详情")
    @GetMapping(value = "/detail")
    public Result<AdverseEvent> getAuditDetail(@RequestParam(name = "id") String id) {
        AdverseEvent event = adverseEventService.getById(id);
        if (event == null) {
            return Result.error("事件不存在");
        }
        return Result.OK(event);
    }

    /**
     * 获取事件流转历史
     * <p>
     * 查询指定事件的所有流转记录，用于审核页面展示
     * </p>
     *
     * @param eventId 事件ID
     * @return 流转记录列表
     */
    @AutoLog(value = "科室审核-获取流转历史")
    @Operation(summary = "获取事件流转历史")
    @GetMapping(value = "/flowHistory")
    public Result<List<AdverseEventFlow>> getFlowHistory(@RequestParam(name = "eventId") String eventId) {
        List<AdverseEventFlow> flowList = flowService.getFlowHistory(eventId);
        return Result.OK(flowList);
    }
}
