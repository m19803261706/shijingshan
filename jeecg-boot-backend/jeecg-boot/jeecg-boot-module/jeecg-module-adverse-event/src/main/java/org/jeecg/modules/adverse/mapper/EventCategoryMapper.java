package org.jeecg.modules.adverse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.adverse.entity.EventCategory;

/**
 * 事件分类表 Mapper 接口
 * <p>
 * 提供事件分类的基础 CRUD 操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Mapper
public interface EventCategoryMapper extends BaseMapper<EventCategory> {
}
