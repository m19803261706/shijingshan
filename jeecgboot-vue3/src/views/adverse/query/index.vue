<template>
  <div class="p-4">
    <!-- 表格 -->
    <BasicTable @register="registerTable">
      <!-- 工具栏 -->
      <template #toolbar>
        <a-button type="primary" @click="handleExport" :loading="exportLoading">
          <template #icon><DownloadOutlined /></template>
          导出 Excel
        </a-button>
      </template>

      <!-- 事件级别列 -->
      <template #level="{ record }">
        <a-tag :color="getLevelColor(record.level)">{{ getLevelText(record.level) }}</a-tag>
      </template>

      <!-- 状态列 -->
      <template #status="{ record }">
        <a-tag :color="getStatusColor(record.status)">{{ getStatusText(record.status) }}</a-tag>
      </template>

      <!-- 操作列 -->
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              label: '查看详情',
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

<script lang="ts" name="adverse-query" setup>
/**
 * 综合查询页面
 * @description 高级搜索和导出功能
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref } from 'vue';
import { BasicTable, TableAction, BasicColumn, FormSchema } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { useMessage } from '/@/hooks/web/useMessage';
import { queryEventList, exportEventExcel } from '/@/api/adverse/query';
import { eventStatusOptions, eventLevelOptions } from '../event/event.data';
import EventDetailModal from '../components/EventDetailModal.vue';
import { DownloadOutlined } from '@ant-design/icons-vue';
import { downloadByData } from '/@/utils/file/download';
import dayjs from 'dayjs';

const { createMessage } = useMessage();

// 注册详情弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();

// 导出加载状态
const exportLoading = ref(false);

/**
 * 表格列配置（增强版）
 */
const columns: BasicColumn[] = [
  {
    title: '事件编号',
    dataIndex: 'eventCode',
    width: 150,
    sorter: true,
  },
  {
    title: '事件标题',
    dataIndex: 'title',
    width: 200,
  },
  {
    title: '事件分类',
    dataIndex: 'categoryId_dictText',
    width: 120,
  },
  {
    title: '事件级别',
    dataIndex: 'level',
    width: 140,
    slots: { customRender: 'level' },
    sorter: true,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    slots: { customRender: 'status' },
  },
  {
    title: '上报科室',
    dataIndex: 'reportDeptId_dictText',
    width: 120,
  },
  {
    title: '上报人',
    dataIndex: 'createBy_dictText',
    width: 100,
  },
  {
    title: '发生时间',
    dataIndex: 'occurTime',
    width: 160,
    sorter: true,
  },
  {
    title: '上报时间',
    dataIndex: 'createTime',
    width: 160,
    sorter: true,
  },
];

/**
 * 高级搜索表单配置
 */
const searchFormSchema: FormSchema[] = [
  {
    label: '事件编号',
    field: 'eventCode',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '事件标题',
    field: 'title',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '事件状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: eventStatusOptions,
      placeholder: '请选择状态',
      allowClear: true,
    },
    colProps: { span: 6 },
  },
  {
    label: '事件级别',
    field: 'level',
    component: 'Select',
    componentProps: {
      options: eventLevelOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择级别',
      allowClear: true,
    },
    colProps: { span: 6 },
  },
  {
    label: '上报科室',
    field: 'reportDeptId',
    component: 'JSelectDept',
    componentProps: {
      placeholder: '请选择科室',
    },
    colProps: { span: 6 },
  },
  {
    label: '发生时间',
    field: 'occurTime',
    component: 'RangePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      placeholder: ['开始时间', '结束时间'],
      style: { width: '100%' },
    },
    colProps: { span: 6 },
  },
  {
    label: '上报时间',
    field: 'createTime',
    component: 'RangePicker',
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      placeholder: ['开始时间', '结束时间'],
      style: { width: '100%' },
    },
    colProps: { span: 6 },
  },
];

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'adverse-query',
  tableProps: {
    title: '综合查询',
    api: queryEventList,
    columns: columns,
    size: 'small',
    formConfig: {
      schemas: searchFormSchema,
      labelWidth: 80,
      autoSubmitOnEnter: true,
    },
    actionColumn: {
      width: 100,
    },
    useSearchForm: true,
    showTableSetting: true,
    bordered: true,
    beforeFetch: (params) => {
      // 处理时间范围参数
      if (params.occurTime && Array.isArray(params.occurTime)) {
        params.occurTime_begin = params.occurTime[0];
        params.occurTime_end = params.occurTime[1];
        delete params.occurTime;
      }
      if (params.createTime && Array.isArray(params.createTime)) {
        params.createTime_begin = params.createTime[0];
        params.createTime_end = params.createTime[1];
        delete params.createTime;
      }
      return params;
    },
  },
});

// 注册表格
const [registerTable, { getForm }] = tableContext;

/**
 * 获取级别文本
 */
function getLevelText(level: string) {
  const item = eventLevelOptions.find((i) => i.value === level);
  return item?.label || level;
}

/**
 * 获取级别颜色
 */
function getLevelColor(level: string) {
  const item = eventLevelOptions.find((i) => i.value === level);
  return item?.color || 'default';
}

/**
 * 获取状态文本
 */
function getStatusText(status: string) {
  const item = eventStatusOptions.find((i) => i.value === status);
  return item?.label || status;
}

/**
 * 获取状态颜色
 */
function getStatusColor(status: string) {
  const item = eventStatusOptions.find((i) => i.value === status);
  return item?.color || 'default';
}

/**
 * 查看详情
 */
function handleDetail(record: Recordable) {
  openDetailModal(true, { record });
}

/**
 * 导出 Excel
 */
async function handleExport() {
  try {
    exportLoading.value = true;
    const formValues = getForm()?.getFieldsValue() || {};

    // 处理时间范围参数
    const params = { ...formValues };
    if (params.occurTime && Array.isArray(params.occurTime)) {
      params.occurTime_begin = params.occurTime[0];
      params.occurTime_end = params.occurTime[1];
      delete params.occurTime;
    }
    if (params.createTime && Array.isArray(params.createTime)) {
      params.createTime_begin = params.createTime[0];
      params.createTime_end = params.createTime[1];
      delete params.createTime;
    }

    const data = await exportEventExcel(params);
    const fileName = `不良事件查询_${dayjs().format('YYYYMMDD_HHmmss')}.xlsx`;
    downloadByData(data, fileName);
    createMessage.success('导出成功');
  } catch (e) {
    console.error('导出失败', e);
    createMessage.error('导出失败');
  } finally {
    exportLoading.value = false;
  }
}
</script>

<style scoped>
.p-4 {
  padding: 16px;
}
</style>
