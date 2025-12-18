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

    <!-- 事件详情弹窗 -->
    <EventDetailModal @register="registerDetailModal" />

    <!-- 处理弹窗 -->
    <ProcessModal @register="registerProcessModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="adverse-process-pending" setup>
/**
 * 待处理列表页面
 * @description 职能科室待处理事件列表，支持要求整改或直接结案
 * @author TC Agent
 * @since 2025-12-18
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { columns, searchFormSchema } from '../event/event.data';
import { getPendingProcessList } from '/@/api/adverse/process';
import EventDetailModal from '../components/EventDetailModal.vue';
import ProcessModal from '../components/ProcessModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerProcessModal, { openModal: openProcessModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'adverse-process-pending',
  tableProps: {
    title: '待处理列表',
    api: getPendingProcessList,
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
 * 处理操作
 */
function handleProcess(record: Recordable) {
  openProcessModal(true, { record, mode: 'process' });
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
