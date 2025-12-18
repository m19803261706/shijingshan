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
          ]"
        />
      </template>
    </BasicTable>

    <!-- 事件详情弹窗 -->
    <EventDetailModal @register="registerDetailModal" />
  </div>
</template>

<script lang="ts" name="adverse-process-closed" setup>
/**
 * 已结案列表页面
 * @description 查看已结案的事件历史记录
 * @author TC Agent
 * @since 2025-12-18
 */
import { BasicTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { columns, searchFormSchema } from '../event/event.data';
import { getClosedList } from '/@/api/adverse/process';
import EventDetailModal from '../components/EventDetailModal.vue';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'adverse-process-closed',
  tableProps: {
    title: '已结案列表',
    api: getClosedList,
    columns: columns,
    size: 'small',
    formConfig: {
      schemas: searchFormSchema,
    },
    actionColumn: {
      width: 80,
    },
    beforeFetch: (params) => {
      return Object.assign({ column: 'createTime', order: 'desc' }, params);
    },
  },
});

// 注册表格
const [registerTable] = tableContext;

/**
 * 查看详情
 */
function handleDetail(record: Recordable) {
  openDetailModal(true, { record });
}
</script>

<style scoped>
.p-4 {
  padding: 16px;
}
</style>
