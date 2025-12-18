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
import org.jeecg.modules.adverse.entity.DrugAdverseConcomitantDrug;
import org.jeecg.modules.adverse.entity.DrugAdverseReport;
import org.jeecg.modules.adverse.entity.DrugAdverseSuspectDrug;
import org.jeecg.modules.adverse.service.IDrugAdverseReportService;
import org.jeecg.modules.adverse.vo.DrugAdverseReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 药品不良反应报告 Controller
 * <p>
 * 提供药品不良反应报告的 CRUD API 接口，支持主子表联动
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Slf4j
@Tag(name = "药品不良反应报告")
@RestController
@RequestMapping("/adverse/drugReport")
public class DrugAdverseReportController {

    @Autowired
    private IDrugAdverseReportService drugAdverseReportService;

    /**
     * 分页查询报告列表
     *
     * @param report   查询条件
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param req      请求对象
     * @return 分页结果
     */
    @AutoLog(value = "药品不良反应报告-分页列表查询")
    @Operation(summary = "分页查询报告列表")
    @GetMapping(value = "/list")
    public Result<IPage<DrugAdverseReport>> queryPageList(DrugAdverseReport report,
                                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest req) {
        QueryWrapper<DrugAdverseReport> queryWrapper = QueryGenerator.initQueryWrapper(report, req.getParameterMap());
        queryWrapper.orderByDesc("create_time");

        Page<DrugAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<DrugAdverseReport> pageList = drugAdverseReportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 我的报告列表
     * <p>
     * 仅查询当前登录用户创建的报告
     * </p>
     *
     * @param report   查询条件
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param req      请求对象
     * @return 分页结果
     */
    @AutoLog(value = "药品不良反应报告-我的报告列表")
    @Operation(summary = "我的报告列表")
    @GetMapping(value = "/myList")
    public Result<IPage<DrugAdverseReport>> queryMyList(DrugAdverseReport report,
                                                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                         HttpServletRequest req) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        QueryWrapper<DrugAdverseReport> queryWrapper = QueryGenerator.initQueryWrapper(report, req.getParameterMap());
        // 仅查询本人创建的报告
        queryWrapper.eq("create_by", loginUser.getUsername());
        queryWrapper.orderByDesc("create_time");

        Page<DrugAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<DrugAdverseReport> pageList = drugAdverseReportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 根据ID查询报告详情
     *
     * @param id 报告ID
     * @return 报告详情
     */
    @AutoLog(value = "药品不良反应报告-查询详情")
    @Operation(summary = "根据ID查询报告详情")
    @GetMapping(value = "/queryById")
    public Result<DrugAdverseReport> queryById(@RequestParam(name = "id") String id) {
        DrugAdverseReport report = drugAdverseReportService.getById(id);
        if (report == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(report);
    }

    /**
     * 根据ID查询报告详情（含子表）
     *
     * @param id 报告ID
     * @return 报告详情（含子表数据）
     */
    @AutoLog(value = "药品不良反应报告-查询详情（含子表）")
    @Operation(summary = "根据ID查询报告详情（含子表）")
    @GetMapping(value = "/queryDetailById")
    public Result<DrugAdverseReportVO> queryDetailById(@RequestParam(name = "id") String id) {
        DrugAdverseReport report = drugAdverseReportService.getById(id);
        if (report == null) {
            return Result.error("未找到对应数据");
        }

        // 获取子表数据
        List<DrugAdverseSuspectDrug> suspectDrugs = drugAdverseReportService.getSuspectDrugsByReportId(id);
        List<DrugAdverseConcomitantDrug> concomitantDrugs = drugAdverseReportService.getConcomitantDrugsByReportId(id);

        // 组装返回结果
        DrugAdverseReportVO vo = new DrugAdverseReportVO();
        vo.setReport(report);
        vo.setSuspectDrugs(suspectDrugs);
        vo.setConcomitantDrugs(concomitantDrugs);

        return Result.OK(vo);
    }

    /**
     * 查询怀疑药品列表
     *
     * @param reportId 报告ID
     * @return 怀疑药品列表
     */
    @Operation(summary = "查询怀疑药品列表")
    @GetMapping(value = "/suspectDrugs")
    public Result<List<DrugAdverseSuspectDrug>> querySuspectDrugs(@RequestParam(name = "reportId") String reportId) {
        List<DrugAdverseSuspectDrug> list = drugAdverseReportService.getSuspectDrugsByReportId(reportId);
        return Result.OK(list);
    }

    /**
     * 查询并用药品列表
     *
     * @param reportId 报告ID
     * @return 并用药品列表
     */
    @Operation(summary = "查询并用药品列表")
    @GetMapping(value = "/concomitantDrugs")
    public Result<List<DrugAdverseConcomitantDrug>> queryConcomitantDrugs(@RequestParam(name = "reportId") String reportId) {
        List<DrugAdverseConcomitantDrug> list = drugAdverseReportService.getConcomitantDrugsByReportId(reportId);
        return Result.OK(list);
    }

    /**
     * 新增报告（保存草稿）
     *
     * @param vo 报告数据（含子表）
     * @return 操作结果
     */
    @AutoLog(value = "药品不良反应报告-新增")
    @Operation(summary = "新增报告（保存草稿）")
    @PostMapping(value = "/add")
    public Result<DrugAdverseReport> add(@RequestBody DrugAdverseReportVO vo) {
        try {
            DrugAdverseReport saved = drugAdverseReportService.saveDraft(
                    vo.getReport(),
                    vo.getSuspectDrugs(),
                    vo.getConcomitantDrugs()
            );
            return Result.OK("添加成功！", saved);
        } catch (Exception e) {
            log.error("新增报告失败", e);
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 编辑报告
     *
     * @param vo 报告数据（含子表）
     * @return 操作结果
     */
    @AutoLog(value = "药品不良反应报告-编辑")
    @Operation(summary = "编辑报告")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<DrugAdverseReport> edit(@RequestBody DrugAdverseReportVO vo) {
        // 校验是否可编辑
        if (vo.getReport() == null || vo.getReport().getId() == null) {
            return Result.error("报告ID不能为空");
        }

        if (!drugAdverseReportService.canEdit(vo.getReport().getId())) {
            return Result.error("当前状态不允许编辑");
        }

        try {
            DrugAdverseReport saved = drugAdverseReportService.saveDraft(
                    vo.getReport(),
                    vo.getSuspectDrugs(),
                    vo.getConcomitantDrugs()
            );
            return Result.OK("编辑成功！", saved);
        } catch (Exception e) {
            log.error("编辑报告失败", e);
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 删除报告
     * <p>
     * 仅草稿状态可删除
     * </p>
     *
     * @param id 报告ID
     * @return 操作结果
     */
    @AutoLog(value = "药品不良反应报告-删除")
    @Operation(summary = "删除报告")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id") String id) {
        if (drugAdverseReportService.deleteReportWithDetails(id)) {
            return Result.OK("删除成功！");
        } else {
            return Result.error("删除失败，仅草稿状态可删除");
        }
    }

    /**
     * 批量删除报告
     *
     * @param ids 报告ID列表，逗号分隔
     * @return 操作结果
     */
    @AutoLog(value = "药品不良反应报告-批量删除")
    @Operation(summary = "批量删除报告")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        int successCount = drugAdverseReportService.deleteBatchWithDetails(idList);
        return Result.OK("成功删除 " + successCount + " 条记录");
    }

    /**
     * 提交报告
     * <p>
     * 将报告状态从草稿/退回改为待审核
     * </p>
     *
     * @param id 报告ID
     * @return 操作结果
     */
    @AutoLog(value = "药品不良反应报告-提交")
    @Operation(summary = "提交报告")
    @PostMapping(value = "/submit")
    public Result<String> submit(@RequestParam(name = "id") String id) {
        if (drugAdverseReportService.submitReport(id)) {
            return Result.OK("提交成功！");
        } else {
            return Result.error("提交失败，请检查必填字段是否完整");
        }
    }

    /**
     * 保存草稿
     * <p>
     * 保存或更新报告草稿
     * </p>
     *
     * @param vo 报告数据（含子表）
     * @return 操作结果
     */
    @AutoLog(value = "药品不良反应报告-保存草稿")
    @Operation(summary = "保存草稿")
    @PostMapping(value = "/saveDraft")
    public Result<DrugAdverseReport> saveDraft(@RequestBody DrugAdverseReportVO vo) {
        try {
            DrugAdverseReport saved = drugAdverseReportService.saveDraft(
                    vo.getReport(),
                    vo.getSuspectDrugs(),
                    vo.getConcomitantDrugs()
            );
            return Result.OK("保存成功！", saved);
        } catch (Exception e) {
            log.error("保存草稿失败", e);
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 校验报告是否可编辑
     *
     * @param id 报告ID
     * @return 是否可编辑
     */
    @Operation(summary = "校验报告是否可编辑")
    @GetMapping(value = "/canEdit")
    public Result<Boolean> canEdit(@RequestParam(name = "id") String id) {
        return Result.OK(drugAdverseReportService.canEdit(id));
    }
}
