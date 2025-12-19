package org.jeecg.modules.adverse.entity;

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
 * 药品不良反应流转记录表
 * <p>
 * 记录报告在各状态间的流转历史，用于审计追踪
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Data
@TableName("drug_adverse_flow")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "药品不良反应流转记录表")
public class DrugAdverseFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 关联报告ID
     */
    @Schema(description = "关联报告ID")
    private String reportId;

    /**
     * 操作类型
     * <p>
     * submit-提交, audit_pass-审核通过, audit_reject-审核退回,
     * resubmit-重新提交, process_start-开始处理, require_rectify-要求整改,
     * submit_rectify-提交整改, confirm_rectify-确认整改, close-结案
     * </p>
     */
    @Dict(dicCode = "drug_adr_flow_action")
    @Schema(description = "操作类型")
    private String action;

    /**
     * 操作前状态
     */
    @Dict(dicCode = "drug_adr_status")
    @Schema(description = "操作前状态")
    private String fromStatus;

    /**
     * 操作后状态
     */
    @Dict(dicCode = "drug_adr_status")
    @Schema(description = "操作后状态")
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
     * 操作备注
     */
    @Schema(description = "操作备注")
    private String comment;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;
}
