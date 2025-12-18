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

    <!-- 事件详情弹窗 -->
    <EventDetailModal @register="registerDetailModal" />

    <!-- 审核弹窗 -->
    <AuditModal @register="registerAuditModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="adverse-audit-pending" setup>
/**
 * 待审核列表页面
 * @description 科室待审核事件列表，支持审核通过和退回
 * @author TC Agent
 * @since 2025-12-18
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { columns, searchFormSchema } from '../event/event.data';
import { getPendingAuditList } from '/@/api/adverse/audit';
import EventDetailModal from '../components/EventDetailModal.vue';
import AuditModal from '../components/AuditModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerAuditModal, { openModal: openAuditModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'adverse-audit-pending',
  tableProps: {
    title: '待审核列表',
    api: getPendingAuditList,
    columns: columns,
    size: 'small',
    formConfig: {
      schemas: searchFormSchema.filter((s) => s.field !== 'status'),
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
</script>

<style scoped>
.p-4 {
  padding: 16px;
}
</style>
