package org.jeecg.modules.adverse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.adverse.entity.AdverseEvent;
import org.jeecg.modules.adverse.entity.AdverseEventRectify;
import org.jeecg.modules.adverse.service.IAdverseEventFlowService;
import org.jeecg.modules.adverse.service.IAdverseEventRectifyService;
import org.jeecg.modules.adverse.service.IAdverseEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 职能处理 Controller
 * <p>
 * 提供职能科室处理功能的 API 接口，包括：
 * - 待处理列表查询
 * - 待确认列表查询
 * - 已结案列表查询
 * - 要求整改
 * - 直接结案
 * - 确认整改
 * - 退回整改
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Tag(name = "职能处理管理")
@RestController
@RequestMapping("/adverse/process")
public class ProcessController {

    /**
     * 事件状态常量
     */
    private static final String STATUS_PENDING_PROCESS = "pending_process";
    private static final String STATUS_PENDING_RECTIFY = "pending_rectify";
    private static final String STATUS_RECTIFYING = "rectifying";
    private static final String STATUS_CLOSED = "closed";

    @Autowired
    private IAdverseEventService adverseEventService;

    @Autowired
    private IAdverseEventRectifyService rectifyService;

    @Autowired
    private IAdverseEventFlowService flowService;

    /**
     * 获取待处理事件列表
     * <p>
     * 查询所有待处理状态的事件，供职能科室处理
     * </p>
     *
     * @param pageNo     页码
     * @param pageSize   每页条数
     * @param title      事件标题（模糊查询）
     * @param level      事件级别
     * @param categoryId 事件分类
     * @return 分页结果
     */
    @AutoLog(value = "职能处理-待处理列表")
    @Operation(summary = "获取待处理事件列表")
    @GetMapping(value = "/pendingList")
    public Result<IPage<AdverseEvent>> getPendingList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "level", required = false) String level,
            @RequestParam(name = "categoryId", required = false) String categoryId) {

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询待处理状态
        queryWrapper.eq(AdverseEvent::getStatus, STATUS_PENDING_PROCESS);
        // 标题模糊查询
        if (oConvertUtils.isNotEmpty(title)) {
            queryWrapper.like(AdverseEvent::getTitle, title);
        }
        // 级别筛选
        if (oConvertUtils.isNotEmpty(level)) {
            queryWrapper.eq(AdverseEvent::getLevel, level);
        }
        // 分类筛选
        if (oConvertUtils.isNotEmpty(categoryId)) {
            queryWrapper.eq(AdverseEvent::getCategoryId, categoryId);
        }
        // 按提交时间倒序
        queryWrapper.orderByDesc(AdverseEvent::getSubmitTime);

        // 分页查询
        Page<AdverseEvent> page = new Page<>(pageNo, pageSize);
        IPage<AdverseEvent> pageList = adverseEventService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取待确认事件列表
     * <p>
     * 查询整改中状态且整改已提交的事件，供职能科室确认或退回
     * </p>
     *
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param title    事件标题（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "职能处理-待确认列表")
    @Operation(summary = "获取待确认事件列表")
    @GetMapping(value = "/pendingConfirmList")
    public Result<IPage<AdverseEvent>> getPendingConfirmList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "title", required = false) String title) {

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询整改中状态
        queryWrapper.eq(AdverseEvent::getStatus, STATUS_RECTIFYING);
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
     * 获取已结案事件列表
     * <p>
     * 查询所有已结案的事件
     * </p>
     *
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param title    事件标题（模糊查询）
     * @param level    事件级别
     * @return 分页结果
     */
    @AutoLog(value = "职能处理-已结案列表")
    @Operation(summary = "获取已结案事件列表")
    @GetMapping(value = "/closedList")
    public Result<IPage<AdverseEvent>> getClosedList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "level", required = false) String level) {

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询已结案状态
        queryWrapper.eq(AdverseEvent::getStatus, STATUS_CLOSED);
        // 标题模糊查询
        if (oConvertUtils.isNotEmpty(title)) {
            queryWrapper.like(AdverseEvent::getTitle, title);
        }
        // 级别筛选
        if (oConvertUtils.isNotEmpty(level)) {
            queryWrapper.eq(AdverseEvent::getLevel, level);
        }
        // 按结案时间倒序
        queryWrapper.orderByDesc(AdverseEvent::getCloseTime);

        // 分页查询
        Page<AdverseEvent> page = new Page<>(pageNo, pageSize);
        IPage<AdverseEvent> pageList = adverseEventService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 要求整改
     * <p>
     * 职能科室提出整改要求，事件流转到待整改状态
     * </p>
     *
     * @param id          事件ID
     * @param requirement 整改要求
     * @param deadline    整改期限
     * @return 操作结果
     */
    @AutoLog(value = "职能处理-要求整改")
    @Operation(summary = "要求整改")
    @PostMapping(value = "/requireRectify")
    public Result<String> requireRectify(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "requirement") String requirement,
            @RequestParam(name = "deadline") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline) {

        // 校验参数
        if (oConvertUtils.isEmpty(requirement)) {
            return Result.error("整改要求不能为空");
        }
        if (deadline == null) {
            return Result.error("整改期限不能为空");
        }

        // 校验事件是否可处理
        if (!adverseEventService.canProcess(id)) {
            return Result.error("该事件当前状态不允许要求整改");
        }

        // 执行要求整改
        boolean success = adverseEventService.requireRectify(id, requirement, deadline);
        if (success) {
            return Result.OK("已提出整改要求");
        } else {
            return Result.error("操作失败，请稍后重试");
        }
    }

    /**
     * 直接结案
     * <p>
     * 职能科室判定无需整改，直接结案
     * </p>
     *
     * @param id      事件ID
     * @param comment 结案说明（选填）
     * @return 操作结果
     */
    @AutoLog(value = "职能处理-直接结案")
    @Operation(summary = "直接结案")
    @PostMapping(value = "/close")
    public Result<String> closeDirectly(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment", required = false) String comment) {

        // 校验事件是否可处理
        if (!adverseEventService.canProcess(id)) {
            return Result.error("该事件当前状态不允许直接结案");
        }

        // 执行直接结案
        boolean success = adverseEventService.closeDirectly(id, comment);
        if (success) {
            return Result.OK("已结案");
        } else {
            return Result.error("操作失败，请稍后重试");
        }
    }

    /**
     * 确认整改
     * <p>
     * 职能科室确认整改措施有效，事件结案
     * </p>
     *
     * @param id      事件ID
     * @param comment 确认意见（选填）
     * @return 操作结果
     */
    @AutoLog(value = "职能处理-确认整改")
    @Operation(summary = "确认整改")
    @PostMapping(value = "/confirmRectify")
    public Result<String> confirmRectify(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment", required = false) String comment) {

        // 校验事件是否可确认整改
        if (!adverseEventService.canConfirmRectify(id)) {
            return Result.error("该事件当前状态不允许确认整改");
        }

        // 执行确认整改
        boolean success = adverseEventService.confirmRectify(id, comment);
        if (success) {
            return Result.OK("整改已确认，事件已结案");
        } else {
            return Result.error("操作失败，请稍后重试");
        }
    }

    /**
     * 退回整改
     * <p>
     * 职能科室判定整改措施不足，退回重新整改
     * </p>
     *
     * @param id      事件ID
     * @param comment 退回原因（必填）
     * @return 操作结果
     */
    @AutoLog(value = "职能处理-退回整改")
    @Operation(summary = "退回整改")
    @PostMapping(value = "/rejectRectify")
    public Result<String> rejectRectify(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment") String comment) {

        // 校验退回原因
        if (oConvertUtils.isEmpty(comment)) {
            return Result.error("退回原因不能为空");
        }

        // 校验事件是否可确认整改
        if (!adverseEventService.canConfirmRectify(id)) {
            return Result.error("该事件当前状态不允许退回整改");
        }

        // 执行退回整改
        boolean success = adverseEventService.rejectRectifyResult(id, comment);
        if (success) {
            return Result.OK("已退回，等待科室重新整改");
        } else {
            return Result.error("操作失败，请稍后重试");
        }
    }

    /**
     * 获取事件处理详情
     * <p>
     * 获取事件信息、整改记录和流转历史
     * </p>
     *
     * @param id 事件ID
     * @return 事件详情
     */
    @AutoLog(value = "职能处理-获取详情")
    @Operation(summary = "获取事件处理详情")
    @GetMapping(value = "/detail")
    public Result<AdverseEvent> getProcessDetail(@RequestParam(name = "id") String id) {
        AdverseEvent event = adverseEventService.getById(id);
        if (event == null) {
            return Result.error("事件不存在");
        }
        return Result.OK(event);
    }

    /**
     * 获取整改记录列表
     * <p>
     * 获取指定事件的所有整改记录
     * </p>
     *
     * @param eventId 事件ID
     * @return 整改记录列表
     */
    @AutoLog(value = "职能处理-获取整改记录")
    @Operation(summary = "获取整改记录列表")
    @GetMapping(value = "/rectifyHistory")
    public Result<List<AdverseEventRectify>> getRectifyHistory(@RequestParam(name = "eventId") String eventId) {
        List<AdverseEventRectify> rectifyList = rectifyService.getRectifyHistory(eventId);
        return Result.OK(rectifyList);
    }

    /**
     * 获取待整改事件列表
     * <p>
     * 查询待整改状态的事件列表
     * </p>
     *
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param title    事件标题（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "职能处理-待整改列表")
    @Operation(summary = "获取待整改事件列表")
    @GetMapping(value = "/pendingRectifyList")
    public Result<IPage<AdverseEvent>> getPendingRectifyList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "title", required = false) String title) {

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询待整改状态
        queryWrapper.eq(AdverseEvent::getStatus, STATUS_PENDING_RECTIFY);
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
}
