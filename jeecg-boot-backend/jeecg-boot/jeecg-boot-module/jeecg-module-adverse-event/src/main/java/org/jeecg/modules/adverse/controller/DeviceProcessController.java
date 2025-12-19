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
import org.jeecg.modules.adverse.entity.DeviceAdverseFlow;
import org.jeecg.modules.adverse.entity.DeviceAdverseRectify;
import org.jeecg.modules.adverse.entity.DeviceAdverseReport;
import org.jeecg.modules.adverse.service.IDeviceAdverseFlowService;
import org.jeecg.modules.adverse.service.IDeviceAdverseRectifyService;
import org.jeecg.modules.adverse.service.IDeviceAdverseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 医疗器械不良事件器械科处理 Controller
 * <p>
 * 提供器械科处理医疗器械不良事件报告的 API 接口，包括：
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
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/66">Issue #66</a>
 */
@Slf4j
@Tag(name = "医疗器械不良事件器械科处理管理")
@RestController
@RequestMapping("/adverse/device/process")
public class DeviceProcessController {

    /**
     * 报告状态常量
     */
    private static final String STATUS_PENDING_PROCESS = "pending_process";
    private static final String STATUS_PENDING_RECTIFY = "pending_rectify";
    private static final String STATUS_RECTIFYING = "rectifying";
    private static final String STATUS_CLOSED = "closed";

    /**
     * 结案方式常量
     */
    private static final String CLOSE_TYPE_DIRECT = "direct";
    private static final String CLOSE_TYPE_RECTIFY = "rectify";

    /**
     * 流转操作类型常量
     */
    private static final String ACTION_CLOSE_DIRECT = "close_direct";
    private static final String ACTION_REQUIRE_RECTIFY = "require_rectify";
    private static final String ACTION_CONFIRM_APPROVE = "confirm_approve";
    private static final String ACTION_CONFIRM_REJECT = "confirm_reject";

    @Autowired
    private IDeviceAdverseReportService reportService;

    @Autowired
    private IDeviceAdverseRectifyService rectifyService;

    @Autowired
    private IDeviceAdverseFlowService flowService;

    // ==================== 列表查询接口 ====================

    /**
     * 获取待处理报告列表
     * <p>
     * 查询待处理状态的医疗器械不良事件报告（status = pending_process）
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportCode   报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "器械处理-待处理列表")
    @Operation(summary = "获取待处理报告列表")
    @GetMapping(value = "/pending")
    public Result<IPage<DeviceAdverseReport>> getPendingList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportCode", required = false) String reportCode,
            @RequestParam(name = "patientName", required = false) String patientName) {

        // 构建查询条件
        LambdaQueryWrapper<DeviceAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询待处理状态
        queryWrapper.eq(DeviceAdverseReport::getStatus, STATUS_PENDING_PROCESS);
        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportCode)) {
            queryWrapper.like(DeviceAdverseReport::getReportCode, reportCode);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(DeviceAdverseReport::getPatientName, patientName);
        }
        // 按创建时间倒序
        queryWrapper.orderByDesc(DeviceAdverseReport::getCreateTime);

        // 分页查询
        Page<DeviceAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<DeviceAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取待确认报告列表
     * <p>
     * 查询待整改确认状态的医疗器械不良事件报告（status = pending_rectify 或 rectifying）
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportCode   报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "器械处理-待确认列表")
    @Operation(summary = "获取待确认报告列表")
    @GetMapping(value = "/confirming")
    public Result<IPage<DeviceAdverseReport>> getConfirmingList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportCode", required = false) String reportCode,
            @RequestParam(name = "patientName", required = false) String patientName) {

        // 构建查询条件
        LambdaQueryWrapper<DeviceAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 查询待整改和整改中状态
        queryWrapper.in(DeviceAdverseReport::getStatus, STATUS_PENDING_RECTIFY, STATUS_RECTIFYING);
        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportCode)) {
            queryWrapper.like(DeviceAdverseReport::getReportCode, reportCode);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(DeviceAdverseReport::getPatientName, patientName);
        }
        // 按创建时间倒序
        queryWrapper.orderByDesc(DeviceAdverseReport::getCreateTime);

        // 分页查询
        Page<DeviceAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<DeviceAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取已结案报告列表
     * <p>
     * 查询已结案状态的医疗器械不良事件报告（status = closed）
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportCode   报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @param closeType    结案方式筛选（direct-直接结案，rectify-整改结案）
     * @return 分页结果
     */
    @AutoLog(value = "器械处理-已结案列表")
    @Operation(summary = "获取已结案报告列表")
    @GetMapping(value = "/closed")
    public Result<IPage<DeviceAdverseReport>> getClosedList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportCode", required = false) String reportCode,
            @RequestParam(name = "patientName", required = false) String patientName,
            @RequestParam(name = "closeType", required = false) String closeType) {

        // 构建查询条件
        LambdaQueryWrapper<DeviceAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询已结案状态
        queryWrapper.eq(DeviceAdverseReport::getStatus, STATUS_CLOSED);
        // 结案方式筛选
        if (oConvertUtils.isNotEmpty(closeType)) {
            queryWrapper.eq(DeviceAdverseReport::getCloseType, closeType);
        }
        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportCode)) {
            queryWrapper.like(DeviceAdverseReport::getReportCode, reportCode);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(DeviceAdverseReport::getPatientName, patientName);
        }
        // 按结案时间倒序
        queryWrapper.orderByDesc(DeviceAdverseReport::getCloseTime);

        // 分页查询
        Page<DeviceAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<DeviceAdverseReport> pageList = reportService.page(page, queryWrapper);

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
    @AutoLog(value = "器械处理-直接结案")
    @Operation(summary = "直接结案")
    @PostMapping(value = "/closeDirect")
    public Result<String> closeDirect(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment", required = false) String comment) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 获取报告
        DeviceAdverseReport report = reportService.getById(id);
        if (report == null) {
            return Result.error("报告不存在");
        }

        // 校验状态
        if (!STATUS_PENDING_PROCESS.equals(report.getStatus())) {
            return Result.error("当前报告状态不允许直接结案");
        }

        // 更新报告状态
        Date now = new Date();
        String oldStatus = report.getStatus();
        report.setStatus(STATUS_CLOSED);
        report.setCloseType(CLOSE_TYPE_DIRECT);
        report.setProcessUserId(loginUser.getId());
        report.setProcessUserName(loginUser.getRealname());
        report.setProcessTime(now);
        report.setProcessComment(comment);
        report.setCloseUserId(loginUser.getId());
        report.setCloseUserName(loginUser.getRealname());
        report.setCloseTime(now);
        report.setCloseComment(comment);
        report.setUpdateBy(loginUser.getUsername());
        report.setUpdateTime(now);

        reportService.updateById(report);

        // 记录流转历史
        flowService.recordFlow(id, ACTION_CLOSE_DIRECT, oldStatus, STATUS_CLOSED,
                loginUser.getId(), loginUser.getRealname(), comment);

        log.info("直接结案，报告ID: {}, 编号: {}, 处理人: {}",
                id, report.getReportCode(), loginUser.getRealname());
        return Result.OK("结案成功");
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
     * @param comment     备注
     * @return 操作结果
     */
    @AutoLog(value = "器械处理-要求整改")
    @Operation(summary = "要求整改")
    @PostMapping(value = "/requireRectify")
    public Result<String> requireRectify(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "requirement") String requirement,
            @RequestParam(name = "deadline") @DateTimeFormat(pattern = "yyyy-MM-dd") Date deadline,
            @RequestParam(name = "comment", required = false) String comment) {

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

        // 获取报告
        DeviceAdverseReport report = reportService.getById(id);
        if (report == null) {
            return Result.error("报告不存在");
        }

        // 校验状态
        if (!STATUS_PENDING_PROCESS.equals(report.getStatus())) {
            return Result.error("当前报告状态不允许下发整改");
        }

        // 创建整改记录
        rectifyService.createRectify(id, requirement, deadline,
                loginUser.getId(), loginUser.getRealname(), comment);

        // 更新报告状态
        Date now = new Date();
        String oldStatus = report.getStatus();
        report.setStatus(STATUS_PENDING_RECTIFY);
        report.setProcessUserId(loginUser.getId());
        report.setProcessUserName(loginUser.getRealname());
        report.setProcessTime(now);
        report.setProcessComment(comment);
        report.setUpdateBy(loginUser.getUsername());
        report.setUpdateTime(now);

        reportService.updateById(report);

        // 记录流转历史
        flowService.recordFlow(id, ACTION_REQUIRE_RECTIFY, oldStatus, STATUS_PENDING_RECTIFY,
                loginUser.getId(), loginUser.getRealname(), "整改要求: " + requirement);

        log.info("下发整改要求，报告ID: {}, 编号: {}, 处理人: {}",
                id, report.getReportCode(), loginUser.getRealname());
        return Result.OK("整改要求已下发");
    }

    /**
     * 确认整改通过
     * <p>
     * 确认整改已完成，报告结案
     * </p>
     *
     * @param rectifyId 整改记录ID
     * @param comment   确认意见
     * @return 操作结果
     */
    @AutoLog(value = "器械处理-确认整改通过")
    @Operation(summary = "确认整改通过")
    @PostMapping(value = "/confirmApprove")
    public Result<String> confirmApprove(
            @RequestParam(name = "rectifyId") String rectifyId,
            @RequestParam(name = "comment", required = false) String comment) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 获取整改记录
        DeviceAdverseRectify rectify = rectifyService.getById(rectifyId);
        if (rectify == null) {
            return Result.error("整改记录不存在");
        }

        // 校验整改状态
        if (!rectifyService.canConfirm(rectifyId)) {
            return Result.error("当前整改状态不允许确认");
        }

        // 确认整改通过
        boolean success = rectifyService.confirmApprove(rectifyId,
                loginUser.getId(), loginUser.getRealname(), comment);
        if (!success) {
            return Result.error("确认失败，请稍后重试");
        }

        // 更新报告状态为结案
        String reportId = rectify.getReportId();
        DeviceAdverseReport report = reportService.getById(reportId);
        if (report != null) {
            Date now = new Date();
            String oldStatus = report.getStatus();
            report.setStatus(STATUS_CLOSED);
            report.setCloseType(CLOSE_TYPE_RECTIFY);
            report.setCloseUserId(loginUser.getId());
            report.setCloseUserName(loginUser.getRealname());
            report.setCloseTime(now);
            report.setCloseComment(comment);
            report.setUpdateBy(loginUser.getUsername());
            report.setUpdateTime(now);

            reportService.updateById(report);

            // 记录流转历史
            flowService.recordFlow(reportId, ACTION_CONFIRM_APPROVE, oldStatus, STATUS_CLOSED,
                    loginUser.getId(), loginUser.getRealname(), comment);

            log.info("确认整改通过，报告ID: {}, 编号: {}, 确认人: {}",
                    reportId, report.getReportCode(), loginUser.getRealname());
        }

        return Result.OK("整改确认通过，报告已结案");
    }

    /**
     * 退回整改
     * <p>
     * 退回整改，要求重新整改
     * </p>
     *
     * @param rectifyId 整改记录ID
     * @param comment   退回原因
     * @return 操作结果
     */
    @AutoLog(value = "器械处理-退回整改")
    @Operation(summary = "退回整改")
    @PostMapping(value = "/confirmReject")
    public Result<String> confirmReject(
            @RequestParam(name = "rectifyId") String rectifyId,
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

        // 获取整改记录
        DeviceAdverseRectify rectify = rectifyService.getById(rectifyId);
        if (rectify == null) {
            return Result.error("整改记录不存在");
        }

        // 校验整改状态
        if (!rectifyService.canConfirm(rectifyId)) {
            return Result.error("当前整改状态不允许退回");
        }

        // 退回整改
        boolean success = rectifyService.confirmReject(rectifyId,
                loginUser.getId(), loginUser.getRealname(), comment);
        if (!success) {
            return Result.error("退回失败，请稍后重试");
        }

        // 更新报告状态为待整改
        String reportId = rectify.getReportId();
        DeviceAdverseReport report = reportService.getById(reportId);
        if (report != null) {
            Date now = new Date();
            String oldStatus = report.getStatus();
            report.setStatus(STATUS_PENDING_RECTIFY);
            report.setUpdateBy(loginUser.getUsername());
            report.setUpdateTime(now);

            reportService.updateById(report);

            // 记录流转历史
            flowService.recordFlow(reportId, ACTION_CONFIRM_REJECT, oldStatus, STATUS_PENDING_RECTIFY,
                    loginUser.getId(), loginUser.getRealname(), comment);

            log.info("退回整改，报告ID: {}, 编号: {}, 确认人: {}, 原因: {}",
                    reportId, report.getReportCode(), loginUser.getRealname(), comment);
        }

        return Result.OK("整改已退回");
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
    @AutoLog(value = "器械处理-获取详情")
    @Operation(summary = "获取报告处理详情")
    @GetMapping(value = "/detail")
    public Result<DeviceAdverseReport> getProcessDetail(@RequestParam(name = "id") String id) {
        DeviceAdverseReport report = reportService.getById(id);
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
    @AutoLog(value = "器械处理-获取整改记录")
    @Operation(summary = "获取整改记录列表")
    @GetMapping(value = "/rectifyHistory")
    public Result<List<DeviceAdverseRectify>> getRectifyHistory(@RequestParam(name = "reportId") String reportId) {
        List<DeviceAdverseRectify> rectifyList = rectifyService.selectByReportId(reportId);
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
    @AutoLog(value = "器械处理-获取流转历史")
    @Operation(summary = "获取报告流转历史")
    @GetMapping(value = "/flowHistory")
    public Result<List<DeviceAdverseFlow>> getFlowHistory(@RequestParam(name = "reportId") String reportId) {
        List<DeviceAdverseFlow> flowList = flowService.getFlowListByReportId(reportId);
        return Result.OK(flowList);
    }
}
