package org.jeecg.modules.adverse.blood.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseFlow;

/**
 * 输血使用不良事件流转记录 Mapper 接口
 * <p>
 * 提供输血使用不良事件流转记录的基础 CRUD 操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/78">Issue #78</a>
 */
@Mapper
public interface BloodAdverseFlowMapper extends BaseMapper<BloodAdverseFlow> {

    /**
     * 根据报告ID查询流转记录列表
     *
     * @param reportId 报告ID
     * @return 流转记录列表（按创建时间排序）
     */
    List<BloodAdverseFlow> selectByReportId(@Param("reportId") String reportId);
}
