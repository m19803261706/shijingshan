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

    // ==================== 职能处理相关方法 ====================

    /**
     * 要求整改
     * <p>
     * 职能科室要求科室进行整改，创建整改记录
     * 事件状态变更为待整改
     * </p>
     *
     * @param id          事件ID
     * @param requirement 整改要求
     * @param deadline    整改期限
     * @return 是否操作成功
     */
    boolean requireRectify(String id, String requirement, java.util.Date deadline);

    /**
     * 直接结案
     * <p>
     * 职能科室判定无需整改，直接结案
     * 适用于已妥善处理或无需进一步处理的事件
     * </p>
     *
     * @param id      事件ID
     * @param comment 结案说明
     * @return 是否操作成功
     */
    boolean closeDirectly(String id, String comment);

    /**
     * 确认整改完成
     * <p>
     * 职能科室确认科室的整改措施有效，结案
     * </p>
     *
     * @param id      事件ID
     * @param comment 确认意见
     * @return 是否操作成功
     */
    boolean confirmRectify(String id, String comment);

    /**
     * 退回整改
     * <p>
     * 职能科室判定整改措施不足，退回重新整改
     * </p>
     *
     * @param id      事件ID
     * @param comment 退回原因
     * @return 是否操作成功
     */
    boolean rejectRectifyResult(String id, String comment);

    /**
     * 校验是否可以职能处理
     * <p>
     * 仅待处理状态可以进行职能处理（要求整改或直接结案）
     * </p>
     *
     * @param id 事件ID
     * @return 是否可处理
     */
    boolean canProcess(String id);

    /**
     * 校验是否可以确认/退回整改
     * <p>
     * 仅整改中状态且整改已提交时可操作
     * </p>
     *
     * @param id 事件ID
     * @return 是否可确认整改
     */
    boolean canConfirmRectify(String id);
}
