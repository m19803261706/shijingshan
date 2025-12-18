package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.adverse.entity.DrugAdverseSuspectDrug;
import org.jeecg.modules.adverse.mapper.DrugAdverseSuspectDrugMapper;
import org.jeecg.modules.adverse.service.IDrugAdverseSuspectDrugService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 药品不良反应报告-怀疑药品子表 服务实现类
 * <p>
 * 实现怀疑药品的业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Slf4j
@Service
public class DrugAdverseSuspectDrugServiceImpl extends ServiceImpl<DrugAdverseSuspectDrugMapper, DrugAdverseSuspectDrug>
        implements IDrugAdverseSuspectDrugService {

    @Override
    public List<DrugAdverseSuspectDrug> selectByReportId(String reportId) {
        return baseMapper.selectByReportId(reportId);
    }

    @Override
    public int deleteByReportId(String reportId) {
        return baseMapper.deleteByReportId(reportId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchByReportId(String reportId, List<DrugAdverseSuspectDrug> suspectDrugs) {
        if (suspectDrugs == null || suspectDrugs.isEmpty()) {
            return true;
        }

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser != null ? loginUser.getUsername() : null;
        Date now = new Date();

        // 设置关联ID和排序号
        int sortOrder = 0;
        for (DrugAdverseSuspectDrug drug : suspectDrugs) {
            drug.setReportId(reportId);
            drug.setSortOrder(sortOrder++);

            if (oConvertUtils.isEmpty(drug.getId())) {
                // 新增
                drug.setCreateBy(username);
                drug.setCreateTime(now);
            } else {
                // 更新
                drug.setUpdateBy(username);
                drug.setUpdateTime(now);
            }
        }

        return this.saveBatch(suspectDrugs);
    }
}
