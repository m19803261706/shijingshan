package org.jeecg.modules.adverse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 仪表盘数据 VO
 * <p>
 * 汇总展示不良事件系统的关键指标
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-18
 */
@Data
@Schema(description = "仪表盘数据")
public class DashboardVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 总览指标 ====================

    /**
     * 事件总数
     */
    @Schema(description = "事件总数")
    private Long totalCount;

    /**
     * 本月新增数量
     */
    @Schema(description = "本月新增数量")
    private Long thisMonthCount;

    /**
     * 待审核数量
     */
    @Schema(description = "待审核数量")
    private Long pendingAuditCount;

    /**
     * 待处理数量
     */
    @Schema(description = "待处理数量")
    private Long pendingProcessCount;

    /**
     * 整改中数量
     */
    @Schema(description = "整改中数量")
    private Long rectifyingCount;

    /**
     * 已结案数量
     */
    @Schema(description = "已结案数量")
    private Long closedCount;

    // ==================== 比率指标 ====================

    /**
     * 结案率（已结案/总数 * 100）
     */
    @Schema(description = "结案率（百分比）")
    private BigDecimal closeRate;

    /**
     * 整改完成率（已结案/需整改总数 * 100）
     */
    @Schema(description = "整改完成率（百分比）")
    private BigDecimal rectifyCompleteRate;

    // ==================== Top 统计 ====================

    /**
     * 事件数量 Top 5 科室
     */
    @Schema(description = "事件数量 Top 5 科室")
    private List<StatItemVO> topDepartments;

    /**
     * 事件数量 Top 5 分类
     */
    @Schema(description = "事件数量 Top 5 分类")
    private List<StatItemVO> topCategories;

    // ==================== 级别分布 ====================

    /**
     * 各级别事件数量
     */
    @Schema(description = "各级别事件数量")
    private List<StatItemVO> levelDistribution;

    // ==================== 近期趋势 ====================

    /**
     * 近 6 个月趋势
     */
    @Schema(description = "近 6 个月趋势")
    private List<TrendItemVO> recentTrend;
}
