<!--
 * 医疗器械选择弹窗组件
 * @description 通用的医疗器械选择弹窗，支持搜索常用器械配置
 *              按使用次数降序排列，常用器械优先显示
 * @author TC Agent
 * @since 2025-12-19
-->
<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="选择常用医疗器械"
    :width="1000"
    :footer="null"
    @visible-change="handleVisibleChange"
  >
    <!-- 搜索区域 -->
    <div class="mb-4">
      <a-input-search
        v-model:value="keyword"
        placeholder="请输入产品名称、商品名称、生产企业或注册证号搜索"
        enter-button="搜索"
        size="large"
        allow-clear
        @search="handleSearch"
        @change="handleSearchChange"
      />
    </div>

    <!-- 表格区域 -->
    <a-table
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      :pagination="false"
      :scroll="{ y: 400 }"
      row-key="id"
      size="small"
      :row-class-name="getRowClassName"
      :custom-row="customRow"
    >
      <!-- 使用次数列 -->
      <template #useCount="{ text }">
        <a-tag :color="getUseCountColor(text)">{{ text || 0 }}</a-tag>
      </template>

      <!-- 操作列 -->
      <template #action="{ record }">
        <a-button type="link" size="small" @click="handleSelect(record)">
          选择
        </a-button>
      </template>
    </a-table>

    <!-- 提示信息 -->
    <div class="mt-2 text-gray-500 text-sm">
      提示：点击行或点击"选择"按钮选择器械，按使用次数排序，常用器械优先显示
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 医疗器械选择弹窗组件
 */
import { ref, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { searchDeviceConfig, updateDeviceConfigUseCount } from '/@/api/adverse/deviceConfig';

// 组件名称
defineOptions({ name: 'DeviceSelectModal' });

// 事件定义
const emit = defineEmits<{
  (e: 'select', device: any): void;
  (e: 'register'): void;
}>();

// 状态
const keyword = ref('');
const dataSource = ref<any[]>([]);
const loading = ref(false);

// 表格列配置
const columns = [
  {
    title: '产品名称',
    dataIndex: 'productName',
    width: 180,
    ellipsis: true,
  },
  {
    title: '商品名称',
    dataIndex: 'tradeName',
    width: 120,
    ellipsis: true,
  },
  {
    title: '注册证号',
    dataIndex: 'registrationNo',
    width: 150,
    ellipsis: true,
  },
  {
    title: '生产企业',
    dataIndex: 'manufacturerName',
    width: 180,
    ellipsis: true,
  },
  {
    title: '型号规格',
    dataIndex: 'modelSpec',
    width: 120,
    ellipsis: true,
  },
  {
    title: '使用次数',
    dataIndex: 'useCount',
    width: 80,
    align: 'center',
    slots: { customRender: 'useCount' },
  },
  {
    title: '操作',
    key: 'action',
    width: 80,
    align: 'center',
    slots: { customRender: 'action' },
  },
];

// 弹窗配置
const [registerModal, { closeModal }] = useModalInner(async () => {
  // 打开弹窗时加载初始数据
  await loadData();
});

/**
 * 加载数据
 */
async function loadData() {
  loading.value = true;
  try {
    const res = await searchDeviceConfig(unref(keyword));
    dataSource.value = res || [];
  } catch (error) {
    console.error('加载器械列表失败:', error);
    dataSource.value = [];
  } finally {
    loading.value = false;
  }
}

/**
 * 搜索处理
 */
function handleSearch() {
  loadData();
}

/**
 * 搜索框内容变化处理（清空时重新加载）
 */
function handleSearchChange(e: any) {
  if (!e.target.value) {
    loadData();
  }
}

/**
 * 选择器械
 */
async function handleSelect(record: any) {
  // 更新使用次数
  try {
    await updateDeviceConfigUseCount(record.id);
  } catch (error) {
    console.error('更新使用次数失败:', error);
  }

  // 返回选中的器械信息（包含所有可用字段）
  emit('select', {
    productName: record.productName,
    tradeName: record.tradeName,
    registrationNo: record.registrationNo,
    manufacturerName: record.manufacturerName,
    manufacturerAddress: record.manufacturerAddress,
    manufacturerContact: record.manufacturerContact,
    modelSpec: record.modelSpec,
    productCode: record.productCode,
    productBatch: record.productBatch,
  });

  // 关闭弹窗
  closeModal();
}

/**
 * 弹窗显示状态变化处理
 */
function handleVisibleChange(visible: boolean) {
  if (!visible) {
    // 关闭时重置状态
    keyword.value = '';
    dataSource.value = [];
  }
}

/**
 * 获取使用次数的颜色
 */
function getUseCountColor(count: number) {
  if (!count || count === 0) return 'default';
  if (count >= 10) return 'green';
  if (count >= 5) return 'blue';
  return 'orange';
}

/**
 * 获取行样式类名
 */
function getRowClassName(record: any) {
  return 'cursor-pointer hover:bg-blue-50';
}

/**
 * 自定义行属性（点击行选择）
 */
function customRow(record: any) {
  return {
    onClick: () => {
      handleSelect(record);
    },
  };
}
</script>

<style lang="less" scoped>
// 器械选择弹窗样式
:deep(.ant-table-row) {
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #e6f7ff !important;
  }
}
</style>
