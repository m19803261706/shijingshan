package org.jeecg.modules.adverse.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 事件分类表
 * <p>
 * 支持多级分类管理，用于不良事件的分类
 * 支持层级结构：一级分类 → 二级分类 → 三级分类
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Data
@TableName("event_category")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "事件分类表")
public class EventCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private String id;

    /**
     * 父级ID
     * <p>NULL表示顶级分类</p>
     */
    @Schema(description = "父级ID")
    private String parentId;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称", width = 20)
    @Schema(description = "分类名称")
    private String name;

    /**
     * 分类编码
     * <p>便于程序识别，如 DRUG、NURSING 等</p>
     */
    @Excel(name = "分类编码", width = 15)
    @Schema(description = "分类编码")
    private String code;

    /**
     * 分类描述
     */
    @Schema(description = "分类描述")
    private String description;

    /**
     * 层级
     * <p>
     * 1: 一级分类<br>
     * 2: 二级分类<br>
     * 3: 三级分类
     * </p>
     */
    @Excel(name = "层级", width = 10)
    @Schema(description = "层级（1-一级 2-二级 3-三级）")
    private Integer level;

    /**
     * 排序号
     * <p>同级内排序，数字越小越靠前</p>
     */
    @Excel(name = "排序", width = 10)
    @Schema(description = "排序号")
    private Integer sortOrder;

    /**
     * 状态
     * <p>
     * 1: 启用<br>
     * 0: 禁用
     * </p>
     */
    @Excel(name = "状态", width = 10, dicCode = "category_status")
    @Schema(description = "状态（1-启用 0-禁用）")
    private Integer status;

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

    // ========== 非数据库字段 ==========

    /**
     * 父级分类名称
     * <p>用于前端展示，非数据库字段</p>
     */
    @TableField(exist = false)
    @Schema(description = "父级分类名称")
    private String parentName;

    /**
     * 是否有子分类
     * <p>用于前端树形展示，非数据库字段</p>
     */
    @TableField(exist = false)
    @Schema(description = "是否有子分类")
    private Boolean hasChildren;
}
