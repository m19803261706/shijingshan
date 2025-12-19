package org.jeecg.modules.adverse.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 药品不良反应整改表
 * <p>
 * 记录药品不良反应报告的整改要求、整改措施和确认结果
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see DrugAdverseReport
 */
@Data
@TableName("drug_adverse_rectify")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "药品不良反应整改表")
public class DrugAdverseRectify implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 主键 ====================

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
     * 整改轮次（第几次整改）
     */
    @Schema(description = "整改轮次")
    private Integer rectifyRound;

    /**
     * 上一轮整改记录ID（退回后创建新记录时关联）
     */
    @Schema(description = "上一轮整改记录ID")
    private String prevRectifyId;

    // ==================== 整改要求（药剂科填写） ====================

    /**
     * 整改要求
     */
    @Schema(description = "整改要求")
    private String requirement;

    /**
     * 整改期限
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "整改期限")
    private Date deadline;

    /**
     * 下发人ID
     */
    @Schema(description = "下发人ID")
    private String requireUserId;

    /**
     * 下发人姓名
     */
    @Schema(description = "下发人姓名")
    private String requireUserName;

    /**
     * 下发时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "下发时间")
    private Date requireTime;

    /**
     * 下发备注
     */
    @Schema(description = "下发备注")
    private String requireComment;

    // ==================== 整改措施（临床人员填写） ====================

    /**
     * 整改措施
     */
    @Schema(description = "整改措施")
    private String measures;

    /**
     * 完成情况
     */
    @Schema(description = "完成情况")
    private String completion;

    /**
     * 提交人ID
     */
    @Schema(description = "提交人ID")
    private String submitUserId;

    /**
     * 提交人姓名
     */
    @Schema(description = "提交人姓名")
    private String submitUserName;

    /**
     * 提交时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "提交时间")
    private Date submitTime;

    // ==================== 确认结果（药剂科填写） ====================

    /**
     * 确认结果
     * <p>approved-通过 / rejected-退回</p>
     */
    @Dict(dicCode = "drug_adr_confirm_result")
    @Schema(description = "确认结果：approved-通过 / rejected-退回")
    private String confirmResult;

    /**
     * 确认人ID
     */
    @Schema(description = "确认人ID")
    private String confirmUserId;

    /**
     * 确认人姓名
     */
    @Schema(description = "确认人姓名")
    private String confirmUserName;

    /**
     * 确认时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "确认时间")
    private Date confirmTime;

    /**
     * 确认意见
     */
    @Schema(description = "确认意见")
    private String confirmComment;

    // ==================== 状态 ====================

    /**
     * 状态
     * <p>pending-待整改 / submitted-已提交 / approved-已通过 / rejected-已退回</p>
     */
    @Dict(dicCode = "drug_adr_rectify_status")
    @Schema(description = "状态：pending-待整改 / submitted-已提交 / approved-已通过 / rejected-已退回")
    private String status;

    // ==================== 系统字段 ====================

    /**
     * 创建人登录名
     */
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新人登录名
     */
    @Dict(dictTable = "sys_user", dicCode = "username", dicText = "realname")
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    /**
     * 删除标记
     * <p>0-正常 1-已删除</p>
     */
    @TableLogic
    @Schema(description = "删除标记")
    private Integer delFlag;

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Integer tenantId;
}
