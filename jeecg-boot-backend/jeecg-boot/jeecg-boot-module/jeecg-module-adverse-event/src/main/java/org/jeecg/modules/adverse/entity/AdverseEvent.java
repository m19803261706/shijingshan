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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 不良事件主表
 * <p>
 * 存储不良事件的基本信息、患者信息和处理状态
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Data
@TableName("adverse_event")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "不良事件主表")
public class AdverseEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 事件编号
     * <p>自动生成，格式：AE+年月日+4位序号，如：AE202501180001</p>
     */
    @Excel(name = "事件编号", width = 20)
    @Schema(description = "事件编号")
    private String eventCode;

    /**
     * 事件标题
     */
    @Excel(name = "事件标题", width = 30)
    @Schema(description = "事件标题")
    private String title;

    /**
     * 事件分类ID
     * <p>关联 event_category 表</p>
     */
    @Schema(description = "事件分类ID")
    @Dict(dictTable = "event_category", dicCode = "id", dicText = "name")
    private String categoryId;

    /**
     * 事件级别
     * <p>I级/II级/III级/IV级，对应字典 event_level</p>
     */
    @Excel(name = "事件级别", width = 10, dicCode = "event_level")
    @Dict(dicCode = "event_level")
    @Schema(description = "事件级别")
    private String level;

    /**
     * 事件发生时间
     */
    @Excel(name = "发生时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "事件发生时间")
    private Date occurTime;

    /**
     * 事件发生地点
     */
    @Excel(name = "发生地点", width = 25)
    @Schema(description = "事件发生地点")
    private String occurPlace;

    /**
     * 上报科室ID
     * <p>关联 sys_depart 表</p>
     */
    @Schema(description = "上报科室ID")
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String departmentId;

    /**
     * 上报人ID
     * <p>关联 sys_user 表</p>
     */
    @Schema(description = "上报人ID")
    @Dict(dictTable = "sys_user", dicCode = "id", dicText = "realname")
    private String reporterId;

    /**
     * 上报人姓名
     * <p>冗余字段，便于查询展示</p>
     */
    @Excel(name = "上报人", width = 15)
    @Schema(description = "上报人姓名")
    private String reporterName;

    /**
     * 患者姓名
     */
    @Excel(name = "患者姓名", width = 15)
    @Schema(description = "患者姓名")
    private String patientName;

    /**
     * 患者性别
     * <p>1:男 2:女</p>
     */
    @Excel(name = "患者性别", width = 10, dicCode = "sex")
    @Dict(dicCode = "sex")
    @Schema(description = "患者性别")
    private String patientGender;

    /**
     * 患者年龄
     */
    @Excel(name = "患者年龄", width = 10)
    @Schema(description = "患者年龄")
    private Integer patientAge;

    /**
     * 患者床号
     */
    @Excel(name = "床号", width = 10)
    @Schema(description = "患者床号")
    private String patientBedNo;

    /**
     * 患者住院号
     */
    @Excel(name = "住院号", width = 15)
    @Schema(description = "患者住院号")
    private String patientAdmissionNo;

    /**
     * 事件经过详细描述
     */
    @Schema(description = "事件经过详细描述")
    private String description;

    /**
     * 初步原因分析
     */
    @Schema(description = "初步原因分析")
    private String causeAnalysis;

    /**
     * 已采取的紧急措施
     */
    @Schema(description = "已采取的紧急措施")
    private String immediateMeasures;

    /**
     * 事件状态
     * <p>
     * draft: 草稿<br>
     * pending_review: 待审核<br>
     * returned: 已退回<br>
     * pending_process: 待处理<br>
     * pending_rectify: 待整改<br>
     * rectifying: 整改中<br>
     * closed: 已结案
     * </p>
     */
    @Excel(name = "状态", width = 15, dicCode = "event_status")
    @Dict(dicCode = "event_status")
    @Schema(description = "事件状态")
    private String status;

    /**
     * 附件信息
     * <p>JSON格式存储</p>
     */
    @Schema(description = "附件信息（JSON格式）")
    private String attachments;

    /**
     * 当前处理人ID
     */
    @Schema(description = "当前处理人ID")
    @Dict(dictTable = "sys_user", dicCode = "id", dicText = "realname")
    private String currentHandlerId;

    /**
     * 当前处理科室ID
     */
    @Schema(description = "当前处理科室ID")
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String currentHandlerDept;

    /**
     * 首次提交时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "首次提交时间")
    private Date submitTime;

    /**
     * 结案时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结案时间")
    private Date closeTime;

    /**
     * 是否匿名上报
     * <p>0:否 1:是</p>
     */
    @Schema(description = "是否匿名上报")
    private Integer isAnonymous;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

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

    /**
     * 所属部门编码
     */
    @Schema(description = "所属部门编码")
    private String sysOrgCode;

    /**
     * 租户ID
     * <p>多租户支持</p>
     */
    @Schema(description = "租户ID")
    private Integer tenantId;

    /**
     * 删除标记
     * <p>0:正常 1:已删除</p>
     */
    @TableLogic
    @Schema(description = "删除标记")
    private Integer delFlag;
}
