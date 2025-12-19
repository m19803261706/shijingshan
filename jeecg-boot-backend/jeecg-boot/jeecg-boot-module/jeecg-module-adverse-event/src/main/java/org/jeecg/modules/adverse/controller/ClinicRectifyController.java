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
import org.jeecg.modules.adverse.entity.DrugAdverseRectify;
import org.jeecg.modules.adverse.entity.DrugAdverseReport;
import org.jeecg.modules.adverse.service.IDrugAdverseFlowService;
import org.jeecg.modules.adverse.service.IDrugAdverseRectifyService;
import org.jeecg.modules.adverse.service.IDrugAdverseReportService;
import org.jeecg.modules.adverse.vo.DrugAdverseReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 临床科室整改提交 Controller
 * <p>
 * 提供临床科室人员查看和提交整改的 API 接口，包括：
 * - 药品不良反应待整改列表
 * - 整改详情查看
 * - 整改措施提交
 * - 整改历史查询
 * </p>
 * <p>
 * 支持多科室类型扩展（药品不良反应、护理部、医务科等）
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/54">#54</a>
 */
@Slf4j
@Tag(name = "临床科室整改提交管理")
@RestController
@RequestMapping("/adverse/clinic/rectify")
public class ClinicRectifyController {

    /**
     * 报告状态常量
     */
    private static final String STATUS_PENDING_RECTIFY = "pending_rectify";
    private static final String STATUS_RECTIFYING = "rectifying";

    /**
     * 整改状态常量
     */
    private static final String RECTIFY_STATUS_PENDING = "pending";
    private static final String RECTIFY_STATUS_SUBMITTED = "submitted";
    private static final String RECTIFY_STATUS_REJECTED = "rejected";

    /**
     * 流转操作类型常量
     */
    private static final String ACTION_SUBMIT_RECTIFY = "submit_rectify";

    @Autowired
    private IDrugAdverseReportService reportService;

    @Autowired
    private IDrugAdverseRectifyService rectifyService;

    @Autowired
    private IDrugAdverseFlowService flowService;

    // ==================== 药品不良反应整改 ====================

    /**
     * 获取药品不良反应待整改列表
     * <p>
     * 查询当前用户所属科室的待整改药品不良反应报告
     * 包含：待整改(pending)、已退回(rejected) 状态的整改记录
     * </p>
     *
     * @param pageNo      页码
     * @param pageSize    每页条数
     * @param reportNo    报告编号（模糊查询）
     * @param patientName 患者姓名（模糊查询）
     * @param status      整改状态筛选（pending/rejected/submitted）
     * @return 分页结果
     */
    @AutoLog(value = "临床整改-药品不良反应列表")
    @Operation(summary = "获取药品不良反应待整改列表")
    @GetMapping(value = "/drugList")
    public Result<IPage<DrugAdverseReport>> getDrugRectifyList(
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

        // 查询待整改状态的报告
        queryWrapper.in(DrugAdverseReport::getStatus, STATUS_PENDING_RECTIFY, STATUS_RECTIFYING);

        // 按当前用户的科室筛选
        String userDept = loginUser.getOrgCode();
        if (oConvertUtils.isNotEmpty(userDept)) {
            queryWrapper.eq(DrugAdverseReport::getDepartmentId, userDept);
        }

        // 报告编号模糊查询
        if (oConvertUtils.isNotEmpty(reportNo)) {
            queryWrapper.like(DrugAdverseReport::getReportCode, reportNo);
        }
        // 患者姓名模糊查询
        if (oConvertUtils.isNotEmpty(patientName)) {
            queryWrapper.like(DrugAdverseReport::getPatientName, patientName);
        }

        // 按整改期限排序（即将到期的排前面）
        queryWrapper.orderByAsc(DrugAdverseReport::getUpdateTime);

        // 分页查询
        Page<DrugAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<DrugAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取药品不良反应整改详情
     * <p>
     * 获取报告和当前整改要求的完整信息
     * </p>
     *
     * @param reportId 报告ID
     * @return 报告详情和整改信息
     */
    @AutoLog(value = "临床整改-药品不良反应详情")
    @Operation(summary = "获取药品不良反应整改详情")
    @GetMapping(value = "/drugDetail")
    public Result<DrugAdverseReportVO> getDrugRectifyDetail(@RequestParam(name = "reportId") String reportId) {
        DrugAdverseReport report = reportService.getById(reportId);
        if (report == null) {
            return Result.error("报告不存在");
        }

        // 构建 VO，包含子表数据
        DrugAdverseReportVO vo = new DrugAdverseReportVO();
        vo.setReport(report);
        vo.setSuspectDrugs(reportService.getSuspectDrugsByReportId(reportId));
        vo.setConcomitantDrugs(reportService.getConcomitantDrugsByReportId(reportId));

        return Result.OK(vo);
    }

    /**
     * 提交药品不良反应整改
     * <p>
     * 临床人员填写整改措施并提交
     * </p>
     *
     * @param rectifyId  整改记录ID
     * @param measures   整改措施
     * @param completion 完成情况
     * @return 操作结果
     */
    @AutoLog(value = "临床整改-提交药品不良反应整改")
    @Operation(summary = "提交药品不良反应整改")
    @PostMapping(value = "/drugSubmit")
    public Result<String> submitDrugRectify(
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
        DrugAdverseRectify rectify = rectifyService.getById(rectifyId);
        if (rectify == null) {
            return Result.error("整改记录不存在");
        }

        // 校验整改状态（只有 pending 或 rejected 状态可以提交）
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
        DrugAdverseReport report = reportService.getById(reportId);
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

            log.info("提交整改，报告ID: {}, 编号: {}, 提交人: {}",
                    reportId, report.getReportCode(), loginUser.getRealname());
        }

        return Result.OK("整改提交成功，等待审核确认");
    }

    /**
     * 获取药品不良反应整改历史
     * <p>
     * 查询指定报告的所有整改记录
     * </p>
     *
     * @param reportId 报告ID
     * @return 整改记录列表
     */
    @AutoLog(value = "临床整改-药品不良反应整改历史")
    @Operation(summary = "获取药品不良反应整改历史")
    @GetMapping(value = "/drugHistory")
    public Result<List<DrugAdverseRectify>> getDrugRectifyHistory(@RequestParam(name = "reportId") String reportId) {
        List<DrugAdverseRectify> rectifyList = rectifyService.selectByReportId(reportId);
        return Result.OK(rectifyList);
    }

    /**
     * 获取当前整改记录
     * <p>
     * 查询指定报告的最新（待处理）整改记录
     * </p>
     *
     * @param reportId 报告ID
     * @return 最新整改记录
     */
    @AutoLog(value = "临床整改-获取当前整改记录")
    @Operation(summary = "获取当前整改记录")
    @GetMapping(value = "/drugCurrentRectify")
    public Result<DrugAdverseRectify> getCurrentDrugRectify(@RequestParam(name = "reportId") String reportId) {
        DrugAdverseRectify rectify = rectifyService.getLatestByReportId(reportId);
        if (rectify == null) {
            return Result.error("未找到整改记录");
        }
        return Result.OK(rectify);
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
    @AutoLog(value = "临床整改-获取流转历史")
    @Operation(summary = "获取报告流转历史")
    @GetMapping(value = "/drugFlowHistory")
    public Result<List<DrugAdverseFlow>> getDrugFlowHistory(@RequestParam(name = "reportId") String reportId) {
        List<DrugAdverseFlow> flowList = flowService.getFlowListByReportId(reportId);
        return Result.OK(flowList);
    }

    // ==================== 预留扩展接口 ====================
    // 后续添加：护理部整改、医务科整改等
    // 每个科室类型对应一组 API：
    // - /nursingList, /nursingDetail, /nursingSubmit, /nursingHistory
    // - /medicalList, /medicalDetail, /medicalSubmit, /medicalHistory
}
