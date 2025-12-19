package org.jeecg.modules.adverse.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.adverse.entity.DrugAdverseFlow;

/**
 * 药品不良反应流转记录 Mapper 接口
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Mapper
public interface DrugAdverseFlowMapper extends BaseMapper<DrugAdverseFlow> {

    /**
     * 根据报告ID查询流转记录列表
     *
     * @param reportId 报告ID
     * @return 流转记录列表（按创建时间排序）
     */
    List<DrugAdverseFlow> selectByReportId(@Param("reportId") String reportId);
}
