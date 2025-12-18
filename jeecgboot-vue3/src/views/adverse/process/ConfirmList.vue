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
              label: '确认',
              color: 'success',
              onClick: handleConfirm.bind(null, record),
            },
          ]"
        />
      </template>
    </BasicTable>

    <!-- 事件详情弹窗 -->
    <EventDetailModal @register="registerDetailModal" />

    <!-- 确认弹窗 -->
    <ProcessModal @register="registerConfirmModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="adverse-process-confirm" setup>
/**
 * 待确认列表页面
 * @description 整改提交后待确认的事件列表，支持确认整改或退回整改
 * @author TC Agent
 * @since 2025-12-18
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { columns, searchFormSchema } from '../event/event.data';
import { getConfirmList } from '/@/api/adverse/process';
import EventDetailModal from '../components/EventDetailModal.vue';
import ProcessModal from '../components/ProcessModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerConfirmModal, { openModal: openConfirmModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'adverse-process-confirm',
  tableProps: {
    title: '待确认列表',
    api: getConfirmList,
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
 * 确认操作
 */
function handleConfirm(record: Recordable) {
  openConfirmModal(true, { record, mode: 'confirm' });
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
