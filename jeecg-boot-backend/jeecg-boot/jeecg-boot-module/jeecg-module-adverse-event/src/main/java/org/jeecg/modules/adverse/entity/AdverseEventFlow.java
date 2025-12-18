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
 * 事件流转记录表
 * <p>
 * 记录不良事件从创建到结案的每一步操作，支持完整的流程追溯
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Data
@TableName("adverse_event_flow")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "事件流转记录表")
public class AdverseEventFlow implements Serializable {

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
     * 原状态
     * <p>流转前的状态</p>
     */
    @Excel(name = "原状态", width = 15, dicCode = "event_status")
    @Dict(dicCode = "event_status")
    @Schema(description = "原状态")
    private String fromStatus;

    /**
     * 目标状态
     * <p>流转后的状态</p>
     */
    @Excel(name = "目标状态", width = 15, dicCode = "event_status")
    @Dict(dicCode = "event_status")
    @Schema(description = "目标状态")
    private String toStatus;

    /**
     * 操作类型
     * <p>
     * submit: 提交<br>
     * approve: 审核通过<br>
     * reject: 退回<br>
     * process: 处理<br>
     * require_rectify: 要求整改<br>
     * submit_rectify: 提交整改<br>
     * confirm_rectify: 确认整改<br>
     * close: 结案
     * </p>
     */
    @Excel(name = "操作类型", width = 15, dicCode = "flow_action")
    @Dict(dicCode = "flow_action")
    @Schema(description = "操作类型")
    private String action;

    /**
     * 操作人ID
     * <p>关联 sys_user 表</p>
     */
    @Schema(description = "操作人ID")
    @Dict(dictTable = "sys_user", dicCode = "id", dicText = "realname")
    private String operatorId;

    /**
     * 操作人姓名
     * <p>冗余字段，便于查询展示</p>
     */
    @Excel(name = "操作人", width = 15)
    @Schema(description = "操作人姓名")
    private String operatorName;

    /**
     * 操作人部门ID
     * <p>关联 sys_depart 表</p>
     */
    @Schema(description = "操作人部门ID")
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String operatorDeptId;

    /**
     * 操作人部门名称
     * <p>冗余字段，便于查询展示</p>
     */
    @Excel(name = "操作部门", width = 20)
    @Schema(description = "操作人部门名称")
    private String operatorDeptName;

    /**
     * 审批意见/操作备注
     */
    @Schema(description = "审批意见")
    private String comment;

    /**
     * 附件信息
     * <p>JSON格式，如退回说明附件</p>
     */
    @Schema(description = "附件信息（JSON格式）")
    private String attachments;

    /**
     * 下一处理人ID
     */
    @Schema(description = "下一处理人ID")
    @Dict(dictTable = "sys_user", dicCode = "id", dicText = "realname")
    private String nextHandlerId;

    /**
     * 下一处理部门ID
     */
    @Schema(description = "下一处理部门ID")
    @Dict(dictTable = "sys_depart", dicCode = "id", dicText = "depart_name")
    private String nextHandlerDept;

    /**
     * 处理期限
     * <p>如整改期限</p>
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "处理期限")
    private Date deadline;

    /**
     * 创建人登录名
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @Excel(name = "操作时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;
}
