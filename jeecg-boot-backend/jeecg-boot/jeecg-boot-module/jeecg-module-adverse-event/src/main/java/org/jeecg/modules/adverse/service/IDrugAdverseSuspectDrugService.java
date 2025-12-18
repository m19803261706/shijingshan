package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.DrugAdverseSuspectDrug;

import java.util.List;

/**
 * 药品不良反应报告-怀疑药品子表 服务接口
 * <p>
 * 提供怀疑药品的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
public interface IDrugAdverseSuspectDrugService extends IService<DrugAdverseSuspectDrug> {

    /**
     * 根据报告ID查询怀疑药品列表
     *
     * @param reportId 报告ID
     * @return 怀疑药品列表
     */
    List<DrugAdverseSuspectDrug> selectByReportId(String reportId);

    /**
     * 根据报告ID删除怀疑药品
     *
     * @param reportId 报告ID
     * @return 删除的记录数
     */
    int deleteByReportId(String reportId);

    /**
     * 批量保存怀疑药品
     *
     * @param reportId     报告ID
     * @param suspectDrugs 怀疑药品列表
     * @return 是否保存成功
     */
    boolean saveBatchByReportId(String reportId, List<DrugAdverseSuspectDrug> suspectDrugs);
}
