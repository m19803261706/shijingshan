<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'事件详情'"
    :width="900"
    :footer="null"
  >
    <div v-if="eventData">
      <!-- 事件基本信息 -->
      <a-descriptions title="基本信息" bordered :column="2" size="small" class="mb-4">
        <a-descriptions-item label="事件编号">{{ eventData.eventCode }}</a-descriptions-item>
        <a-descriptions-item label="事件标题">{{ eventData.title }}</a-descriptions-item>
        <a-descriptions-item label="事件分类">{{ eventData.categoryId_dictText }}</a-descriptions-item>
        <a-descriptions-item label="事件级别">
          <a-tag :color="getLevelColor(eventData.level)">{{ getLevelText(eventData.level) }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="当前状态">
          <a-tag :color="getStatusColor(eventData.status)">{{ getStatusText(eventData.status) }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="发生时间">{{ eventData.occurTime }}</a-descriptions-item>
        <a-descriptions-item label="发生地点" :span="2">{{ eventData.occurLocation }}</a-descriptions-item>
      </a-descriptions>

      <!-- 患者信息 -->
      <a-descriptions title="患者信息" bordered :column="3" size="small" class="mb-4">
        <a-descriptions-item label="患者姓名">{{ eventData.patientName || '-' }}</a-descriptions-item>
        <a-descriptions-item label="性别">{{ eventData.patientGender_dictText || '-' }}</a-descriptions-item>
        <a-descriptions-item label="年龄">{{ eventData.patientAge || '-' }}</a-descriptions-item>
        <a-descriptions-item label="住院号">{{ eventData.patientAdmissionNo || '-' }}</a-descriptions-item>
        <a-descriptions-item label="床号">{{ eventData.patientBedNo || '-' }}</a-descriptions-item>
      </a-descriptions>

      <!-- 事件描述 -->
      <a-descriptions title="事件描述" bordered :column="1" size="small" class="mb-4">
        <a-descriptions-item label="事件经过">
          <div style="white-space: pre-wrap;">{{ eventData.description }}</div>
        </a-descriptions-item>
        <a-descriptions-item label="原因分析">
          <div style="white-space: pre-wrap;">{{ eventData.causeAnalysis || '-' }}</div>
        </a-descriptions-item>
      </a-descriptions>

      <!-- 流转记录时间线 -->
      <a-card title="流转记录" size="small" class="mb-4">
        <a-timeline v-if="flowRecords.length > 0">
          <a-timeline-item
            v-for="record in flowRecords"
            :key="record.id"
            :color="getActionColor(record.action)"
          >
            <p class="flow-title">
              <strong>{{ getActionText(record.action) }}</strong>
              <span class="flow-time">{{ record.createTime }}</span>
            </p>
            <p class="flow-operator">操作人：{{ record.operatorName }}（{{ record.operatorDeptName }}）</p>
            <p v-if="record.comment" class="flow-comment">意见：{{ record.comment }}</p>
          </a-timeline-item>
        </a-timeline>
        <a-empty v-else description="暂无流转记录" />
      </a-card>
    </div>
    <a-spin v-else />
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 事件详情弹窗
 * @description 显示完整事件信息和流转时间线
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { queryEventById } from '/@/api/adverse/event';
import { getFlowRecords } from '/@/api/adverse/process';
import { eventStatusOptions, eventLevelOptions } from '../event/event.data';

// 事件数据
const eventData = ref<Recordable | null>(null);
// 流转记录
const flowRecords = ref<any[]>([]);

// 注册弹窗
const [registerModal] = useModalInner(async (data) => {
  eventData.value = null;
  flowRecords.value = [];

  if (data?.record) {
    // 获取事件详情
    const res = await queryEventById({ id: data.record.id });
    eventData.value = res;

    // 获取流转记录
    try {
      const flowRes = await getFlowRecords({ eventId: data.record.id });
      flowRecords.value = flowRes || [];
    } catch (e) {
      flowRecords.value = [];
    }
  }
});

/**
 * 获取状态文本
 */
function getStatusText(status: string) {
  return eventStatusOptions.find((i) => i.value === status)?.label || status;
}

/**
 * 获取状态颜色
 */
function getStatusColor(status: string) {
  return eventStatusOptions.find((i) => i.value === status)?.color || 'default';
}

/**
 * 获取级别文本
 */
function getLevelText(level: string) {
  return eventLevelOptions.find((i) => i.value === level)?.label || level;
}

/**
 * 获取级别颜色
 */
function getLevelColor(level: string) {
  return eventLevelOptions.find((i) => i.value === level)?.color || 'default';
}

/**
 * 操作类型映射
 */
const actionMap = {
  submit: { text: '提交上报', color: 'blue' },
  approve: { text: '审核通过', color: 'green' },
  reject: { text: '审核退回', color: 'red' },
  resubmit: { text: '重新提交', color: 'blue' },
  require_rectify: { text: '要求整改', color: 'orange' },
  submit_rectify: { text: '提交整改', color: 'blue' },
  confirm_rectify: { text: '确认整改', color: 'green' },
  reject_rectify: { text: '退回整改', color: 'red' },
  close: { text: '结案', color: 'green' },
};

/**
 * 获取操作文本
 */
function getActionText(action: string) {
  return actionMap[action]?.text || action;
}

/**
 * 获取操作颜色
 */
function getActionColor(action: string) {
  return actionMap[action]?.color || 'gray';
}
</script>

<style scoped>
.mb-4 {
  margin-bottom: 16px;
}

.flow-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.flow-time {
  color: #999;
  font-size: 12px;
}

.flow-operator {
  color: #666;
  font-size: 13px;
  margin-bottom: 4px;
}

.flow-comment {
  color: #333;
  background: #f5f5f5;
  padding: 8px;
  border-radius: 4px;
}
</style>
