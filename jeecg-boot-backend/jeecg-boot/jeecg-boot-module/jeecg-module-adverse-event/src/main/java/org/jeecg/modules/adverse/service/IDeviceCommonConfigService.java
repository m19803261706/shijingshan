package org.jeecg.modules.adverse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.adverse.entity.DeviceCommonConfig;

import java.util.List;

/**
 * 常用医疗器械配置 服务接口
 *
 * @author TC Agent
 * @since 2025-12-19
 */
public interface IDeviceCommonConfigService extends IService<DeviceCommonConfig> {

    /**
     * 搜索常用医疗器械
     * <p>
     * 按产品名称、商品名称、生产企业、注册证号搜索
     * 结果按使用次数降序排列
     * </p>
     *
     * @param keyword 搜索关键词
     * @return 匹配的器械列表
     */
    List<DeviceCommonConfig> search(String keyword);

    /**
     * 更新使用次数
     * <p>
     * 使用次数+1，更新最后使用时间
     * </p>
     *
     * @param id 配置ID
     */
    void updateUseCount(String id);

    /**
     * 保存器械配置（如果不存在）
     * <p>
     * 根据产品名称+生产企业+型号规格判断是否存在
     * 不存在则新增，存在则不处理
     * </p>
     *
     * @param config 器械配置
     * @return 是否新增成功
     */
    boolean saveIfNotExists(DeviceCommonConfig config);
}
