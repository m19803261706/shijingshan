<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'整改记录'"
    :width="1000"
    :footer="null"
  >
    <a-spin :spinning="loading">
      <a-timeline v-if="rectifyList.length > 0" mode="left" class="rectify-timeline">
        <a-timeline-item
          v-for="(item, index) in rectifyList"
          :key="index"
          :color="getStatusColor(item.status)"
        >
          <template #dot>
            <component :is="getStatusIcon(item.status)" style="font-size: 16px" />
          </template>
          <a-card size="small" class="rectify-card">
            <a-row :gutter="16">
              <a-col :span="24">
                <a-descriptions :column="3" size="small" bordered>
                  <a-descriptions-item label="整改要求" :span="3">
                    {{ item.requirement }}
                  </a-descriptions-item>
                  <a-descriptions-item label="整改期限">{{ item.deadline }}</a-descriptions-item>
                  <a-descriptions-item label="下发人">{{ item.requireUserName }}</a-descriptions-item>
                  <a-descriptions-item label="下发时间">{{ item.requireTime }}</a-descriptions-item>
                  <a-descriptions-item label="整改措施" :span="3">
                    {{ item.measures || '-' }}
                  </a-descriptions-item>
                  <a-descriptions-item label="完成情况" :span="3">
                    {{ item.completion || '-' }}
                  </a-descriptions-item>
                  <a-descriptions-item label="提交人">{{ item.submitUserName || '-' }}</a-descriptions-item>
                  <a-descriptions-item label="提交时间">{{ item.submitTime || '-' }}</a-descriptions-item>
                  <a-descriptions-item label="状态">
                    <a-tag :color="getStatusColor(item.status)">
                      {{ getStatusLabel(item.status) }}
                    </a-tag>
                  </a-descriptions-item>
                  <a-descriptions-item label="确认意见" :span="3" v-if="item.confirmComment">
                    {{ item.confirmComment }}
                  </a-descriptions-item>
                </a-descriptions>
              </a-col>
            </a-row>
          </a-card>
        </a-timeline-item>
      </a-timeline>
      <a-empty v-else description="暂无整改记录" />
    </a-spin>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 整改记录弹窗
 * @description 查看报告的所有整改记录
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/51
 */
import { ref, h } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { getDrugRectifyHistory } from '/@/api/adverse/drugProcess';
import {
  ClockCircleOutlined,
  SendOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
} from '@ant-design/icons-vue';

// 加载状态
const loading = ref(false);
// 整改记录列表
const rectifyList = ref<Recordable[]>([]);

// 注册弹窗
const [registerModal] = useModalInner(async (data) => {
  rectifyList.value = [];

  if (data?.reportId) {
    loading.value = true;
    try {
      const result = await getDrugRectifyHistory(data.reportId);
      rectifyList.value = result || [];
    } finally {
      loading.value = false;
    }
  }
});

/**
 * 获取状态标签
 */
function getStatusLabel(status: string): string {
  const map: Record<string, string> = {
    pending: '待整改',
    submitted: '已提交',
    approved: '已通过',
    rejected: '已退回',
  };
  return map[status] || status;
}

/**
 * 获取状态颜色
 */
function getStatusColor(status: string): string {
  const map: Record<string, string> = {
    pending: 'orange',
    submitted: 'blue',
    approved: 'green',
    rejected: 'red',
  };
  return map[status] || 'default';
}

/**
 * 获取状态图标
 */
function getStatusIcon(status: string) {
  const map: Record<string, any> = {
    pending: ClockCircleOutlined,
    submitted: SendOutlined,
    approved: CheckCircleOutlined,
    rejected: CloseCircleOutlined,
  };
  return map[status] || ClockCircleOutlined;
}
</script>

<style scoped>
.rectify-timeline {
  padding: 16px;
}

.rectify-card {
  margin-bottom: 0;
}
</style>
