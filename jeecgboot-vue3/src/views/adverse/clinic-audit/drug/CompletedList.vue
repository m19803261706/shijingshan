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
              label: '流转历史',
              onClick: handleFlowHistory.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>

    <!-- 报告详情弹窗 -->
    <DrugReportDetailModal @register="registerDetailModal" />

    <!-- 流转历史弹窗 -->
    <FlowHistoryModal @register="registerFlowModal" />
  </div>
</template>

<script lang="ts" name="clinic-audit-drug-completed" setup>
/**
 * 药品不良反应已审核列表页面
 * @description 临床科室已审核药品不良反应报告列表，显示审核结果和流转历史
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/46
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { completedColumns, completedSearchFormSchema } from './drugAudit.data';
import { getDrugCompletedAuditList } from '/@/api/adverse/drugAudit';
import DrugReportDetailModal from './components/DrugReportDetailModal.vue';
import FlowHistoryModal from './components/FlowHistoryModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerFlowModal, { openModal: openFlowModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'clinic-audit-drug-completed',
  tableProps: {
    title: '已审核报告列表',
    api: getDrugCompletedAuditList,
    columns: completedColumns,
    size: 'small',
    formConfig: {
      schemas: completedSearchFormSchema,
    },
    actionColumn: {
      width: 180,
    },
    beforeFetch: (params) => {
      return Object.assign({ column: 'auditTime', order: 'desc' }, params);
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
