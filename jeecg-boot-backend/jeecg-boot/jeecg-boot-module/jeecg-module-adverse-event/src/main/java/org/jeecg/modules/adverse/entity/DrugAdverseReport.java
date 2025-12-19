package org.jeecg.modules.adverse.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * 药品不良反应报告主表
 * <p>
 * 按照国家药品不良反应/事件报告表标准设计，存储完整的报告信息
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Data
@TableName("drug_adverse_report")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "药品不良反应报告主表")
public class DrugAdverseReport implements Serializable {

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
     * <p>自动生成：ADR+年月日+4位序号，如：ADR202512190001</p>
     */
    @Excel(name = "报告编号", width = 20)
    @Schema(description = "报告编号")
    private String reportCode;

    // ==================== 报告基本信息 ====================

    /**
     * 报告类型
     * <p>first-首次报告, follow_up-跟踪报告</p>
     */
    @Excel(name = "报告类型", width = 12, dicCode = "drug_adr_report_type")
    @Dict(dicCode = "drug_adr_report_type")
    @Schema(description = "报告类型：first-首次报告, follow_up-跟踪报告")
    private String reportType;

    /**
     * 严重程度
     * <p>new-新的, serious-严重, general-一般</p>
     */
    @Excel(name = "严重程度", width = 10, dicCode = "drug_adr_severity")
    @Dict(dicCode = "drug_adr_severity")
    @Schema(description = "严重程度：new-新的, serious-严重, general-一般")
    private String severityType;

    /**
     * 报告单位类别
     * <p>hospital-医疗机构, business-经营企业, manufacture-生产企业, personal-个人, other-其他</p>
     */
    @Excel(name = "报告单位类别", width = 15, dicCode = "drug_adr_unit_category")
    @Dict(dicCode = "drug_adr_unit_category")
    @Schema(description = "报告单位类别")
    private String unitCategory;

    // ==================== 患者基本信息 ====================

    /**
     * 患者姓名
     */
    @Excel(name = "患者姓名", width = 12)
    @Schema(description = "患者姓名")
    private String patientName;

    /**
     * 患者性别
     * <p>M-男, F-女</p>
     */
    @Excel(name = "性别", width = 8, dicCode = "sex")
    @Dict(dicCode = "sex")
    @Schema(description = "患者性别：M-男, F-女")
    private String patientGender;

    /**
     * 出生日期
     */
    @Excel(name = "出生日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "出生日期")
    private Date patientBirthDate;

    /**
     * 民族
     */
    @Excel(name = "民族", width = 10)
    @Schema(description = "民族")
    private String patientNationality;

    /**
     * 体重(kg)
     */
    @Excel(name = "体重(kg)", width = 10)
    @Schema(description = "体重(kg)")
    private BigDecimal patientWeight;

    /**
     * 联系方式
     */
    @Excel(name = "联系电话", width = 15)
    @Schema(description = "患者联系方式")
    private String patientPhone;

    /**
     * 原患疾病
     */
    @Schema(description = "原患疾病")
    private String originalDisease;

    /**
     * 医院名称
     */
    @Excel(name = "医院名称", width = 25)
    @Schema(description = "医院名称")
    private String hospitalName;

    /**
     * 病历号/门诊号
     */
    @Excel(name = "病历号", width = 15)
    @Schema(description = "病历号/门诊号")
    private String medicalRecordNo;

    // ==================== 既往/家族不良反应史 ====================

    /**
     * 既往药品不良反应
     * <p>yes-有, no-无, unknown-不详</p>
     */
    @Dict(dicCode = "drug_adr_history")
    @Schema(description = "既往药品不良反应：yes-有, no-无, unknown-不详")
    private String historyAdr;

    /**
     * 既往不良反应详情
     */
    @Schema(description = "既往不良反应详情")
    private String historyAdrDetail;

    /**
     * 家族药品不良反应
     * <p>yes-有, no-无, unknown-不详</p>
     */
    @Dict(dicCode = "drug_adr_history")
    @Schema(description = "家族药品不良反应：yes-有, no-无, unknown-不详")
    private String familyAdr;

    /**
     * 家族不良反应详情
     */
    @Schema(description = "家族不良反应详情")
    private String familyAdrDetail;

    // ==================== 相关重要信息（病史）====================

    /**
     * 吸烟史
     * <p>0-否 1-是</p>
     */
    @Schema(description = "吸烟史（0-否 1-是）")
    private Integer hasSmoking;

    /**
     * 饮酒史
     * <p>0-否 1-是</p>
     */
    @Schema(description = "饮酒史（0-否 1-是）")
    private Integer hasDrinking;

    /**
     * 妊娠期
     * <p>0-否 1-是</p>
     */
    @Schema(description = "妊娠期（0-否 1-是）")
    private Integer isPregnant;

    /**
     * 肝病史
     * <p>0-否 1-是</p>
     */
    @Schema(description = "肝病史（0-否 1-是）")
    private Integer hasLiverDisease;

    /**
     * 肾病史
     * <p>0-否 1-是</p>
     */
    @Schema(description = "肾病史（0-否 1-是）")
    private Integer hasKidneyDisease;

    /**
     * 过敏史
     * <p>0-否 1-是</p>
     */
    @Schema(description = "过敏史（0-否 1-是）")
    private Integer hasAllergy;

    /**
     * 其他病史
     */
    @Schema(description = "其他病史")
    private String otherHistory;

    // ==================== 不良反应/事件信息 ====================

    /**
     * 不良反应/事件名称
     */
    @Excel(name = "不良反应名称", width = 25)
    @Schema(description = "不良反应/事件名称")
    private String reactionName;

    /**
     * 不良反应/事件发生时间
     */
    @Excel(name = "发生时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "不良反应/事件发生时间")
    private Date reactionTime;

    /**
     * 不良反应/事件过程描述
     * <p>包括症状、体征、临床检验等及处理情况</p>
     */
    @Schema(description = "不良反应/事件过程描述（包括症状、体征、临床检验等）及处理情况")
    private String reactionDescription;

    // ==================== 事件过程描述结构化字段（1:1复刻国标表单） ====================

    /**
     * 患者病因/原因
     */
    @Schema(description = "患者病因/原因")
    private String patientReason;

    /**
     * 发病年份
     */
    @Schema(description = "发病年份")
    private Integer onsetYear;

    /**
     * 发病月份
     */
    @Schema(description = "发病月份")
    private Integer onsetMonth;

    /**
     * 发病日期
     */
    @Schema(description = "发病日期")
    private Integer onsetDay;

    /**
     * 发病小时
     */
    @Schema(description = "发病小时")
    private Integer onsetHour;

    /**
     * 给药途径
     * <p>多选JSON：iv_drip-静滴, im-肌注, oral-口服, topical-外用</p>
     */
    @Schema(description = "给药途径（多选JSON）")
    private String routeType;

    /**
     * 药物名称（事件描述用）
     */
    @Schema(description = "药物名称（事件描述用）")
    private String drugNameDesc;

    /**
     * 剂量数值
     */
    @Schema(description = "剂量数值")
    private BigDecimal doseAmount;

    /**
     * 剂量单位
     * <p>多选JSON：g, mg, ug, ml, IU</p>
     */
    @Schema(description = "剂量单位（多选JSON）")
    private String doseType;

    /**
     * 溶媒名称
     */
    @Schema(description = "溶媒名称")
    private String solventName;

    /**
     * 溶媒量(ml)
     */
    @Schema(description = "溶媒量(ml)")
    private BigDecimal solventVolume;

    /**
     * 输液时长数值
     */
    @Schema(description = "输液时长数值")
    private Integer infusionDuration;

    /**
     * 输液时长单位
     * <p>多选JSON：day-天, hour-小时, minute-分钟</p>
     */
    @Schema(description = "输液时长单位（多选JSON）")
    private String infusionTimeUnit;

    /**
     * 已输入量(ml)
     */
    @Schema(description = "已输入量(ml)")
    private BigDecimal infusedVolume;

    /**
     * 出现的症状
     */
    @Schema(description = "出现的症状")
    private String symptoms;

    /**
     * 治疗方法
     */
    @Schema(description = "治疗方法")
    private String treatmentMethods;

    /**
     * 恢复时长数值
     */
    @Schema(description = "恢复时长数值")
    private Integer recoveryDuration;

    /**
     * 恢复时长单位
     * <p>多选JSON：day-天, hour-小时, minute-分钟</p>
     */
    @Schema(description = "恢复时长单位（多选JSON）")
    private String recoveryTimeUnit;

    /**
     * 症状转归
     * <p>多选JSON：improved-逐渐有所好转, no_change-无明显好转, worsened-进一步加重</p>
     */
    @Schema(description = "症状转归（多选JSON）")
    private String symptomOutcome;

    /**
     * 后续用药
     * <p>多选JSON：discontinued-未再继续使用该药, continued-继续使用该药</p>
     */
    @Schema(description = "后续用药（多选JSON）")
    private String subsequentUsage;

    // ==================== 不良反应/事件结果 ====================

    /**
     * 结果
     * <p>cured-痊愈, improved-好转, not_improved-未好转, unknown-不详, sequela-有后遗症, death-死亡</p>
     */
    @Excel(name = "结果", width = 12, dicCode = "drug_adr_result")
    @Dict(dicCode = "drug_adr_result")
    @Schema(description = "结果：cured-痊愈, improved-好转, not_improved-未好转, unknown-不详, sequela-有后遗症, death-死亡")
    private String reactionResult;

    /**
     * 后遗症表现
     */
    @Schema(description = "后遗症表现")
    private String sequelaDescription;

    /**
     * 死亡时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "死亡时间")
    private Date deathTime;

    /**
     * 直接死因
     */
    @Schema(description = "直接死因")
    private String deathCause;

    // ==================== 停药或减量后情况 ====================

    /**
     * 停药或减量后反应是否消失或减轻
     * <p>yes-是, no-否, unknown-不明, not_stop-未停药或未减量</p>
     */
    @Dict(dicCode = "drug_adr_stop_reaction")
    @Schema(description = "停药或减量后反应是否消失或减轻")
    private String stopDrugReaction;

    /**
     * 再次使用后是否再次出现
     * <p>yes-是, no-否, unknown-不明, not_use-未再使用</p>
     */
    @Dict(dicCode = "drug_adr_rechallenge")
    @Schema(description = "再次使用后是否再次出现")
    private String rechallengeReaction;

    // ==================== 对原患疾病的影响 ====================

    /**
     * 影响
     * <p>none-不明显, prolonged-病程延长, aggravated-病情加重, sequela-导致后遗症, death-导致死亡</p>
     */
    @Dict(dicCode = "drug_adr_disease_impact")
    @Schema(description = "对原患疾病的影响")
    private String diseaseImpact;

    // ==================== 关联性评价 ====================

    /**
     * 报告人评价
     * <p>definite-肯定, probable-很可能, possible-可能, unlikely-可能无关, pending-待评价, unable-无法评价</p>
     */
    @Dict(dicCode = "drug_adr_evaluation")
    @Schema(description = "报告人评价")
    private String reporterEvaluation;

    /**
     * 报告单位评价
     */
    @Dict(dicCode = "drug_adr_evaluation")
    @Schema(description = "报告单位评价")
    private String unitEvaluation;

    /**
     * 评价人签名
     */
    @Schema(description = "评价人签名")
    private String unitEvaluatorName;

    // ==================== 报告人信息 ====================

    /**
     * 报告人联系电话
     */
    @Schema(description = "报告人联系电话")
    private String reporterPhone;

    /**
     * 职业
     * <p>doctor-医生, pharmacist-药师, nurse-护士, other-其他</p>
     */
    @Dict(dicCode = "drug_adr_profession")
    @Schema(description = "报告人职业")
    private String reporterProfession;

    /**
     * 电子邮箱
     */
    @Schema(description = "报告人电子邮箱")
    private String reporterEmail;

    /**
     * 报告人签名
     */
    @Schema(description = "报告人签名")
    private String reporterSignature;

    // ==================== 报告单位信息 ====================

    /**
     * 报告单位名称
     */
    @Excel(name = "报告单位", width = 25)
    @Schema(description = "报告单位名称")
    private String unitName;

    /**
     * 联系人
     */
    @Schema(description = "单位联系人")
    private String unitContact;

    /**
     * 单位电话
     */
    @Schema(description = "单位电话")
    private String unitPhone;

    /**
     * 报告日期
     */
    @Excel(name = "报告日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "报告日期")
    private Date reportDate;

    // ==================== 生产企业填写信息来源 ====================

    /**
     * 信息来源
     * <p>hospital-医疗机构, business-经营企业, personal-个人, literature-文献报道, study-上市后研究, other-其他</p>
     */
    @Dict(dicCode = "drug_adr_info_source")
    @Schema(description = "信息来源")
    private String infoSource;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    // ==================== 系统字段 ====================

    /**
     * 状态
     * <p>draft-草稿, pending_audit-待审核, returned-已退回, pending_process-待处理, closed-已结案</p>
     */
    @Excel(name = "状态", width = 12, dicCode = "drug_adr_status")
    @Dict(dicCode = "drug_adr_status")
    @Schema(description = "状态")
    private String status;

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

    // ==================== 处理相关字段 ====================

    /**
     * 处理人ID
     */
    @Schema(description = "处理人ID")
    private String processUserId;

    /**
     * 处理人姓名
     */
    @Schema(description = "处理人姓名")
    private String processUserName;

    /**
     * 处理时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "处理时间")
    private Date processTime;

    /**
     * 处理意见
     */
    @Schema(description = "处理意见")
    private String processComment;

    // ==================== 结案相关字段 ====================

    /**
     * 结案方式
     * <p>direct-直接结案 / rectify-整改结案</p>
     */
    @Dict(dicCode = "drug_adr_close_type")
    @Schema(description = "结案方式：direct-直接结案 / rectify-整改结案")
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

    // ==================== 科室相关字段 ====================

    /**
     * 上报科室ID
     * <p>关联 sys_depart 表</p>
     */
    @Schema(description = "上报科室ID")
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String departmentId;

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
     * <p>0-正常 1-已删除</p>
     */
    @TableLogic
    @Schema(description = "删除标记")
    private Integer delFlag;
}
