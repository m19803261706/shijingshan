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
import org.jeecg.modules.adverse.entity.DrugAdverseFlow;
import org.jeecg.modules.adverse.entity.DrugAdverseReport;
import org.jeecg.modules.adverse.service.IDrugAdverseFlowService;
import org.jeecg.modules.adverse.service.IDrugAdverseReportService;
import org.jeecg.modules.adverse.vo.DrugAdverseReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 药品不良反应审核 Controller
 * <p>
 * 提供药品不良反应报告审核功能的 API 接口，包括：
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
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/45">#45</a>
 */
@Slf4j
@Tag(name = "药品不良反应审核管理")
@RestController
@RequestMapping("/adverse/drug/audit")
public class DrugAuditController {

    /**
     * 报告状态常量
     */
    private static final String STATUS_PENDING_AUDIT = "pending_audit";
    private static final String STATUS_PENDING_PROCESS = "pending_process";
    private static final String STATUS_RETURNED = "returned";

    /**
     * 流转操作类型常量
     */
    private static final String ACTION_AUDIT_PASS = "audit_pass";
    private static final String ACTION_AUDIT_REJECT = "audit_reject";

    @Autowired
    private IDrugAdverseReportService reportService;

    @Autowired
    private IDrugAdverseFlowService flowService;

    /**
     * 获取待审核报告列表
     * <p>
     * 查询当前登录用户所在科室的待审核药品不良反应报告
     * </p>
     *
     * @param pageNo       页码
     * @param pageSize     每页条数
     * @param reportNo     报告编号（模糊查询）
     * @param patientName  患者姓名（模糊查询）
     * @return 分页结果
     */
    @AutoLog(value = "药品审核-待审核列表")
    @Operation(summary = "获取待审核报告列表")
    @GetMapping(value = "/pending")
    public Result<IPage<DrugAdverseReport>> getPendingList(
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
        LambdaQueryWrapper<DrugAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询待审核状态
        queryWrapper.eq(DrugAdverseReport::getStatus, STATUS_PENDING_AUDIT);
        // 只查询当前用户所在科室的报告
        queryWrapper.eq(DrugAdverseReport::getDepartmentId, loginUser.getOrgCode());
        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportNo)) {
            queryWrapper.like(DrugAdverseReport::getReportCode, reportNo);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(DrugAdverseReport::getPatientName, patientName);
        }
        // 按创建时间倒序
        queryWrapper.orderByDesc(DrugAdverseReport::getCreateTime);

        // 分页查询
        Page<DrugAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<DrugAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取已审核报告列表
     * <p>
     * 查询当前登录用户所在科室的已审核药品不良反应报告（已通过或已退回）
     * </p>
     *
     * @param pageNo      页码
     * @param pageSize    每页条数
     * @param reportNo    报告编号（模糊查询）
     * @param patientName 患者姓名（模糊查询）
     * @param status      状态筛选（pending_process-已通过，returned-已退回）
     * @return 分页结果
     */
    @AutoLog(value = "药品审核-已审核列表")
    @Operation(summary = "获取已审核报告列表")
    @GetMapping(value = "/completed")
    public Result<IPage<DrugAdverseReport>> getCompletedList(
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
        LambdaQueryWrapper<DrugAdverseReport> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询当前用户审核过的报告（auditUserId = 当前用户）
        queryWrapper.eq(DrugAdverseReport::getAuditUserId, loginUser.getId());
        // 排除待审核状态（待审核的不算已审核）
        queryWrapper.ne(DrugAdverseReport::getStatus, STATUS_PENDING_AUDIT);
        // 如果指定了状态筛选
        if (oConvertUtils.isNotEmpty(status)) {
            queryWrapper.eq(DrugAdverseReport::getStatus, status);
        }
        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportNo)) {
            queryWrapper.like(DrugAdverseReport::getReportCode, reportNo);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(DrugAdverseReport::getPatientName, patientName);
        }
        // 按审核时间倒序
        queryWrapper.orderByDesc(DrugAdverseReport::getAuditTime);

        // 分页查询
        Page<DrugAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<DrugAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 审核通过
     * <p>
     * 将报告状态从待审核改为待处理，流转至职能科室
     * </p>
     *
     * @param id      报告ID
     * @param comment 审核意见（选填）
     * @return 操作结果
     */
    @AutoLog(value = "药品审核-审核通过")
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
    @AutoLog(value = "药品审核-审核退回")
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
     * 获取报告完整信息（含子表），用于审核页面展示
     * </p>
     *
     * @param id 报告ID
     * @return 报告详情
     */
    @AutoLog(value = "药品审核-获取详情")
    @Operation(summary = "获取报告审核详情")
    @GetMapping(value = "/detail")
    public Result<DrugAdverseReportVO> getAuditDetail(@RequestParam(name = "id") String id) {
        DrugAdverseReport report = reportService.getById(id);
        if (report == null) {
            return Result.error("报告不存在");
        }

        // 构建 VO，包含子表数据
        DrugAdverseReportVO vo = new DrugAdverseReportVO();
        vo.setReport(report);
        vo.setSuspectDrugs(reportService.getSuspectDrugsByReportId(id));
        vo.setConcomitantDrugs(reportService.getConcomitantDrugsByReportId(id));

        return Result.OK(vo);
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
    @AutoLog(value = "药品审核-获取流转历史")
    @Operation(summary = "获取报告流转历史")
    @GetMapping(value = "/flowHistory")
    public Result<List<DrugAdverseFlow>> getFlowHistory(@RequestParam(name = "reportId") String reportId) {
        List<DrugAdverseFlow> flowList = flowService.getFlowListByReportId(reportId);
        return Result.OK(flowList);
    }
}
