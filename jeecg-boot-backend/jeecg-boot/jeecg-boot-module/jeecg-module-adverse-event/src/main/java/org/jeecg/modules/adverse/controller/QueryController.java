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
import org.jeecg.modules.adverse.entity.AdverseEventRectify;
import org.jeecg.modules.adverse.service.IAdverseEventFlowService;
import org.jeecg.modules.adverse.service.IAdverseEventRectifyService;
import org.jeecg.modules.adverse.service.IAdverseEventService;
import org.jeecg.modules.adverse.service.IEventCategoryService;
import org.jeecg.modules.adverse.vo.AdverseEventDetailVO;
import org.jeecg.modules.adverse.vo.AdverseEventQueryVO;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 事件查询 Controller
 * <p>
 * 提供事件综合查询功能的 API 接口，包括：
 * - 多条件综合查询
 * - 完整详情查询（含流转记录）
 * - Excel 导出
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Tag(name = "事件查询管理")
@RestController
@RequestMapping("/adverse/query")
public class QueryController {

    @Autowired
    private IAdverseEventService adverseEventService;

    @Autowired
    private IAdverseEventFlowService flowService;

    @Autowired
    private IAdverseEventRectifyService rectifyService;

    @Autowired
    private IEventCategoryService categoryService;

    /**
     * 综合查询列表
     * <p>
     * 支持多条件组合查询，根据用户角色控制数据范围
     * </p>
     *
     * @param queryVO  查询条件
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    @AutoLog(value = "事件查询-综合查询列表")
    @Operation(summary = "综合查询列表")
    @GetMapping(value = "/list")
    public Result<IPage<AdverseEvent>> queryList(
            AdverseEventQueryVO queryVO,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = buildQueryWrapper(queryVO, loginUser);

        // 分页查询
        Page<AdverseEvent> page = new Page<>(pageNo, pageSize);
        IPage<AdverseEvent> pageList = adverseEventService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取完整详情
     * <p>
     * 返回事件基本信息、流转记录和整改记录
     * </p>
     *
     * @param id 事件ID
     * @return 事件详情
     */
    @AutoLog(value = "事件查询-获取完整详情")
    @Operation(summary = "获取事件完整详情")
    @GetMapping(value = "/detail/{id}")
    public Result<AdverseEventDetailVO> getEventDetail(@PathVariable(name = "id") String id) {
        // 获取事件基本信息
        AdverseEvent event = adverseEventService.getById(id);
        if (event == null) {
            return Result.error("事件不存在");
        }

        // 获取流转记录
        List<AdverseEventFlow> flowList = flowService.getFlowListByEventId(id);

        // 获取整改记录
        List<AdverseEventRectify> rectifyList = rectifyService.getRectifyHistory(id);

        // 构建详情 VO
        AdverseEventDetailVO detailVO = new AdverseEventDetailVO(event, flowList, rectifyList);

        // 设置分类名称
        if (oConvertUtils.isNotEmpty(event.getCategoryId())) {
            var category = categoryService.getById(event.getCategoryId());
            if (category != null) {
                detailVO.setCategoryName(category.getName());
            }
        }

        return Result.OK(detailVO);
    }

    /**
     * 导出 Excel
     * <p>
     * 根据查询条件导出事件列表
     * </p>
     *
     * @param queryVO 查询条件
     * @param request 请求对象
     * @return ModelAndView
     */
    @AutoLog(value = "事件查询-导出Excel")
    @Operation(summary = "导出Excel")
    @GetMapping(value = "/export")
    public ModelAndView exportExcel(AdverseEventQueryVO queryVO, HttpServletRequest request) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = buildQueryWrapper(queryVO, loginUser);

        // 查询数据（限制最大导出数量为 10000）
        queryWrapper.last("LIMIT 10000");
        List<AdverseEvent> exportList = adverseEventService.list(queryWrapper);

        // 导出 Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "不良事件列表");
        mv.addObject(NormalExcelConstants.CLASS, AdverseEvent.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("不良事件数据", "数据列表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);

        return mv;
    }

    /**
     * 根据状态查询统计
     * <p>
     * 查询各状态的事件数量
     * </p>
     *
     * @return 统计结果
     */
    @AutoLog(value = "事件查询-状态统计")
    @Operation(summary = "查询各状态事件数量")
    @GetMapping(value = "/statusCount")
    public Result<?> getStatusCount() {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 各状态的事件数量
        java.util.Map<String, Long> statusCount = new java.util.HashMap<>();

        String[] statuses = {"draft", "pending_audit", "returned", "pending_process",
                "pending_rectify", "rectifying", "closed"};

        for (String status : statuses) {
            LambdaQueryWrapper<AdverseEvent> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AdverseEvent::getStatus, status);
            applyDataPermission(wrapper, loginUser);
            long count = adverseEventService.count(wrapper);
            statusCount.put(status, count);
        }

        return Result.OK(statusCount);
    }

    /**
     * 根据级别查询统计
     * <p>
     * 查询各级别的事件数量
     * </p>
     *
     * @return 统计结果
     */
    @AutoLog(value = "事件查询-级别统计")
    @Operation(summary = "查询各级别事件数量")
    @GetMapping(value = "/levelCount")
    public Result<?> getLevelCount() {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 各级别的事件数量
        java.util.Map<String, Long> levelCount = new java.util.HashMap<>();

        String[] levels = {"level_1", "level_2", "level_3", "level_4"};

        for (String level : levels) {
            LambdaQueryWrapper<AdverseEvent> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AdverseEvent::getLevel, level);
            applyDataPermission(wrapper, loginUser);
            long count = adverseEventService.count(wrapper);
            levelCount.put(level, count);
        }

        return Result.OK(levelCount);
    }

    /**
     * 构建查询条件
     *
     * @param queryVO   查询条件 VO
     * @param loginUser 当前用户
     * @return 查询条件
     */
    private LambdaQueryWrapper<AdverseEvent> buildQueryWrapper(AdverseEventQueryVO queryVO, LoginUser loginUser) {
        LambdaQueryWrapper<AdverseEvent> queryWrapper = new LambdaQueryWrapper<>();

        // 事件编号模糊查询
        if (oConvertUtils.isNotEmpty(queryVO.getEventCode())) {
            queryWrapper.like(AdverseEvent::getEventCode, queryVO.getEventCode());
        }

        // 事件标题模糊查询
        if (oConvertUtils.isNotEmpty(queryVO.getTitle())) {
            queryWrapper.like(AdverseEvent::getTitle, queryVO.getTitle());
        }

        // 事件分类
        if (oConvertUtils.isNotEmpty(queryVO.getCategoryId())) {
            queryWrapper.eq(AdverseEvent::getCategoryId, queryVO.getCategoryId());
        }

        // 事件级别
        if (oConvertUtils.isNotEmpty(queryVO.getLevel())) {
            queryWrapper.eq(AdverseEvent::getLevel, queryVO.getLevel());
        }

        // 事件状态
        if (oConvertUtils.isNotEmpty(queryVO.getStatus())) {
            queryWrapper.eq(AdverseEvent::getStatus, queryVO.getStatus());
        }

        // 上报科室
        if (oConvertUtils.isNotEmpty(queryVO.getDepartmentId())) {
            queryWrapper.eq(AdverseEvent::getDepartmentId, queryVO.getDepartmentId());
        }

        // 上报人ID
        if (oConvertUtils.isNotEmpty(queryVO.getReporterId())) {
            queryWrapper.eq(AdverseEvent::getReporterId, queryVO.getReporterId());
        }

        // 上报人姓名模糊查询
        if (oConvertUtils.isNotEmpty(queryVO.getReporterName())) {
            queryWrapper.like(AdverseEvent::getReporterName, queryVO.getReporterName());
        }

        // 发生时间范围
        if (queryVO.getOccurTimeBegin() != null) {
            queryWrapper.ge(AdverseEvent::getOccurTime, queryVO.getOccurTimeBegin());
        }
        if (queryVO.getOccurTimeEnd() != null) {
            queryWrapper.le(AdverseEvent::getOccurTime, queryVO.getOccurTimeEnd());
        }

        // 创建时间范围
        if (queryVO.getCreateTimeBegin() != null) {
            queryWrapper.ge(AdverseEvent::getCreateTime, queryVO.getCreateTimeBegin());
        }
        if (queryVO.getCreateTimeEnd() != null) {
            queryWrapper.le(AdverseEvent::getCreateTime, queryVO.getCreateTimeEnd());
        }

        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(queryVO.getPatientName())) {
            queryWrapper.like(AdverseEvent::getPatientName, queryVO.getPatientName());
        }

        // 住院号精确查询
        if (oConvertUtils.isNotEmpty(queryVO.getPatientAdmissionNo())) {
            queryWrapper.eq(AdverseEvent::getPatientAdmissionNo, queryVO.getPatientAdmissionNo());
        }

        // 应用数据权限
        applyDataPermission(queryWrapper, loginUser);

        // 排序：按创建时间倒序
        queryWrapper.orderByDesc(AdverseEvent::getCreateTime);

        return queryWrapper;
    }

    /**
     * 应用数据权限
     * <p>
     * 根据用户角色控制可查看的数据范围：
     * - 普通用户：仅本人上报的事件
     * - 科室审核人员：本科室的事件
     * - 职能科室：所有事件（可根据需要限制分管科室）
     * - 管理员：所有事件
     * </p>
     *
     * @param queryWrapper 查询条件
     * @param loginUser    当前用户
     */
    private void applyDataPermission(LambdaQueryWrapper<AdverseEvent> queryWrapper, LoginUser loginUser) {
        // TODO: 根据实际角色体系实现数据权限控制
        // 目前简单实现：如果是管理员角色则不限制，否则按科室限制

        // 获取用户角色（简化处理，实际应查询用户角色）
        // 这里暂时不做限制，后续根据实际角色体系完善
        // 如果需要限制，可以取消下面的注释

        // 示例：按科室限制
        // if (!isAdmin(loginUser)) {
        //     queryWrapper.eq(AdverseEvent::getDepartmentId, loginUser.getOrgCode());
        // }
    }
}
