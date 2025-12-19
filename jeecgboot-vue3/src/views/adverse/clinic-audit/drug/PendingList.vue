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
              label: '审核',
              color: 'success',
              onClick: handleAudit.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>

    <!-- 报告详情弹窗 -->
    <DrugReportDetailModal @register="registerDetailModal" />

    <!-- 审核弹窗 -->
    <DrugAuditModal @register="registerAuditModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="clinic-audit-drug-pending" setup>
/**
 * 药品不良反应待审核列表页面
 * @description 临床科室待审核药品不良反应报告列表，支持审核通过和退回
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/46
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { pendingColumns, pendingSearchFormSchema } from './drugAudit.data';
import { getDrugPendingAuditList } from '/@/api/adverse/drugAudit';
import DrugReportDetailModal from './components/DrugReportDetailModal.vue';
import DrugAuditModal from './components/DrugAuditModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerAuditModal, { openModal: openAuditModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'clinic-audit-drug-pending',
  tableProps: {
    title: '待审核报告列表',
    api: getDrugPendingAuditList,
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
 * 审核操作
 */
function handleAudit(record: Recordable) {
  openAuditModal(true, { record });
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
