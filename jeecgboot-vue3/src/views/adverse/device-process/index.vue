<template>
  <div class="device-process-container">
    <a-tabs v-model:activeKey="activeTab" type="card">
      <a-tab-pane key="pending" tab="待处理">
        <PendingList ref="pendingListRef" />
      </a-tab-pane>
      <a-tab-pane key="confirming" tab="待确认">
        <ConfirmingList ref="confirmingListRef" />
      </a-tab-pane>
      <a-tab-pane key="closed" tab="已结案">
        <ClosedList ref="closedListRef" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts" setup>
/**
 * 医疗器械不良事件器械科处理主页面
 * @description 使用 Tab 切换待处理、待确认和已结案列表
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #70
 */
import { ref } from 'vue';
import PendingList from './PendingList.vue';
import ConfirmingList from './ConfirmingList.vue';
import ClosedList from './ClosedList.vue';

// 当前激活的 Tab
const activeTab = ref('pending');

// 子组件引用
const pendingListRef = ref();
const confirmingListRef = ref();
const closedListRef = ref();
</script>

<style lang="less" scoped>
.device-process-container {
  padding: 16px;
}
</style>
