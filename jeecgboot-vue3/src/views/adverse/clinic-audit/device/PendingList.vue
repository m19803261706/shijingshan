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
    <DeviceReportDetailModal @register="registerDetailModal" />

    <!-- 审核弹窗 -->
    <DeviceAuditModal @register="registerAuditModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="clinic-audit-device-pending" setup>
/**
 * 医疗器械不良事件待审核列表页面
 * @description 临床科室待审核医疗器械不良事件报告列表，支持审核通过和退回
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #69
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { pendingColumns, pendingSearchFormSchema } from './deviceAudit.data';
import { getDevicePendingAuditList } from '/@/api/adverse/deviceAudit';
import DeviceReportDetailModal from './components/DeviceReportDetailModal.vue';
import DeviceAuditModal from './components/DeviceAuditModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerAuditModal, { openModal: openAuditModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'clinic-audit-device-pending',
  tableProps: {
    title: '待审核报告列表',
    api: getDevicePendingAuditList,
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
