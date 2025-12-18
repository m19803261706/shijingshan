package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.DrugAdverseConcomitantDrug;

import java.util.List;

/**
 * 药品不良反应报告-并用药品子表 服务接口
 * <p>
 * 提供并用药品的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
public interface IDrugAdverseConcomitantDrugService extends IService<DrugAdverseConcomitantDrug> {

    /**
     * 根据报告ID查询并用药品列表
     *
     * @param reportId 报告ID
     * @return 并用药品列表
     */
    List<DrugAdverseConcomitantDrug> selectByReportId(String reportId);

    /**
     * 根据报告ID删除并用药品
     *
     * @param reportId 报告ID
     * @return 删除的记录数
     */
    int deleteByReportId(String reportId);

    /**
     * 批量保存并用药品
     *
     * @param reportId         报告ID
     * @param concomitantDrugs 并用药品列表
     * @return 是否保存成功
     */
    boolean saveBatchByReportId(String reportId, List<DrugAdverseConcomitantDrug> concomitantDrugs);
}
