package org.jeecg.modules.adverse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.adverse.entity.AdverseEvent;

/**
 * 不良事件主表 Mapper 接口
 * <p>
 * 提供不良事件的基础 CRUD 操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Mapper
public interface AdverseEventMapper extends BaseMapper<AdverseEvent> {

    /**
     * 获取今日最大事件编号序号
     * <p>用于生成新的事件编号</p>
     *
     * @param prefix 事件编号前缀，如 "AE20250118"
     * @return 今日最大序号，如果没有则返回 null
     */
    @Select("SELECT MAX(CAST(SUBSTRING(event_code, LENGTH(#{prefix}) + 1) AS UNSIGNED)) " +
            "FROM adverse_event WHERE event_code LIKE CONCAT(#{prefix}, '%')")
    Integer getMaxEventCodeSeq(@Param("prefix") String prefix);
}
