package org.jeecg.modules.adverse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecg.modules.adverse.entity.DrugAdverseConcomitantDrug;
import org.jeecg.modules.adverse.entity.DrugAdverseReport;
import org.jeecg.modules.adverse.entity.DrugAdverseSuspectDrug;

import java.io.Serializable;
import java.util.List;

/**
 * 药品不良反应报告 VO
 * <p>
 * 用于接收和返回主子表联动数据
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Data
@Schema(description = "药品不良反应报告VO（含子表）")
public class DrugAdverseReportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报告主表数据
     */
    @Schema(description = "报告主表数据")
    private DrugAdverseReport report;

    /**
     * 怀疑药品列表
     */
    @Schema(description = "怀疑药品列表")
    private List<DrugAdverseSuspectDrug> suspectDrugs;

    /**
     * 并用药品列表
     */
    @Schema(description = "并用药品列表")
    private List<DrugAdverseConcomitantDrug> concomitantDrugs;
}
