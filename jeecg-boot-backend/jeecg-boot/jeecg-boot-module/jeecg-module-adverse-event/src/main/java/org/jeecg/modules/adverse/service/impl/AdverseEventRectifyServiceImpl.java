package org.jeecg.modules.adverse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.adverse.entity.AdverseEventRectify;
import org.jeecg.modules.adverse.mapper.AdverseEventRectifyMapper;
import org.jeecg.modules.adverse.service.IAdverseEventRectifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 整改记录 服务实现类
 * <p>
 * 实现整改记录的核心业务逻辑
 * </p>
 *
 * @author TC Agent
 * @since 2025-01-18
 */
@Slf4j
@Service
public class AdverseEventRectifyServiceImpl extends ServiceImpl<AdverseEventRectifyMapper, AdverseEventRectify> implements IAdverseEventRectifyService {

    /**
     * 整改状态常量
     */
    private static final String RECTIFY_STATUS_PENDING = "pending";
    private static final String RECTIFY_STATUS_SUBMITTED = "submitted";
    private static final String RECTIFY_STATUS_APPROVED = "approved";
    private static final String RECTIFY_STATUS_REJECTED = "rejected";

    /**
     * 审核结果常量
     */
    private static final String REVIEW_RESULT_APPROVED = "approved";
    private static final String REVIEW_RESULT_REJECTED = "rejected";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdverseEventRectify createRectifyRequirement(String eventId, String requirement, Date deadline) {
        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 获取下一个整改轮次
        Integer maxNo = baseMapper.getMaxRectifyNo(eventId);
        int nextNo = (maxNo == null) ? 1 : maxNo + 1;

        // 创建整改记录
        AdverseEventRectify rectify = new AdverseEventRectify();
        rectify.setEventId(eventId);
        rectify.setRectifyNo(nextNo);
        rectify.setRequirement(requirement);
        rectify.setDeadline(deadline);
        rectify.setRequirementBy(loginUser.getId());
        rectify.setRequirementTime(new Date());
        rectify.setStatus(RECTIFY_STATUS_PENDING);
        rectify.setCreateBy(loginUser.getUsername());
        rectify.setCreateTime(new Date());

        this.save(rectify);

        log.info("创建整改要求，事件ID: {}, 轮次: {}", eventId, nextNo);
        return rectify;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitRectify(String rectifyId, String measures, String result, String attachments) {
        AdverseEventRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            log.error("整改记录不存在，ID: {}", rectifyId);
            return false;
        }

        // 校验状态（仅待整改或被退回状态可提交）
        String status = rectify.getStatus();
        if (!RECTIFY_STATUS_PENDING.equals(status) && !RECTIFY_STATUS_REJECTED.equals(status)) {
            log.error("整改状态不允许提交，当前状态: {}", status);
            return false;
        }

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 更新整改记录
        rectify.setMeasures(measures);
        rectify.setResult(result);
        rectify.setAttachments(attachments);
        rectify.setStatus(RECTIFY_STATUS_SUBMITTED);
        rectify.setSubmitTime(new Date());
        rectify.setUpdateBy(loginUser.getUsername());
        rectify.setUpdateTime(new Date());

        this.updateById(rectify);

        log.info("提交整改，ID: {}, 轮次: {}", rectifyId, rectify.getRectifyNo());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveRectify(String rectifyId, String comment) {
        AdverseEventRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            log.error("整改记录不存在，ID: {}", rectifyId);
            return false;
        }

        // 校验状态（仅已提交状态可审核）
        if (!RECTIFY_STATUS_SUBMITTED.equals(rectify.getStatus())) {
            log.error("整改状态不允许审核，当前状态: {}", rectify.getStatus());
            return false;
        }

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 更新整改记录
        rectify.setStatus(RECTIFY_STATUS_APPROVED);
        rectify.setReviewerId(loginUser.getId());
        rectify.setReviewerName(loginUser.getRealname());
        rectify.setReviewComment(comment);
        rectify.setReviewResult(REVIEW_RESULT_APPROVED);
        rectify.setReviewTime(new Date());
        rectify.setUpdateBy(loginUser.getUsername());
        rectify.setUpdateTime(new Date());

        this.updateById(rectify);

        log.info("整改审核通过，ID: {}", rectifyId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectRectify(String rectifyId, String comment) {
        AdverseEventRectify rectify = this.getById(rectifyId);
        if (rectify == null) {
            log.error("整改记录不存在，ID: {}", rectifyId);
            return false;
        }

        // 校验状态（仅已提交状态可审核）
        if (!RECTIFY_STATUS_SUBMITTED.equals(rectify.getStatus())) {
            log.error("整改状态不允许审核，当前状态: {}", rectify.getStatus());
            return false;
        }

        // 获取当前登录用户
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // 更新整改记录
        rectify.setStatus(RECTIFY_STATUS_REJECTED);
        rectify.setReviewerId(loginUser.getId());
        rectify.setReviewerName(loginUser.getRealname());
        rectify.setReviewComment(comment);
        rectify.setReviewResult(REVIEW_RESULT_REJECTED);
        rectify.setReviewTime(new Date());
        rectify.setUpdateBy(loginUser.getUsername());
        rectify.setUpdateTime(new Date());

        this.updateById(rectify);

        log.info("整改审核退回，ID: {}, 原因: {}", rectifyId, comment);
        return true;
    }

    @Override
    public List<AdverseEventRectify> getRectifyHistory(String eventId) {
        return baseMapper.listByEventId(eventId);
    }

    @Override
    public AdverseEventRectify getCurrentRectify(String eventId) {
        return baseMapper.getCurrentRectify(eventId);
    }
}
