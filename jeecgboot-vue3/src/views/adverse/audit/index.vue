<template>
  <div class="audit-container">
    <a-tabs v-model:activeKey="activeTab" type="card">
      <a-tab-pane key="pending" tab="待审核">
        <PendingList />
      </a-tab-pane>
      <a-tab-pane key="completed" tab="已审核">
        <CompletedList />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts" setup>
/**
 * 报告审核页面入口
 * @author TC Agent
 * @since 2025-12-19
 */
import { ref } from 'vue';
import PendingList from './PendingList.vue';
import CompletedList from './CompletedList.vue';

const activeTab = ref('pending');
</script>

<style lang="less" scoped>
.audit-container {
  padding: 16px;
}
</style>
