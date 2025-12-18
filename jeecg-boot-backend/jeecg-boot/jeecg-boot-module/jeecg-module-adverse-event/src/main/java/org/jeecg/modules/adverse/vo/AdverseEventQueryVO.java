package org.jeecg.modules.adverse.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 不良事件查询条件 VO
 * <p>
 * 用于综合查询接口的多条件筛选
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Data
@Schema(description = "不良事件查询条件")
public class AdverseEventQueryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件编号（模糊查询）
     */
    @Schema(description = "事件编号")
    private String eventCode;

    /**
     * 事件标题（模糊查询）
     */
    @Schema(description = "事件标题")
    private String title;

    /**
     * 事件分类ID
     */
    @Schema(description = "事件分类ID")
    private String categoryId;

    /**
     * 事件级别
     */
    @Schema(description = "事件级别")
    private String level;

    /**
     * 事件状态
     */
    @Schema(description = "事件状态")
    private String status;

    /**
     * 上报科室ID
     */
    @Schema(description = "上报科室ID")
    private String departmentId;

    /**
     * 上报人ID
     */
    @Schema(description = "上报人ID")
    private String reporterId;

    /**
     * 上报人姓名（模糊查询）
     */
    @Schema(description = "上报人姓名")
    private String reporterName;

    /**
     * 发生时间 - 开始
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "发生时间开始")
    private Date occurTimeBegin;

    /**
     * 发生时间 - 结束
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "发生时间结束")
    private Date occurTimeEnd;

    /**
     * 创建时间 - 开始
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "创建时间开始")
    private Date createTimeBegin;

    /**
     * 创建时间 - 结束
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "创建时间结束")
    private Date createTimeEnd;

    /**
     * 患者姓名（模糊查询）
     */
    @Schema(description = "患者姓名")
    private String patientName;

    /**
     * 住院号（精确查询）
     */
    @Schema(description = "住院号")
    private String patientAdmissionNo;
}
