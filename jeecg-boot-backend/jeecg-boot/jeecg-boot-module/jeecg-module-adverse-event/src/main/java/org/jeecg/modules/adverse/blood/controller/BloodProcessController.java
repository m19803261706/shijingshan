package org.jeecg.modules.adverse.blood.controller;

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
import org.jeecg.modules.adverse.blood.entity.BloodAdverseFlow;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseRectify;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseReport;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseFlowService;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseRectifyService;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 输血科处理 Controller
 * <p>
 * 提供输血科处理输血使用不良事件报告的 API 接口，包括：
 * - 待处理列表查询
 * - 待确认列表查询
 * - 已结案列表查询
 * - 直接结案
 * - 要求整改
 * - 确认整改
 * - 退回整改
 * - 获取整改记录
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/80">Issue #80</a>
 */
@Slf4j
@Tag(name = "输血科处理管理")
@RestController
@RequestMapping("/adverse/blood/process")
public class BloodProcessController {

    /**
     * 报告状态常量
     */
    private static final String STATUS_PENDING_PROCESS = "pending_process";
    private static final String STATUS_PENDING_RECTIFY = "pending_rectify";
    private static final String STATUS_RECTIFYING = "rectifying";
    private static final String STATUS_CLOSED = "closed";

    @Autowired
    private IBloodAdverseReportService reportService;

    @Autowired
    private IBloodAdverseRectifyService rectifyService;

    @Autowired
    private IBloodAdverseFlowService flowService;

    // ==================== 列表查询接口 ====================

    /**
     * 获取待处理报告列表
     * <p>
     * 查询待处理状态的输血使用不良事件报告（status = pending_process）
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportNo     报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "输血处理-待处理列表")
    @Operation(summary = "获取待处理报告列表")
    @GetMapping(value = "/pending")
    public Result<IPage<BloodAdverseReport>> getPendingList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportNo", required = false) String reportNo,
            @RequestParam(name = "patientName", required = false) String patientName) {

        // 构建查询条件
        LambdaQueryWrapper<BloodAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询待处理状态
        queryWrapper.eq(BloodAdverseReport::getStatus, STATUS_PENDING_PROCESS);
        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportNo)) {
            queryWrapper.like(BloodAdverseReport::getReportCode, reportNo);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(BloodAdverseReport::getPatientName, patientName);
        }
        // 按创建时间倒序
        queryWrapper.orderByDesc(BloodAdverseReport::getCreateTime);

        // 分页查询
        Page<BloodAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<BloodAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取待确认报告列表
     * <p>
     * 查询待整改确认状态的输血使用不良事件报告（status = pending_rectify 或 rectifying）
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportNo     报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "输血处理-待确认列表")
    @Operation(summary = "获取待确认报告列表")
    @GetMapping(value = "/confirming")
    public Result<IPage<BloodAdverseReport>> getConfirmingList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportNo", required = false) String reportNo,
            @RequestParam(name = "patientName", required = false) String patientName) {

        // 构建查询条件
        LambdaQueryWrapper<BloodAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 查询待整改和整改中状态
        queryWrapper.in(BloodAdverseReport::getStatus, STATUS_PENDING_RECTIFY, STATUS_RECTIFYING);
        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportNo)) {
            queryWrapper.like(BloodAdverseReport::getReportCode, reportNo);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(BloodAdverseReport::getPatientName, patientName);
        }
        // 按创建时间倒序
        queryWrapper.orderByDesc(BloodAdverseReport::getCreateTime);

        // 分页查询
        Page<BloodAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<BloodAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取已结案报告列表
     * <p>
     * 查询已结案状态的输血使用不良事件报告（status = closed）
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportNo     报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @param closeType    结案方式筛选（direct-直接结案，rectify-整改结案）
     * @return 分页结果
     */
    @AutoLog(value = "输血处理-已结案列表")
    @Operation(summary = "获取已结案报告列表")
    @GetMapping(value = "/closed")
    public Result<IPage<BloodAdverseReport>> getClosedList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportNo", required = false) String reportNo,
            @RequestParam(name = "patientName", required = false) String patientName,
            @RequestParam(name = "closeType", required = false) String closeType) {

        // 构建查询条件
        LambdaQueryWrapper<BloodAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询已结案状态
        queryWrapper.eq(BloodAdverseReport::getStatus, STATUS_CLOSED);
        // 结案方式筛选
        if (oConvertUtils.isNotEmpty(closeType)) {
            queryWrapper.eq(BloodAdverseReport::getCloseType, closeType);
        }
        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportNo)) {
            queryWrapper.like(BloodAdverseReport::getReportCode, reportNo);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(BloodAdverseReport::getPatientName, patientName);
        }
        // 按结案时间倒序
        queryWrapper.orderByDesc(BloodAdverseReport::getCloseTime);

        // 分页查询
        Page<BloodAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<BloodAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    // ==================== 处理操作接口 ====================

    /**
     * 直接结案
     * <p>
     * 将报告直接结案，不需要整改流程
     * </p>
     *
     * @param id      报告ID
     * @param comment 结案意见
     * @return 操作结果
     */
    @AutoLog(value = "输血处理-直接结案")
    @Operation(summary = "直接结案")
    @PostMapping(value = "/close")
    public Result<String> closeDirect(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment", required = false) String comment) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 校验状态
        if (!reportService.canProcess(id)) {
            return Result.error("当前报告状态不允许直接结案");
        }

        // 执行直接结案
        boolean success = reportService.closeDirect(id, loginUser.getId(), loginUser.getRealname(), comment);
        if (success) {
            return Result.OK("结案成功");
        } else {
            return Result.error("结案失败，请稍后重试");
        }
    }

    /**
     * 要求整改
     * <p>
     * 下发整改要求，创建整改记录
     * </p>
     *
     * @param id          报告ID
     * @param requirement 整改要求
     * @param deadline    整改期限
     * @return 操作结果
     */
    @AutoLog(value = "输血处理-要求整改")
    @Operation(summary = "要求整改")
    @PostMapping(value = "/requireRectify")
    public Result<String> requireRectify(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "requirement") String requirement,
            @RequestParam(name = "deadline") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 校验必填参数
        if (oConvertUtils.isEmpty(requirement)) {
            return Result.error("整改要求不能为空");
        }
        if (deadline == null) {
            return Result.error("整改期限不能为空");
        }

        // 校验状态
        if (!reportService.canProcess(id)) {
            return Result.error("当前报告状态不允许下发整改");
        }

        // 执行要求整改
        boolean success = reportService.requireRectify(id, requirement, deadline,
                loginUser.getId(), loginUser.getRealname());
        if (success) {
            return Result.OK("整改要求已下发");
        } else {
            return Result.error("下发失败，请稍后重试");
        }
    }

    /**
     * 确认整改通过
     * <p>
     * 确认整改已完成，报告结案
     * </p>
     *
     * @param id      报告ID
     * @param comment 确认意见
     * @return 操作结果
     */
    @AutoLog(value = "输血处理-确认整改通过")
    @Operation(summary = "确认整改通过")
    @PostMapping(value = "/confirmRectify")
    public Result<String> confirmRectify(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment", required = false) String comment) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 校验整改状态
        if (!reportService.canConfirmRectify(id)) {
            return Result.error("当前状态不允许确认整改");
        }

        // 确认整改通过并结案
        boolean success = reportService.confirmRectifyAndClose(id,
                loginUser.getId(), loginUser.getRealname(), comment);
        if (success) {
            return Result.OK("整改确认通过，报告已结案");
        } else {
            return Result.error("确认失败，请稍后重试");
        }
    }

    /**
     * 退回整改
     * <p>
     * 退回整改，要求重新整改
     * </p>
     *
     * @param id      报告ID
     * @param comment 退回原因
     * @return 操作结果
     */
    @AutoLog(value = "输血处理-退回整改")
    @Operation(summary = "退回整改")
    @PostMapping(value = "/rejectRectify")
    public Result<String> rejectRectify(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment") String comment) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 校验退回原因
        if (oConvertUtils.isEmpty(comment)) {
            return Result.error("退回原因不能为空");
        }

        // 校验整改状态
        if (!reportService.canConfirmRectify(id)) {
            return Result.error("当前状态不允许退回整改");
        }

        // 退回整改
        boolean success = reportService.confirmRectifyReject(id,
                loginUser.getId(), loginUser.getRealname(), comment);
        if (success) {
            return Result.OK("整改已退回");
        } else {
            return Result.error("退回失败，请稍后重试");
        }
    }

    // ==================== 查询详情接口 ====================

    /**
     * 获取报告处理详情
     * <p>
     * 获取报告完整信息，用于处理页面展示
     * </p>
     *
     * @param id 报告ID
     * @return 报告详情
     */
    @AutoLog(value = "输血处理-获取详情")
    @Operation(summary = "获取报告处理详情")
    @GetMapping(value = "/detail")
    public Result<BloodAdverseReport> getProcessDetail(@RequestParam(name = "id") String id) {
        BloodAdverseReport report = reportService.getById(id);
        if (report == null) {
            return Result.error("报告不存在");
        }
        return Result.OK(report);
    }

    /**
     * 获取整改记录列表
     * <p>
     * 查询指定报告的所有整改记录
     * </p>
     *
     * @param reportId 报告ID
     * @return 整改记录列表
     */
    @AutoLog(value = "输血处理-获取整改记录")
    @Operation(summary = "获取整改记录列表")
    @GetMapping(value = "/rectifyHistory")
    public Result<List<BloodAdverseRectify>> getRectifyHistory(@RequestParam(name = "reportId") String reportId) {
        List<BloodAdverseRectify> rectifyList = rectifyService.selectByReportId(reportId);
        return Result.OK(rectifyList);
    }

    /**
     * 获取报告流转历史
     * <p>
     * 查询指定报告的所有流转记录
     * </p>
     *
     * @param reportId 报告ID
     * @return 流转记录列表
     */
    @AutoLog(value = "输血处理-获取流转历史")
    @Operation(summary = "获取报告流转历史")
    @GetMapping(value = "/flowHistory")
    public Result<List<BloodAdverseFlow>> getFlowHistory(@RequestParam(name = "reportId") String reportId) {
        List<BloodAdverseFlow> flowList = flowService.getFlowListByReportId(reportId);
        return Result.OK(flowList);
    }
}
