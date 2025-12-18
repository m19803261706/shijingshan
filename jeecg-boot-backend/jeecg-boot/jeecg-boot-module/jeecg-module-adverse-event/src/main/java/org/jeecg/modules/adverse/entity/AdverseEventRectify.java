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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 整改记录表
 * <p>
 * 支持多轮整改，记录每轮整改的要求、措施、结果和审核情况
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Data
@TableName("adverse_event_rectify")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "整改记录表")
public class AdverseEventRectify implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 关联事件ID
     * <p>关联 adverse_event 表</p>
     */
    @Schema(description = "关联事件ID")
    private String eventId;

    /**
     * 整改轮次
     * <p>第几次整改，支持多轮整改</p>
     */
    @Excel(name = "整改轮次", width = 10)
    @Schema(description = "整改轮次")
    private Integer rectifyNo;

    /**
     * 整改要求
     * <p>职能部门提出的整改要求</p>
     */
    @Schema(description = "整改要求")
    private String requirement;

    /**
     * 要求提出人ID
     */
    @Schema(description = "要求提出人ID")
    @Dict(dictTable = "sys_user", dicCode = "id", dicText = "realname")
    private String requirementBy;

    /**
     * 要求提出时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "要求提出时间")
    private Date requirementTime;

    /**
     * 整改期限
     */
    @Excel(name = "整改期限", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "整改期限")
    private Date deadline;

    /**
     * 整改措施
     * <p>科室填写的整改措施</p>
     */
    @Schema(description = "整改措施")
    private String measures;

    /**
     * 整改结果
     * <p>整改后的效果描述</p>
     */
    @Schema(description = "整改结果")
    private String result;

    /**
     * 整改附件
     * <p>JSON格式，整改前后对比照片等</p>
     */
    @Schema(description = "整改附件（JSON格式）")
    private String attachments;

    /**
     * 整改状态
     * <p>
     * pending: 待整改<br>
     * submitted: 已提交<br>
     * approved: 已通过<br>
     * rejected: 未通过
     * </p>
     */
    @Excel(name = "整改状态", width = 15, dicCode = "rectify_status")
    @Dict(dicCode = "rectify_status")
    @Schema(description = "整改状态")
    private String status;

    /**
     * 整改提交时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "整改提交时间")
    private Date submitTime;

    /**
     * 审核人ID
     * <p>职能部门审核人</p>
     */
    @Schema(description = "审核人ID")
    @Dict(dictTable = "sys_user", dicCode = "id", dicText = "realname")
    private String reviewerId;

    /**
     * 审核人姓名
     * <p>冗余字段，便于查询展示</p>
     */
    @Excel(name = "审核人", width = 15)
    @Schema(description = "审核人姓名")
    private String reviewerName;

    /**
     * 审核意见
     */
    @Schema(description = "审核意见")
    private String reviewComment;

    /**
     * 审核时间
     */
    @Excel(name = "审核时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审核时间")
    private Date reviewTime;

    /**
     * 审核结果
     * <p>approved: 通过, rejected: 不通过</p>
     */
    @Excel(name = "审核结果", width = 15, dicCode = "review_result")
    @Dict(dicCode = "review_result")
    @Schema(description = "审核结果")
    private String reviewResult;

    /**
     * 创建人登录名
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
     * 更新人登录名
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
