package org.jeecg.modules.adverse.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.adverse.entity.DrugAdverseFlow;
import org.jeecg.modules.adverse.mapper.DrugAdverseFlowMapper;
import org.jeecg.modules.adverse.service.IDrugAdverseFlowService;
import org.springframework.stereotype.Service;

/**
 * 药品不良反应流转记录 Service 实现类
 *
 * @author TC Agent
 * @since 2025-12-19
 */
@Slf4j
@Service
public class DrugAdverseFlowServiceImpl extends ServiceImpl<DrugAdverseFlowMapper, DrugAdverseFlow>
        implements IDrugAdverseFlowService {

    @Override
    public List<DrugAdverseFlow> getFlowListByReportId(String reportId) {
        return baseMapper.selectByReportId(reportId);
    }

    @Override
    public DrugAdverseFlow recordFlow(String reportId, String action, String fromStatus,
                                      String toStatus, String operatorId, String operatorName, String comment) {
        DrugAdverseFlow flow = new DrugAdverseFlow();
        flow.setReportId(reportId);
        flow.setAction(action);
        flow.setFromStatus(fromStatus);
        flow.setToStatus(toStatus);
        flow.setOperatorId(operatorId);
        flow.setOperatorName(operatorName);
        flow.setComment(comment);
        flow.setCreateTime(new Date());

        this.save(flow);
        log.info("记录流转操作: reportId={}, action={}, fromStatus={}, toStatus={}",
                reportId, action, fromStatus, toStatus);

        return flow;
    }
}
