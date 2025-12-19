<template>
  <div class="blood-rectify-container">
    <a-tabs v-model:activeKey="activeTab" type="card" @change="handleTabChange">
      <a-tab-pane key="pending" tab="待整改">
        <BloodRectifyPending ref="pendingListRef" />
      </a-tab-pane>
      <a-tab-pane key="submitted" tab="已提交">
        <BloodRectifySubmitted ref="submittedListRef" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts" setup>
/**
 * 输血使用不良事件临床整改主页面
 * @description 使用 Tab 切换待整改和已提交列表
 * @author TC Agent
 * @since 2025-12-20
 * @see https://github.com/m19803261706/shijingshan/issues/87
 */
import { ref } from 'vue';
import BloodRectifyPending from './BloodRectifyPending.vue';
import BloodRectifySubmitted from './BloodRectifySubmitted.vue';

// 当前激活的 Tab
const activeTab = ref('pending');

// 子组件引用
const pendingListRef = ref();
const submittedListRef = ref();

/**
 * Tab 切换处理
 */
function handleTabChange(key: string) {
  // 切换时刷新对应列表
  if (key === 'pending' && pendingListRef.value) {
    pendingListRef.value.reload?.();
  } else if (key === 'submitted' && submittedListRef.value) {
    submittedListRef.value.reload?.();
  }
}
</script>

<style lang="less" scoped>
.blood-rectify-container {
  padding: 16px;
}
</style>
