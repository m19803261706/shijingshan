package org.jeecg.modules.adverse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.adverse.entity.DrugAdverseReport;

/**
 * 药品不良反应报告主表 Mapper 接口
 * <p>
 * 提供药品不良反应报告的基础 CRUD 操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Mapper
public interface DrugAdverseReportMapper extends BaseMapper<DrugAdverseReport> {

    /**
     * 获取今日最大报告编号序号
     * <p>用于生成新的报告编号</p>
     *
     * @param prefix 报告编号前缀，如 "ADR20251219"
     * @return 今日最大序号，如果没有则返回 null
     */
    @Select("SELECT MAX(CAST(SUBSTRING(report_code, LENGTH(#{prefix}) + 1) AS UNSIGNED)) " +
            "FROM drug_adverse_report WHERE report_code LIKE CONCAT(#{prefix}, '%')")
    Integer getMaxReportCodeSeq(@Param("prefix") String prefix);
}
