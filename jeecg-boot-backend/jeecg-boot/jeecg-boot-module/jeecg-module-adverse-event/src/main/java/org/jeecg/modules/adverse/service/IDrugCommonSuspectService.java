package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.DrugCommonSuspect;

import java.util.List;

/**
 * 常用怀疑药品配置表 服务接口
 * <p>
 * 提供常用怀疑药品的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
public interface IDrugCommonSuspectService extends IService<DrugCommonSuspect> {

    /**
     * 模糊搜索常用药品（用于快速选择）
     * <p>按使用次数降序排列，常用优先</p>
     *
     * @param keyword 搜索关键词（匹配通用名称、商品名称、生产厂家）
     * @return 匹配的药品列表
     */
    List<DrugCommonSuspect> searchByKeyword(String keyword);

    /**
     * 保存药品（如果不存在）
     * <p>
     * 判断条件：通用名称 + 生产厂家 + 批准文号 的组合不存在
     * </p>
     *
     * @param drug 药品信息
     * @return true-保存成功，false-已存在未保存
     */
    boolean saveIfNotExists(DrugCommonSuspect drug);

    /**
     * 更新使用次数（+1）
     * <p>在选择常用药品时调用</p>
     *
     * @param id 药品ID
     * @return 是否更新成功
     */
    boolean incrementUseCount(String id);

    /**
     * 检查药品是否已存在
     *
     * @param genericName  通用名称
     * @param manufacturer 生产厂家
     * @param approvalNo   批准文号
     * @return true-已存在，false-不存在
     */
    boolean exists(String genericName, String manufacturer, String approvalNo);
}
