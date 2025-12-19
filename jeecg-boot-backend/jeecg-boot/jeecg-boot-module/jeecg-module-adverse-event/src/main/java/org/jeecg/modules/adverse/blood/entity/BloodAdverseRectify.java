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
 * 输血使用不良事件整改记录表
 * <p>
 * 记录整改要求、整改措施和确认结果，支持多轮次整改
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/77">Issue #77</a>
 */
@Data
@TableName("blood_adverse_rectify")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "输血使用不良事件整改记录表")
public class BloodAdverseRectify implements Serializable {

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
     * 整改轮次
     */
    @Schema(description = "整改轮次")
    private Integer round;

    // ==================== 整改要求（输血科下发） ====================

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

    // ==================== 整改提交（临床提交） ====================

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

    // ==================== 确认结果（输血科确认） ====================

    /**
     * 状态
     * <p>pending-待整改, submitted-已提交, approved-已通过, rejected-已退回</p>
     */
    @Dict(dicCode = "rectify_status")
    @Schema(description = "状态：pending-待整改, submitted-已提交, approved-已通过, rejected-已退回")
    private String status;

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

    // ==================== 标准字段 ====================

    /**
     * 创建人
     */
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
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;
}
