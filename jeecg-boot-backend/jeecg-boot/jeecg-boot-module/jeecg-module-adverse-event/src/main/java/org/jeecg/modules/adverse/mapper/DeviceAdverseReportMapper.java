package org.jeecg.modules.adverse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.adverse.entity.DeviceAdverseReport;

/**
 * 医疗器械不良事件报告主表 Mapper 接口
 * <p>
 * 提供医疗器械不良事件报告的基础 CRUD 操作
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/61">Issue #61</a>
 */
@Mapper
public interface DeviceAdverseReportMapper extends BaseMapper<DeviceAdverseReport> {

    /**
     * 获取今日最大报告编号序号
     * <p>用于生成新的报告编号</p>
     *
     * @param prefix 报告编号前缀，如 "MDE20251219"
     * @return 今日最大序号，如果没有则返回 null
     */
    @Select("SELECT MAX(CAST(SUBSTRING(report_code, LENGTH(#{prefix}) + 1) AS UNSIGNED)) " +
            "FROM device_adverse_report WHERE report_code LIKE CONCAT(#{prefix}, '%')")
    Integer getMaxReportCodeSeq(@Param("prefix") String prefix);
}
