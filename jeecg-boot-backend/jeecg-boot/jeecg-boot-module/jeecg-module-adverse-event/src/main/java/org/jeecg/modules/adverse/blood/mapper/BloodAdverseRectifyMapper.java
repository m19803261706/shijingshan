package org.jeecg.modules.adverse.blood.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseRectify;

/**
 * 输血使用不良事件整改记录表 Mapper 接口
 * <p>
 * 提供输血使用不良事件整改记录的基础 CRUD 操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/78">Issue #78</a>
 */
@Mapper
public interface BloodAdverseRectifyMapper extends BaseMapper<BloodAdverseRectify> {

    /**
     * 根据报告ID查询整改记录列表
     * <p>按整改轮次升序排列</p>
     *
     * @param reportId 报告ID
     * @return 整改记录列表
     */
    @Select("SELECT * FROM blood_adverse_rectify WHERE report_id = #{reportId} ORDER BY round ASC")
    List<BloodAdverseRectify> selectByReportId(@Param("reportId") String reportId);

    /**
     * 查询报告的最新整改记录
     *
     * @param reportId 报告ID
     * @return 最新的整改记录（轮次最大）
     */
    @Select("SELECT * FROM blood_adverse_rectify WHERE report_id = #{reportId} ORDER BY round DESC LIMIT 1")
    BloodAdverseRectify selectLatestByReportId(@Param("reportId") String reportId);

    /**
     * 统计报告的整改轮次
     *
     * @param reportId 报告ID
     * @return 整改轮次数
     */
    @Select("SELECT COUNT(*) FROM blood_adverse_rectify WHERE report_id = #{reportId}")
    int countByReportId(@Param("reportId") String reportId);

    /**
     * 获取报告的当前整改轮次号
     * <p>用于创建新的整改记录时获取下一轮次号</p>
     *
     * @param reportId 报告ID
     * @return 当前最大轮次号，如果没有则返回 0
     */
    @Select("SELECT COALESCE(MAX(round), 0) FROM blood_adverse_rectify WHERE report_id = #{reportId}")
    int getMaxRound(@Param("reportId") String reportId);
}
