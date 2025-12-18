<template>
  <div class="p-4">
    <!-- 表格 -->
    <BasicTable @register="registerTable">
      <!-- 整改期限列 -->
      <template #deadline="{ record }">
        <span :class="getDeadlineClass(record.deadline)">
          {{ record.deadline }}
          <a-tag v-if="isOverdue(record.deadline)" color="red">已逾期</a-tag>
          <a-tag v-else-if="isNearDeadline(record.deadline)" color="orange">即将到期</a-tag>
        </span>
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
            },
          ]"
        />
      </template>
    </BasicTable>

    <!-- 事件详情弹窗 -->
    <EventDetailModal @register="registerDetailModal" />

    <!-- 整改历史弹窗 -->
    <RectifyHistoryModal @register="registerHistoryModal" />

    <!-- 整改表单弹窗 -->
    <RectifyFormModal @register="registerFormModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" name="adverse-rectify-pending" setup>
/**
 * 待整改列表页面
 * @description 显示需要整改的事件，支持查看要求和填写整改
 * @author TC Agent
 * @since 2025-12-18
 */
import { BasicTable, TableAction, BasicColumn } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import { useListPage } from '/@/hooks/system/useListPage';
import { columns as baseColumns, searchFormSchema } from '../event/event.data';
import { getPendingRectifyList } from '/@/api/adverse/rectify';
import EventDetailModal from '../components/EventDetailModal.vue';
import RectifyHistoryModal from '../components/RectifyHistoryModal.vue';
import RectifyFormModal from '../components/RectifyFormModal.vue';
import dayjs from 'dayjs';

// 注册弹窗
const [registerDetailModal, { openModal: openDetailModal }] = useModal();
const [registerHistoryModal, { openModal: openHistoryModal }] = useModal();
const [registerFormModal, { openModal: openFormModal }] = useModal();

// 扩展列配置，添加整改期限列
const columns: BasicColumn[] = [
  ...baseColumns.filter((c) => c.dataIndex !== 'status'),
  {
    title: '整改期限',
    dataIndex: 'deadline',
    width: 180,
    slots: { customRender: 'deadline' },
  },
  {
    title: '整改次数',
    dataIndex: 'rectifyCount',
    width: 100,
  },
];

// 列表页面公共参数、方法
const { tableContext } = useListPage({
  designScope: 'adverse-rectify-pending',
  tableProps: {
    title: '待整改列表',
    api: getPendingRectifyList,
    columns: columns,
    size: 'small',
    formConfig: {
      schemas: searchFormSchema.filter((s) => s.field !== 'status'),
    },
    actionColumn: {
      width: 220,
    },
    beforeFetch: (params) => {
      return Object.assign({ column: 'deadline', order: 'asc' }, params);
    },
  },
});

// 注册表格
const [registerTable, { reload }] = tableContext;

/**
 * 判断是否已逾期
 */
function isOverdue(deadline: string): boolean {
  if (!deadline) return false;
  return dayjs().isAfter(dayjs(deadline), 'day');
}

/**
 * 判断是否即将到期（3天内）
 */
function isNearDeadline(deadline: string): boolean {
  if (!deadline) return false;
  const diff = dayjs(deadline).diff(dayjs(), 'day');
  return diff >= 0 && diff <= 3;
}

/**
 * 获取期限样式类
 */
function getDeadlineClass(deadline: string): string {
  if (isOverdue(deadline)) return 'text-red-500';
  if (isNearDeadline(deadline)) return 'text-orange-500';
  return '';
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
  openHistoryModal(true, { eventId: record.id });
}

/**
 * 填写整改
 */
function handleRectify(record: Recordable) {
  openFormModal(true, { record });
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

.text-red-500 {
  color: #ef4444;
}

.text-orange-500 {
  color: #f97316;
}
</style>
