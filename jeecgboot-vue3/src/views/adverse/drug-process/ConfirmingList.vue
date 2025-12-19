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
            },
            {
              label: '确认',
              color: 'success',
              onClick: handleConfirm.bind(null, record),
              ifShow: record.status === 'rectifying',
            },
          ]"
        />
      </template>
    </BasicTable>

    <!-- 报告详情弹窗 -->
    <DrugReportDetailModal @register="registerDetailModal" />

    <!-- 整改记录弹窗 -->
    <RectifyHistoryModal @register="registerRectifyModal" />

    <!-- 确认整改弹窗 -->
    <ConfirmRectifyModal @register="registerConfirmModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="drug-process-confirming" setup>
/**
 * 药品不良反应待确认列表页面
 * @description 药剂科待确认整改的药品不良反应报告列表
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/51
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { confirmingColumns, pendingSearchFormSchema } from './drugProcess.data';
import { getDrugConfirmingList } from '/@/api/adverse/drugProcess';
import DrugReportDetailModal from '../clinic-audit/drug/components/DrugReportDetailModal.vue';
import RectifyHistoryModal from './components/RectifyHistoryModal.vue';
import ConfirmRectifyModal from './components/ConfirmRectifyModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerRectifyModal, { openModal: openRectifyModal }] = useModal();
const [registerConfirmModal, { openModal: openConfirmModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'drug-process-confirming',
  tableProps: {
    title: '待确认报告列表',
    api: getDrugConfirmingList,
    columns: confirmingColumns,
    size: 'small',
    formConfig: {
      schemas: pendingSearchFormSchema,
    },
    actionColumn: {
      width: 200,
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
 * 查看整改记录
 */
function handleRectifyHistory(record: Recordable) {
  openRectifyModal(true, { reportId: record.id });
}

/**
 * 确认整改
 */
function handleConfirm(record: Recordable) {
  openConfirmModal(true, { record });
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
