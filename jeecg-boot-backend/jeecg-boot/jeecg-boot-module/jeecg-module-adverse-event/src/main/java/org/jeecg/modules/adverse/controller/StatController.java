package org.jeecg.modules.adverse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.adverse.entity.AdverseEvent;
import org.jeecg.modules.adverse.entity.EventCategory;
import org.jeecg.modules.adverse.service.IAdverseEventService;
import org.jeecg.modules.adverse.service.IEventCategoryService;
import org.jeecg.modules.adverse.vo.DashboardVO;
import org.jeecg.modules.adverse.vo.StatItemVO;
import org.jeecg.modules.adverse.vo.StatResultVO;
import org.jeecg.modules.adverse.vo.TrendItemVO;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计分析 Controller
 * <p>
 * 提供各维度统计数据的 API 接口，包括：
 * - 按科室/分类/级别/状态统计
 * - 时间趋势统计（月/季/年）
 * - 仪表盘汇总数据
 * - 统计报表导出
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-18
 */
@Slf4j
@Tag(name = "统计分析")
@RestController
@RequestMapping("/adverse/stat")
public class StatController {

    @Autowired
    private IAdverseEventService adverseEventService;

    @Autowired
    private IEventCategoryService categoryService;

    /**
     * 按科室统计
     *
     * @param startDate 开始日期（可选）
     * @param endDate   结束日期（可选）
     * @return 统计结果
     */
    @AutoLog(value = "统计分析-按科室统计")
    @Operation(summary = "按科室统计")
    @GetMapping(value = "/byDepartment")
    public Result<StatResultVO> statByDepartment(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> wrapper = buildTimeRangeWrapper(startDate, endDate);

        // 查询所有事件
        List<AdverseEvent> events = adverseEventService.list(wrapper);

        // 按科室分组统计
        Map<String, Long> countMap = events.stream()
                .filter(e -> e.getDepartmentId() != null)
                .collect(Collectors.groupingBy(AdverseEvent::getDepartmentId, Collectors.counting()));

        // 转换为 StatItemVO 列表
        List<StatItemVO> items = countMap.entrySet().stream()
                .map(entry -> {
                    // TODO: 根据科室 ID 查询科室名称
                    String deptName = "科室-" + entry.getKey();
                    return new StatItemVO(entry.getKey(), deptName, entry.getValue());
                })
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());

        // 构建结果
        StatResultVO result = new StatResultVO();
        result.setTotal((long) events.size());
        result.setItems(items);
        result.calculateRatios();

        return Result.OK(result);
    }

    /**
     * 按分类统计
     *
     * @param startDate 开始日期（可选）
     * @param endDate   结束日期（可选）
     * @return 统计结果
     */
    @AutoLog(value = "统计分析-按分类统计")
    @Operation(summary = "按分类统计")
    @GetMapping(value = "/byCategory")
    public Result<StatResultVO> statByCategory(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> wrapper = buildTimeRangeWrapper(startDate, endDate);

        // 查询所有事件
        List<AdverseEvent> events = adverseEventService.list(wrapper);

        // 按分类分组统计
        Map<String, Long> countMap = events.stream()
                .filter(e -> e.getCategoryId() != null)
                .collect(Collectors.groupingBy(AdverseEvent::getCategoryId, Collectors.counting()));

        // 查询分类名称
        Map<String, String> categoryNameMap = new HashMap<>();
        if (!countMap.isEmpty()) {
            List<EventCategory> categories = categoryService.listByIds(countMap.keySet());
            categoryNameMap = categories.stream()
                    .collect(Collectors.toMap(EventCategory::getId, EventCategory::getName));
        }

        // 转换为 StatItemVO 列表
        final Map<String, String> finalCategoryNameMap = categoryNameMap;
        List<StatItemVO> items = countMap.entrySet().stream()
                .map(entry -> {
                    String categoryName = finalCategoryNameMap.getOrDefault(entry.getKey(), "未知分类");
                    return new StatItemVO(entry.getKey(), categoryName, entry.getValue());
                })
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());

        // 构建结果
        StatResultVO result = new StatResultVO();
        result.setTotal((long) events.size());
        result.setItems(items);
        result.calculateRatios();

        return Result.OK(result);
    }

    /**
     * 按级别统计
     *
     * @param startDate 开始日期（可选）
     * @param endDate   结束日期（可选）
     * @return 统计结果
     */
    @AutoLog(value = "统计分析-按级别统计")
    @Operation(summary = "按级别统计")
    @GetMapping(value = "/byLevel")
    public Result<StatResultVO> statByLevel(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> wrapper = buildTimeRangeWrapper(startDate, endDate);

        // 查询所有事件
        List<AdverseEvent> events = adverseEventService.list(wrapper);

        // 按级别分组统计
        Map<String, Long> countMap = events.stream()
                .filter(e -> e.getLevel() != null)
                .collect(Collectors.groupingBy(AdverseEvent::getLevel, Collectors.counting()));

        // 级别名称映射
        Map<String, String> levelNameMap = new LinkedHashMap<>();
        levelNameMap.put("level_1", "一级（警告事件）");
        levelNameMap.put("level_2", "二级（不良后果事件）");
        levelNameMap.put("level_3", "三级（未遂事件）");
        levelNameMap.put("level_4", "四级（隐患事件）");

        // 转换为 StatItemVO 列表（按级别顺序）
        List<StatItemVO> items = levelNameMap.entrySet().stream()
                .map(entry -> new StatItemVO(
                        entry.getKey(),
                        entry.getValue(),
                        countMap.getOrDefault(entry.getKey(), 0L)))
                .collect(Collectors.toList());

        // 构建结果
        StatResultVO result = new StatResultVO();
        result.setTotal((long) events.size());
        result.setItems(items);
        result.calculateRatios();

        return Result.OK(result);
    }

    /**
     * 按状态统计
     *
     * @param startDate 开始日期（可选）
     * @param endDate   结束日期（可选）
     * @return 统计结果
     */
    @AutoLog(value = "统计分析-按状态统计")
    @Operation(summary = "按状态统计")
    @GetMapping(value = "/byStatus")
    public Result<StatResultVO> statByStatus(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> wrapper = buildTimeRangeWrapper(startDate, endDate);

        // 查询所有事件
        List<AdverseEvent> events = adverseEventService.list(wrapper);

        // 按状态分组统计
        Map<String, Long> countMap = events.stream()
                .filter(e -> e.getStatus() != null)
                .collect(Collectors.groupingBy(AdverseEvent::getStatus, Collectors.counting()));

        // 状态名称映射
        Map<String, String> statusNameMap = new LinkedHashMap<>();
        statusNameMap.put("draft", "草稿");
        statusNameMap.put("pending_audit", "待审核");
        statusNameMap.put("returned", "已退回");
        statusNameMap.put("pending_process", "待处理");
        statusNameMap.put("pending_rectify", "待整改");
        statusNameMap.put("rectifying", "整改中");
        statusNameMap.put("closed", "已结案");

        // 转换为 StatItemVO 列表（按状态顺序）
        List<StatItemVO> items = statusNameMap.entrySet().stream()
                .map(entry -> new StatItemVO(
                        entry.getKey(),
                        entry.getValue(),
                        countMap.getOrDefault(entry.getKey(), 0L)))
                .collect(Collectors.toList());

        // 构建结果
        StatResultVO result = new StatResultVO();
        result.setTotal((long) events.size());
        result.setItems(items);
        result.calculateRatios();

        return Result.OK(result);
    }

    /**
     * 时间趋势统计
     *
     * @param granularity 粒度：month（月）、quarter（季度）、year（年）
     * @param periods     统计周期数（默认 12）
     * @return 趋势数据列表
     */
    @AutoLog(value = "统计分析-时间趋势")
    @Operation(summary = "时间趋势统计")
    @GetMapping(value = "/trend")
    public Result<List<TrendItemVO>> statTrend(
            @RequestParam(defaultValue = "month") String granularity,
            @RequestParam(defaultValue = "12") Integer periods) {

        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        // 限制最大周期数
        if (periods > 36) {
            periods = 36;
        }

        List<TrendItemVO> trendList = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = periods - 1; i >= 0; i--) {
            TrendItemVO item = new TrendItemVO();
            Date periodStart;
            Date periodEnd;
            String periodLabel;

            switch (granularity.toLowerCase()) {
                case "quarter":
                    // 季度统计
                    LocalDate quarterDate = now.minusMonths(i * 3L);
                    int quarter = quarterDate.get(IsoFields.QUARTER_OF_YEAR);
                    int year = quarterDate.getYear();
                    periodLabel = year + "-Q" + quarter;

                    LocalDate quarterStart = quarterDate.withMonth((quarter - 1) * 3 + 1).withDayOfMonth(1);
                    LocalDate quarterEnd = quarterStart.plusMonths(3).minusDays(1);
                    periodStart = java.sql.Date.valueOf(quarterStart);
                    periodEnd = java.sql.Date.valueOf(quarterEnd);
                    break;

                case "year":
                    // 年度统计
                    int targetYear = now.getYear() - i;
                    periodLabel = String.valueOf(targetYear);

                    LocalDate yearStart = LocalDate.of(targetYear, 1, 1);
                    LocalDate yearEnd = LocalDate.of(targetYear, 12, 31);
                    periodStart = java.sql.Date.valueOf(yearStart);
                    periodEnd = java.sql.Date.valueOf(yearEnd);
                    break;

                default:
                    // 月度统计（默认）
                    YearMonth targetMonth = YearMonth.from(now).minusMonths(i);
                    periodLabel = targetMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));

                    periodStart = java.sql.Date.valueOf(targetMonth.atDay(1));
                    periodEnd = java.sql.Date.valueOf(targetMonth.atEndOfMonth());
                    break;
            }

            item.setPeriod(periodLabel);

            // 查询该时间段的统计数据
            // 上报数量（按创建时间）
            LambdaQueryWrapper<AdverseEvent> reportWrapper = buildTimeRangeWrapper(periodStart, periodEnd);
            item.setReportCount(adverseEventService.count(reportWrapper));

            // 已结案数量
            LambdaQueryWrapper<AdverseEvent> closedWrapper = buildTimeRangeWrapper(periodStart, periodEnd);
            closedWrapper.eq(AdverseEvent::getStatus, "closed");
            item.setClosedCount(adverseEventService.count(closedWrapper));

            // 整改中数量
            LambdaQueryWrapper<AdverseEvent> rectifyingWrapper = buildTimeRangeWrapper(periodStart, periodEnd);
            rectifyingWrapper.in(AdverseEvent::getStatus, Arrays.asList("pending_rectify", "rectifying"));
            item.setRectifyingCount(adverseEventService.count(rectifyingWrapper));

            trendList.add(item);
        }

        return Result.OK(trendList);
    }

    /**
     * 仪表盘汇总数据
     *
     * @return 仪表盘数据
     */
    @AutoLog(value = "统计分析-仪表盘")
    @Operation(summary = "仪表盘汇总数据")
    @GetMapping(value = "/dashboard")
    public Result<DashboardVO> getDashboard() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser == null) {
            return Result.error("请先登录");
        }

        DashboardVO dashboard = new DashboardVO();

        // ==================== 总览指标 ====================

        // 事件总数
        long totalCount = adverseEventService.count();
        dashboard.setTotalCount(totalCount);

        // 本月新增数量
        YearMonth currentMonth = YearMonth.now();
        LambdaQueryWrapper<AdverseEvent> thisMonthWrapper = new LambdaQueryWrapper<>();
        thisMonthWrapper.ge(AdverseEvent::getCreateTime, java.sql.Date.valueOf(currentMonth.atDay(1)));
        thisMonthWrapper.le(AdverseEvent::getCreateTime, java.sql.Date.valueOf(currentMonth.atEndOfMonth()));
        dashboard.setThisMonthCount(adverseEventService.count(thisMonthWrapper));

        // 待审核数量
        LambdaQueryWrapper<AdverseEvent> pendingAuditWrapper = new LambdaQueryWrapper<>();
        pendingAuditWrapper.eq(AdverseEvent::getStatus, "pending_audit");
        dashboard.setPendingAuditCount(adverseEventService.count(pendingAuditWrapper));

        // 待处理数量
        LambdaQueryWrapper<AdverseEvent> pendingProcessWrapper = new LambdaQueryWrapper<>();
        pendingProcessWrapper.eq(AdverseEvent::getStatus, "pending_process");
        dashboard.setPendingProcessCount(adverseEventService.count(pendingProcessWrapper));

        // 整改中数量
        LambdaQueryWrapper<AdverseEvent> rectifyingWrapper = new LambdaQueryWrapper<>();
        rectifyingWrapper.in(AdverseEvent::getStatus, Arrays.asList("pending_rectify", "rectifying"));
        long rectifyingCount = adverseEventService.count(rectifyingWrapper);
        dashboard.setRectifyingCount(rectifyingCount);

        // 已结案数量
        LambdaQueryWrapper<AdverseEvent> closedWrapper = new LambdaQueryWrapper<>();
        closedWrapper.eq(AdverseEvent::getStatus, "closed");
        long closedCount = adverseEventService.count(closedWrapper);
        dashboard.setClosedCount(closedCount);

        // ==================== 比率指标 ====================

        // 结案率
        if (totalCount > 0) {
            BigDecimal closeRate = BigDecimal.valueOf(closedCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);
            dashboard.setCloseRate(closeRate);
        } else {
            dashboard.setCloseRate(BigDecimal.ZERO);
        }

        // 整改完成率（需整改的事件 = 整改中 + 已结案中整改过的）
        long needRectifyCount = rectifyingCount + closedCount;
        if (needRectifyCount > 0) {
            BigDecimal rectifyCompleteRate = BigDecimal.valueOf(closedCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(needRectifyCount), 2, RoundingMode.HALF_UP);
            dashboard.setRectifyCompleteRate(rectifyCompleteRate);
        } else {
            dashboard.setRectifyCompleteRate(BigDecimal.valueOf(100));
        }

        // ==================== Top 统计 ====================

        // Top 5 科室
        Result<StatResultVO> deptResult = statByDepartment(null, null);
        if (deptResult.isSuccess() && deptResult.getResult() != null) {
            List<StatItemVO> topDepts = deptResult.getResult().getItems().stream()
                    .limit(5)
                    .collect(Collectors.toList());
            dashboard.setTopDepartments(topDepts);
        }

        // Top 5 分类
        Result<StatResultVO> categoryResult = statByCategory(null, null);
        if (categoryResult.isSuccess() && categoryResult.getResult() != null) {
            List<StatItemVO> topCategories = categoryResult.getResult().getItems().stream()
                    .limit(5)
                    .collect(Collectors.toList());
            dashboard.setTopCategories(topCategories);
        }

        // ==================== 级别分布 ====================
        Result<StatResultVO> levelResult = statByLevel(null, null);
        if (levelResult.isSuccess() && levelResult.getResult() != null) {
            dashboard.setLevelDistribution(levelResult.getResult().getItems());
        }

        // ==================== 近期趋势 ====================
        Result<List<TrendItemVO>> trendResult = statTrend("month", 6);
        if (trendResult.isSuccess() && trendResult.getResult() != null) {
            dashboard.setRecentTrend(trendResult.getResult());
        }

        return Result.OK(dashboard);
    }

    /**
     * 导出统计报表
     *
     * @param reportType 报表类型：department/category/level/status
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @param request    请求对象
     * @return ModelAndView
     */
    @AutoLog(value = "统计分析-导出报表")
    @Operation(summary = "导出统计报表")
    @GetMapping(value = "/export")
    public ModelAndView exportReport(
            @RequestParam(defaultValue = "all") String reportType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            HttpServletRequest request) {

        // 构建查询条件
        LambdaQueryWrapper<AdverseEvent> queryWrapper = buildTimeRangeWrapper(startDate, endDate);

        // 查询数据（限制最大导出数量为 10000）
        queryWrapper.last("LIMIT 10000");
        List<AdverseEvent> exportList = adverseEventService.list(queryWrapper);

        // 导出 Excel
        String fileName = "不良事件统计报表";
        if (startDate != null || endDate != null) {
            fileName += "_" + (startDate != null ? new java.text.SimpleDateFormat("yyyyMMdd").format(startDate) : "")
                    + "-" + (endDate != null ? new java.text.SimpleDateFormat("yyyyMMdd").format(endDate) : "");
        }

        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, fileName);
        mv.addObject(NormalExcelConstants.CLASS, AdverseEvent.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("不良事件统计报表", "数据列表"));
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);

        return mv;
    }

    /**
     * 构建时间范围查询条件
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 查询条件
     */
    private LambdaQueryWrapper<AdverseEvent> buildTimeRangeWrapper(Date startDate, Date endDate) {
        LambdaQueryWrapper<AdverseEvent> wrapper = new LambdaQueryWrapper<>();

        if (startDate != null) {
            wrapper.ge(AdverseEvent::getCreateTime, startDate);
        }
        if (endDate != null) {
            wrapper.le(AdverseEvent::getCreateTime, endDate);
        }

        return wrapper;
    }
}
