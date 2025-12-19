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
              label: '整改记录',
              onClick: handleRectifyHistory.bind(null, record),
              ifShow: record.closeType === 'rectify',
            },
            {
              label: '流转历史',
              onClick: handleFlowHistory.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>

    <!-- 报告详情弹窗 -->
    <DeviceReportDetailModal @register="registerDetailModal" />

    <!-- 整改记录弹窗 -->
    <RectifyHistoryModal @register="registerRectifyModal" />

    <!-- 流转历史弹窗 -->
    <FlowHistoryModal @register="registerFlowModal" />
  </div>
</template>

<script lang="ts" name="device-process-closed" setup>
/**
 * 医疗器械不良事件已结案列表页面
 * @description 器械科已结案的医疗器械不良事件报告列表
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #70
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { closedColumns, closedSearchFormSchema } from './deviceProcess.data';
import { getDeviceClosedList } from '/@/api/adverse/deviceProcess';
import DeviceReportDetailModal from '../clinic-audit/device/components/DeviceReportDetailModal.vue';
import RectifyHistoryModal from './components/RectifyHistoryModal.vue';
import FlowHistoryModal from '../clinic-audit/device/components/FlowHistoryModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerRectifyModal, { openModal: openRectifyModal }] = useModal();
const [registerFlowModal, { openModal: openFlowModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'device-process-closed',
  tableProps: {
    title: '已结案报告列表',
    api: getDeviceClosedList,
    columns: closedColumns,
    size: 'small',
    formConfig: {
      schemas: closedSearchFormSchema,
    },
    actionColumn: {
      width: 200,
    },
    beforeFetch: (params) => {
      return Object.assign({ column: 'closeTime', order: 'desc' }, params);
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
 * 查看整改记录
 */
function handleRectifyHistory(record: Recordable) {
  openRectifyModal(true, { reportId: record.id });
}

/**
 * 查看流转历史
 */
function handleFlowHistory(record: Recordable) {
  openFlowModal(true, { reportId: record.id });
}

// 暴露 reload 方法供父组件调用
defineExpose({ reload });
</script>

<style scoped>
.p-4 {
  padding: 16px;
}
</style>
