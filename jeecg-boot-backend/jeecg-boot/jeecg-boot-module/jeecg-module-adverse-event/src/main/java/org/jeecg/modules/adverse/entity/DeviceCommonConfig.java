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

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 常用医疗器械配置表
 * <p>
 * 存储常用的医疗器械信息，支持快速选择和使用统计
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Data
@TableName("device_common_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "常用医疗器械配置表")
public class DeviceCommonConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== 主键 ====================

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    // ==================== 器械基本信息 ====================

    /**
     * 产品名称
     */
    @Excel(name = "产品名称", width = 25)
    @Schema(description = "产品名称")
    private String productName;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称", width = 20)
    @Schema(description = "商品名称")
    private String tradeName;

    /**
     * 注册证号
     */
    @Excel(name = "注册证号", width = 20)
    @Schema(description = "注册证号")
    private String registrationNo;

    /**
     * 生产企业名称
     */
    @Excel(name = "生产企业", width = 25)
    @Schema(description = "生产企业名称")
    private String manufacturerName;

    /**
     * 生产企业地址
     */
    @Excel(name = "企业地址", width = 30)
    @Schema(description = "生产企业地址")
    private String manufacturerAddress;

    /**
     * 企业联系方式
     */
    @Excel(name = "联系方式", width = 15)
    @Schema(description = "企业联系方式")
    private String manufacturerContact;

    /**
     * 型号规格
     */
    @Excel(name = "型号规格", width = 20)
    @Schema(description = "型号规格")
    private String modelSpec;

    /**
     * 产品编号
     */
    @Excel(name = "产品编号", width = 15)
    @Schema(description = "产品编号")
    private String productCode;

    /**
     * 产品批号
     */
    @Excel(name = "产品批号", width = 15)
    @Schema(description = "产品批号")
    private String productBatch;

    // ==================== 使用统计 ====================

    /**
     * 使用次数（用于排序推荐，常用优先）
     */
    @Excel(name = "使用次数", width = 10)
    @Schema(description = "使用次数")
    private Integer useCount;

    /**
     * 最后使用时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后使用时间")
    private Date lastUsedTime;

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

    /**
     * 删除标记
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
