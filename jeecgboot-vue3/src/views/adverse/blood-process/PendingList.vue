<template>
  <div class="p-4">
    <!-- 表格 -->
    <BasicTable @register="registerTable">
      <!-- 操作列 -->
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              label: '查看',
              onClick: handleDetail.bind(null, record),
            },
            {
              label: '处理',
              color: 'success',
              onClick: handleProcess.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>

    <!-- 报告详情弹窗 -->
    <BloodReportDetailModal @register="registerDetailModal" />

    <!-- 处理弹窗 -->
    <BloodProcessModal @register="registerProcessModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="blood-process-pending" setup>
/**
 * 输血使用不良事件待处理列表页面
 * @description 输血科待处理输血使用不良事件报告列表
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/86
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { pendingColumns, pendingSearchFormSchema } from './bloodProcess.data';
import { getBloodPendingProcessList } from '/@/api/adverse/bloodProcess';
import BloodReportDetailModal from '../clinic-audit/blood/components/BloodReportDetailModal.vue';
import BloodProcessModal from './components/BloodProcessModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerProcessModal, { openModal: openProcessModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'blood-process-pending',
  tableProps: {
    title: '待处理报告列表',
    api: getBloodPendingProcessList,
    columns: pendingColumns,
    size: 'small',
    formConfig: {
      schemas: pendingSearchFormSchema,
    },
    actionColumn: {
      width: 150,
    },
    beforeFetch: (params) => {
      return Object.assign({ column: 'createTime', order: 'desc' }, params);
    },
  },
});

// 注册表格
const [registerTable, { reload }] = tableContext;

/**
 * 查看详情
 */
function handleDetail(record: Recordable) {
  openDetailModal(true, { record });
}

/**
 * 处理操作
 */
function handleProcess(record: Recordable) {
  openProcessModal(true, { record });
}

/**
 * 操作成功回调
 */
function handleSuccess() {
  reload();
}

// 暴露 reload 方法供父组件调用
defineExpose({ reload });
</script>

<style scoped>
.p-4 {
  padding: 16px;
}
</style>
