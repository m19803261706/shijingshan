<template>
  <div class="blood-rectify-submitted">
    <!-- 表格 -->
    <BasicTable @register="registerTable">
      <!-- 整改状态列 -->
      <template #rectifyStatus="{ record }">
        <a-tag :color="getRectifyStatusColor(record.rectifyStatus)">
          {{ getRectifyStatusText(record.rectifyStatus) }}
        </a-tag>
      </template>

      <!-- 确认结果列 -->
      <template #confirmResult="{ record }">
        <template v-if="record.confirmResult">
          <a-tag :color="record.confirmResult === 'approved' ? 'success' : 'error'">
            {{ record.confirmResult === 'approved' ? '已通过' : '已退回' }}
          </a-tag>
        </template>
        <span v-else>-</span>
      </template>

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
              onClick: handleHistory.bind(null, record),
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
    <BloodReportDetailModal @register="registerDetailModal" />

    <!-- 整改历史弹窗 -->
    <RectifyHistoryModal @register="registerHistoryModal" />

    <!-- 流转历史弹窗 -->
    <FlowHistoryModal @register="registerFlowModal" />
  </div>
</template>

<script lang="ts" name="blood-rectify-submitted" setup>
/**
 * 输血使用不良事件已提交整改列表
 * @description 临床科室人员查看已提交整改的报告和确认结果
 * @author TC Agent
 * @since 2025-12-20
 * @see https://github.com/m19803261706/shijingshan/issues/87
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { submittedColumns, searchFormSchema, rectifyStatusOptions } from './bloodRectify.data';
import { getBloodSubmittedRectifyList } from '/@/api/adverse/bloodRectify';
import BloodReportDetailModal from '../../clinic-audit/blood/components/BloodReportDetailModal.vue';
import RectifyHistoryModal from '../../blood-process/components/RectifyHistoryModal.vue';
import FlowHistoryModal from '../../clinic-audit/blood/components/FlowHistoryModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerHistoryModal, { openModal: openHistoryModal }] = useModal();
const [registerFlowModal, { openModal: openFlowModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'blood-rectify-submitted',
  tableProps: {
    title: '输血使用不良事件已提交整改列表',
    api: getBloodSubmittedRectifyList,
    columns: submittedColumns,
    size: 'small',
    formConfig: {
      schemas: searchFormSchema,
    },
    actionColumn: {
      width: 200,
    },
    beforeFetch: (params) => {
      return Object.assign({ column: 'submitTime', order: 'desc' }, params);
    },
  },
});

// 注册表格
const [registerTable, { reload }] = tableContext;

/**
 * 获取整改状态文本
 */
function getRectifyStatusText(status: string): string {
  const item = rectifyStatusOptions.find((i) => i.value === status);
  return item?.label || status || '-';
}

/**
 * 获取整改状态颜色
 */
function getRectifyStatusColor(status: string): string {
  const item = rectifyStatusOptions.find((i) => i.value === status);
  return item?.color || 'default';
}

/**
 * 查看详情
 */
function handleDetail(record: Recordable) {
  openDetailModal(true, { record });
}

/**
 * 查看整改历史
 */
function handleHistory(record: Recordable) {
  openHistoryModal(true, { reportId: record.id });
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
.blood-rectify-submitted {
  padding: 0;
}
</style>
