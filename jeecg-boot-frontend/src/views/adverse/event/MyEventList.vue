<template>
  <div class="p-4">
    <!-- 表格 -->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!-- 表格标题区域 -->
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">
          新增上报
        </a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined" />
                批量删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button>
            批量操作
            <Icon icon="mdi:chevron-down" />
          </a-button>
        </a-dropdown>
      </template>

      <!-- 操作列 -->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
      </template>
    </BasicTable>

    <!-- 事件表单弹窗 -->
    <EventFormModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="adverse-event-myList" setup>
/**
 * 我的上报列表页面
 * @description 展示当前用户上报的所有事件，支持新增、编辑、删除、查看
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref } from 'vue';
import { BasicTable, TableAction, ActionItem } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { useListPage } from '/@/hooks/system/useListPage';
import { columns, searchFormSchema } from './event.data';
import { getMyEventList, deleteEvent, batchDeleteEvent } from '/@/api/adverse/event';
import EventFormModal from './components/EventFormModal.vue';
import { Icon } from '/@/components/Icon';

const { createMessage, createConfirm } = useMessage();

// 注册弹窗
const [registerModal, { openModal }] = useModal();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'adverse-event-list',
  tableProps: {
    title: '我的上报列表',
    api: getMyEventList,
    columns: columns,
    size: 'small',
    formConfig: {
      schemas: searchFormSchema,
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
const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

/**
 * 新增事件
 */
function handleCreate() {
  openModal(true, {
    isUpdate: false,
    isDetail: false,
  });
}

/**
 * 编辑事件
 */
function handleEdit(record: Recordable) {
  openModal(true, {
    record,
    isUpdate: true,
    isDetail: false,
  });
}

/**
 * 查看详情
 */
function handleDetail(record: Recordable) {
  openModal(true, {
    record,
    isUpdate: true,
    isDetail: true,
  });
}

/**
 * 删除事件
 */
async function handleDelete(record: Recordable) {
  await deleteEvent({ id: record.id });
  createMessage.success('删除成功');
  reload();
}

/**
 * 批量删除
 */
async function batchHandleDelete() {
  // 检查是否有非草稿状态的事件
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '确定要删除选中的事件吗？仅草稿状态的事件可以删除。',
    onOk: async () => {
      await batchDeleteEvent({ ids: selectedRowKeys.value.join(',') });
      createMessage.success('删除成功');
      selectedRowKeys.value = [];
      reload();
    },
  });
}

/**
 * 重新提交（退回后）
 */
function handleResubmit(record: Recordable) {
  openModal(true, {
    record,
    isUpdate: true,
    isDetail: false,
  });
}

/**
 * 操作成功回调
 */
function handleSuccess() {
  reload();
}

/**
 * 操作栏配置
 */
function getTableAction(record: Recordable): ActionItem[] {
  // 草稿状态可编辑
  if (record.status === 'draft') {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
    ];
  }
  // 退回状态可重新提交
  if (record.status === 'returned') {
    return [
      {
        label: '重新提交',
        onClick: handleResubmit.bind(null, record),
      },
    ];
  }
  // 其他状态只能查看
  return [
    {
      label: '查看',
      onClick: handleDetail.bind(null, record),
    },
  ];
}

/**
 * 下拉操作栏配置
 */
function getDropDownAction(record: Recordable): ActionItem[] {
  const actions: ActionItem[] = [
    {
      label: '详情',
      onClick: handleDetail.bind(null, record),
    },
  ];

  // 草稿状态可删除
  if (record.status === 'draft') {
    actions.push({
      label: '删除',
      popConfirm: {
        title: '确定要删除此事件吗？',
        confirm: handleDelete.bind(null, record),
      },
    });
  }

  return actions;
}
</script>

<style scoped>
.p-4 {
  padding: 16px;
}
</style>
