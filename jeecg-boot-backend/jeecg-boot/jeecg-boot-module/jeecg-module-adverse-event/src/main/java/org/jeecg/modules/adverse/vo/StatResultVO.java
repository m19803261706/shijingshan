package org.jeecg.modules.adverse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 统计结果 VO
 * <p>
 * 包含统计总数和各项明细
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统计结果")
public class StatResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    @Schema(description = "总数")
    private Long total;

    /**
     * 统计项列表
     */
    @Schema(description = "统计项列表")
    private List<StatItemVO> items;

    /**
     * 计算各项占比
     * <p>
     * 根据 total 计算每个 item 的 ratio
     * </p>
     */
    public void calculateRatios() {
        if (total == null || total == 0 || items == null) {
            return;
        }
        for (StatItemVO item : items) {
            if (item.getCount() != null) {
                BigDecimal ratio = BigDecimal.valueOf(item.getCount())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
                item.setRatio(ratio);
            }
        }
    }
}
