<template>
  <div class="drug-rectify-list">
    <!-- 表格 -->
    <BasicTable @register="registerTable">
      <!-- 整改期限列 -->
      <template #deadline="{ record }">
        <span :class="getDeadlineClass(record)">
          {{ getDeadlineText(record) }}
          <a-tag v-if="isOverdue(record)" color="red" size="small">已逾期</a-tag>
          <a-tag v-else-if="isNearDeadline(record)" color="orange" size="small">即将到期</a-tag>
        </span>
      </template>

      <!-- 整改状态列 -->
      <template #rectifyStatus="{ record }">
        <a-tag :color="getRectifyStatusColor(record)">
          {{ getRectifyStatusText(record) }}
        </a-tag>
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
              label: '整改历史',
              onClick: handleHistory.bind(null, record),
            },
            {
              label: '填写整改',
              color: 'success',
              onClick: handleRectify.bind(null, record),
              ifShow: canSubmitRectify(record),
            },
          ]"
        />
      </template>
    </BasicTable>

    <!-- 报告详情弹窗 -->
    <DrugReportDetailModal @register="registerDetailModal" />

    <!-- 整改历史弹窗 -->
    <RectifyHistoryModal @register="registerHistoryModal" />

    <!-- 整改表单弹窗 -->
    <RectifyFormModal @register="registerFormModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="clinic-rectify-drug-list" setup>
/**
 * 药品不良反应整改列表
 * @description 临床科室人员查看和提交药品不良反应整改
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/55
 */
import { ref, onMounted } from 'vue';
import { BasicTable, TableAction, BasicColumn, FormSchema } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { getDrugRectifyList, getDrugCurrentRectify } from '/@/api/adverse/clinicRectify';
import DrugReportDetailModal from '../../clinic-audit/drug/components/DrugReportDetailModal.vue';
import RectifyHistoryModal from '../../drug-process/components/RectifyHistoryModal.vue';
import RectifyFormModal from './RectifyFormModal.vue';
import { render } from '/@/utils/common/renderUtils';
import dayjs from 'dayjs';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerHistoryModal, { openModal: openHistoryModal }] = useModal();
const [registerFormModal, { openModal: openFormModal }] = useModal();

// 当前整改记录缓存
const currentRectifyMap = ref<Record<string, any>>({});

/**
 * 报告类型映射
 */
const reportTypeOptions = [
  { label: '首次报告', value: 'first', color: 'processing' },
  { label: '跟踪报告', value: 'follow_up', color: 'warning' },
];

/**
 * 严重程度映射
 */
const severityTypeOptions = [
  { label: '新的', value: 'new', color: 'blue' },
  { label: '严重', value: 'serious', color: 'red' },
  { label: '一般', value: 'general', color: 'default' },
];

/**
 * 整改状态映射
 */
const rectifyStatusOptions = [
  { label: '待整改', value: 'pending', color: 'warning' },
  { label: '已提交', value: 'submitted', color: 'processing' },
  { label: '已退回', value: 'rejected', color: 'error' },
];

/**
 * 表格列配置
 */
const columns: BasicColumn[] = [
  {
    title: '报告编号',
    dataIndex: 'reportCode',
    width: 150,
  },
  {
    title: '报告类型',
    dataIndex: 'reportType',
    width: 100,
    customRender: ({ text }) => {
      const item = reportTypeOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '严重程度',
    dataIndex: 'severityType',
    width: 100,
    customRender: ({ text }) => {
      const item = severityTypeOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '患者姓名',
    dataIndex: 'patientName',
    width: 100,
  },
  {
    title: '不良反应名称',
    dataIndex: 'reactionName',
    width: 150,
  },
  {
    title: '整改期限',
    dataIndex: 'deadline',
    width: 160,
    slots: { customRender: 'deadline' },
  },
  {
    title: '整改状态',
    dataIndex: 'rectifyStatus',
    width: 100,
    slots: { customRender: 'rectifyStatus' },
  },
  {
    title: '上报科室',
    dataIndex: 'departmentId_dictText',
    width: 120,
  },
];

/**
 * 搜索表单配置
 */
const searchFormSchema: FormSchema[] = [
  {
    label: '报告编号',
    field: 'reportNo',
    component: 'Input',
    componentProps: {
      placeholder: '请输入报告编号',
    },
    colProps: { span: 6 },
  },
  {
    label: '患者姓名',
    field: 'patientName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入患者姓名',
    },
    colProps: { span: 6 },
  },
];

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'clinic-rectify-drug',
  tableProps: {
    title: '药品不良反应待整改列表',
    api: getDrugRectifyList,
    columns: columns,
    size: 'small',
    formConfig: {
      schemas: searchFormSchema,
    },
    actionColumn: {
      width: 200,
    },
    beforeFetch: (params) => {
      return Object.assign({ column: 'updateTime', order: 'asc' }, params);
    },
    afterFetch: async (records) => {
      // 加载每条记录的当前整改信息
      for (const record of records) {
        try {
          const rectify = await getDrugCurrentRectify(record.id);
          if (rectify) {
            currentRectifyMap.value[record.id] = rectify;
          }
        } catch (e) {
          // 忽略错误
        }
      }
      return records;
    },
  },
});

// 注册表格
const [registerTable, { reload }] = tableContext;

/**
 * 获取当前整改记录
 */
function getCurrentRectify(record: Recordable): Recordable | null {
  return currentRectifyMap.value[record.id] || null;
}

/**
 * 获取整改期限文本
 */
function getDeadlineText(record: Recordable): string {
  const rectify = getCurrentRectify(record);
  return rectify?.deadline || '-';
}

/**
 * 判断是否已逾期
 */
function isOverdue(record: Recordable): boolean {
  const rectify = getCurrentRectify(record);
  if (!rectify?.deadline) return false;
  return dayjs().isAfter(dayjs(rectify.deadline), 'day');
}

/**
 * 判断是否即将到期（3天内）
 */
function isNearDeadline(record: Recordable): boolean {
  const rectify = getCurrentRectify(record);
  if (!rectify?.deadline) return false;
  const diff = dayjs(rectify.deadline).diff(dayjs(), 'day');
  return diff >= 0 && diff <= 3;
}

/**
 * 获取期限样式类
 */
function getDeadlineClass(record: Recordable): string {
  if (isOverdue(record)) return 'text-red';
  if (isNearDeadline(record)) return 'text-orange';
  return '';
}

/**
 * 获取整改状态文本
 */
function getRectifyStatusText(record: Recordable): string {
  const rectify = getCurrentRectify(record);
  const status = rectify?.status || 'pending';
  const item = rectifyStatusOptions.find((i) => i.value === status);
  return item?.label || status;
}

/**
 * 获取整改状态颜色
 */
function getRectifyStatusColor(record: Recordable): string {
  const rectify = getCurrentRectify(record);
  const status = rectify?.status || 'pending';
  const item = rectifyStatusOptions.find((i) => i.value === status);
  return item?.color || 'default';
}

/**
 * 判断是否可以提交整改
 */
function canSubmitRectify(record: Recordable): boolean {
  const rectify = getCurrentRectify(record);
  if (!rectify) return true;
  // 待整改或已退回状态可提交
  return rectify.status === 'pending' || rectify.status === 'rejected';
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
 * 填写整改
 */
function handleRectify(record: Recordable) {
  const rectify = getCurrentRectify(record);
  openFormModal(true, { record, rectify });
}

/**
 * 操作成功回调
 */
function handleSuccess() {
  // 清空缓存并刷新
  currentRectifyMap.value = {};
  reload();
}

// 暴露 reload 方法供父组件调用
defineExpose({ reload });
</script>

<style scoped>
.drug-rectify-list {
  padding: 0;
}

.text-red {
  color: #ef4444;
}

.text-orange {
  color: #f97316;
}
</style>
