package org.jeecg.modules.adverse.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.DrugAdverseFlow;

/**
 * 药品不良反应流转记录 Service 接口
 *
 * @author TC Agent
 * @since 2025-12-19
 */
public interface IDrugAdverseFlowService extends IService<DrugAdverseFlow> {

    /**
     * 根据报告ID查询流转记录列表
     *
     * @param reportId 报告ID
     * @return 流转记录列表（按创建时间排序）
     */
    List<DrugAdverseFlow> getFlowListByReportId(String reportId);

    /**
     * 记录流转操作
     *
     * @param reportId     报告ID
     * @param action       操作类型
     * @param fromStatus   操作前状态
     * @param toStatus     操作后状态
     * @param operatorId   操作人ID
     * @param operatorName 操作人姓名
     * @param comment      操作备注
     * @return 流转记录
     */
    DrugAdverseFlow recordFlow(String reportId, String action, String fromStatus,
                               String toStatus, String operatorId, String operatorName, String comment);
}
