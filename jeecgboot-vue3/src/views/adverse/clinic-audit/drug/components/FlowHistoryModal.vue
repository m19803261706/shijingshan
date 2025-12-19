<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="流转历史"
    :width="800"
    :footer="null"
  >
    <a-spin :spinning="loading">
      <a-timeline v-if="flowList.length > 0">
        <a-timeline-item
          v-for="(item, index) in flowList"
          :key="index"
          :color="getActionColor(item.action)"
        >
          <template #dot>
            <component :is="getActionIcon(item.action)" style="font-size: 16px" />
          </template>
          <div class="flow-item">
            <div class="flow-header">
              <span class="flow-action">{{ getActionLabel(item.action) }}</span>
              <span class="flow-time">{{ item.createTime }}</span>
            </div>
            <div class="flow-body">
              <a-descriptions :column="2" size="small">
                <a-descriptions-item label="操作人">{{ item.operatorName }}</a-descriptions-item>
                <a-descriptions-item label="状态变更">
                  <a-tag :color="getStatusColor(item.fromStatus)">{{ getStatusLabel(item.fromStatus) }}</a-tag>
                  <span style="margin: 0 8px;">→</span>
                  <a-tag :color="getStatusColor(item.toStatus)">{{ getStatusLabel(item.toStatus) }}</a-tag>
                </a-descriptions-item>
                <a-descriptions-item v-if="item.comment" label="备注" :span="2">
                  {{ item.comment }}
                </a-descriptions-item>
              </a-descriptions>
            </div>
          </div>
        </a-timeline-item>
      </a-timeline>
      <a-empty v-else description="暂无流转记录" />
    </a-spin>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 流转历史弹窗
 * @description 展示报告的所有流转记录
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/46
 */
import { ref, h } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { getDrugFlowHistory } from '/@/api/adverse/drugAudit';
import {
  FormOutlined,
  SendOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  SyncOutlined,
  FileSearchOutlined,
} from '@ant-design/icons-vue';

// 加载状态
const loading = ref(false);
// 流转记录列表
const flowList = ref<Recordable[]>([]);

// 注册弹窗
const [registerModal] = useModalInner(async (data) => {
  if (data?.reportId) {
    loading.value = true;
    try {
      const result = await getDrugFlowHistory(data.reportId);
      flowList.value = result || [];
    } finally {
      loading.value = false;
    }
  }
});

/**
 * 获取操作标签
 */
function getActionLabel(action: string): string {
  const map: Record<string, string> = {
    create: '创建报告',
    submit: '提交审核',
    audit_pass: '审核通过',
    audit_reject: '审核退回',
    resubmit: '重新提交',
    process: '处理',
    close: '结案',
  };
  return map[action] || action;
}

/**
 * 获取操作颜色
 */
function getActionColor(action: string): string {
  const map: Record<string, string> = {
    create: 'blue',
    submit: 'blue',
    audit_pass: 'green',
    audit_reject: 'red',
    resubmit: 'blue',
    process: 'green',
    close: 'gray',
  };
  return map[action] || 'blue';
}

/**
 * 获取操作图标
 */
function getActionIcon(action: string) {
  const map: Record<string, any> = {
    create: FormOutlined,
    submit: SendOutlined,
    audit_pass: CheckCircleOutlined,
    audit_reject: CloseCircleOutlined,
    resubmit: SyncOutlined,
    process: FileSearchOutlined,
    close: CheckCircleOutlined,
  };
  return map[action] || FormOutlined;
}

/**
 * 获取状态标签
 */
function getStatusLabel(status: string): string {
  const map: Record<string, string> = {
    draft: '草稿',
    pending_audit: '待审核',
    returned: '已退回',
    pending_process: '待处理',
    closed: '已结案',
  };
  return map[status] || status || '-';
}

/**
 * 获取状态颜色
 */
function getStatusColor(status: string): string {
  const map: Record<string, string> = {
    draft: 'default',
    pending_audit: 'processing',
    returned: 'warning',
    pending_process: 'processing',
    closed: 'success',
  };
  return map[status] || 'default';
}
</script>

<style scoped>
.flow-item {
  padding: 8px 0;
}

.flow-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.flow-action {
  font-weight: 600;
  font-size: 14px;
}

.flow-time {
  color: #999;
  font-size: 12px;
}

.flow-body {
  background: #fafafa;
  padding: 12px;
  border-radius: 4px;
}
</style>
