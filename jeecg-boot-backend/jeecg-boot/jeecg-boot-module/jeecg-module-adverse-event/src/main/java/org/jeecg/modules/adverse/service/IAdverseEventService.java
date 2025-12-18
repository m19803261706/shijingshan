package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.AdverseEvent;

/**
 * 不良事件主表 服务接口
 * <p>
 * 提供不良事件的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
public interface IAdverseEventService extends IService<AdverseEvent> {

    /**
     * 生成事件编号
     * <p>
     * 规则：AE + 年月日(yyyyMMdd) + 4位序号
     * 例如：AE202501180001
     * </p>
     *
     * @return 新的事件编号
     */
    String generateEventCode();

    /**
     * 提交事件上报
     * <p>
     * 校验必填字段，更新状态为待审核，记录流转日志
     * </p>
     *
     * @param id 事件ID
     * @return 是否提交成功
     */
    boolean submitEvent(String id);

    /**
     * 保存草稿
     * <p>
     * 保存或更新事件草稿，状态为 draft
     * </p>
     *
     * @param event 事件信息
     * @return 保存后的事件
     */
    AdverseEvent saveDraft(AdverseEvent event);

    /**
     * 删除事件
     * <p>
     * 仅草稿状态的事件可以删除
     * </p>
     *
     * @param id 事件ID
     * @return 是否删除成功
     */
    boolean deleteEvent(String id);

    /**
     * 校验事件是否可编辑
     * <p>
     * 仅 draft 和 returned 状态可编辑
     * </p>
     *
     * @param id 事件ID
     * @return 是否可编辑
     */
    boolean canEdit(String id);

    // ==================== 科室审核相关方法 ====================

    /**
     * 审核通过
     * <p>
     * 将事件状态从待审核改为待处理，流转至职能科室
     * </p>
     *
     * @param id      事件ID
     * @param comment 审核意见
     * @return 是否操作成功
     */
    boolean auditPass(String id, String comment);

    /**
     * 审核退回
     * <p>
     * 将事件状态从待审核改为已退回，需填写退回原因
     * </p>
     *
     * @param id      事件ID
     * @param comment 退回原因（必填）
     * @return 是否操作成功
     */
    boolean auditReject(String id, String comment);

    /**
     * 校验是否可以审核
     * <p>
     * 仅待审核状态可以审核
     * </p>
     *
     * @param id 事件ID
     * @return 是否可审核
     */
    boolean canAudit(String id);
}
