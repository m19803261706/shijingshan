package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.adverse.entity.DeviceCommonConfig;
import org.jeecg.modules.adverse.mapper.DeviceCommonConfigMapper;
import org.jeecg.modules.adverse.service.IDeviceCommonConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 常用医疗器械配置 服务实现类
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Service
public class DeviceCommonConfigServiceImpl extends ServiceImpl<DeviceCommonConfigMapper, DeviceCommonConfig>
        implements IDeviceCommonConfigService {

    /**
     * 搜索限制数量
     */
    private static final int SEARCH_LIMIT = 50;

    @Override
    public List<DeviceCommonConfig> search(String keyword) {
        LambdaQueryWrapper<DeviceCommonConfig> queryWrapper = new LambdaQueryWrapper<>();

        // 关键词搜索
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                    .like(DeviceCommonConfig::getProductName, keyword)
                    .or().like(DeviceCommonConfig::getTradeName, keyword)
                    .or().like(DeviceCommonConfig::getManufacturerName, keyword)
                    .or().like(DeviceCommonConfig::getRegistrationNo, keyword)
                    .or().like(DeviceCommonConfig::getModelSpec, keyword)
            );
        }

        // 按使用次数降序、最后使用时间降序排列
        queryWrapper.orderByDesc(DeviceCommonConfig::getUseCount)
                .orderByDesc(DeviceCommonConfig::getLastUsedTime)
                .last("LIMIT " + SEARCH_LIMIT);

        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUseCount(String id) {
        DeviceCommonConfig config = getById(id);
        if (config != null) {
            config.setUseCount((config.getUseCount() == null ? 0 : config.getUseCount()) + 1);
            config.setLastUsedTime(new Date());
            updateById(config);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveIfNotExists(DeviceCommonConfig config) {
        if (config == null || StringUtils.isBlank(config.getProductName())) {
            return false;
        }

        // 查询是否已存在（根据产品名称+生产企业+型号规格判断）
        LambdaQueryWrapper<DeviceCommonConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceCommonConfig::getProductName, config.getProductName());

        if (StringUtils.isNotBlank(config.getManufacturerName())) {
            queryWrapper.eq(DeviceCommonConfig::getManufacturerName, config.getManufacturerName());
        }

        if (StringUtils.isNotBlank(config.getModelSpec())) {
            queryWrapper.eq(DeviceCommonConfig::getModelSpec, config.getModelSpec());
        }

        long count = count(queryWrapper);

        if (count == 0) {
            // 不存在，新增
            config.setUseCount(1);
            config.setLastUsedTime(new Date());
            return save(config);
        }

        return false;
    }
}
