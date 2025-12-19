package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.DeviceAdverseReport;

/**
 * 医疗器械不良事件报告主表 服务接口
 * <p>
 * 提供医疗器械不良事件报告的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/61">Issue #61</a>
 */
public interface IDeviceAdverseReportService extends IService<DeviceAdverseReport> {

    /**
     * 生成报告编号
     * <p>
     * 规则：MDE + 年月日(yyyyMMdd) + 4位序号
     * 例如：MDE202512190001
     * </p>
     *
     * @return 新的报告编号
     */
    String generateReportCode();

    /**
     * 保存草稿
     * <p>
     * 保存或更新报告草稿，状态为 draft
     * </p>
     *
     * @param report 报告主表
     * @return 保存后的报告
     */
    DeviceAdverseReport saveDraft(DeviceAdverseReport report);

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
     * 校验报告是否可编辑
     * <p>
     * 仅 draft 和 returned 状态可编辑
     * </p>
     *
     * @param id 报告ID
     * @return 是否可编辑
     */
    boolean canEdit(String id);

    // ==================== 审核相关方法 ====================

    /**
     * 判断报告是否可审核
     * <p>
     * 仅 pending_audit 状态的报告可审核
     * </p>
     *
     * @param id 报告ID
     * @return 是否可审核
     */
    boolean canAudit(String id);

    /**
     * 审核通过
     * <p>
     * 将报告状态从 pending_audit 改为 pending_process，
     * 记录审核信息和流转历史
     * </p>
     *
     * @param id           报告ID
     * @param auditUserId  审核人ID
     * @param auditUserName 审核人姓名
     * @param comment      审核意见（选填）
     * @return 是否成功
     */
    boolean auditPass(String id, String auditUserId, String auditUserName, String comment);

    /**
     * 审核退回
     * <p>
     * 将报告状态从 pending_audit 改为 returned，
     * 记录退回原因和流转历史
     * </p>
     *
     * @param id           报告ID
     * @param auditUserId  审核人ID
     * @param auditUserName 审核人姓名
     * @param comment      退回原因（必填）
     * @return 是否成功
     */
    boolean auditReject(String id, String auditUserId, String auditUserName, String comment);

    // ==================== 处理相关方法 ====================

    /**
     * 判断报告是否可处理
     * <p>
     * 仅 pending_process 状态的报告可处理
     * </p>
     *
     * @param id 报告ID
     * @return 是否可处理
     */
    boolean canProcess(String id);

    /**
     * 判断报告是否可提交整改
     * <p>
     * 仅 pending_rectify 状态（待整改）的报告可提交整改
     * </p>
     *
     * @param id 报告ID
     * @return 是否可提交整改
     */
    boolean canSubmitRectify(String id);
}
