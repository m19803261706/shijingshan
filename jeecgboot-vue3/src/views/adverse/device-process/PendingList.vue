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
    <DeviceReportDetailModal @register="registerDetailModal" />

    <!-- 处理弹窗 -->
    <DeviceProcessModal @register="registerProcessModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="device-process-pending" setup>
/**
 * 医疗器械不良事件待处理列表页面
 * @description 器械科待处理医疗器械不良事件报告列表
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #70
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { pendingColumns, pendingSearchFormSchema } from './deviceProcess.data';
import { getDevicePendingProcessList } from '/@/api/adverse/deviceProcess';
import DeviceReportDetailModal from '../clinic-audit/device/components/DeviceReportDetailModal.vue';
import DeviceProcessModal from './components/DeviceProcessModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerProcessModal, { openModal: openProcessModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'device-process-pending',
  tableProps: {
    title: '待处理报告列表',
    api: getDevicePendingProcessList,
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
