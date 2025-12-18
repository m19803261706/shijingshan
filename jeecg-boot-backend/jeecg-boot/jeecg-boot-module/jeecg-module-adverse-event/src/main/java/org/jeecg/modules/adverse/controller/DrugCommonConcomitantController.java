package org.jeecg.modules.adverse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.adverse.entity.DrugCommonConcomitant;
import org.jeecg.modules.adverse.service.IDrugCommonConcomitantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 常用并用药品配置 Controller
 * <p>
 * 提供常用并用药品的 CRUD 和快速搜索 API
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Slf4j
@Tag(name = "常用并用药品配置")
@RestController
@RequestMapping("/adverse/drugCommonConcomitant")
public class DrugCommonConcomitantController extends JeecgController<DrugCommonConcomitant, IDrugCommonConcomitantService> {

    @Autowired
    private IDrugCommonConcomitantService drugCommonConcomitantService;

    /**
     * 分页查询列表
     *
     * @param drugCommonConcomitant 查询条件
     * @param pageNo                页码
     * @param pageSize              每页条数
     * @param req                   请求对象
     * @return 分页结果
     */
    @AutoLog(value = "常用并用药品-分页列表查询")
    @Operation(summary = "分页查询列表")
    @GetMapping(value = "/list")
    public Result<IPage<DrugCommonConcomitant>> queryPageList(DrugCommonConcomitant drugCommonConcomitant,
                                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               HttpServletRequest req) {
        QueryWrapper<DrugCommonConcomitant> queryWrapper = QueryGenerator.initQueryWrapper(drugCommonConcomitant, req.getParameterMap());
        queryWrapper.orderByDesc("use_count", "update_time");

        Page<DrugCommonConcomitant> page = new Page<>(pageNo, pageSize);
        IPage<DrugCommonConcomitant> pageList = drugCommonConcomitantService.page(page, queryWrapper);

        return Result.OK(pageList);
    }

    /**
     * 快速搜索（用于下拉选择）
     * <p>按使用次数降序排列，常用优先</p>
     *
     * @param keyword 搜索关键词
     * @return 匹配的药品列表
     */
    @AutoLog(value = "常用并用药品-快速搜索")
    @Operation(summary = "快速搜索（用于下拉选择）")
    @GetMapping(value = "/search")
    public Result<List<DrugCommonConcomitant>> search(@RequestParam(name = "keyword", required = false) String keyword) {
        List<DrugCommonConcomitant> list = drugCommonConcomitantService.searchByKeyword(keyword);
        return Result.OK(list);
    }

    /**
     * 根据ID查询详情
     *
     * @param id 主键ID
     * @return 详情
     */
    @AutoLog(value = "常用并用药品-查询详情")
    @Operation(summary = "根据ID查询详情")
    @GetMapping(value = "/queryById")
    public Result<DrugCommonConcomitant> queryById(@RequestParam(name = "id") String id) {
        DrugCommonConcomitant drugCommonConcomitant = drugCommonConcomitantService.getById(id);
        if (drugCommonConcomitant == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(drugCommonConcomitant);
    }

    /**
     * 新增
     *
     * @param drugCommonConcomitant 药品信息
     * @return 操作结果
     */
    @AutoLog(value = "常用并用药品-新增")
    @Operation(summary = "新增")
    @PostMapping(value = "/add")
    public Result<DrugCommonConcomitant> add(@RequestBody DrugCommonConcomitant drugCommonConcomitant) {
        // 检查是否已存在
        if (drugCommonConcomitantService.exists(drugCommonConcomitant.getGenericName(),
                drugCommonConcomitant.getManufacturer(), drugCommonConcomitant.getApprovalNo())) {
            return Result.error("该药品配置已存在");
        }

        drugCommonConcomitant.setUseCount(0);
        drugCommonConcomitant.setCreateTime(new Date());
        drugCommonConcomitantService.save(drugCommonConcomitant);
        return Result.OK("添加成功！", drugCommonConcomitant);
    }

    /**
     * 编辑
     *
     * @param drugCommonConcomitant 药品信息
     * @return 操作结果
     */
    @AutoLog(value = "常用并用药品-编辑")
    @Operation(summary = "编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<DrugCommonConcomitant> edit(@RequestBody DrugCommonConcomitant drugCommonConcomitant) {
        DrugCommonConcomitant existing = drugCommonConcomitantService.getById(drugCommonConcomitant.getId());
        if (existing == null) {
            return Result.error("未找到对应数据");
        }

        drugCommonConcomitant.setUpdateTime(new Date());
        drugCommonConcomitantService.updateById(drugCommonConcomitant);
        return Result.OK("编辑成功！", drugCommonConcomitant);
    }

    /**
     * 删除
     *
     * @param id 主键ID
     * @return 操作结果
     */
    @AutoLog(value = "常用并用药品-删除")
    @Operation(summary = "删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id") String id) {
        drugCommonConcomitantService.removeById(id);
        return Result.OK("删除成功！");
    }

    /**
     * 批量删除
     *
     * @param ids 主键ID列表（逗号分隔）
     * @return 操作结果
     */
    @AutoLog(value = "常用并用药品-批量删除")
    @Operation(summary = "批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids") String ids) {
        drugCommonConcomitantService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 更新使用次数
     * <p>在选择常用药品时调用</p>
     *
     * @param id 主键ID
     * @return 操作结果
     */
    @AutoLog(value = "常用并用药品-更新使用次数")
    @Operation(summary = "更新使用次数")
    @PutMapping(value = "/updateUseCount")
    public Result<String> updateUseCount(@RequestParam(name = "id") String id) {
        boolean success = drugCommonConcomitantService.incrementUseCount(id);
        return success ? Result.OK("更新成功！") : Result.error("更新失败");
    }

    /**
     * 导出Excel
     *
     * @param request               请求
     * @param drugCommonConcomitant 查询条件
     * @return ModelAndView
     */
    @Operation(summary = "导出Excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DrugCommonConcomitant drugCommonConcomitant) {
        return super.exportXls(request, drugCommonConcomitant, DrugCommonConcomitant.class, "常用并用药品配置");
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
        return super.importExcel(request, response, DrugCommonConcomitant.class);
    }
}
