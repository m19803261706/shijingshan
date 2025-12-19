<template>
  <div class="drug-audit-container">
    <a-tabs v-model:activeKey="activeTab" type="card">
      <a-tab-pane key="pending" tab="待审核">
        <PendingList ref="pendingListRef" />
      </a-tab-pane>
      <a-tab-pane key="completed" tab="已审核">
        <CompletedList ref="completedListRef" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts" setup>
/**
 * 药品不良反应临床科室审核主页面
 * @description 使用 Tab 切换待审核和已审核列表
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/46
 */
import { ref } from 'vue';
import PendingList from './PendingList.vue';
import CompletedList from './CompletedList.vue';

// 当前激活的 Tab
const activeTab = ref('pending');

// 子组件引用
const pendingListRef = ref();
const completedListRef = ref();
</script>

<style lang="less" scoped>
.drug-audit-container {
  padding: 16px;
}
</style>
