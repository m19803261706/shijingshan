package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.AdverseEventRectify;

import java.util.Date;
import java.util.List;

/**
 * 整改记录 服务接口
 * <p>
 * 提供整改记录的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
public interface IAdverseEventRectifyService extends IService<AdverseEventRectify> {

    /**
     * 创建整改要求
     * <p>
     * 职能科室提出整改要求，创建新的整改记录
     * </p>
     *
     * @param eventId     事件ID
     * @param requirement 整改要求
     * @param deadline    整改期限
     * @return 创建的整改记录
     */
    AdverseEventRectify createRectifyRequirement(String eventId, String requirement, Date deadline);

    /**
     * 提交整改
     * <p>
     * 科室提交整改措施和结果
     * </p>
     *
     * @param rectifyId   整改记录ID
     * @param measures    整改措施
     * @param result      整改结果
     * @param attachments 整改附件
     * @return 是否提交成功
     */
    boolean submitRectify(String rectifyId, String measures, String result, String attachments);

    /**
     * 审核整改（通过）
     * <p>
     * 职能科室审核通过整改
     * </p>
     *
     * @param rectifyId 整改记录ID
     * @param comment   审核意见
     * @return 是否操作成功
     */
    boolean approveRectify(String rectifyId, String comment);

    /**
     * 审核整改（退回）
     * <p>
     * 职能科室退回整改，需重新整改
     * </p>
     *
     * @param rectifyId 整改记录ID
     * @param comment   退回原因
     * @return 是否操作成功
     */
    boolean rejectRectify(String rectifyId, String comment);

    /**
     * 获取事件的整改历史
     *
     * @param eventId 事件ID
     * @return 整改记录列表
     */
    List<AdverseEventRectify> getRectifyHistory(String eventId);

    /**
     * 获取当前整改记录
     *
     * @param eventId 事件ID
     * @return 当前整改记录
     */
    AdverseEventRectify getCurrentRectify(String eventId);
}
