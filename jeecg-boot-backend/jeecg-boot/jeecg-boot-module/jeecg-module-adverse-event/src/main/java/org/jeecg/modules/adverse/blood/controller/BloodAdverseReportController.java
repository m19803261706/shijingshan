package org.jeecg.modules.adverse.blood.controller;

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
import org.jeecg.modules.adverse.blood.entity.BloodAdverseFlow;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseRectify;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseReport;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseFlowService;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseRectifyService;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 输血使用不良事件报告 Controller
 * <p>
 * 提供输血使用不良事件报告的 CRUD API 接口
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/80">Issue #80</a>
 */
@Slf4j
@Tag(name = "输血使用不良事件报告")
@RestController
@RequestMapping("/adverse/blood/report")
public class BloodAdverseReportController {

    @Autowired
    private IBloodAdverseReportService reportService;

    @Autowired
    private IBloodAdverseFlowService flowService;

    @Autowired
    private IBloodAdverseRectifyService rectifyService;

    /**
     * 分页查询报告列表
     *
     * @param report   查询条件
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param req      请求对象
     * @return 分页结果
     */
    @AutoLog(value = "输血不良事件报告-分页列表查询")
    @Operation(summary = "分页查询报告列表")
    @GetMapping(value = "/list")
    public Result<IPage<BloodAdverseReport>> queryPageList(BloodAdverseReport report,
                                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                            HttpServletRequest req) {
        QueryWrapper<BloodAdverseReport> queryWrapper = QueryGenerator.initQueryWrapper(report, req.getParameterMap());
        queryWrapper.orderByDesc("create_time");

        Page<BloodAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<BloodAdverseReport> pageList = reportService.page(page, queryWrapper);

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
    @AutoLog(value = "输血不良事件报告-我的报告列表")
    @Operation(summary = "我的报告列表")
    @GetMapping(value = "/myList")
    public Result<IPage<BloodAdverseReport>> queryMyList(BloodAdverseReport report,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          HttpServletRequest req) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        QueryWrapper<BloodAdverseReport> queryWrapper = QueryGenerator.initQueryWrapper(report, req.getParameterMap());
        // 仅查询本人创建的报告
        queryWrapper.eq("create_by", loginUser.getUsername());
        queryWrapper.orderByDesc("create_time");

        Page<BloodAdverseReport> page = new Page<>(pageNo, pageSize);
        IPage<BloodAdverseReport> pageList = reportService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 根据ID查询报告详情
     *
     * @param id 报告ID
     * @return 报告详情
     */
    @AutoLog(value = "输血不良事件报告-查询详情")
    @Operation(summary = "根据ID查询报告详情")
    @GetMapping(value = "/queryById")
    public Result<BloodAdverseReport> queryById(@RequestParam(name = "id") String id) {
        BloodAdverseReport report = reportService.getById(id);
        if (report == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(report);
    }

    /**
     * 新增报告（保存草稿）
     *
     * @param report 报告数据
     * @return 操作结果
     */
    @AutoLog(value = "输血不良事件报告-新增")
    @Operation(summary = "新增报告（保存草稿）")
    @PostMapping(value = "/add")
    public Result<BloodAdverseReport> add(@RequestBody BloodAdverseReport report) {
        try {
            BloodAdverseReport saved = reportService.saveDraft(report);
            return Result.OK("添加成功！", saved);
        } catch (Exception e) {
            log.error("新增报告失败", e);
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 编辑报告
     *
     * @param report 报告数据
     * @return 操作结果
     */
    @AutoLog(value = "输血不良事件报告-编辑")
    @Operation(summary = "编辑报告")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<BloodAdverseReport> edit(@RequestBody BloodAdverseReport report) {
        // 校验是否可编辑
        if (report == null || report.getId() == null) {
            return Result.error("报告ID不能为空");
        }

        if (!reportService.canEdit(report.getId())) {
            return Result.error("当前状态不允许编辑");
        }

        try {
            BloodAdverseReport saved = reportService.saveDraft(report);
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
    @AutoLog(value = "输血不良事件报告-删除")
    @Operation(summary = "删除报告")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id") String id) {
        if (reportService.deleteReport(id)) {
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
    @AutoLog(value = "输血不良事件报告-批量删除")
    @Operation(summary = "批量删除报告")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        int successCount = 0;
        for (String id : idList) {
            if (reportService.deleteReport(id)) {
                successCount++;
            }
        }
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
    @AutoLog(value = "输血不良事件报告-提交")
    @Operation(summary = "提交报告")
    @PostMapping(value = "/submit")
    public Result<String> submit(@RequestParam(name = "id") String id) {
        if (reportService.submitReport(id)) {
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
     * @param report 报告数据
     * @return 操作结果
     */
    @AutoLog(value = "输血不良事件报告-保存草稿")
    @Operation(summary = "保存草稿")
    @PostMapping(value = "/saveDraft")
    public Result<BloodAdverseReport> saveDraft(@RequestBody BloodAdverseReport report) {
        try {
            BloodAdverseReport saved = reportService.saveDraft(report);
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
        return Result.OK(reportService.canEdit(id));
    }

    /**
     * 获取流转记录列表
     *
     * @param reportId 报告ID
     * @return 流转记录列表
     */
    @Operation(summary = "获取流转记录列表")
    @GetMapping(value = "/flowHistory")
    public Result<List<BloodAdverseFlow>> getFlowHistory(@RequestParam(name = "reportId") String reportId) {
        List<BloodAdverseFlow> list = flowService.getFlowListByReportId(reportId);
        return Result.OK(list);
    }

    /**
     * 获取整改记录列表
     *
     * @param reportId 报告ID
     * @return 整改记录列表
     */
    @Operation(summary = "获取整改记录列表")
    @GetMapping(value = "/rectifyHistory")
    public Result<List<BloodAdverseRectify>> getRectifyHistory(@RequestParam(name = "reportId") String reportId) {
        List<BloodAdverseRectify> list = rectifyService.selectByReportId(reportId);
        return Result.OK(list);
    }
}
