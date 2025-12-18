package org.jeecg.modules.adverse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 统计项 VO
 * <p>
 * 用于表示各维度统计结果的单项数据
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统计项")
public class StatItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 统计项编码
     */
    @Schema(description = "统计项编码")
    private String code;

    /**
     * 统计项名称
     */
    @Schema(description = "统计项名称")
    private String name;

    /**
     * 数量
     */
    @Schema(description = "数量")
    private Long count;

    /**
     * 占比（百分比）
     */
    @Schema(description = "占比（百分比）")
    private BigDecimal ratio;

    /**
     * 构造方法（不含占比）
     *
     * @param code  编码
     * @param name  名称
     * @param count 数量
     */
    public StatItemVO(String code, String name, Long count) {
        this.code = code;
        this.name = name;
        this.count = count;
    }
}
