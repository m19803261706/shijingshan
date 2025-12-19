package org.jeecg.modules.adverse.blood.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 输血使用不良事件流转记录表
 * <p>
 * 记录报告从提交到结案的所有流转操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/77">Issue #77</a>
 */
@Data
@TableName("blood_adverse_flow")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "输血使用不良事件流转记录表")
public class BloodAdverseFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 报告ID
     */
    @Schema(description = "报告ID")
    private String reportId;

    /**
     * 操作类型
     * <p>
     * submit-提交, audit_pass-审核通过, audit_reject-审核退回,
     * close_direct-直接结案, require_rectify-要求整改,
     * submit_rectify-提交整改, confirm_approve-确认通过, confirm_reject-确认退回
     * </p>
     */
    @Dict(dicCode = "flow_action")
    @Schema(description = "操作类型")
    private String action;

    /**
     * 原状态
     */
    @Dict(dicCode = "blood_report_status")
    @Schema(description = "原状态")
    private String fromStatus;

    /**
     * 新状态
     */
    @Dict(dicCode = "blood_report_status")
    @Schema(description = "新状态")
    private String toStatus;

    /**
     * 操作人ID
     */
    @Schema(description = "操作人ID")
    private String operatorId;

    /**
     * 操作人姓名
     */
    @Schema(description = "操作人姓名")
    private String operatorName;

    /**
     * 操作意见
     */
    @Schema(description = "操作意见")
    private String comment;

    /**
     * 操作时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "操作时间")
    private Date createTime;
}
