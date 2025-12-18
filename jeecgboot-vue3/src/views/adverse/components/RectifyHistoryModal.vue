<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'整改历史'"
    :width="700"
    :footer="null"
  >
    <a-spin :spinning="loading">
      <a-timeline v-if="historyList.length > 0" mode="left">
        <a-timeline-item
          v-for="(item, index) in historyList"
          :key="item.id"
          :color="getTimelineColor(item.status)"
        >
          <template #dot>
            <span class="timeline-dot">{{ historyList.length - index }}</span>
          </template>

          <a-card size="small" class="history-card">
            <template #title>
              <div class="history-header">
                <a-tag :color="getStatusColor(item.status)">{{ getStatusText(item.status) }}</a-tag>
                <span class="history-time">{{ item.submitTime }}</span>
              </div>
            </template>

            <a-descriptions :column="1" size="small">
              <a-descriptions-item label="整改措施">
                <div style="white-space: pre-wrap;">{{ item.measures }}</div>
              </a-descriptions-item>
              <a-descriptions-item v-if="item.result" label="整改结果">
                <div style="white-space: pre-wrap;">{{ item.result }}</div>
              </a-descriptions-item>
              <a-descriptions-item v-if="item.attachments" label="附件">
                <a-button type="link" size="small">查看附件</a-button>
              </a-descriptions-item>
            </a-descriptions>

            <!-- 审核信息 -->
            <div v-if="item.reviewComment" class="review-section">
              <a-divider style="margin: 12px 0;" />
              <div class="review-info">
                <span class="review-label">审核意见：</span>
                <span>{{ item.reviewComment }}</span>
              </div>
              <div class="review-info">
                <span class="review-label">审核人：</span>
                <span>{{ item.reviewerName }} ({{ item.reviewTime }})</span>
              </div>
            </div>
          </a-card>
        </a-timeline-item>
      </a-timeline>

      <a-empty v-else description="暂无整改记录" />
    </a-spin>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 整改历史弹窗
 * @description 时间线展示整改历史
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { getRectifyHistory } from '/@/api/adverse/rectify';

// 加载状态
const loading = ref(false);
// 历史列表
const historyList = ref<any[]>([]);

// 注册弹窗
const [registerModal] = useModalInner(async (data) => {
  historyList.value = [];

  if (data?.eventId) {
    loading.value = true;
    try {
      const res = await getRectifyHistory({ eventId: data.eventId });
      historyList.value = res || [];
    } catch (e) {
      console.error('获取整改历史失败', e);
    } finally {
      loading.value = false;
    }
  }
});

/**
 * 整改状态映射
 */
const statusMap = {
  draft: { text: '草稿', color: 'default' },
  submitted: { text: '已提交', color: 'processing' },
  confirmed: { text: '已确认', color: 'success' },
  rejected: { text: '已退回', color: 'error' },
};

/**
 * 获取状态文本
 */
function getStatusText(status: string) {
  return statusMap[status]?.text || status;
}

/**
 * 获取状态颜色
 */
function getStatusColor(status: string) {
  return statusMap[status]?.color || 'default';
}

/**
 * 获取时间线颜色
 */
function getTimelineColor(status: string) {
  const colorMap = {
    draft: 'gray',
    submitted: 'blue',
    confirmed: 'green',
    rejected: 'red',
  };
  return colorMap[status] || 'gray';
}
</script>

<style scoped>
.history-card {
  margin-bottom: 8px;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.history-time {
  color: #999;
  font-size: 12px;
}

.timeline-dot {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background: #1890ff;
  color: #fff;
  border-radius: 50%;
  font-size: 12px;
}

.review-section {
  background: #fafafa;
  padding: 8px;
  border-radius: 4px;
  margin-top: 8px;
}

.review-info {
  font-size: 13px;
  margin-bottom: 4px;
}

.review-label {
  color: #666;
}
</style>
