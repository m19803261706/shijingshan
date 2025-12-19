package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.DeviceAdverseRectify;

import java.util.Date;
import java.util.List;

/**
 * 医疗器械不良事件整改表 服务接口
 * <p>
 * 提供医疗器械不良事件整改记录的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see DeviceAdverseRectify
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/63">Issue #63</a>
 */
public interface IDeviceAdverseRectifyService extends IService<DeviceAdverseRectify> {

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
     * @return 整改记录列表（按创建时间倒序）
     */
    List<DeviceAdverseRectify> selectByReportId(String reportId);

    /**
     * 查询报告的最新整改记录
     *
     * @param reportId 报告ID
     * @return 最新的整改记录，无记录返回 null
     */
    DeviceAdverseRectify getLatestByReportId(String reportId);

    /**
     * 统计报告的整改次数
     *
     * @param reportId 报告ID
     * @return 整改次数
     */
    int countByReportId(String reportId);

    // ==================== 整改流程方法 ====================

    /**
     * 创建整改要求
     * <p>
     * 器械科下发整改要求，创建整改记录
     * </p>
     *
     * @param reportId         报告ID
     * @param requirement      整改要求
     * @param deadline         整改期限
     * @param requireUserId    下发人ID
     * @param requireUserName  下发人姓名
     * @param requireComment   下发备注
     * @return 创建的整改记录
     */
    DeviceAdverseRectify createRectify(String reportId, String requirement, Date deadline,
                                        String requireUserId, String requireUserName, String requireComment);

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
     * 器械科确认整改已完成
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
     * 器械科退回整改，要求重新整改
     * </p>
     *
     * @param rectifyId        整改记录ID
     * @param confirmUserId    确认人ID
     * @param confirmUserName  确认人姓名
     * @param confirmComment   退回原因
     * @return 是否退回成功
     */
    boolean confirmReject(String rectifyId, String confirmUserId, String confirmUserName, String confirmComment);

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
