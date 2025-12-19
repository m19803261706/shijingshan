<template>
  <div class="clinic-rectify-container">
    <a-tabs v-model:activeKey="activeTab" type="card" @change="handleTabChange">
      <!-- 药品不良反应整改 -->
      <a-tab-pane key="drug" tab="药品不良反应">
        <DrugRectifyList ref="drugListRef" />
      </a-tab-pane>

      <!-- 医疗器械不良事件整改 -->
      <a-tab-pane key="device" tab="医疗器械">
        <DeviceRectifyList ref="deviceListRef" />
      </a-tab-pane>

      <!-- 输血使用不良事件整改 -->
      <a-tab-pane key="blood" tab="输血不良事件">
        <BloodRectifyList ref="bloodListRef" />
      </a-tab-pane>

      <!-- 护理部整改（预留） -->
      <a-tab-pane key="nursing" tab="护理部" disabled>
        <a-empty description="功能开发中..." />
      </a-tab-pane>

      <!-- 医务科整改（预留） -->
      <a-tab-pane key="medical" tab="医务科" disabled>
        <a-empty description="功能开发中..." />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts" setup>
/**
 * 临床科室整改提交主页面
 * @description Tab 容器，分类展示不同科室类型的整改任务
 * @author TC Agent
 * @since 2025-12-19
 * @updated 2025-12-20 添加输血使用不良事件整改 Tab
 * @see https://github.com/m19803261706/shijingshan/issues/55
 * @see Issue #71
 */
import { ref } from 'vue';
import DrugRectifyList from './components/DrugRectifyList.vue';
import DeviceRectifyList from './components/DeviceRectifyList.vue';
import BloodRectifyList from './blood/index.vue';

// 当前激活的 Tab
const activeTab = ref('drug');

// 列表组件引用
const drugListRef = ref();
const deviceListRef = ref();
const bloodListRef = ref();

/**
 * Tab 切换处理
 */
function handleTabChange(key: string) {
  // 切换时可刷新对应列表
  if (key === 'drug' && drugListRef.value) {
    drugListRef.value.reload?.();
  } else if (key === 'device' && deviceListRef.value) {
    deviceListRef.value.reload?.();
  } else if (key === 'blood' && bloodListRef.value) {
    bloodListRef.value.reload?.();
  }
}
</script>

<style lang="less" scoped>
.clinic-rectify-container {
  padding: 16px;
  background: #fff;
  min-height: calc(100vh - 120px);

  :deep(.ant-tabs-nav) {
    margin-bottom: 16px;
  }
}
</style>
