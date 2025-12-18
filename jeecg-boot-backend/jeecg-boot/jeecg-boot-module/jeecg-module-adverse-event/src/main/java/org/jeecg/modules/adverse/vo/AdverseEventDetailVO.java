package org.jeecg.modules.adverse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jeecg.modules.adverse.entity.AdverseEvent;
import org.jeecg.modules.adverse.entity.AdverseEventFlow;
import org.jeecg.modules.adverse.entity.AdverseEventRectify;

import java.io.Serializable;
import java.util.List;

/**
 * 不良事件详情 VO
 * <p>
 * 包含事件基本信息、流转记录和整改记录
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Data
@Schema(description = "不良事件详情")
public class AdverseEventDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件基本信息
     */
    @Schema(description = "事件基本信息")
    private AdverseEvent event;

    /**
     * 流转记录列表
     */
    @Schema(description = "流转记录列表")
    private List<AdverseEventFlow> flowList;

    /**
     * 整改记录列表
     */
    @Schema(description = "整改记录列表")
    private List<AdverseEventRectify> rectifyList;

    /**
     * 事件分类名称
     */
    @Schema(description = "事件分类名称")
    private String categoryName;

    /**
     * 上报科室名称
     */
    @Schema(description = "上报科室名称")
    private String departmentName;

    /**
     * 构造方法
     */
    public AdverseEventDetailVO() {
    }

    /**
     * 构造方法
     *
     * @param event       事件信息
     * @param flowList    流转记录
     * @param rectifyList 整改记录
     */
    public AdverseEventDetailVO(AdverseEvent event, List<AdverseEventFlow> flowList, List<AdverseEventRectify> rectifyList) {
        this.event = event;
        this.flowList = flowList;
        this.rectifyList = rectifyList;
    }
}
