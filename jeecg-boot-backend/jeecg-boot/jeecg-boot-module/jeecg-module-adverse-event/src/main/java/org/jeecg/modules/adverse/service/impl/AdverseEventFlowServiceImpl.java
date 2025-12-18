package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.adverse.entity.AdverseEventFlow;
import org.jeecg.modules.adverse.mapper.AdverseEventFlowMapper;
import org.jeecg.modules.adverse.service.IAdverseEventFlowService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 事件流转记录表 服务实现类
 * <p>
 * 实现事件流转记录的核心业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Service
public class AdverseEventFlowServiceImpl extends ServiceImpl<AdverseEventFlowMapper, AdverseEventFlow> implements IAdverseEventFlowService {

    @Override
    public AdverseEventFlow logFlow(String eventId, String fromStatus, String toStatus,
                                    String action, String comment) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 创建流转记录
        AdverseEventFlow flow = new AdverseEventFlow();
        flow.setEventId(eventId);
        flow.setFromStatus(fromStatus);
        flow.setToStatus(toStatus);
        flow.setAction(action);
        flow.setComment(comment);

        // 设置操作人信息
        if (loginUser != null) {
            flow.setOperatorId(loginUser.getId());
            flow.setOperatorName(loginUser.getRealname());
            flow.setOperatorDeptId(loginUser.getOrgCode());
            // 部门名称需要通过其他方式查询，这里暂不设置
            flow.setCreateBy(loginUser.getUsername());
        }
        flow.setCreateTime(new Date());

        // 保存记录
        this.save(flow);

        log.info("记录事件流转：事件ID={}, 操作={}, 状态变更: {} -> {}",
                eventId, action, fromStatus, toStatus);

        return flow;
    }

    @Override
    public List<AdverseEventFlow> getFlowListByEventId(String eventId) {
        LambdaQueryWrapper<AdverseEventFlow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdverseEventFlow::getEventId, eventId)
                .orderByDesc(AdverseEventFlow::getCreateTime);
        return this.list(queryWrapper);
    }
}
