package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.adverse.entity.EventCategory;
import org.jeecg.modules.adverse.mapper.EventCategoryMapper;
import org.jeecg.modules.adverse.service.IEventCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 事件分类表 服务实现类
 * <p>
 * 实现事件分类的核心业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Service
public class EventCategoryServiceImpl extends ServiceImpl<EventCategoryMapper, EventCategory> implements IEventCategoryService {

    /**
     * 启用状态
     */
    private static final Integer STATUS_ACTIVE = 1;

    @Override
    public List<EventCategory> getCategoryTree() {
        // 获取所有启用的分类
        List<EventCategory> allCategories = getActiveCategories();

        // 按父ID分组
        Map<String, List<EventCategory>> parentMap = allCategories.stream()
                .collect(Collectors.groupingBy(
                        cat -> oConvertUtils.isEmpty(cat.getParentId()) ? "ROOT" : cat.getParentId()
                ));

        // 获取顶级分类
        List<EventCategory> rootCategories = parentMap.getOrDefault("ROOT", new ArrayList<>());

        // 递归构建树形结构
        for (EventCategory root : rootCategories) {
            buildTree(root, parentMap);
        }

        return rootCategories;
    }

    @Override
    public List<EventCategory> getChildCategories(String parentId) {
        LambdaQueryWrapper<EventCategory> queryWrapper = new LambdaQueryWrapper<>();

        if (oConvertUtils.isEmpty(parentId)) {
            // 获取顶级分类
            queryWrapper.isNull(EventCategory::getParentId)
                    .or()
                    .eq(EventCategory::getParentId, "");
        } else {
            queryWrapper.eq(EventCategory::getParentId, parentId);
        }

        queryWrapper.eq(EventCategory::getStatus, STATUS_ACTIVE)
                .orderByAsc(EventCategory::getSortOrder);

        List<EventCategory> categories = this.list(queryWrapper);

        // 设置是否有子分类
        for (EventCategory category : categories) {
            category.setHasChildren(hasChildren(category.getId()));
        }

        return categories;
    }

    @Override
    public List<EventCategory> getActiveCategories() {
        LambdaQueryWrapper<EventCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EventCategory::getStatus, STATUS_ACTIVE)
                .orderByAsc(EventCategory::getLevel)
                .orderByAsc(EventCategory::getSortOrder);
        return this.list(queryWrapper);
    }

    /**
     * 递归构建树形结构
     *
     * @param parent    父分类
     * @param parentMap 父ID分组Map
     */
    private void buildTree(EventCategory parent, Map<String, List<EventCategory>> parentMap) {
        List<EventCategory> children = parentMap.get(parent.getId());
        if (children != null && !children.isEmpty()) {
            parent.setHasChildren(true);
            for (EventCategory child : children) {
                buildTree(child, parentMap);
            }
        } else {
            parent.setHasChildren(false);
        }
    }

    /**
     * 判断是否有子分类
     *
     * @param categoryId 分类ID
     * @return 是否有子分类
     */
    private boolean hasChildren(String categoryId) {
        LambdaQueryWrapper<EventCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EventCategory::getParentId, categoryId)
                .eq(EventCategory::getStatus, STATUS_ACTIVE);
        return this.count(queryWrapper) > 0;
    }
}
