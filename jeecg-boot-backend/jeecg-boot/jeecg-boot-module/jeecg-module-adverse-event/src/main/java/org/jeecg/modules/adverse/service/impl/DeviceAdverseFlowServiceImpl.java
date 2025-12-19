package org.jeecg.modules.adverse.service.impl;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.adverse.entity.DeviceAdverseFlow;
import org.jeecg.modules.adverse.mapper.DeviceAdverseFlowMapper;
import org.jeecg.modules.adverse.service.IDeviceAdverseFlowService;
import org.springframework.stereotype.Service;

/**
 * 医疗器械不良事件流转记录 Service 实现类
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/62">Issue #62</a>
 */
@Slf4j
@Service
public class DeviceAdverseFlowServiceImpl extends ServiceImpl<DeviceAdverseFlowMapper, DeviceAdverseFlow>
        implements IDeviceAdverseFlowService {

    @Override
    public List<DeviceAdverseFlow> getFlowListByReportId(String reportId) {
        return baseMapper.selectByReportId(reportId);
    }

    @Override
    public DeviceAdverseFlow recordFlow(String reportId, String action, String fromStatus,
                                        String toStatus, String operatorId, String operatorName, String comment) {
        DeviceAdverseFlow flow = new DeviceAdverseFlow();
        flow.setReportId(reportId);
        flow.setAction(action);
        flow.setFromStatus(fromStatus);
        flow.setToStatus(toStatus);
        flow.setOperatorId(operatorId);
        flow.setOperatorName(operatorName);
        flow.setComment(comment);
        flow.setCreateTime(new Date());

        this.save(flow);
        log.info("记录医疗器械流转操作: reportId={}, action={}, fromStatus={}, toStatus={}",
                reportId, action, fromStatus, toStatus);

        return flow;
    }
}
