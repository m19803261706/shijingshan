<!--
 * 常用医疗器械配置 - 列表页
 * @description 提供常用医疗器械的列表展示、搜索、新增、编辑、删除、导入导出功能
 * @author TC Agent
 * @since 2025-12-19
-->
<template>
  <div class="p-4">
    <BasicTable @register="registerTable" :rowSelection="rowSelection">
      <!-- 表格标题左侧按钮 -->
      <template #tableTitle>
        <a-button type="primary" preIcon="ant-design:plus-outlined" @click="handleAdd">
          新增
        </a-button>
        <a-button
          type="primary"
          danger
          preIcon="ant-design:delete-outlined"
          :disabled="!selectedRowKeys.length"
          @click="handleBatchDelete"
        >
          批量删除
        </a-button>
        <a-upload
          name="file"
          :showUploadList="false"
          :action="importUrl"
          :headers="headers"
          @change="handleImportChange"
        >
          <a-button preIcon="ant-design:upload-outlined">导入</a-button>
        </a-upload>
        <a-button preIcon="ant-design:download-outlined" @click="handleExport">
          导出
        </a-button>
      </template>

      <!-- 使用次数列 -->
      <template #useCount="{ text }">
        <a-tag :color="getUseCountColor(text)">{{ text || 0 }}</a-tag>
      </template>

      <!-- 操作列 -->
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:edit-outlined',
              tooltip: '编辑',
              onClick: handleEdit.bind(null, record),
            },
            {
              icon: 'ant-design:delete-outlined',
              tooltip: '删除',
              color: 'error',
              popConfirm: {
                title: '确定要删除吗？',
                placement: 'left',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>

    <!-- 新增/编辑弹窗 -->
    <ConfigModal @register="registerModal" @success="reload" />
  </div>
</template>

<script lang="ts" setup>
/**
 * 常用医疗器械配置列表页
 */
import { ref } from 'vue';
import { BasicTable, TableAction } from '/@/components/Table';
import { useListPage } from '/@/hooks/system/useListPage';
import { useModal } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { getToken } from '/@/utils/auth';
import { columns, searchFormSchema } from './config.data';
import {
  getDeviceConfigList,
  deleteDeviceConfig,
  deleteDeviceConfigBatch,
  getExportUrl,
  getImportUrl,
} from '/@/api/adverse/deviceConfig';
import ConfigModal from './ConfigModal.vue';

// 页面标题
defineOptions({ name: 'DeviceCommonConfigList' });

const { createMessage } = useMessage();

// 导入URL和请求头
const importUrl = ref(getImportUrl);
const headers = ref({ 'X-Access-Token': getToken() });

// 弹窗注册
const [registerModal, { openModal }] = useModal();

// 表格配置
const { tableContext } = useListPage({
  designScope: 'adverse-device-config-list',
  tableProps: {
    title: '常用医疗器械配置',
    api: getDeviceConfigList,
    columns: columns,
    size: 'small',
    formConfig: {
      schemas: searchFormSchema,
      fieldMapToTime: [],
    },
    rowKey: 'id',
    actionColumn: {
      width: 120,
      fixed: 'right',
    },
    showIndexColumn: true,
  },
});

const [registerTable, { reload, getForm }, { rowSelection, selectedRowKeys }] = tableContext;

/**
 * 获取使用次数颜色
 */
function getUseCountColor(count: number) {
  if (!count || count === 0) return 'default';
  if (count >= 10) return 'green';
  if (count >= 5) return 'blue';
  return 'orange';
}

/**
 * 新增
 */
function handleAdd() {
  openModal(true, { isUpdate: false });
}

/**
 * 编辑
 */
function handleEdit(record: Recordable) {
  openModal(true, { isUpdate: true, record });
}

/**
 * 删除
 */
async function handleDelete(record: Recordable) {
  try {
    await deleteDeviceConfig(record.id);
    createMessage.success('删除成功');
    reload();
  } catch (error) {
    console.error('删除失败:', error);
  }
}

/**
 * 批量删除
 */
async function handleBatchDelete() {
  if (!selectedRowKeys.value.length) {
    createMessage.warning('请选择要删除的记录');
    return;
  }
  try {
    await deleteDeviceConfigBatch(selectedRowKeys.value.join(','));
    createMessage.success('批量删除成功');
    selectedRowKeys.value = [];
    reload();
  } catch (error) {
    console.error('批量删除失败:', error);
  }
}

/**
 * 导出
 */
function handleExport() {
  const form = getForm();
  const values = form.getFieldsValue();
  const params = new URLSearchParams(values).toString();
  const url = `${getExportUrl}?${params}`;
  window.open(url, '_blank');
}

/**
 * 导入变化处理
 */
function handleImportChange(info: any) {
  if (info.file.status === 'done') {
    if (info.file.response?.success) {
      createMessage.success('导入成功');
      reload();
    } else {
      createMessage.error(info.file.response?.message || '导入失败');
    }
  } else if (info.file.status === 'error') {
    createMessage.error('导入失败');
  }
}
</script>

<style lang="less" scoped>
// 列表页样式
</style>
