package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.DrugAdverseConcomitantDrug;
import org.jeecg.modules.adverse.entity.DrugAdverseReport;
import org.jeecg.modules.adverse.entity.DrugAdverseSuspectDrug;

import java.util.List;

/**
 * 药品不良反应报告主表 服务接口
 * <p>
 * 提供药品不良反应报告的业务逻辑操作，支持主子表联动
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
public interface IDrugAdverseReportService extends IService<DrugAdverseReport> {

    /**
     * 生成报告编号
     * <p>
     * 规则：ADR + 年月日(yyyyMMdd) + 4位序号
     * 例如：ADR202512190001
     * </p>
     *
     * @return 新的报告编号
     */
    String generateReportCode();

    /**
     * 保存报告（含子表）
     * <p>
     * 同时保存主表和子表数据
     * </p>
     *
     * @param report          报告主表
     * @param suspectDrugs    怀疑药品列表
     * @param concomitantDrugs 并用药品列表
     * @return 保存后的报告
     */
    DrugAdverseReport saveReportWithDetails(DrugAdverseReport report,
                                             List<DrugAdverseSuspectDrug> suspectDrugs,
                                             List<DrugAdverseConcomitantDrug> concomitantDrugs);

    /**
     * 更新报告（含子表）
     * <p>
     * 先删除原有子表数据，再重新保存
     * </p>
     *
     * @param report          报告主表
     * @param suspectDrugs    怀疑药品列表
     * @param concomitantDrugs 并用药品列表
     * @return 更新后的报告
     */
    DrugAdverseReport updateReportWithDetails(DrugAdverseReport report,
                                               List<DrugAdverseSuspectDrug> suspectDrugs,
                                               List<DrugAdverseConcomitantDrug> concomitantDrugs);

    /**
     * 保存草稿
     * <p>
     * 保存或更新报告草稿，状态为 draft
     * </p>
     *
     * @param report          报告主表
     * @param suspectDrugs    怀疑药品列表
     * @param concomitantDrugs 并用药品列表
     * @return 保存后的报告
     */
    DrugAdverseReport saveDraft(DrugAdverseReport report,
                                 List<DrugAdverseSuspectDrug> suspectDrugs,
                                 List<DrugAdverseConcomitantDrug> concomitantDrugs);

    /**
     * 提交报告
     * <p>
     * 校验必填字段，更新状态为待审核
     * </p>
     *
     * @param id 报告ID
     * @return 是否提交成功
     */
    boolean submitReport(String id);

    /**
     * 删除报告（含子表）
     * <p>
     * 仅草稿状态的报告可以删除
     * </p>
     *
     * @param id 报告ID
     * @return 是否删除成功
     */
    boolean deleteReportWithDetails(String id);

    /**
     * 批量删除报告（含子表）
     *
     * @param ids 报告ID列表
     * @return 删除成功的数量
     */
    int deleteBatchWithDetails(List<String> ids);

    /**
     * 校验报告是否可编辑
     * <p>
     * 仅 draft 和 returned 状态可编辑
     * </p>
     *
     * @param id 报告ID
     * @return 是否可编辑
     */
    boolean canEdit(String id);

    /**
     * 根据ID查询报告详情（含子表）
     *
     * @param id 报告ID
     * @return 报告详情（含子表数据）
     */
    DrugAdverseReport getReportWithDetails(String id);

    /**
     * 获取怀疑药品列表
     *
     * @param reportId 报告ID
     * @return 怀疑药品列表
     */
    List<DrugAdverseSuspectDrug> getSuspectDrugsByReportId(String reportId);

    /**
     * 获取并用药品列表
     *
     * @param reportId 报告ID
     * @return 并用药品列表
     */
    List<DrugAdverseConcomitantDrug> getConcomitantDrugsByReportId(String reportId);
}
