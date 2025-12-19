package org.jeecg.modules.adverse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.adverse.entity.DrugAdverseRectify;

import java.util.List;

/**
 * 药品不良反应整改表 Mapper 接口
 * <p>
 * 提供药品不良反应整改记录的基础 CRUD 操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see DrugAdverseRectify
 */
@Mapper
public interface DrugAdverseRectifyMapper extends BaseMapper<DrugAdverseRectify> {

    /**
     * 根据报告ID查询整改记录列表
     * <p>按创建时间倒序排列</p>
     *
     * @param reportId 报告ID
     * @return 整改记录列表
     */
    @Select("SELECT * FROM drug_adverse_rectify WHERE report_id = #{reportId} AND del_flag = 0 ORDER BY create_time DESC")
    List<DrugAdverseRectify> selectByReportId(@Param("reportId") String reportId);

    /**
     * 查询报告的最新整改记录
     *
     * @param reportId 报告ID
     * @return 最新的整改记录
     */
    @Select("SELECT * FROM drug_adverse_rectify WHERE report_id = #{reportId} AND del_flag = 0 ORDER BY create_time DESC LIMIT 1")
    DrugAdverseRectify selectLatestByReportId(@Param("reportId") String reportId);

    /**
     * 统计报告的整改次数
     *
     * @param reportId 报告ID
     * @return 整改次数
     */
    @Select("SELECT COUNT(*) FROM drug_adverse_rectify WHERE report_id = #{reportId} AND del_flag = 0")
    int countByReportId(@Param("reportId") String reportId);
}
