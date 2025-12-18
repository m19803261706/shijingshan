package org.jeecg.modules.adverse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.adverse.entity.DrugAdverseConcomitantDrug;

import java.util.List;

/**
 * 药品不良反应报告-并用药品子表 Mapper 接口
 * <p>
 * 提供并用药品的基础 CRUD 操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Mapper
public interface DrugAdverseConcomitantDrugMapper extends BaseMapper<DrugAdverseConcomitantDrug> {

    /**
     * 根据报告ID查询并用药品列表
     *
     * @param reportId 报告ID
     * @return 并用药品列表
     */
    @Select("SELECT * FROM drug_adverse_concomitant_drug WHERE report_id = #{reportId} ORDER BY sort_order ASC")
    List<DrugAdverseConcomitantDrug> selectByReportId(@Param("reportId") String reportId);

    /**
     * 根据报告ID删除并用药品
     *
     * @param reportId 报告ID
     * @return 删除的记录数
     */
    @Delete("DELETE FROM drug_adverse_concomitant_drug WHERE report_id = #{reportId}")
    int deleteByReportId(@Param("reportId") String reportId);
}
