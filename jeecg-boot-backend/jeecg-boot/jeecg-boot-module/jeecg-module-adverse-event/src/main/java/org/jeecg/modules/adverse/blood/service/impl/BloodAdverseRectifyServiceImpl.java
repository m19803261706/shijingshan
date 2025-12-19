package org.jeecg.modules.adverse.blood.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.adverse.blood.entity.BloodAdverseRectify;
import org.jeecg.modules.adverse.blood.mapper.BloodAdverseRectifyMapper;
import org.jeecg.modules.adverse.blood.service.IBloodAdverseRectifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 输血使用不良事件整改记录表 服务实现类
 * <p>
 * 实现输血使用不良事件整改记录的核心业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 * @see <a href="https://github.com/m19803261706/shijingshan/issues/79">Issue #79</a>
 */
@Slf4j
@Service
public class BloodAdverseRectifyServiceImpl extends ServiceImpl<BloodAdverseRectifyMapper, BloodAdverseRectify>
        implements IBloodAdverseRectifyService {

    // ==================== 查询方法 ====================

    @Override
    public List<BloodAdverseRectify> selectByReportId(String reportId) {
        return baseMapper.selectByReportId(reportId);
    }

    @Override
    public BloodAdverseRectify getLatestByReportId(String reportId) {
        return baseMapper.selectLatestByReportId(reportId);
    }

    @Override
    public int countByReportId(String reportId) {
        return baseMapper.countByReportId(reportId);
    }

    @Override
    public int getMaxRound(String reportId) {
        return baseMapper.getMaxRound(reportId);
    }

    // ==================== 整改流程方法 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BloodAdverseRectify createRectify(String reportId, String requirement, Date deadline,
                                              String requireUserId, String requireUserName) {
        // 获取当前最大轮次
        int maxRound = getMaxRound(reportId);
        int nextRound = maxRound + 1;

        // 创建整改记录
        BloodAdverseRectify rectify = new BloodAdverseRectify();
        rectify.setReportId(reportId);
        rectify.setRound(nextRound);
        rectify.setRequirement(requirement);
        rectify.setDeadline(deadline);
        rectify.setRequireUserId(requireUserId);
        rectify.setRequireUserName(requireUserName);
        rectify.setRequireTime(new Date());
        rectify.setStatus(STATUS_PENDING);
        rectify.setCreateBy(requireUserName);
        rectify.setCreateTime(new Date());

        this.save(rectify);

        log.info("创建输血不良事件整改要求，报告ID: {}, 整改记录ID: {}, 轮次: {}, 下发人: {}",
                reportId, rectify.getId(), nextRound, requireUserName);
        return rectify;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitRectify(String rectifyId, String measures, String completion,
                                  String submitUserId, String submitUserName) {
        // 1. 获取整改记录
        BloodAdverseRectify rectify = this.getById(rectifyId);
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

        log.info("提交输血不良事件整改措施，整改记录ID: {}, 轮次: {}, 提交人: {}",
                rectifyId, rectify.getRound(), submitUserName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmApprove(String rectifyId, String confirmUserId, String confirmUserName, String confirmComment) {
        // 1. 获取整改记录
        BloodAdverseRectify rectify = this.getById(rectifyId);
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
        rectify.setConfirmUserId(confirmUserId);
        rectify.setConfirmUserName(confirmUserName);
        rectify.setConfirmTime(now);
        rectify.setConfirmComment(confirmComment);
        rectify.setStatus(STATUS_APPROVED);
        rectify.setUpdateBy(confirmUserName);
        rectify.setUpdateTime(now);

        this.updateById(rectify);

        log.info("确认输血不良事件整改通过，整改记录ID: {}, 轮次: {}, 确认人: {}",
                rectifyId, rectify.getRound(), confirmUserName);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReject(String rectifyId, String confirmUserId, String confirmUserName,
                                  String confirmComment, Date nextDeadline) {
        // 1. 获取整改记录
        BloodAdverseRectify rectify = this.getById(rectifyId);
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
        rectify.setConfirmUserId(confirmUserId);
        rectify.setConfirmUserName(confirmUserName);
        rectify.setConfirmTime(now);
        rectify.setConfirmComment(confirmComment);
        rectify.setStatus(STATUS_REJECTED);
        rectify.setUpdateBy(confirmUserName);
        rectify.setUpdateTime(now);
        this.updateById(rectify);

        // 4. 创建下一轮整改记录
        int nextRound = (rectify.getRound() != null ? rectify.getRound() : 1) + 1;
        BloodAdverseRectify nextRectify = new BloodAdverseRectify();
        nextRectify.setReportId(rectify.getReportId());
        nextRectify.setRound(nextRound);
        nextRectify.setRequirement(rectify.getRequirement());  // 继承整改要求
        nextRectify.setDeadline(nextDeadline != null ? nextDeadline : rectify.getDeadline());  // 继承或更新期限
        nextRectify.setRequireUserId(confirmUserId);
        nextRectify.setRequireUserName(confirmUserName);
        nextRectify.setRequireTime(now);
        nextRectify.setStatus(STATUS_PENDING);
        nextRectify.setCreateBy(confirmUserName);
        nextRectify.setCreateTime(now);
        this.save(nextRectify);

        log.info("退回输血不良事件整改，原记录ID: {}, 轮次: {}, 新记录ID: {}, 新轮次: {}, 确认人: {}, 原因: {}",
                rectifyId, rectify.getRound(), nextRectify.getId(), nextRound, confirmUserName, confirmComment);
        return true;
    }

    // ==================== 状态检查方法 ====================

    @Override
    public boolean canSubmit(String rectifyId) {
        BloodAdverseRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            return false;
        }
        // 仅待整改状态可提交
        return STATUS_PENDING.equals(rectify.getStatus());
    }

    @Override
    public boolean canConfirm(String rectifyId) {
        BloodAdverseRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            return false;
        }
        return STATUS_SUBMITTED.equals(rectify.getStatus());
    }
}
