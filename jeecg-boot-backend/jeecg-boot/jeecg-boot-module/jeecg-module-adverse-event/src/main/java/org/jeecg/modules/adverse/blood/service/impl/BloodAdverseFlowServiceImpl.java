package org.jeecg.modules.adverse.blood.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseFlow;
import org.jeecg.modules.adverse.blood.mapper.BloodAdverseFlowMapper;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseFlowService;
import org.springframework.stereotype.Service;

/**
 * 输血使用不良事件流转记录 Service 实现类
 * <p>
 * 实现输血使用不良事件流转记录的核心业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/79">Issue #79</a>
 */
@Slf4j
@Service
public class BloodAdverseFlowServiceImpl extends ServiceImpl<BloodAdverseFlowMapper, BloodAdverseFlow>
        implements IBloodAdverseFlowService {

    @Override
    public List<BloodAdverseFlow> getFlowListByReportId(String reportId) {
        return baseMapper.selectByReportId(reportId);
    }

    @Override
    public BloodAdverseFlow recordFlow(String reportId, String action, String fromStatus,
                                       String toStatus, String operatorId, String operatorName, String comment) {
        BloodAdverseFlow flow = new BloodAdverseFlow();
        flow.setReportId(reportId);
        flow.setAction(action);
        flow.setFromStatus(fromStatus);
        flow.setToStatus(toStatus);
        flow.setOperatorId(operatorId);
        flow.setOperatorName(operatorName);
        flow.setComment(comment);
        flow.setCreateTime(new Date());

        this.save(flow);
        log.info("记录输血不良事件流转操作: reportId={}, action={}, fromStatus={}, toStatus={}",
                reportId, action, fromStatus, toStatus);

        return flow;
    }
}
