package org.jeecg.modules.adverse.blood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseRectify;

import java.util.Date;
import java.util.List;

/**
 * 输血使用不良事件整改记录表 服务接口
 * <p>
 * 提供输血使用不良事件整改记录的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/79">Issue #79</a>
 */
public interface IBloodAdverseRectifyService extends IService<BloodAdverseRectify> {

    // ==================== 整改状态常量 ====================

    /**
     * 整改状态：待整改
     */
    String STATUS_PENDING = "pending";

    /**
     * 整改状态：已提交
     */
    String STATUS_SUBMITTED = "submitted";

    /**
     * 整改状态：已通过
     */
    String STATUS_APPROVED = "approved";

    /**
     * 整改状态：已退回
     */
    String STATUS_REJECTED = "rejected";

    // ==================== 查询方法 ====================

    /**
     * 根据报告ID查询整改记录列表
     *
     * @param reportId 报告ID
     * @return 整改记录列表（按轮次升序）
     */
    List<BloodAdverseRectify> selectByReportId(String reportId);

    /**
     * 查询报告的最新整改记录
     *
     * @param reportId 报告ID
     * @return 最新的整改记录（轮次最大），无记录返回 null
     */
    BloodAdverseRectify getLatestByReportId(String reportId);

    /**
     * 统计报告的整改轮次
     *
     * @param reportId 报告ID
     * @return 整改轮次数
     */
    int countByReportId(String reportId);

    /**
     * 获取报告的当前最大轮次号
     *
     * @param reportId 报告ID
     * @return 当前最大轮次号，如果没有则返回 0
     */
    int getMaxRound(String reportId);

    // ==================== 整改流程方法 ====================

    /**
     * 创建整改要求
     * <p>
     * 输血科下发整改要求，创建整改记录
     * </p>
     *
     * @param reportId         报告ID
     * @param requirement      整改要求
     * @param deadline         整改期限
     * @param requireUserId    下发人ID
     * @param requireUserName  下发人姓名
     * @return 创建的整改记录
     */
    BloodAdverseRectify createRectify(String reportId, String requirement, Date deadline,
                                       String requireUserId, String requireUserName);

    /**
     * 提交整改措施
     * <p>
     * 临床人员提交整改措施和完成情况
     * </p>
     *
     * @param rectifyId       整改记录ID
     * @param measures        整改措施
     * @param completion      完成情况
     * @param submitUserId    提交人ID
     * @param submitUserName  提交人姓名
     * @return 是否提交成功
     */
    boolean submitRectify(String rectifyId, String measures, String completion,
                          String submitUserId, String submitUserName);

    /**
     * 确认整改通过
     * <p>
     * 输血科确认整改已完成
     * </p>
     *
     * @param rectifyId        整改记录ID
     * @param confirmUserId    确认人ID
     * @param confirmUserName  确认人姓名
     * @param confirmComment   确认意见
     * @return 是否确认成功
     */
    boolean confirmApprove(String rectifyId, String confirmUserId, String confirmUserName, String confirmComment);

    /**
     * 退回整改
     * <p>
     * 输血科退回整改，创建下一轮整改记录
     * </p>
     *
     * @param rectifyId        整改记录ID
     * @param confirmUserId    确认人ID
     * @param confirmUserName  确认人姓名
     * @param confirmComment   退回原因
     * @param nextDeadline     下一轮整改期限（可选，不传则继承上一轮）
     * @return 是否退回成功
     */
    boolean confirmReject(String rectifyId, String confirmUserId, String confirmUserName,
                          String confirmComment, Date nextDeadline);

    // ==================== 状态检查方法 ====================

    /**
     * 判断整改记录是否可提交
     * <p>
     * 仅 pending 状态可提交
     * </p>
     *
     * @param rectifyId 整改记录ID
     * @return 是否可提交
     */
    boolean canSubmit(String rectifyId);

    /**
     * 判断整改记录是否可确认
     * <p>
     * 仅 submitted 状态可确认
     * </p>
     *
     * @param rectifyId 整改记录ID
     * @return 是否可确认
     */
    boolean canConfirm(String rectifyId);
}
