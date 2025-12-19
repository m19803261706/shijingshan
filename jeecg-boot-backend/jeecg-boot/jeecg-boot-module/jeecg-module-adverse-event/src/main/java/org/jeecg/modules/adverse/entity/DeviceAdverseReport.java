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
 * 医疗器械不良事件报告主表
 * <p>
 * 按照国家食品药品监督管理局《可疑医疗器械不良事件报告表》标准设计
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/61">Issue #61</a>
 */
@Data
@TableName("device_adverse_report")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "医疗器械不良事件报告主表")
public class DeviceAdverseReport implements Serializable {

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
     * <p>自动生成：MDE+年月日+4位序号，如：MDE202512190001</p>
     */
    @Excel(name = "报告编号", width = 20)
    @Schema(description = "报告编号")
    private String reportCode;

    /**
     * 报告日期
     */
    @Excel(name = "报告日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "报告日期")
    private Date reportDate;

    // ==================== A. 患者资料 ====================

    /**
     * 1. 患者姓名
     */
    @Excel(name = "患者姓名", width = 12)
    @Schema(description = "1. 患者姓名")
    private String patientName;

    /**
     * 2. 年龄
     */
    @Excel(name = "年龄", width = 8)
    @Schema(description = "2. 年龄")
    private Integer patientAge;

    /**
     * 3. 性别
     * <p>M-男, F-女</p>
     */
    @Excel(name = "性别", width = 8, dicCode = "sex")
    @Dict(dicCode = "sex")
    @Schema(description = "3. 性别：M-男, F-女")
    private String patientGender;

    /**
     * 4. 预期治疗疾病或作用
     */
    @Schema(description = "4. 预期治疗疾病或作用")
    private String patientDisease;

    // ==================== B. 不良事件情况 ====================

    /**
     * 5. 事件主要表现
     */
    @Schema(description = "5. 事件主要表现")
    private String eventManifestation;

    /**
     * 6. 事件发生日期
     */
    @Excel(name = "事件发生日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "6. 事件发生日期")
    private Date eventOccurDate;

    /**
     * 7. 发现或者知悉日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "7. 发现或者知悉日期")
    private Date eventDiscoverDate;

    /**
     * 8. 医疗器械实际使用场所
     * <p>hospital-医疗机构, home-家庭, other-其它</p>
     */
    @Dict(dicCode = "device_adr_use_place")
    @Schema(description = "8. 医疗器械实际使用场所")
    private String deviceUsePlace;

    /**
     * 使用场所-其它说明
     */
    @Schema(description = "使用场所-其它说明")
    private String deviceUsePlaceOther;

    // ==================== 9. 事件后果（多选） ====================

    /**
     * 死亡
     */
    @Schema(description = "9. 事件后果-死亡（0-否 1-是）")
    private Integer resultDeath;

    /**
     * 死亡时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "死亡时间")
    private Date resultDeathTime;

    /**
     * 危及生命
     */
    @Schema(description = "9. 事件后果-危及生命（0-否 1-是）")
    private Integer resultLifeRisk;

    /**
     * 导致或延长住院
     */
    @Schema(description = "9. 事件后果-导致或延长住院（0-否 1-是）")
    private Integer resultHospitalization;

    /**
     * 机体功能结构永久性损伤
     */
    @Schema(description = "9. 事件后果-机体功能结构永久性损伤（0-否 1-是）")
    private Integer resultPermanentDamage;

    /**
     * 可能导致机体功能机构永久性损伤
     */
    @Schema(description = "9. 事件后果-可能导致永久性损伤（0-否 1-是）")
    private Integer resultInterventionNeeded;

    /**
     * 需要内、外科治疗避免上述永久损伤
     */
    @Schema(description = "9. 事件后果-需要治疗避免永久损伤（0-否 1-是）")
    private Integer resultSurgicalAvoid;

    /**
     * 其它后果
     */
    @Schema(description = "9. 事件后果-其它（0-否 1-是）")
    private Integer resultOther;

    /**
     * 其它后果描述
     */
    @Schema(description = "其它后果描述")
    private String resultOtherDesc;

    // ==================== 10. 事件陈述 ====================

    /**
     * 事件陈述
     * <p>至少包括器械使用时间、使用目的、不良事件情况、对受害者影响、采取的治疗措施、器械联合使用情况</p>
     */
    @Schema(description = "10. 事件陈述")
    private String eventStatement;

    // ==================== C. 医疗器械情况 ====================

    /**
     * 11. 产品名称
     */
    @Excel(name = "产品名称", width = 25)
    @Schema(description = "11. 产品名称")
    private String productName;

    /**
     * 12. 商品名称
     */
    @Schema(description = "12. 商品名称")
    private String tradeName;

    /**
     * 13. 注册证号
     */
    @Excel(name = "注册证号", width = 20)
    @Schema(description = "13. 注册证号")
    private String registrationNo;

    /**
     * 14. 生产企业名称
     */
    @Excel(name = "生产企业", width = 25)
    @Schema(description = "14. 生产企业名称")
    private String manufacturerName;

    /**
     * 生产企业地址
     */
    @Schema(description = "生产企业地址")
    private String manufacturerAddress;

    /**
     * 企业联系方式
     */
    @Schema(description = "企业联系方式")
    private String manufacturerContact;

    /**
     * 15. 型号规格
     */
    @Excel(name = "型号规格", width = 20)
    @Schema(description = "15. 型号规格")
    private String modelSpec;

    /**
     * 产品编号
     */
    @Schema(description = "产品编号")
    private String productCode;

    /**
     * 产品批号
     */
    @Schema(description = "产品批号")
    private String productBatch;

    // ==================== 16. 操作人 ====================

    /**
     * 操作人类型
     * <p>professional-专业人员, non_professional-非专业人员, patient-患者, other-其它</p>
     */
    @Dict(dicCode = "device_adr_operator_type")
    @Schema(description = "16. 操作人类型")
    private String operatorType;

    /**
     * 其它操作人说明
     */
    @Schema(description = "其它操作人说明")
    private String operatorTypeOther;

    // ==================== 产品有效期相关 ====================

    /**
     * 17. 有效期至
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "17. 有效期至")
    private Date validPeriodTo;

    /**
     * 18. 生产日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "18. 生产日期")
    private Date productionDate;

    /**
     * 19. 停用日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "19. 停用日期")
    private Date deactivateDate;

    /**
     * 20. 植入日期（若植入）
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "20. 植入日期（若植入）")
    private Date implantDate;

    // ==================== 21. 事件发生初步原因分析 ====================

    /**
     * 事件发生初步原因分析
     */
    @Schema(description = "21. 事件发生初步原因分析")
    private String causeAnalysis;

    // ==================== 22. 事件初步处理情况 ====================

    /**
     * 事件初步处理情况
     */
    @Schema(description = "22. 事件初步处理情况")
    private String initialHandling;

    // ==================== 23. 事件报告状态（多选） ====================

    /**
     * 已通知使用单位
     */
    @Schema(description = "23. 已通知使用单位（0-否 1-是）")
    private Integer reportStatusUseUnit;

    /**
     * 已通知经营企业
     */
    @Schema(description = "23. 已通知经营企业（0-否 1-是）")
    private Integer reportStatusBusiness;

    /**
     * 已通知生产企业
     */
    @Schema(description = "23. 已通知生产企业（0-否 1-是）")
    private Integer reportStatusManufacturer;

    /**
     * 已通知药监部门
     */
    @Schema(description = "23. 已通知药监部门（0-否 1-是）")
    private Integer reportStatusFda;

    // ==================== D. 不良事件评价 ====================

    /**
     * *1. 时间先后顺序是否合理
     * <p>yes-是, no-否</p>
     */
    @Dict(dicCode = "device_adr_eval_result")
    @Schema(description = "*1. 时间先后顺序是否合理")
    private String evalTimeSequence;

    /**
     * *2. 伤害事件是否属于器械可能导致的伤害类型
     * <p>yes-是, no-否, unknown-无法确定</p>
     */
    @Dict(dicCode = "device_adr_eval_result")
    @Schema(description = "*2. 伤害事件是否属于器械可能导致的伤害类型")
    private String evalHarmType;

    /**
     * *3. 伤害事件是否可以用合并用药和/或械的作用、患者病情或其他非医疗器械因素来解释
     * <p>yes-是, no-否, unknown-无法确定</p>
     */
    @Dict(dicCode = "device_adr_eval_result")
    @Schema(description = "*3. 是否可用其他因素解释")
    private String evalExcludeOther;

    // ==================== 报告人信息 ====================

    /**
     * 报告人类型
     * <p>doctor-医师, technician-技师, nurse-护士, other-其他</p>
     */
    @Dict(dicCode = "device_adr_reporter_type")
    @Schema(description = "报告人类型")
    private String reporterType;

    /**
     * 报告人签名
     */
    @Schema(description = "报告人签名")
    private String reporterSignature;

    // ==================== 系统流转字段 ====================

    /**
     * 状态
     * <p>draft-草稿, pending_audit-待审核, returned-已退回, pending_process-待处理,
     * pending_rectify-待整改, rectifying-整改中, closed-已结案</p>
     */
    @Excel(name = "状态", width = 12, dicCode = "device_adr_status")
    @Dict(dicCode = "device_adr_status")
    @Schema(description = "状态")
    private String status;

    /**
     * 上报科室ID
     */
    @Schema(description = "上报科室ID")
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String departmentId;

    // ==================== 审核相关字段 ====================

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
    @Dict(dicCode = "device_adr_close_type")
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

    // ==================== JeecgBoot 标准系统字段 ====================

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
