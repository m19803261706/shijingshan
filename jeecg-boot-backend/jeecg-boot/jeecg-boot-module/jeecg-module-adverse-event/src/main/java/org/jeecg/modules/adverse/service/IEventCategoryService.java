package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.EventCategory;

import java.util.List;

/**
 * 事件分类表 服务接口
 * <p>
 * 提供事件分类的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
public interface IEventCategoryService extends IService<EventCategory> {

    /**
     * 获取分类树形结构
     *
     * @return 树形分类列表
     */
    List<EventCategory> getCategoryTree();

    /**
     * 根据父ID获取子分类列表
     *
     * @param parentId 父级ID，null 表示获取顶级分类
     * @return 子分类列表
     */
    List<EventCategory> getChildCategories(String parentId);

    /**
     * 获取所有启用状态的分类
     *
     * @return 启用的分类列表
     */
    List<EventCategory> getActiveCategories();
}
