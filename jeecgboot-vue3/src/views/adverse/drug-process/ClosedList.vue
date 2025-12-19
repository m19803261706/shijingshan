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
    <DrugReportDetailModal @register="registerDetailModal" />

    <!-- 整改记录弹窗 -->
    <RectifyHistoryModal @register="registerRectifyModal" />

    <!-- 流转历史弹窗 -->
    <FlowHistoryModal @register="registerFlowModal" />
  </div>
</template>

<script lang="ts" name="drug-process-closed" setup>
/**
 * 药品不良反应已结案列表页面
 * @description 药剂科已结案的药品不良反应报告列表
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/51
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { closedColumns, closedSearchFormSchema } from './drugProcess.data';
import { getDrugClosedList } from '/@/api/adverse/drugProcess';
import DrugReportDetailModal from '../clinic-audit/drug/components/DrugReportDetailModal.vue';
import RectifyHistoryModal from './components/RectifyHistoryModal.vue';
import FlowHistoryModal from '../clinic-audit/drug/components/FlowHistoryModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerRectifyModal, { openModal: openRectifyModal }] = useModal();
const [registerFlowModal, { openModal: openFlowModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'drug-process-closed',
  tableProps: {
    title: '已结案报告列表',
    api: getDrugClosedList,
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
