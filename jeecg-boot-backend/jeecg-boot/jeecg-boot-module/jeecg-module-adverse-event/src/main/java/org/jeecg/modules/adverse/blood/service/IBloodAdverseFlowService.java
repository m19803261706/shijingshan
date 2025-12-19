package org.jeecg.modules.adverse.blood.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseFlow;

/**
 * 输血使用不良事件流转记录 Service 接口
 * <p>
 * 提供输血使用不良事件流转记录的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/79">Issue #79</a>
 */
public interface IBloodAdverseFlowService extends IService<BloodAdverseFlow> {

    /**
     * 根据报告ID查询流转记录列表
     *
     * @param reportId 报告ID
     * @return 流转记录列表（按创建时间排序）
     */
    List<BloodAdverseFlow> getFlowListByReportId(String reportId);

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
    BloodAdverseFlow recordFlow(String reportId, String action, String fromStatus,
                                String toStatus, String operatorId, String operatorName, String comment);
}
