package org.jeecg.modules.adverse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 趋势项 VO
 * <p>
 * 用于时间趋势统计的单个时间点数据
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "趋势项")
public class TrendItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 时间标签（如：2025-01、2025-Q1、2025）
     */
    @Schema(description = "时间标签")
    private String period;

    /**
     * 上报数量
     */
    @Schema(description = "上报数量")
    private Long reportCount;

    /**
     * 已结案数量
     */
    @Schema(description = "已结案数量")
    private Long closedCount;

    /**
     * 整改中数量
     */
    @Schema(description = "整改中数量")
    private Long rectifyingCount;
}
