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
import org.jeecg.modules.adverse.blood.entity.BloodAdverseReport;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseFlowService;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 输血使用不良事件审核 Controller
 * <p>
 * 提供输血使用不良事件报告审核功能的 API 接口，包括：
 * - 待审核列表查询
 * - 已审核列表查询
 * - 审核通过
 * - 审核退回
 * - 获取报告详情
 * - 获取流转历史
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/80">Issue #80</a>
 */
@Slf4j
@Tag(name = "输血使用不良事件审核管理")
@RestController
@RequestMapping("/adverse/blood/audit")
public class BloodAuditController {

    /**
     * 报告状态常量
     */
    private static final String STATUS_PENDING_AUDIT = "pending_audit";
    private static final String STATUS_PENDING_PROCESS = "pending_process";
    private static final String STATUS_RETURNED = "returned";

    @Autowired
    private IBloodAdverseReportService reportService;

    @Autowired
    private IBloodAdverseFlowService flowService;

    /**
     * 获取待审核报告列表
     * <p>
     * 查询当前登录用户所在科室的待审核输血不良事件报告
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportNo     报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "输血审核-待审核列表")
    @Operation(summary = "获取待审核报告列表")
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
        // 只查询待审核状态
        queryWrapper.eq(BloodAdverseReport::getStatus, STATUS_PENDING_AUDIT);
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
     * 获取已审核报告列表
     * <p>
     * 查询当前登录用户所在科室的已审核输血不良事件报告（已通过或已退回）
     * </p>
     *
     * @param pageNo      页码
     * @param pageSize    每页条数
     * @param reportNo    报告编号（模糊查询）
     * @param patientName 患者姓名（模糊查询）
     * @param status      状态筛选（pending_process-已通过，returned-已退回）
     * @return 分页结果
     */
    @AutoLog(value = "输血审核-已审核列表")
    @Operation(summary = "获取已审核报告列表")
    @GetMapping(value = "/completed")
    public Result<IPage<BloodAdverseReport>> getCompletedList(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportNo", required = false) String reportNo,
            @RequestParam(name = "patientName", required = false) String patientName,
            @RequestParam(name = "status", required = false) String status) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<BloodAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询当前用户审核过的报告（auditUserId = 当前用户）
        queryWrapper.eq(BloodAdverseReport::getAuditUserId, loginUser.getId());
        // 排除待审核状态（待审核的不算已审核）
        queryWrapper.ne(BloodAdverseReport::getStatus, STATUS_PENDING_AUDIT);
        // 如果指定了状态筛选
        if (oConvertUtils.isNotEmpty(status)) {
            queryWrapper.eq(BloodAdverseReport::getStatus, status);
        }
        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportNo)) {
            queryWrapper.like(BloodAdverseReport::getReportCode, reportNo);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(BloodAdverseReport::getPatientName, patientName);
        }
        // 按审核时间倒序
        queryWrapper.orderByDesc(BloodAdverseReport::getAuditTime);

        // 分页查询
        Page<BloodAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<BloodAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 审核通过
     * <p>
     * 将报告状态从待审核改为待处理，流转至输血科
     * </p>
     *
     * @param id      报告ID
     * @param comment 审核意见（选填）
     * @return 操作结果
     */
    @AutoLog(value = "输血审核-审核通过")
    @Operation(summary = "审核通过")
    @PostMapping(value = "/pass")
    public Result<String> auditPass(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "comment", required = false) String comment) {

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 校验报告是否可审核
        if (!reportService.canAudit(id)) {
            return Result.error("该报告当前状态不允许审核");
        }

        // 执行审核通过
        boolean success = reportService.auditPass(id, loginUser.getId(), loginUser.getRealname(), comment);
        if (success) {
            return Result.OK("审核通过");
        } else {
            return Result.error("审核失败，请稍后重试");
        }
    }

    /**
     * 审核退回
     * <p>
     * 将报告状态从待审核改为已退回，需填写退回原因
     * </p>
     *
     * @param id      报告ID
     * @param comment 退回原因（必填）
     * @return 操作结果
     */
    @AutoLog(value = "输血审核-审核退回")
    @Operation(summary = "审核退回")
    @PostMapping(value = "/reject")
    public Result<String> auditReject(
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

        // 校验报告是否可审核
        if (!reportService.canAudit(id)) {
            return Result.error("该报告当前状态不允许审核");
        }

        // 执行审核退回
        boolean success = reportService.auditReject(id, loginUser.getId(), loginUser.getRealname(), comment);
        if (success) {
            return Result.OK("已退回");
        } else {
            return Result.error("退回失败，请稍后重试");
        }
    }

    /**
     * 获取报告审核详情
     * <p>
     * 获取报告完整信息，用于审核页面展示
     * </p>
     *
     * @param id 报告ID
     * @return 报告详情
     */
    @AutoLog(value = "输血审核-获取详情")
    @Operation(summary = "获取报告审核详情")
    @GetMapping(value = "/detail")
    public Result<BloodAdverseReport> getAuditDetail(@RequestParam(name = "id") String id) {
        BloodAdverseReport report = reportService.getById(id);
        if (report == null) {
            return Result.error("报告不存在");
        }
        return Result.OK(report);
    }

    /**
     * 获取报告流转历史
     * <p>
     * 查询指定报告的所有流转记录，用于审核页面展示
     * </p>
     *
     * @param reportId 报告ID
     * @return 流转记录列表
     */
    @AutoLog(value = "输血审核-获取流转历史")
    @Operation(summary = "获取报告流转历史")
    @GetMapping(value = "/flowHistory")
    public Result<List<BloodAdverseFlow>> getFlowHistory(@RequestParam(name = "reportId") String reportId) {
        List<BloodAdverseFlow> flowList = flowService.getFlowListByReportId(reportId);
        return Result.OK(flowList);
    }
}
