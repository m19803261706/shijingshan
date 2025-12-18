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
 * 药品不良反应报告-并用药品子表
 * <p>
 * 存储报告中涉及的并用药品信息，用于评估药物相互作用
 * 一个报告可以有多个并用药品
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Data
@TableName("drug_adverse_concomitant_drug")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "药品不良反应报告-并用药品子表")
public class DrugAdverseConcomitantDrug implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 主键和关联 ====================

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 关联报告ID
     * <p>关联 drug_adverse_report.id</p>
     */
    @Schema(description = "关联报告ID")
    private String reportId;

    /**
     * 排序号
     */
    @Schema(description = "排序号")
    private Integer sortOrder;

    // ==================== 药品基本信息 ====================

    /**
     * 批准文号
     */
    @Excel(name = "批准文号", width = 20)
    @Schema(description = "批准文号")
    private String approvalNo;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称", width = 20)
    @Schema(description = "商品名称")
    private String tradeName;

    /**
     * 通用名称（含剂型）
     */
    @Excel(name = "通用名称", width = 25)
    @Schema(description = "通用名称（含剂型）")
    private String genericName;

    /**
     * 生产厂家
     */
    @Excel(name = "生产厂家", width = 25)
    @Schema(description = "生产厂家")
    private String manufacturer;

    /**
     * 生产批号
     */
    @Excel(name = "生产批号", width = 15)
    @Schema(description = "生产批号")
    private String batchNo;

    // ==================== 用法用量 ====================

    /**
     * 单次剂量（数值部分）
     */
    @Excel(name = "单次剂量", width = 12)
    @Schema(description = "单次剂量（数值部分）")
    private String dosePerTime;

    /**
     * 剂量单位
     * <p>g-克, mg-毫克, ug-微克, ml-毫升, IU-国际单位, other-其他</p>
     */
    @Dict(dicCode = "drug_adr_dose_unit")
    @Schema(description = "剂量单位")
    private String doseUnit;

    /**
     * 给药途径
     * <p>oral-口服, iv-静脉注射, iv_drip-静脉滴注, im-肌肉注射, sc-皮下注射, topical-外用, other-其他</p>
     */
    @Dict(dicCode = "drug_adr_route")
    @Schema(description = "给药途径")
    private String route;

    /**
     * 日次数
     * <p>如：每日3次、每8小时一次等</p>
     */
    @Excel(name = "日次数", width = 15)
    @Schema(description = "日次数")
    private String frequency;

    // ==================== 用药时间 ====================

    /**
     * 用药开始时间
     */
    @Excel(name = "开始日期", width = 12, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "用药开始时间")
    private Date startDate;

    /**
     * 用药截止时间
     */
    @Excel(name = "截止日期", width = 12, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "用药截止时间")
    private Date endDate;

    // ==================== 用药原因 ====================

    /**
     * 用药原因
     */
    @Excel(name = "用药原因", width = 20)
    @Schema(description = "用药原因")
    private String indication;

    // ==================== 系统字段 ====================

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
