package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.AdverseEventFlow;

import java.util.List;

/**
 * 事件流转记录表 服务接口
 * <p>
 * 提供事件流转记录的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
public interface IAdverseEventFlowService extends IService<AdverseEventFlow> {

    /**
     * 记录流转日志
     *
     * @param eventId      事件ID
     * @param fromStatus   原状态
     * @param toStatus     目标状态
     * @param action       操作类型
     * @param comment      操作备注
     * @return 流转记录
     */
    AdverseEventFlow logFlow(String eventId, String fromStatus, String toStatus,
                             String action, String comment);

    /**
     * 获取事件的流转记录列表
     *
     * @param eventId 事件ID
     * @return 流转记录列表（按时间倒序）
     */
    List<AdverseEventFlow> getFlowListByEventId(String eventId);
}
