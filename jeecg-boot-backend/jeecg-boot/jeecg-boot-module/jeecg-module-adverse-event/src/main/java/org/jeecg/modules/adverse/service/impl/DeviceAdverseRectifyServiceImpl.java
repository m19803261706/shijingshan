package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.adverse.entity.DeviceAdverseRectify;
import org.jeecg.modules.adverse.mapper.DeviceAdverseRectifyMapper;
import org.jeecg.modules.adverse.service.IDeviceAdverseRectifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 医疗器械不良事件整改表 服务实现类
 * <p>
 * 实现医疗器械不良事件整改记录的核心业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see DeviceAdverseRectify
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/63">Issue #63</a>
 */
@Slf4j
@Service
public class DeviceAdverseRectifyServiceImpl extends ServiceImpl<DeviceAdverseRectifyMapper, DeviceAdverseRectify>
        implements IDeviceAdverseRectifyService {

    // ==================== 查询方法 ====================

    @Override
    public List<DeviceAdverseRectify> selectByReportId(String reportId) {
        return baseMapper.selectByReportId(reportId);
    }

    @Override
    public DeviceAdverseRectify getLatestByReportId(String reportId) {
        return baseMapper.selectLatestByReportId(reportId);
    }

    @Override
    public int countByReportId(String reportId) {
        return baseMapper.countByReportId(reportId);
    }

    // ==================== 整改流程方法 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceAdverseRectify createRectify(String reportId, String requirement, Date deadline,
                                               String requireUserId, String requireUserName, String requireComment) {
        // 创建整改记录（第1轮）
        DeviceAdverseRectify rectify = new DeviceAdverseRectify();
        rectify.setReportId(reportId);
        rectify.setRectifyRound(1);  // 第1轮整改
        rectify.setRequirement(requirement);
        rectify.setDeadline(deadline);
        rectify.setRequireUserId(requireUserId);
        rectify.setRequireUserName(requireUserName);
        rectify.setRequireTime(new Date());
        rectify.setRequireComment(requireComment);
        rectify.setStatus(STATUS_PENDING);
        rectify.setCreateBy(requireUserName);
        rectify.setCreateTime(new Date());

        this.save(rectify);

        log.info("创建医疗器械整改要求，报告ID: {}, 整改记录ID: {}, 轮次: 1, 下发人: {}",
                reportId, rectify.getId(), requireUserName);
        return rectify;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitRectify(String rectifyId, String measures, String completion,
                                  String submitUserId, String submitUserName) {
        // 1. 获取整改记录
        DeviceAdverseRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            log.error("整改记录不存在，ID: {}", rectifyId);
            return false;
        }

        // 2. 校验状态
        if (!canSubmit(rectifyId)) {
            log.error("整改记录状态不允许提交，当前状态: {}", rectify.getStatus());
            return false;
        }

        // 3. 更新整改记录
        Date now = new Date();
        rectify.setMeasures(measures);
        rectify.setCompletion(completion);
        rectify.setSubmitUserId(submitUserId);
        rectify.setSubmitUserName(submitUserName);
        rectify.setSubmitTime(now);
        rectify.setStatus(STATUS_SUBMITTED);
        rectify.setUpdateBy(submitUserName);
        rectify.setUpdateTime(now);

        this.updateById(rectify);

        log.info("提交医疗器械整改措施，整改记录ID: {}, 提交人: {}", rectifyId, submitUserName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmApprove(String rectifyId, String confirmUserId, String confirmUserName, String confirmComment) {
        // 1. 获取整改记录
        DeviceAdverseRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            log.error("整改记录不存在，ID: {}", rectifyId);
            return false;
        }

        // 2. 校验状态
        if (!canConfirm(rectifyId)) {
            log.error("整改记录状态不允许确认，当前状态: {}", rectify.getStatus());
            return false;
        }

        // 3. 更新整改记录
        Date now = new Date();
        rectify.setConfirmResult("approved");
        rectify.setConfirmUserId(confirmUserId);
        rectify.setConfirmUserName(confirmUserName);
        rectify.setConfirmTime(now);
        rectify.setConfirmComment(confirmComment);
        rectify.setStatus(STATUS_APPROVED);
        rectify.setUpdateBy(confirmUserName);
        rectify.setUpdateTime(now);

        this.updateById(rectify);

        log.info("确认医疗器械整改通过，整改记录ID: {}, 确认人: {}", rectifyId, confirmUserName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReject(String rectifyId, String confirmUserId, String confirmUserName, String confirmComment) {
        // 1. 获取整改记录
        DeviceAdverseRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            log.error("整改记录不存在，ID: {}", rectifyId);
            return false;
        }

        // 2. 校验状态
        if (!canConfirm(rectifyId)) {
            log.error("整改记录状态不允许确认，当前状态: {}", rectify.getStatus());
            return false;
        }

        Date now = new Date();

        // 3. 更新当前整改记录为已退回
        rectify.setConfirmResult("rejected");
        rectify.setConfirmUserId(confirmUserId);
        rectify.setConfirmUserName(confirmUserName);
        rectify.setConfirmTime(now);
        rectify.setConfirmComment(confirmComment);
        rectify.setStatus(STATUS_REJECTED);
        rectify.setUpdateBy(confirmUserName);
        rectify.setUpdateTime(now);
        this.updateById(rectify);

        // 4. 创建下一轮整改记录
        int nextRound = (rectify.getRectifyRound() != null ? rectify.getRectifyRound() : 1) + 1;
        DeviceAdverseRectify nextRectify = new DeviceAdverseRectify();
        nextRectify.setReportId(rectify.getReportId());
        nextRectify.setRectifyRound(nextRound);
        nextRectify.setPrevRectifyId(rectifyId);  // 关联上一轮记录
        nextRectify.setRequirement(rectify.getRequirement());  // 继承整改要求
        nextRectify.setDeadline(rectify.getDeadline());  // 继承整改期限
        nextRectify.setRequireUserId(confirmUserId);
        nextRectify.setRequireUserName(confirmUserName);
        nextRectify.setRequireTime(now);
        nextRectify.setRequireComment("第" + (nextRound - 1) + "次整改被退回，原因：" + confirmComment);
        nextRectify.setStatus(STATUS_PENDING);
        nextRectify.setCreateBy(confirmUserName);
        nextRectify.setCreateTime(now);
        this.save(nextRectify);

        log.info("退回医疗器械整改，原记录ID: {}, 轮次: {}, 新记录ID: {}, 新轮次: {}, 确认人: {}, 原因: {}",
                rectifyId, rectify.getRectifyRound(), nextRectify.getId(), nextRound, confirmUserName, confirmComment);
        return true;
    }

    // ==================== 状态检查方法 ====================

    @Override
    public boolean canSubmit(String rectifyId) {
        DeviceAdverseRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            return false;
        }
        String status = rectify.getStatus();
        return STATUS_PENDING.equals(status);
    }

    @Override
    public boolean canConfirm(String rectifyId) {
        DeviceAdverseRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            return false;
        }
        return STATUS_SUBMITTED.equals(rectify.getStatus());
    }
}
