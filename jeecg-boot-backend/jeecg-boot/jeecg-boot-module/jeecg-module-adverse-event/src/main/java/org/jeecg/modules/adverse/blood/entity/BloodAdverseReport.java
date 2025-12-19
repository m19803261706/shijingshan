package org.jeecg.modules.adverse.blood.entity;

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
 * 输血使用不良事件报告主表
 * <p>
 * 基于《输血使用不良事件报告表》标准设计
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/77">Issue #77</a>
 */
@Data
@TableName("blood_adverse_report")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "输血使用不良事件报告主表")
public class BloodAdverseReport implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 主键 ====================

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 报告编号
     * <p>自动生成：BLD+年月日+4位序号，如：BLD202512190001</p>
     */
    @Excel(name = "报告编号", width = 20)
    @Schema(description = "报告编号")
    private String reportCode;

    // ==================== A. 患者资料 ====================

    /**
     * 患者姓名
     */
    @Excel(name = "患者姓名", width = 12)
    @Schema(description = "患者姓名")
    private String patientName;

    /**
     * 性别
     * <p>M-男, F-女</p>
     */
    @Excel(name = "性别", width = 8, dicCode = "sex")
    @Dict(dicCode = "sex")
    @Schema(description = "性别：M-男, F-女")
    private String patientGender;

    /**
     * 年龄
     */
    @Excel(name = "年龄", width = 8)
    @Schema(description = "年龄")
    private Integer patientAge;

    /**
     * 临床诊断
     */
    @Schema(description = "临床诊断")
    private String clinicalDiagnosis;

    /**
     * 病案号
     */
    @Excel(name = "病案号", width = 15)
    @Schema(description = "病案号")
    private String medicalRecordNo;

    /**
     * 涉及科室
     */
    @Excel(name = "涉及科室", width = 15)
    @Schema(description = "涉及科室")
    private String involvedDept;

    // ==================== B. 不良事件情况 ====================

    /**
     * 事件发生场所（多选，逗号分隔）
     * <p>门诊/急诊/病区/手术室/血透室/ICU/其他</p>
     */
    @Dict(dicCode = "blood_event_place")
    @Schema(description = "事件发生场所（多选，逗号分隔）")
    private String eventPlace;

    /**
     * 其他场所说明
     */
    @Schema(description = "其他场所说明")
    private String eventPlaceOther;

    /**
     * 事件描述
     */
    @Schema(description = "事件描述")
    private String eventDescription;

    // ==================== C. 不良事件内容 ====================

    /**
     * 不良事件名称
     * <p>输血反应/溶血反应/过敏反应/发热反应/细菌污染/TRALI/TA-GVHD/其他</p>
     */
    @Excel(name = "不良事件名称", width = 15)
    @Dict(dicCode = "blood_event_name")
    @Schema(description = "不良事件名称")
    private String eventName;

    /**
     * 其他事件名称说明
     */
    @Schema(description = "其他事件名称说明")
    private String eventNameOther;

    /**
     * 输血事件类型
     * <p>输错血型/输错患者/输错血液成分/输注过期血液/其他</p>
     */
    @Excel(name = "输血事件类型", width = 15)
    @Dict(dicCode = "blood_transfusion_event")
    @Schema(description = "输血事件类型")
    private String transfusionEvent;

    /**
     * 其他输血事件说明
     */
    @Schema(description = "其他输血事件说明")
    private String transfusionEventOther;

    /**
     * 涉及人员
     */
    @Schema(description = "涉及人员")
    private String involvedStaff;

    // ==================== D. 不良事件等级 ====================

    /**
     * 严重程度
     * <p>A-轻微, B-一般, C-严重, D-灾难</p>
     */
    @Excel(name = "严重程度", width = 10)
    @Dict(dicCode = "blood_severity_level")
    @Schema(description = "严重程度：A-轻微, B-一般, C-严重, D-灾难")
    private String severityLevel;

    /**
     * 事件等级
     * <p>I-I级, II-II级, III-III级, IV-IV级</p>
     */
    @Excel(name = "事件等级", width = 10)
    @Dict(dicCode = "blood_event_level")
    @Schema(description = "事件等级：I/II/III/IV")
    private String eventLevel;

    // ==================== E. 事件处理与分析 ====================

    /**
     * 事件处理措施
     */
    @Schema(description = "事件处理措施")
    private String handlingMeasures;

    /**
     * 导致事件的可能原因
     */
    @Schema(description = "导致事件的可能原因")
    private String possibleCauses;

    // ==================== F. 改进措施 ====================

    /**
     * 改进措施
     */
    @Schema(description = "改进措施")
    private String improvementMeasures;

    // ==================== G. 选填项目 ====================

    /**
     * 报告人类型（多选，逗号分隔）
     * <p>患者/患者家属/护士/医生/其他职员/其他</p>
     */
    @Dict(dicCode = "blood_person_type")
    @Schema(description = "报告人类型（多选，逗号分隔）")
    private String reporterType;

    /**
     * 当事人类别（多选，逗号分隔）
     * <p>患者/患者家属/护士/医生/其他职员/其他</p>
     */
    @Dict(dicCode = "blood_person_type")
    @Schema(description = "当事人类别（多选，逗号分隔）")
    private String partyType;

    /**
     * 职称
     * <p>医师/护师/技师/其他</p>
     */
    @Dict(dicCode = "blood_professional_title")
    @Schema(description = "职称")
    private String professionalTitle;

    /**
     * 报告人签名
     */
    @Schema(description = "报告人签名")
    private String reporterSignature;

    /**
     * 科室名称
     */
    @Excel(name = "科室名称", width = 15)
    @Schema(description = "科室名称")
    private String deptName;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String contactPhone;

    /**
     * Email
     */
    @Schema(description = "Email")
    private String email;

    /**
     * 报告日期
     */
    @Excel(name = "报告日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "报告日期")
    private Date reportDate;

    // ==================== 流程状态 ====================

    /**
     * 状态
     * <p>draft-草稿, pending_audit-待审核, returned-已退回, pending_process-待处理,
     * pending_rectify-待整改, rectifying-整改中, closed-已结案</p>
     */
    @Excel(name = "状态", width = 10)
    @Dict(dicCode = "blood_report_status")
    @Schema(description = "状态")
    private String status;

    /**
     * 结案方式
     * <p>direct-直接结案, rectify-整改结案</p>
     */
    @Schema(description = "结案方式：direct-直接结案, rectify-整改结案")
    private String closeType;

    /**
     * 结案人ID
     */
    @Schema(description = "结案人ID")
    private String closeUserId;

    /**
     * 结案人姓名
     */
    @Schema(description = "结案人姓名")
    private String closeUserName;

    /**
     * 结案时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结案时间")
    private Date closeTime;

    /**
     * 结案意见
     */
    @Schema(description = "结案意见")
    private String closeComment;

    // ==================== 审核信息 ====================

    /**
     * 审核人ID
     */
    @Schema(description = "审核人ID")
    private String auditUserId;

    /**
     * 审核人姓名
     */
    @Schema(description = "审核人姓名")
    private String auditUserName;

    /**
     * 审核时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审核时间")
    private Date auditTime;

    /**
     * 审核意见
     */
    @Schema(description = "审核意见")
    private String auditComment;

    // ==================== JeecgBoot 标准字段 ====================

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

    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    private String sysOrgCode;

    /**
     * 删除标志
     * <p>0-正常, 1-已删除</p>
     */
    @TableLogic
    @Schema(description = "删除标志：0-正常, 1-已删除")
    private Integer delFlag;
}
