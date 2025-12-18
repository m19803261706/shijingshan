<template>
  <div class="process-container">
    <a-tabs v-model:activeKey="activeTab" type="card">
      <a-tab-pane key="pending" tab="待处理">
        <PendingList />
      </a-tab-pane>
      <a-tab-pane key="confirm" tab="待确认">
        <ConfirmList />
      </a-tab-pane>
      <a-tab-pane key="closed" tab="已结案">
        <ClosedList />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts" setup>
/**
 * 事件处理页面入口
 * @author TC Agent
 * @since 2025-12-19
 */
import { ref } from 'vue';
import PendingList from './PendingList.vue';
import ConfirmList from './ConfirmList.vue';
import ClosedList from './ClosedList.vue';

const activeTab = ref('pending');
</script>

<style lang="less" scoped>
.process-container {
  padding: 16px;
}
</style>
