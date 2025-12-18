<template>
  <div class="p-4">
    <a-card title="事件详情" :loading="loading">
      <!-- 返回按钮 -->
      <template #extra>
        <a-button @click="goBack">返回</a-button>
      </template>

      <!-- 基本信息 -->
      <a-descriptions title="基本信息" bordered :column="2">
        <a-descriptions-item label="事件编号">{{ eventData.eventCode }}</a-descriptions-item>
        <a-descriptions-item label="事件标题">{{ eventData.title }}</a-descriptions-item>
        <a-descriptions-item label="事件分类">{{ eventData.categoryId_dictText }}</a-descriptions-item>
        <a-descriptions-item label="事件级别">
          <a-tag :color="getLevelColor(eventData.level)">{{ getLevelText(eventData.level) }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="事件状态">
          <a-tag :color="getStatusColor(eventData.status)">{{ getStatusText(eventData.status) }}</a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="发生时间">{{ eventData.occurTime }}</a-descriptions-item>
        <a-descriptions-item label="发生地点">{{ eventData.occurLocation || '-' }}</a-descriptions-item>
        <a-descriptions-item label="上报科室">{{ eventData.departmentId_dictText || '-' }}</a-descriptions-item>
        <a-descriptions-item label="上报人">{{ eventData.reporterName }}</a-descriptions-item>
        <a-descriptions-item label="上报时间">{{ eventData.createTime }}</a-descriptions-item>
      </a-descriptions>

      <!-- 患者信息 -->
      <a-descriptions title="患者信息" bordered :column="3" style="margin-top: 20px" v-if="hasPatientInfo">
        <a-descriptions-item label="姓名">{{ eventData.patientName || '-' }}</a-descriptions-item>
        <a-descriptions-item label="性别">{{ eventData.patientGender_dictText || '-' }}</a-descriptions-item>
        <a-descriptions-item label="年龄">{{ eventData.patientAge ? `${eventData.patientAge}岁` : '-' }}</a-descriptions-item>
        <a-descriptions-item label="住院号">{{ eventData.patientAdmissionNo || '-' }}</a-descriptions-item>
        <a-descriptions-item label="床号">{{ eventData.patientBedNo || '-' }}</a-descriptions-item>
      </a-descriptions>

      <!-- 事件描述 -->
      <a-descriptions title="事件描述" bordered :column="1" style="margin-top: 20px">
        <a-descriptions-item label="事件经过">
          <div style="white-space: pre-wrap">{{ eventData.description || '-' }}</div>
        </a-descriptions-item>
        <a-descriptions-item label="原因分析">
          <div style="white-space: pre-wrap">{{ eventData.causeAnalysis || '-' }}</div>
        </a-descriptions-item>
      </a-descriptions>

      <!-- 附件 -->
      <a-descriptions title="附件" bordered :column="1" style="margin-top: 20px" v-if="eventData.attachments">
        <a-descriptions-item label="附件列表">
          <j-upload v-model:value="eventData.attachments" :disabled="true" />
        </a-descriptions-item>
      </a-descriptions>

      <!-- 流转记录 -->
      <div style="margin-top: 20px" v-if="flowList.length > 0">
        <a-divider>流转记录</a-divider>
        <a-timeline>
          <a-timeline-item v-for="flow in flowList" :key="flow.id" :color="getFlowColor(flow.action)">
            <p><strong>{{ getActionText(flow.action) }}</strong> - {{ flow.operatorName }}</p>
            <p>{{ flow.createTime }}</p>
            <p v-if="flow.remark">备注：{{ flow.remark }}</p>
          </a-timeline-item>
        </a-timeline>
      </div>

      <!-- 整改记录 -->
      <div style="margin-top: 20px" v-if="rectifyList.length > 0">
        <a-divider>整改记录</a-divider>
        <a-collapse>
          <a-collapse-panel v-for="rectify in rectifyList" :key="rectify.id" :header="`第${rectify.rectifyNo}次整改`">
            <a-descriptions bordered :column="2">
              <a-descriptions-item label="整改措施" :span="2">{{ rectify.measures || '-' }}</a-descriptions-item>
              <a-descriptions-item label="整改结果" :span="2">{{ rectify.result || '-' }}</a-descriptions-item>
              <a-descriptions-item label="提交时间">{{ rectify.submitTime || '-' }}</a-descriptions-item>
              <a-descriptions-item label="状态">
                <a-tag :color="getRectifyStatusColor(rectify.status)">{{ getRectifyStatusText(rectify.status) }}</a-tag>
              </a-descriptions-item>
            </a-descriptions>
          </a-collapse-panel>
        </a-collapse>
      </div>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
/**
 * 事件详情页面
 * @description 展示事件完整信息，包括流转记录和整改记录
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { eventLevelOptions, eventStatusOptions } from './event.data';
import { defHttp } from '/@/utils/http/axios';

const route = useRoute();
const router = useRouter();

// 状态
const loading = ref(false);
const eventData = ref<any>({});
const flowList = ref<any[]>([]);
const rectifyList = ref<any[]>([]);

// 是否有患者信息
const hasPatientInfo = computed(() => {
  return eventData.value.patientName || eventData.value.patientAdmissionNo;
});

/**
 * 加载事件详情
 */
async function loadEventDetail() {
  const id = route.params.id as string;
  if (!id) {
    return;
  }

  loading.value = true;
  try {
    const result = await defHttp.get({ url: '/adverse/query/detail/' + id });
    if (result) {
      eventData.value = result.event || {};
      flowList.value = result.flowList || [];
      rectifyList.value = result.rectifyList || [];
    }
  } catch (e) {
    console.error('加载事件详情失败', e);
  } finally {
    loading.value = false;
  }
}

/**
 * 返回上一页
 */
function goBack() {
  router.back();
}

/**
 * 获取级别颜色
 */
function getLevelColor(level: string): string {
  const item = eventLevelOptions.find((i) => i.value === level);
  return item?.color || 'default';
}

/**
 * 获取级别文本
 */
function getLevelText(level: string): string {
  const item = eventLevelOptions.find((i) => i.value === level);
  return item?.label || level;
}

/**
 * 获取状态颜色
 */
function getStatusColor(status: string): string {
  const item = eventStatusOptions.find((i) => i.value === status);
  return item?.color || 'default';
}

/**
 * 获取状态文本
 */
function getStatusText(status: string): string {
  const item = eventStatusOptions.find((i) => i.value === status);
  return item?.label || status;
}

/**
 * 获取流转动作颜色
 */
function getFlowColor(action: string): string {
  const colorMap: Record<string, string> = {
    submit: 'blue',
    audit_pass: 'green',
    audit_reject: 'red',
    process: 'blue',
    require_rectify: 'orange',
    close: 'green',
    rectify_submit: 'blue',
    rectify_confirm: 'green',
    rectify_reject: 'red',
  };
  return colorMap[action] || 'gray';
}

/**
 * 获取动作文本
 */
function getActionText(action: string): string {
  const textMap: Record<string, string> = {
    submit: '提交上报',
    resubmit: '重新提交',
    audit_pass: '审核通过',
    audit_reject: '审核退回',
    process: '处理',
    require_rectify: '要求整改',
    close: '直接结案',
    rectify_submit: '提交整改',
    rectify_confirm: '确认整改',
    rectify_reject: '退回整改',
  };
  return textMap[action] || action;
}

/**
 * 获取整改状态颜色
 */
function getRectifyStatusColor(status: string): string {
  const colorMap: Record<string, string> = {
    draft: 'default',
    submitted: 'processing',
    confirmed: 'success',
    rejected: 'warning',
  };
  return colorMap[status] || 'default';
}

/**
 * 获取整改状态文本
 */
function getRectifyStatusText(status: string): string {
  const textMap: Record<string, string> = {
    draft: '草稿',
    submitted: '已提交',
    confirmed: '已确认',
    rejected: '已退回',
  };
  return textMap[status] || status;
}

// 页面加载时获取数据
onMounted(() => {
  loadEventDetail();
});
</script>

<style scoped>
.p-4 {
  padding: 16px;
}
</style>
