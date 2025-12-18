package org.jeecg.modules.adverse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.adverse.entity.DrugCommonConcomitant;

import java.util.List;

/**
 * 常用并用药品配置表 Mapper 接口
 * <p>
 * 提供常用并用药品的 CRUD 操作和搜索功能
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Mapper
public interface DrugCommonConcomitantMapper extends BaseMapper<DrugCommonConcomitant> {

    /**
     * 模糊搜索常用药品（用于快速选择）
     * <p>按使用次数降序排列，常用优先</p>
     *
     * @param keyword 搜索关键词（匹配通用名称、商品名称、生产厂家）
     * @return 匹配的药品列表
     */
    @Select("<script>" +
            "SELECT * FROM drug_common_concomitant " +
            "WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (generic_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR trade_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR manufacturer LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "ORDER BY use_count DESC, update_time DESC " +
            "LIMIT 50" +
            "</script>")
    List<DrugCommonConcomitant> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 检查药品是否已存在（根据通用名称+生产厂家+批准文号）
     *
     * @param genericName  通用名称
     * @param manufacturer 生产厂家
     * @param approvalNo   批准文号
     * @return 匹配的记录数
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM drug_common_concomitant " +
            "WHERE generic_name = #{genericName} " +
            "<if test='manufacturer != null'> AND manufacturer = #{manufacturer} </if>" +
            "<if test='manufacturer == null'> AND manufacturer IS NULL </if>" +
            "<if test='approvalNo != null'> AND approval_no = #{approvalNo} </if>" +
            "<if test='approvalNo == null'> AND approval_no IS NULL </if>" +
            "</script>")
    int countByUnique(@Param("genericName") String genericName,
                      @Param("manufacturer") String manufacturer,
                      @Param("approvalNo") String approvalNo);

    /**
     * 更新使用次数（+1）
     *
     * @param id 药品ID
     * @return 更新的记录数
     */
    @Update("UPDATE drug_common_concomitant SET use_count = use_count + 1, last_used_time = NOW() WHERE id = #{id}")
    int incrementUseCount(@Param("id") String id);
}
