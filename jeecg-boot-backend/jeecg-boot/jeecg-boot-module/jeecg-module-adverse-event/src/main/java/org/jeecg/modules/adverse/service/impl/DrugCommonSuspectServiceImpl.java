package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.adverse.entity.DrugCommonSuspect;
import org.jeecg.modules.adverse.mapper.DrugCommonSuspectMapper;
import org.jeecg.modules.adverse.service.IDrugCommonSuspectService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 常用怀疑药品配置表 服务实现类
 * <p>
 * 实现常用怀疑药品的业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Slf4j
@Service
public class DrugCommonSuspectServiceImpl extends ServiceImpl<DrugCommonSuspectMapper, DrugCommonSuspect>
        implements IDrugCommonSuspectService {

    @Override
    public List<DrugCommonSuspect> searchByKeyword(String keyword) {
        return baseMapper.searchByKeyword(keyword);
    }

    @Override
    public boolean saveIfNotExists(DrugCommonSuspect drug) {
        // 检查是否已存在
        if (exists(drug.getGenericName(), drug.getManufacturer(), drug.getApprovalNo())) {
            log.debug("药品已存在，跳过保存: genericName={}, manufacturer={}, approvalNo={}",
                    drug.getGenericName(), drug.getManufacturer(), drug.getApprovalNo());
            return false;
        }

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser != null ? loginUser.getUsername() : null;
        Date now = new Date();

        // 设置默认值
        drug.setUseCount(0);
        drug.setCreateBy(username);
        drug.setCreateTime(now);

        log.info("保存新的常用药品配置: genericName={}", drug.getGenericName());
        return this.save(drug);
    }

    @Override
    public boolean incrementUseCount(String id) {
        int updated = baseMapper.incrementUseCount(id);
        return updated > 0;
    }

    @Override
    public boolean exists(String genericName, String manufacturer, String approvalNo) {
        int count = baseMapper.countByUnique(genericName, manufacturer, approvalNo);
        return count > 0;
    }
}
