package org.jeecg.modules.adverse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.adverse.entity.EventCategory;
import org.jeecg.modules.adverse.service.IEventCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 事件分类管理 Controller
 * <p>
 * 提供事件分类的 CRUD 和树形结构查询 API
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Tag(name = "事件分类管理")
@RestController
@RequestMapping("/adverse/category")
public class EventCategoryController {

    @Autowired
    private IEventCategoryService categoryService;

    /**
     * 分页查询分类列表
     *
     * @param category 查询条件
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @param req      请求对象
     * @return 分页结果
     */
    @AutoLog(value = "事件分类-分页列表查询")
    @Operation(summary = "分页查询分类列表")
    @GetMapping(value = "/list")
    public Result<IPage<EventCategory>> queryPageList(EventCategory category,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        QueryWrapper<EventCategory> queryWrapper = QueryGenerator.initQueryWrapper(category, req.getParameterMap());
        queryWrapper.orderByAsc("level", "sort_order");

        Page<EventCategory> page = new Page<>(pageNo, pageSize);
        IPage<EventCategory> pageList = categoryService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 获取分类树形结构
     *
     * @return 树形分类列表
     */
    @AutoLog(value = "事件分类-获取树形结构")
    @Operation(summary = "获取分类树形结构")
    @GetMapping(value = "/tree")
    public Result<List<EventCategory>> getCategoryTree() {
        List<EventCategory> tree = categoryService.getCategoryTree();
        return Result.OK(tree);
    }

    /**
     * 获取子分类列表
     *
     * @param parentId 父级ID，不传则获取顶级分类
     * @return 子分类列表
     */
    @AutoLog(value = "事件分类-获取子分类")
    @Operation(summary = "获取子分类列表")
    @GetMapping(value = "/children")
    public Result<List<EventCategory>> getChildCategories(
            @RequestParam(name = "parentId", required = false) String parentId) {
        List<EventCategory> children = categoryService.getChildCategories(parentId);
        return Result.OK(children);
    }

    /**
     * 获取所有启用的分类
     *
     * @return 启用的分类列表
     */
    @AutoLog(value = "事件分类-获取所有启用分类")
    @Operation(summary = "获取所有启用的分类")
    @GetMapping(value = "/active")
    public Result<List<EventCategory>> getActiveCategories() {
        List<EventCategory> categories = categoryService.getActiveCategories();
        return Result.OK(categories);
    }

    /**
     * 根据ID查询分类详情
     *
     * @param id 分类ID
     * @return 分类详情
     */
    @AutoLog(value = "事件分类-查询详情")
    @Operation(summary = "根据ID查询分类详情")
    @GetMapping(value = "/queryById")
    public Result<EventCategory> queryById(@RequestParam(name = "id") String id) {
        EventCategory category = categoryService.getById(id);
        if (category == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(category);
    }

    /**
     * 新增分类
     *
     * @param category 分类信息
     * @return 操作结果
     */
    @AutoLog(value = "事件分类-新增")
    @Operation(summary = "新增分类")
    @PostMapping(value = "/add")
    public Result<EventCategory> add(@RequestBody EventCategory category) {
        try {
            category.setCreateTime(new Date());
            categoryService.save(category);
            return Result.OK("添加成功！", category);
        } catch (Exception e) {
            log.error("新增分类失败", e);
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 编辑分类
     *
     * @param category 分类信息
     * @return 操作结果
     */
    @AutoLog(value = "事件分类-编辑")
    @Operation(summary = "编辑分类")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<EventCategory> edit(@RequestBody EventCategory category) {
        EventCategory existCategory = categoryService.getById(category.getId());
        if (existCategory == null) {
            return Result.error("未找到对应数据");
        }

        try {
            category.setUpdateTime(new Date());
            categoryService.updateById(category);
            return Result.OK("编辑成功！", category);
        } catch (Exception e) {
            log.error("编辑分类失败", e);
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 操作结果
     */
    @AutoLog(value = "事件分类-删除")
    @Operation(summary = "删除分类")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id") String id) {
        // 检查是否有子分类
        List<EventCategory> children = categoryService.getChildCategories(id);
        if (!children.isEmpty()) {
            return Result.error("该分类下存在子分类，无法删除");
        }

        try {
            categoryService.removeById(id);
            return Result.OK("删除成功！");
        } catch (Exception e) {
            log.error("删除分类失败", e);
            return Result.error("操作失败：" + e.getMessage());
        }
    }

    /**
     * 启用/禁用分类
     *
     * @param id     分类ID
     * @param status 状态（1-启用，0-禁用）
     * @return 操作结果
     */
    @AutoLog(value = "事件分类-修改状态")
    @Operation(summary = "启用/禁用分类")
    @PostMapping(value = "/changeStatus")
    public Result<String> changeStatus(@RequestParam(name = "id") String id,
                                       @RequestParam(name = "status") Integer status) {
        EventCategory category = categoryService.getById(id);
        if (category == null) {
            return Result.error("未找到对应数据");
        }

        category.setStatus(status);
        category.setUpdateTime(new Date());
        categoryService.updateById(category);

        return Result.OK(status == 1 ? "启用成功！" : "禁用成功！");
    }
}
