package org.jeecg.modules.adverse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.adverse.entity.AdverseEventRectify;

import java.util.List;

/**
 * 整改记录 Mapper 接口
 * <p>
 * 提供整改记录的数据库操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Mapper
public interface AdverseEventRectifyMapper extends BaseMapper<AdverseEventRectify> {

    /**
     * 获取事件的最大整改轮次
     *
     * @param eventId 事件ID
     * @return 最大轮次，如果没有整改记录则返回 null
     */
    @Select("SELECT MAX(rectify_no) FROM adverse_event_rectify WHERE event_id = #{eventId}")
    Integer getMaxRectifyNo(@Param("eventId") String eventId);

    /**
     * 获取事件的整改记录列表
     *
     * @param eventId 事件ID
     * @return 整改记录列表，按轮次排序
     */
    @Select("SELECT * FROM adverse_event_rectify WHERE event_id = #{eventId} ORDER BY rectify_no ASC")
    List<AdverseEventRectify> listByEventId(@Param("eventId") String eventId);

    /**
     * 获取事件的当前整改记录（最新一轮）
     *
     * @param eventId 事件ID
     * @return 当前整改记录
     */
    @Select("SELECT * FROM adverse_event_rectify WHERE event_id = #{eventId} ORDER BY rectify_no DESC LIMIT 1")
    AdverseEventRectify getCurrentRectify(@Param("eventId") String eventId);
}
