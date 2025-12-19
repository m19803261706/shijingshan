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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 医疗器械不良事件临床科室整改 Controller
 * <p>
 * 提供临床科室人员查看和提交医疗器械不良事件整改的 API 接口，包括：
 * - 待整改列表查询
 * - 整改详情查看
 * - 整改措施提交
 * - 整改历史查询
 * - 当前整改记录查询
 * - 流转历史查询
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/67">Issue #67</a>
 */
@Slf4j
@Tag(name = "医疗器械不良事件临床科室整改管理")
@RestController
@RequestMapping("/adverse/device/clinicRectify")
public class DeviceClinicRectifyController {

    /**
     * 报告状态常量
     */
    private static final String STATUS_PENDING_RECTIFY = "pending_rectify";
    private static final String STATUS_RECTIFYING = "rectifying";

    /**
     * 流转操作类型常量
     */
    private static final String ACTION_SUBMIT_RECTIFY = "submit_rectify";

    @Autowired
    private IDeviceAdverseReportService reportService;

    @Autowired
    private IDeviceAdverseRectifyService rectifyService;

    @Autowired
    private IDeviceAdverseFlowService flowService;

    // ==================== 待整改列表 ====================

    /**
     * 获取待整改列表
     * <p>
     * 查询当前用户所属科室的待整改医疗器械不良事件报告
     * 包含：待整改(pending_rectify)、整改中(rectifying) 状态的报告
     * </p>
     *
     * @param pageNo      页码
     * @param pageSize    每页条数
     * @param reportCode  报告编号（模糊查询）
     * @param patientName 患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "器械临床整改-待整改列表")
    @Operation(summary = "获取待整改列表")
    @GetMapping(value = "/list")
    public Result<IPage<DeviceAdverseReport>> getPendingRectifyList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportCode", required = false) String reportCode,
            @RequestParam(name = "patientName", required = false) String patientName) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<DeviceAdverseReport> queryWrapper = new LambdaQueryWrapper<>();

        // 查询待整改状态的报告
        queryWrapper.in(DeviceAdverseReport::getStatus, STATUS_PENDING_RECTIFY, STATUS_RECTIFYING);

        // 按当前用户的科室筛选
        String userDept = loginUser.getOrgCode();
        if (oConvertUtils.isNotEmpty(userDept)) {
            queryWrapper.eq(DeviceAdverseReport::getDepartmentId, userDept);
        }

        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportCode)) {
            queryWrapper.like(DeviceAdverseReport::getReportCode, reportCode);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(DeviceAdverseReport::getPatientName, patientName);
        }

        // 按更新时间倒序排序
        queryWrapper.orderByDesc(DeviceAdverseReport::getUpdateTime);

        // 分页查询
        Page<DeviceAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<DeviceAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    // ==================== 整改详情 ====================

    /**
     * 获取整改详情
     * <p>
     * 获取报告完整信息，用于整改页面展示
     * </p>
     *
     * @param reportId 报告ID
     * @return 报告详情
     */
    @AutoLog(value = "器械临床整改-获取详情")
    @Operation(summary = "获取整改详情")
    @GetMapping(value = "/detail")
    public Result<DeviceAdverseReport> getRectifyDetail(@RequestParam(name = "reportId") String reportId) {
        DeviceAdverseReport report = reportService.getById(reportId);
        if (report == null) {
            return Result.error("报告不存在");
        }
        return Result.OK(report);
    }

    // ==================== 提交整改 ====================

    /**
     * 提交整改
     * <p>
     * 临床人员填写整改措施并提交
     * </p>
     *
     * @param rectifyId  整改记录ID
     * @param measures   整改措施
     * @param completion 完成情况
     * @return 操作结果
     */
    @AutoLog(value = "器械临床整改-提交整改")
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

        // 获取整改记录
        DeviceAdverseRectify rectify = rectifyService.getById(rectifyId);
        if (rectify == null) {
            return Result.error("整改记录不存在");
        }

        // 校验整改状态（只有 pending 状态可以提交）
        if (!rectifyService.canSubmit(rectifyId)) {
            return Result.error("当前整改状态不允许提交");
        }

        // 提交整改
        boolean success = rectifyService.submitRectify(rectifyId, measures, completion,
                loginUser.getId(), loginUser.getRealname());
        if (!success) {
            return Result.error("提交失败，请稍后重试");
        }

        // 更新报告状态为整改中
        String reportId = rectify.getReportId();
        DeviceAdverseReport report = reportService.getById(reportId);
        if (report != null) {
            Date now = new Date();
            String oldStatus = report.getStatus();
            report.setStatus(STATUS_RECTIFYING);
            report.setUpdateBy(loginUser.getUsername());
            report.setUpdateTime(now);

            reportService.updateById(report);

            // 记录流转历史
            flowService.recordFlow(reportId, ACTION_SUBMIT_RECTIFY, oldStatus, STATUS_RECTIFYING,
                    loginUser.getId(), loginUser.getRealname(), "整改措施: " + measures);

            log.info("提交器械整改，报告ID: {}, 编号: {}, 提交人: {}",
                    reportId, report.getReportCode(), loginUser.getRealname());
        }

        return Result.OK("整改提交成功，等待审核确认");
    }

    // ==================== 整改历史 ====================

    /**
     * 获取整改历史
     * <p>
     * 查询指定报告的所有整改记录
     * </p>
     *
     * @param reportId 报告ID
     * @return 整改记录列表
     */
    @AutoLog(value = "器械临床整改-整改历史")
    @Operation(summary = "获取整改历史")
    @GetMapping(value = "/history")
    public Result<List<DeviceAdverseRectify>> getRectifyHistory(@RequestParam(name = "reportId") String reportId) {
        List<DeviceAdverseRectify> rectifyList = rectifyService.selectByReportId(reportId);
        return Result.OK(rectifyList);
    }

    // ==================== 当前整改记录 ====================

    /**
     * 获取当前整改记录
     * <p>
     * 查询指定报告的最新（待处理）整改记录
     * </p>
     *
     * @param reportId 报告ID
     * @return 最新整改记录
     */
    @AutoLog(value = "器械临床整改-获取当前整改记录")
    @Operation(summary = "获取当前整改记录")
    @GetMapping(value = "/currentRectify")
    public Result<DeviceAdverseRectify> getCurrentRectify(@RequestParam(name = "reportId") String reportId) {
        DeviceAdverseRectify rectify = rectifyService.getLatestByReportId(reportId);
        if (rectify == null) {
            return Result.error("未找到整改记录");
        }
        return Result.OK(rectify);
    }

    // ==================== 流转历史 ====================

    /**
     * 获取报告流转历史
     * <p>
     * 查询指定报告的所有流转记录
     * </p>
     *
     * @param reportId 报告ID
     * @return 流转记录列表
     */
    @AutoLog(value = "器械临床整改-获取流转历史")
    @Operation(summary = "获取报告流转历史")
    @GetMapping(value = "/flowHistory")
    public Result<List<DeviceAdverseFlow>> getFlowHistory(@RequestParam(name = "reportId") String reportId) {
        List<DeviceAdverseFlow> flowList = flowService.getFlowListByReportId(reportId);
        return Result.OK(flowList);
    }
}
