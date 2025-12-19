package org.jeecg.modules.adverse.controller;

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.adverse.entity.DeviceCommonConfig;
import org.jeecg.modules.adverse.service.IDeviceCommonConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 常用医疗器械配置 控制器
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Tag(name = "常用医疗器械配置")
@RestController
@RequestMapping("/adverse/deviceConfig")
@Slf4j
public class DeviceCommonConfigController extends JeecgController<DeviceCommonConfig, IDeviceCommonConfigService> {

    @Autowired
    private IDeviceCommonConfigService deviceCommonConfigService;

    /**
     * 分页列表查询
     *
     * @param deviceCommonConfig 查询条件
     * @param pageNo             页码
     * @param pageSize           每页条数
     * @param req                请求
     * @return 分页结果
     */
    @Operation(summary = "分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<DeviceCommonConfig>> queryPageList(
            DeviceCommonConfig deviceCommonConfig,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {

        QueryWrapper<DeviceCommonConfig> queryWrapper = QueryGenerator.initQueryWrapper(deviceCommonConfig, req.getParameterMap());
        // 默认按使用次数降序
        queryWrapper.orderByDesc("use_count");

        Page<DeviceCommonConfig> page = new Page<>(pageNo, pageSize);
        IPage<DeviceCommonConfig> pageList = deviceCommonConfigService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 搜索常用器械（用于下拉选择）
     *
     * @param keyword 搜索关键词
     * @return 匹配的器械列表
     */
    @Operation(summary = "搜索常用器械")
    @GetMapping(value = "/search")
    public Result<List<DeviceCommonConfig>> search(
            @RequestParam(name = "keyword", required = false) String keyword) {

        List<DeviceCommonConfig> list = deviceCommonConfigService.search(keyword);
        return Result.OK(list);
    }

    /**
     * 更新使用次数
     *
     * @param id 配置ID
     * @return 操作结果
     */
    @Operation(summary = "更新使用次数")
    @PostMapping(value = "/updateUseCount")
    public Result<String> updateUseCount(@RequestParam(name = "id") String id) {
        deviceCommonConfigService.updateUseCount(id);
        return Result.OK("更新成功");
    }

    /**
     * 新增
     *
     * @param deviceCommonConfig 配置信息
     * @return 操作结果
     */
    @AutoLog(value = "常用医疗器械配置-新增")
    @Operation(summary = "新增")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody DeviceCommonConfig deviceCommonConfig) {
        deviceCommonConfigService.save(deviceCommonConfig);
        return Result.OK("添加成功！");
    }

    /**
     * 保存器械配置（如果不存在）
     * <p>
     * 用于在上报时自动保存新的器械信息
     * </p>
     *
     * @param deviceCommonConfig 配置信息
     * @return 操作结果
     */
    @Operation(summary = "保存器械配置（如果不存在）")
    @PostMapping(value = "/saveIfNotExists")
    public Result<String> saveIfNotExists(@RequestBody DeviceCommonConfig deviceCommonConfig) {
        boolean saved = deviceCommonConfigService.saveIfNotExists(deviceCommonConfig);
        if (saved) {
            return Result.OK("保存成功");
        }
        return Result.OK("器械配置已存在");
    }

    /**
     * 编辑
     *
     * @param deviceCommonConfig 配置信息
     * @return 操作结果
     */
    @AutoLog(value = "常用医疗器械配置-编辑")
    @Operation(summary = "编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody DeviceCommonConfig deviceCommonConfig) {
        deviceCommonConfigService.updateById(deviceCommonConfig);
        return Result.OK("编辑成功！");
    }

    /**
     * 通过id删除
     *
     * @param id 配置ID
     * @return 操作结果
     */
    @AutoLog(value = "常用医疗器械配置-删除")
    @Operation(summary = "通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id") String id) {
        deviceCommonConfigService.removeById(id);
        return Result.OK("删除成功！");
    }

    /**
     * 批量删除
     *
     * @param ids ID列表，逗号分隔
     * @return 操作结果
     */
    @AutoLog(value = "常用医疗器械配置-批量删除")
    @Operation(summary = "批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.deviceCommonConfigService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id 配置ID
     * @return 配置信息
     */
    @Operation(summary = "通过id查询")
    @GetMapping(value = "/queryById")
    public Result<DeviceCommonConfig> queryById(@RequestParam(name = "id") String id) {
        DeviceCommonConfig deviceCommonConfig = deviceCommonConfigService.getById(id);
        if (deviceCommonConfig == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(deviceCommonConfig);
    }

    /**
     * 导出Excel
     *
     * @param request            请求
     * @param deviceCommonConfig 查询条件
     * @return ModelAndView
     */
    @Operation(summary = "导出Excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DeviceCommonConfig deviceCommonConfig) {
        return super.exportXls(request, deviceCommonConfig, DeviceCommonConfig.class, "常用医疗器械配置");
    }

    /**
     * 导入Excel
     *
     * @param request  请求
     * @param response 响应
     * @return 导入结果
     */
    @Operation(summary = "导入Excel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DeviceCommonConfig.class);
    }
}
