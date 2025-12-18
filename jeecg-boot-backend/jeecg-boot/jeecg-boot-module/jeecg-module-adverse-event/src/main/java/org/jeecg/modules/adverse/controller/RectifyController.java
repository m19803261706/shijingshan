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
import org.jeecg.modules.adverse.entity.AdverseEventRectify;
import org.jeecg.modules.adverse.service.IAdverseEventFlowService;
import org.jeecg.modules.adverse.service.IAdverseEventRectifyService;
import org.jeecg.modules.adverse.service.IAdverseEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 整改管理 Controller
 * <p>
 * 提供科室端整改功能的 API 接口，包括：
 * - 待整改列表查询
 * - 整改详情查询
 * - 保存整改草稿
 * - 提交整改
 * - 整改历史查询
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Tag(name = "整改管理")
@RestController
@RequestMapping("/adverse/rectify")
public class RectifyController {

    /**
     * 事件状态常量
     */
    private static final String STATUS_PENDING_RECTIFY = "pending_rectify";

    @Autowired
    private IAdverseEventService adverseEventService;

    @Autowired
    private IAdverseEventRectifyService rectifyService;

    @Autowired
    private IAdverseEventFlowService flowService;

    /**
     * 获取待整改事件列表
     * <p>
     * 查询当前登录用户所在科室需要整改的事件
     * </p>
     *
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param title    事件标题（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "整改管理-待整改列表")
    @Operation(summary = "获取待整改事件列表")
    @GetMapping(value = "/pendingList")
    public Result<IPage<AdverseEvent>> getPendingList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "title", required = false) String title) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询待整改状态
        queryWrapper.eq(AdverseEvent::getStatus, STATUS_PENDING_RECTIFY);
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
     * 获取整改详情
     * <p>
     * 获取整改记录详情，包括整改要求、期限等
     * </p>
     *
     * @param id 整改记录ID
     * @return 整改详情
     */
    @AutoLog(value = "整改管理-获取详情")
    @Operation(summary = "获取整改详情")
    @GetMapping(value = "/detail/{id}")
    public Result<AdverseEventRectify> getRectifyDetail(@PathVariable(name = "id") String id) {
        AdverseEventRectify rectify = rectifyService.getRectifyDetail(id);
        if (rectify == null) {
            return Result.error("整改记录不存在");
        }
        return Result.OK(rectify);
    }

    /**
     * 根据事件ID获取当前整改记录
     * <p>
     * 获取事件的当前整改记录详情
     * </p>
     *
     * @param eventId 事件ID
     * @return 整改详情
     */
    @AutoLog(value = "整改管理-获取当前整改")
    @Operation(summary = "根据事件ID获取当前整改记录")
    @GetMapping(value = "/current")
    public Result<AdverseEventRectify> getCurrentRectify(@RequestParam(name = "eventId") String eventId) {
        AdverseEventRectify rectify = rectifyService.getCurrentRectify(eventId);
        if (rectify == null) {
            return Result.error("没有待处理的整改记录");
        }
        return Result.OK(rectify);
    }

    /**
     * 保存整改草稿
     * <p>
     * 保存整改措施草稿，不提交
     * </p>
     *
     * @param id          整改记录ID
     * @param measures    整改措施
     * @param result      整改结果
     * @param attachments 整改附件
     * @return 操作结果
     */
    @AutoLog(value = "整改管理-保存草稿")
    @Operation(summary = "保存整改草稿")
    @PostMapping(value = "/save")
    public Result<String> saveRectifyDraft(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "measures", required = false) String measures,
            @RequestParam(name = "result", required = false) String result,
            @RequestParam(name = "attachments", required = false) String attachments) {

        // 校验是否可以填写整改
        if (!rectifyService.canFillRectify(id)) {
            return Result.error("该整改记录当前状态不允许编辑");
        }

        // 保存草稿
        boolean success = rectifyService.saveRectifyDraft(id, measures, result, attachments);
        if (success) {
            return Result.OK("保存成功");
        } else {
            return Result.error("保存失败，请稍后重试");
        }
    }

    /**
     * 提交整改
     * <p>
     * 提交整改措施，状态变为待确认
     * </p>
     *
     * @param eventId     事件ID
     * @param measures    整改措施
     * @param result      整改结果
     * @param attachments 整改附件
     * @return 操作结果
     */
    @AutoLog(value = "整改管理-提交整改")
    @Operation(summary = "提交整改")
    @PostMapping(value = "/submit")
    public Result<String> submitRectify(
            @RequestParam(name = "eventId") String eventId,
            @RequestParam(name = "measures") String measures,
            @RequestParam(name = "result") String result,
            @RequestParam(name = "attachments", required = false) String attachments) {

        // 校验必填字段
        if (oConvertUtils.isEmpty(measures)) {
            return Result.error("整改措施不能为空");
        }
        if (oConvertUtils.isEmpty(result)) {
            return Result.error("整改结果不能为空");
        }

        // 校验是否可以提交整改
        if (!adverseEventService.canSubmitRectify(eventId)) {
            return Result.error("该事件当前状态不允许提交整改");
        }

        // 提交整改
        boolean success = adverseEventService.submitRectifyByDepartment(eventId, measures, result, attachments);
        if (success) {
            return Result.OK("提交成功，等待职能科室确认");
        } else {
            return Result.error("提交失败，请稍后重试");
        }
    }

    /**
     * 获取整改历史记录
     * <p>
     * 获取指定事件的所有整改记录历史
     * </p>
     *
     * @param eventId 事件ID
     * @return 整改历史列表
     */
    @AutoLog(value = "整改管理-获取历史记录")
    @Operation(summary = "获取整改历史记录")
    @GetMapping(value = "/history/{eventId}")
    public Result<List<AdverseEventRectify>> getRectifyHistory(@PathVariable(name = "eventId") String eventId) {
        List<AdverseEventRectify> history = rectifyService.getRectifyHistory(eventId);
        return Result.OK(history);
    }

    /**
     * 获取事件详情（整改用）
     * <p>
     * 获取事件信息，用于整改页面展示
     * </p>
     *
     * @param eventId 事件ID
     * @return 事件详情
     */
    @AutoLog(value = "整改管理-获取事件详情")
    @Operation(summary = "获取事件详情")
    @GetMapping(value = "/eventDetail")
    public Result<AdverseEvent> getEventDetail(@RequestParam(name = "eventId") String eventId) {
        AdverseEvent event = adverseEventService.getById(eventId);
        if (event == null) {
            return Result.error("事件不存在");
        }
        return Result.OK(event);
    }

    /**
     * 获取我的整改历史
     * <p>
     * 获取当前用户所在科室的已完成整改列表
     * </p>
     *
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @AutoLog(value = "整改管理-我的整改历史")
    @Operation(summary = "获取我的整改历史")
    @GetMapping(value = "/myHistory")
    public Result<IPage<AdverseEvent>> getMyRectifyHistory(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件（查询整改中和已结案的事件）
        LambdaQueryWrapper<AdverseEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AdverseEvent::getStatus, "rectifying", "closed");
        queryWrapper.eq(AdverseEvent::getDepartmentId, loginUser.getOrgCode());
        queryWrapper.orderByDesc(AdverseEvent::getUpdateTime);

        // 分页查询
        Page<AdverseEvent> page = new Page<>(pageNo, pageSize);
        IPage<AdverseEvent> pageList = adverseEventService.page(page, queryWrapper);

        return Result.OK(pageList);
    }
}
