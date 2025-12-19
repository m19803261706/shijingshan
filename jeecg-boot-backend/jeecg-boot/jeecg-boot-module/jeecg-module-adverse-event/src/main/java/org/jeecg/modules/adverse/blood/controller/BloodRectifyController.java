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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 输血使用不良事件临床整改 Controller
 * <p>
 * 提供临床人员整改输血使用不良事件的 API 接口，包括：
 * - 待整改列表查询
 * - 已整改列表查询
 * - 提交整改
 * - 获取整改详情
 * - 获取整改记录
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/80">Issue #80</a>
 */
@Slf4j
@Tag(name = "输血使用不良事件临床整改管理")
@RestController
@RequestMapping("/adverse/blood/rectify")
public class BloodRectifyController {

    /**
     * 报告状态常量
     */
    private static final String STATUS_PENDING_RECTIFY = "pending_rectify";
    private static final String STATUS_RECTIFYING = "rectifying";
    private static final String STATUS_CLOSED = "closed";

    /**
     * 流转操作类型常量
     */
    private static final String ACTION_SUBMIT_RECTIFY = "submit_rectify";

    @Autowired
    private IBloodAdverseReportService reportService;

    @Autowired
    private IBloodAdverseRectifyService rectifyService;

    @Autowired
    private IBloodAdverseFlowService flowService;

    // ==================== 列表查询接口 ====================

    /**
     * 获取待整改报告列表
     * <p>
     * 查询当前登录用户所在科室的待整改报告
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportNo     报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "输血整改-待整改列表")
    @Operation(summary = "获取待整改报告列表")
    @GetMapping(value = "/pending")
    public Result<IPage<BloodAdverseReport>> getPendingList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportNo", required = false) String reportNo,
            @RequestParam(name = "patientName", required = false) String patientName) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<BloodAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询待整改状态
        queryWrapper.eq(BloodAdverseReport::getStatus, STATUS_PENDING_RECTIFY);
        // 只查询当前用户所在科室的报告
        queryWrapper.eq(BloodAdverseReport::getSysOrgCode, loginUser.getOrgCode());
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
     * 获取整改中报告列表
     * <p>
     * 查询当前登录用户所在科室的整改中报告（已提交，待确认）
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportNo     报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "输血整改-整改中列表")
    @Operation(summary = "获取整改中报告列表")
    @GetMapping(value = "/submitted")
    public Result<IPage<BloodAdverseReport>> getSubmittedList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportNo", required = false) String reportNo,
            @RequestParam(name = "patientName", required = false) String patientName) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<BloodAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询整改中状态
        queryWrapper.eq(BloodAdverseReport::getStatus, STATUS_RECTIFYING);
        // 只查询当前用户所在科室的报告
        queryWrapper.eq(BloodAdverseReport::getSysOrgCode, loginUser.getOrgCode());
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
     * 查询当前登录用户所在科室的已结案报告
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportNo     报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "输血整改-已结案列表")
    @Operation(summary = "获取已结案报告列表")
    @GetMapping(value = "/closed")
    public Result<IPage<BloodAdverseReport>> getClosedList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportNo", required = false) String reportNo,
            @RequestParam(name = "patientName", required = false) String patientName) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<BloodAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询已结案状态
        queryWrapper.eq(BloodAdverseReport::getStatus, STATUS_CLOSED);
        // 只查询当前用户所在科室的报告
        queryWrapper.eq(BloodAdverseReport::getSysOrgCode, loginUser.getOrgCode());
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

    // ==================== 整改操作接口 ====================

    /**
     * 提交整改
     * <p>
     * 临床人员提交整改措施和完成情况
     * </p>
     *
     * @param rectifyId   整改记录ID
     * @param measures    整改措施
     * @param completion  完成情况
     * @return 操作结果
     */
    @AutoLog(value = "输血整改-提交整改")
    @Operation(summary = "提交整改")
    @PostMapping(value = "/submit")
    public Result<String> submitRectify(
            @RequestParam(name = "rectifyId") String rectifyId,
            @RequestParam(name = "measures") String measures,
            @RequestParam(name = "completion") String completion) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 校验必填参数
        if (oConvertUtils.isEmpty(measures)) {
            return Result.error("整改措施不能为空");
        }
        if (oConvertUtils.isEmpty(completion)) {
            return Result.error("完成情况不能为空");
        }

        // 校验整改状态
        if (!rectifyService.canSubmit(rectifyId)) {
            return Result.error("当前整改状态不允许提交");
        }

        // 获取整改记录
        BloodAdverseRectify rectify = rectifyService.getById(rectifyId);
        if (rectify == null) {
            return Result.error("整改记录不存在");
        }

        // 提交整改
        boolean success = rectifyService.submitRectify(rectifyId, measures, completion,
                loginUser.getId(), loginUser.getRealname());
        if (!success) {
            return Result.error("提交失败，请稍后重试");
        }

        // 更新报告状态为整改中
        String reportId = rectify.getReportId();
        BloodAdverseReport report = reportService.getById(reportId);
        if (report != null && STATUS_PENDING_RECTIFY.equals(report.getStatus())) {
            Date now = new Date();
            String oldStatus = report.getStatus();
            report.setStatus(STATUS_RECTIFYING);
            report.setUpdateBy(loginUser.getUsername());
            report.setUpdateTime(now);

            reportService.updateById(report);

            // 记录流转历史
            flowService.recordFlow(reportId, ACTION_SUBMIT_RECTIFY, oldStatus, STATUS_RECTIFYING,
                    loginUser.getId(), loginUser.getRealname(), "提交整改措施");

            log.info("提交整改，报告ID: {}, 编号: {}, 提交人: {}",
                    reportId, report.getReportCode(), loginUser.getRealname());
        }

        return Result.OK("整改提交成功");
    }

    // ==================== 查询详情接口 ====================

    /**
     * 获取报告整改详情
     * <p>
     * 获取报告完整信息，用于整改页面展示
     * </p>
     *
     * @param id 报告ID
     * @return 报告详情
     */
    @AutoLog(value = "输血整改-获取详情")
    @Operation(summary = "获取报告整改详情")
    @GetMapping(value = "/detail")
    public Result<BloodAdverseReport> getRectifyDetail(@RequestParam(name = "id") String id) {
        BloodAdverseReport report = reportService.getById(id);
        if (report == null) {
            return Result.error("报告不存在");
        }
        return Result.OK(report);
    }

    /**
     * 获取当前待提交的整改记录
     * <p>
     * 查询指定报告的当前待整改记录
     * </p>
     *
     * @param reportId 报告ID
     * @return 当前整改记录
     */
    @AutoLog(value = "输血整改-获取当前整改记录")
    @Operation(summary = "获取当前待提交的整改记录")
    @GetMapping(value = "/currentRectify")
    public Result<BloodAdverseRectify> getCurrentRectify(@RequestParam(name = "reportId") String reportId) {
        BloodAdverseRectify rectify = rectifyService.getLatestByReportId(reportId);
        if (rectify == null) {
            return Result.error("未找到整改记录");
        }
        return Result.OK(rectify);
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
    @AutoLog(value = "输血整改-获取整改记录")
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
    @AutoLog(value = "输血整改-获取流转历史")
    @Operation(summary = "获取报告流转历史")
    @GetMapping(value = "/flowHistory")
    public Result<List<BloodAdverseFlow>> getFlowHistory(@RequestParam(name = "reportId") String reportId) {
        List<BloodAdverseFlow> flowList = flowService.getFlowListByReportId(reportId);
        return Result.OK(flowList);
    }
}
