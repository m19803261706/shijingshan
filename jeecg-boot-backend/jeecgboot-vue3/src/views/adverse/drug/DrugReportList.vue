<template>
  <div class="p-4">
    <!-- 表格 -->
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!-- 表格标题区域 -->
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleCreate">
          新增报告
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
  </div>
</template>

<script lang="ts" name="adverse-drug-list" setup>
/**
 * 药品不良反应报告列表页面
 * @description 展示当前用户创建的药品不良反应报告，支持新增、编辑、删除、查看、提交
 * @author TC Agent
 * @since 2025-12-19
 */
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { BasicTable, TableAction, ActionItem } from '/@/components/Table';
import { useMessage } from '/@/hooks/web/useMessage';
import { useListPage } from '/@/hooks/system/useListPage';
import { columns, searchFormSchema } from './drug.data';
import { getMyDrugReportList, deleteDrugReport, batchDeleteDrugReport, submitDrugReport } from '/@/api/adverse/drug';
import { Icon } from '/@/components/Icon';

const router = useRouter();
const { createMessage, createConfirm } = useMessage();

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'adverse-drug-list',
  tableProps: {
    title: '我的药品不良反应报告',
    api: getMyDrugReportList,
    columns: columns,
    size: 'small',
    formConfig: {
      schemas: searchFormSchema,
    },
    actionColumn: {
      width: 180,
    },
    beforeFetch: (params) => {
      return Object.assign({ column: 'createTime', order: 'desc' }, params);
    },
  },
});

// 注册表格
const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext;

/**
 * 新增报告
 */
function handleCreate() {
  router.push('/adverse/drug/form');
}

/**
 * 编辑报告
 */
function handleEdit(record: Recordable) {
  router.push({
    path: '/adverse/drug/form',
    query: { id: record.id },
  });
}

/**
 * 查看详情
 */
function handleDetail(record: Recordable) {
  router.push({
    path: '/adverse/drug/form',
    query: { id: record.id, mode: 'view' },
  });
}

/**
 * 提交报告
 */
async function handleSubmit(record: Recordable) {
  createConfirm({
    iconType: 'info',
    title: '确认提交',
    content: '确定要提交此报告吗？提交后将进入审核流程。',
    onOk: async () => {
      await submitDrugReport(record.id);
      createMessage.success('提交成功');
      reload();
    },
  });
}

/**
 * 删除报告
 */
async function handleDelete(record: Recordable) {
  await deleteDrugReport(record.id);
  createMessage.success('删除成功');
  reload();
}

/**
 * 批量删除
 */
async function batchHandleDelete() {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '确定要删除选中的报告吗？仅草稿状态的报告可以删除。',
    onOk: async () => {
      await batchDeleteDrugReport(selectedRowKeys.value.join(','));
      createMessage.success('删除成功');
      selectedRowKeys.value = [];
      reload();
    },
  });
}

/**
 * 操作栏配置
 */
function getTableAction(record: Recordable): ActionItem[] {
  // 草稿状态可编辑和提交
  if (record.status === 'draft') {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
      },
      {
        label: '提交',
        color: 'success',
        onClick: handleSubmit.bind(null, record),
      },
    ];
  }
  // 退回状态可重新编辑
  if (record.status === 'returned') {
    return [
      {
        label: '修改',
        onClick: handleEdit.bind(null, record),
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
        title: '确定要删除此报告吗？',
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
