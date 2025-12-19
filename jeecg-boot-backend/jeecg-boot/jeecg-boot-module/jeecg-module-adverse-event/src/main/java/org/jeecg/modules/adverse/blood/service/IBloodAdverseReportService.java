package org.jeecg.modules.adverse.blood.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseReport;

/**
 * 输血使用不良事件报告主表 服务接口
 * <p>
 * 提供输血使用不良事件报告的业务逻辑操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/79">Issue #79</a>
 */
public interface IBloodAdverseReportService extends IService<BloodAdverseReport> {

    // ==================== 报告编号 ====================

    /**
     * 生成报告编号
     * <p>
     * 规则：BLD + 年月日(yyyyMMdd) + 4位序号
     * 例如：BLD202512190001
     * </p>
     *
     * @return 新的报告编号
     */
    String generateReportCode();

    // ==================== CRUD 方法 ====================

    /**
     * 保存报告
     *
     * @param report 报告主表
     * @return 保存后的报告
     */
    BloodAdverseReport saveReport(BloodAdverseReport report);

    /**
     * 更新报告
     *
     * @param report 报告主表
     * @return 更新后的报告
     */
    BloodAdverseReport updateReport(BloodAdverseReport report);

    /**
     * 保存草稿
     * <p>
     * 保存或更新报告草稿，状态为 draft
     * </p>
     *
     * @param report 报告主表
     * @return 保存后的报告
     */
    BloodAdverseReport saveDraft(BloodAdverseReport report);

    /**
     * 删除报告
     * <p>
     * 仅草稿状态的报告可以删除
     * </p>
     *
     * @param id 报告ID
     * @return 是否删除成功
     */
    boolean deleteReport(String id);

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

    // ==================== 提交相关方法 ====================

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
     * 直接结案
     * <p>
     * 将报告状态从 pending_process 改为 closed，
     * 结案方式为 direct
     * </p>
     *
     * @param id           报告ID
     * @param closeUserId  结案人ID
     * @param closeUserName 结案人姓名
     * @param comment      结案意见
     * @return 是否成功
     */
    boolean closeDirect(String id, String closeUserId, String closeUserName, String comment);

    /**
     * 要求整改
     * <p>
     * 将报告状态从 pending_process 改为 pending_rectify，
     * 同时创建整改记录
     * </p>
     *
     * @param id           报告ID
     * @param requirement  整改要求
     * @param deadline     整改期限
     * @param operatorId   操作人ID
     * @param operatorName 操作人姓名
     * @return 是否成功
     */
    boolean requireRectify(String id, String requirement, java.util.Date deadline,
                           String operatorId, String operatorName);

    // ==================== 整改相关方法 ====================

    /**
     * 判断报告是否可提交整改
     * <p>
     * 仅 pending_rectify 或 rectifying 状态（有待整改记录）的报告可提交整改
     * </p>
     *
     * @param id 报告ID
     * @return 是否可提交整改
     */
    boolean canSubmitRectify(String id);

    /**
     * 判断报告是否可确认整改
     * <p>
     * 仅 rectifying 状态（有已提交的整改记录）的报告可确认
     * </p>
     *
     * @param id 报告ID
     * @return 是否可确认整改
     */
    boolean canConfirmRectify(String id);

    /**
     * 确认整改通过并结案
     * <p>
     * 确认整改通过后，报告状态改为 closed，结案方式为 rectify
     * </p>
     *
     * @param id               报告ID
     * @param confirmUserId    确认人ID
     * @param confirmUserName  确认人姓名
     * @param confirmComment   确认意见
     * @return 是否成功
     */
    boolean confirmRectifyAndClose(String id, String confirmUserId, String confirmUserName, String confirmComment);

    /**
     * 确认整改退回
     * <p>
     * 退回整改后，创建下一轮整改记录
     * </p>
     *
     * @param id               报告ID
     * @param confirmUserId    确认人ID
     * @param confirmUserName  确认人姓名
     * @param confirmComment   退回原因
     * @return 是否成功
     */
    boolean confirmRectifyReject(String id, String confirmUserId, String confirmUserName, String confirmComment);
}
