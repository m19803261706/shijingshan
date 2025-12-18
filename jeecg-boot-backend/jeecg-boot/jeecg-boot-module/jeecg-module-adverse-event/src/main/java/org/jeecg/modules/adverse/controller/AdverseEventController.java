package org.jeecg.modules.adverse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.adverse.entity.AdverseEvent;
import org.jeecg.modules.adverse.entity.AdverseEventFlow;
import org.jeecg.modules.adverse.service.IAdverseEventFlowService;
import org.jeecg.modules.adverse.service.IAdverseEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 不良事件上报 Controller
 * <p>
 * 提供不良事件的上报、编辑、查询等 API 接口
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Tag(name = "不良事件上报")
@RestController
@RequestMapping("/adverse/event")
public class AdverseEventController {

    @Autowired
    private IAdverseEventService adverseEventService;

    @Autowired
    private IAdverseEventFlowService flowService;

    /**
     * 分页查询事件列表
     *
     * @param adverseEvent 查询条件
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param req          请求对象
     * @return 分页结果
     */
    @AutoLog(value = "不良事件-分页列表查询")
    @Operation(summary = "分页查询事件列表")
    @GetMapping(value = "/list")
    public Result<IPage<AdverseEvent>> queryPageList(AdverseEvent adverseEvent,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req) {
        QueryWrapper<AdverseEvent> queryWrapper = QueryGenerator.initQueryWrapper(adverseEvent, req.getParameterMap());
        queryWrapper.orderByDesc("create_time");

        Page<AdverseEvent> page = new Page<>(pageNo, pageSize);
        IPage<AdverseEvent> pageList = adverseEventService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 我的上报列表
     * <p>
     * 仅查询当前登录用户上报的事件
     * </p>
     *
     * @param adverseEvent 查询条件
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param req          请求对象
     * @return 分页结果
     */
    @AutoLog(value = "不良事件-我的上报列表")
    @Operation(summary = "我的上报列表")
    @GetMapping(value = "/myList")
    public Result<IPage<AdverseEvent>> queryMyList(AdverseEvent adverseEvent,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   HttpServletRequest req) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        QueryWrapper<AdverseEvent> queryWrapper = QueryGenerator.initQueryWrapper(adverseEvent, req.getParameterMap());
        // 仅查询本人上报的事件
        queryWrapper.eq("reporter_id", loginUser.getId());
        queryWrapper.orderByDesc("create_time");

        Page<AdverseEvent> page = new Page<>(pageNo, pageSize);
        IPage<AdverseEvent> pageList = adverseEventService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 根据ID查询事件详情
     *
     * @param id 事件ID
     * @return 事件详情
     */
    @AutoLog(value = "不良事件-查询详情")
    @Operation(summary = "根据ID查询事件详情")
    @GetMapping(value = "/queryById")
    public Result<AdverseEvent> queryById(@RequestParam(name = "id") String id) {
        AdverseEvent event = adverseEventService.getById(id);
        if (event == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(event);
    }

    /**
     * 新增事件（保存草稿）
     *
     * @param adverseEvent 事件信息
     * @return 操作结果
     */
    @AutoLog(value = "不良事件-新增")
    @Operation(summary = "新增事件（保存草稿）")
    @PostMapping(value = "/add")
    public Result<AdverseEvent> add(@RequestBody AdverseEvent adverseEvent) {
        try {
            AdverseEvent saved = adverseEventService.saveDraft(adverseEvent);
            return Result.OK("添加成功！", saved);
        } catch (Exception e) {
            log.error("新增事件失败", e);
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 编辑事件
     *
     * @param adverseEvent 事件信息
     * @return 操作结果
     */
    @AutoLog(value = "不良事件-编辑")
    @Operation(summary = "编辑事件")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<AdverseEvent> edit(@RequestBody AdverseEvent adverseEvent) {
        // 校验是否可编辑
        if (!adverseEventService.canEdit(adverseEvent.getId())) {
            return Result.error("当前状态不允许编辑");
        }

        try {
            AdverseEvent saved = adverseEventService.saveDraft(adverseEvent);
            return Result.OK("编辑成功！", saved);
        } catch (Exception e) {
            log.error("编辑事件失败", e);
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 删除事件
     * <p>
     * 仅草稿状态可删除
     * </p>
     *
     * @param id 事件ID
     * @return 操作结果
     */
    @AutoLog(value = "不良事件-删除")
    @Operation(summary = "删除事件")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id") String id) {
        if (adverseEventService.deleteEvent(id)) {
            return Result.OK("删除成功！");
        } else {
            return Result.error("删除失败，仅草稿状态可删除");
        }
    }

    /**
     * 批量删除事件
     *
     * @param ids 事件ID列表，逗号分隔
     * @return 操作结果
     */
    @AutoLog(value = "不良事件-批量删除")
    @Operation(summary = "批量删除事件")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids") String ids) {
        String[] idArray = ids.split(",");
        int successCount = 0;
        for (String id : idArray) {
            if (adverseEventService.deleteEvent(id)) {
                successCount++;
            }
        }
        return Result.OK("成功删除 " + successCount + " 条记录");
    }

    /**
     * 提交上报
     * <p>
     * 将事件状态从草稿/退回改为待审核
     * </p>
     *
     * @param id 事件ID
     * @return 操作结果
     */
    @AutoLog(value = "不良事件-提交上报")
    @Operation(summary = "提交上报")
    @PostMapping(value = "/submit")
    public Result<String> submit(@RequestParam(name = "id") String id) {
        if (adverseEventService.submitEvent(id)) {
            return Result.OK("提交成功！");
        } else {
            return Result.error("提交失败，请检查必填字段是否完整");
        }
    }

    /**
     * 获取事件流转记录
     *
     * @param id 事件ID
     * @return 流转记录列表
     */
    @AutoLog(value = "不良事件-查询流转记录")
    @Operation(summary = "获取事件流转记录")
    @GetMapping(value = "/flow/{id}")
    public Result<List<AdverseEventFlow>> getFlowList(@PathVariable("id") String id) {
        List<AdverseEventFlow> flowList = flowService.getFlowListByEventId(id);
        return Result.OK(flowList);
    }

    /**
     * 校验事件是否可编辑
     *
     * @param id 事件ID
     * @return 是否可编辑
     */
    @Operation(summary = "校验事件是否可编辑")
    @GetMapping(value = "/canEdit")
    public Result<Boolean> canEdit(@RequestParam(name = "id") String id) {
        return Result.OK(adverseEventService.canEdit(id));
    }
}
