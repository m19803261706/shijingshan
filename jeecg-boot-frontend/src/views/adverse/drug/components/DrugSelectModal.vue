<!--
 * 药品选择弹窗组件
 * <p>
 * 通用的药品选择弹窗，支持选择常用怀疑药品或常用并用药品
 * 按使用次数降序排列，常用药品优先显示
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
-->
<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="title"
    :width="900"
    :footer="null"
    @visible-change="handleVisibleChange"
  >
    <!-- 搜索区域 -->
    <div class="mb-4">
      <a-input-search
        v-model:value="keyword"
        placeholder="请输入通用名称、商品名称或生产厂家搜索"
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
      提示：点击行或点击"选择"按钮选择药品，按使用次数排序，常用药品优先显示
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 药品选择弹窗组件
 * 支持怀疑药品和并用药品两种类型
 */
import { ref, computed, unref, watch } from 'vue';
import { BasicModal, useModalInner } from '@/components/Modal';
import { searchSuspect, updateSuspectUseCount } from '@/api/adverse/suspect';
import { searchConcomitant, updateConcomitantUseCount } from '@/api/adverse/concomitant';

// 组件名称
defineOptions({ name: 'DrugSelectModal' });

// Props 定义
interface Props {
  /** 药品类型：suspect-怀疑药品，concomitant-并用药品 */
  type?: 'suspect' | 'concomitant';
}

const props = withDefaults(defineProps<Props>(), {
  type: 'suspect',
});

// 事件定义
const emit = defineEmits<{
  (e: 'select', drug: any): void;
  (e: 'register'): void;
}>();

// 状态
const keyword = ref('');
const dataSource = ref<any[]>([]);
const loading = ref(false);

// 弹窗标题
const title = computed(() => {
  return props.type === 'suspect' ? '选择常用怀疑药品' : '选择常用并用药品';
});

// 表格列配置
const columns = [
  {
    title: '通用名称',
    dataIndex: 'genericName',
    width: 150,
    ellipsis: true,
  },
  {
    title: '商品名称',
    dataIndex: 'tradeName',
    width: 120,
    ellipsis: true,
  },
  {
    title: '生产厂家',
    dataIndex: 'manufacturer',
    width: 150,
    ellipsis: true,
  },
  {
    title: '批准文号',
    dataIndex: 'approvalNo',
    width: 120,
    ellipsis: true,
  },
  {
    title: '给药途径',
    dataIndex: 'route',
    width: 100,
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
    const searchFn = props.type === 'suspect' ? searchSuspect : searchConcomitant;
    const res = await searchFn(unref(keyword));
    dataSource.value = res || [];
  } catch (error) {
    console.error('加载药品列表失败:', error);
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
 * 选择药品
 * @param record 药品记录
 */
async function handleSelect(record: any) {
  // 更新使用次数
  try {
    const updateFn = props.type === 'suspect' ? updateSuspectUseCount : updateConcomitantUseCount;
    await updateFn(record.id);
  } catch (error) {
    console.error('更新使用次数失败:', error);
  }

  // 返回选中的药品信息
  emit('select', {
    genericName: record.genericName,
    tradeName: record.tradeName,
    manufacturer: record.manufacturer,
    approvalNo: record.approvalNo,
    route: record.route,
    dosageForm: record.dosageForm,
    specification: record.specification,
    dosage: record.dosage,
  });

  // 关闭弹窗
  closeModal();
}

/**
 * 弹窗显示状态变化处理
 * @param visible 是否显示
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
 * @param count 使用次数
 */
function getUseCountColor(count: number) {
  if (!count || count === 0) return 'default';
  if (count >= 10) return 'green';
  if (count >= 5) return 'blue';
  return 'orange';
}

/**
 * 获取行样式类名
 * @param record 记录
 */
function getRowClassName(record: any) {
  return 'cursor-pointer hover:bg-blue-50';
}

/**
 * 自定义行属性（点击行选择）
 * @param record 记录
 */
function customRow(record: any) {
  return {
    onClick: () => {
      handleSelect(record);
    },
  };
}

// 监听 type 变化，重新加载数据
watch(() => props.type, () => {
  if (dataSource.value.length > 0) {
    loadData();
  }
});
</script>

<style lang="less" scoped>
// 药品选择弹窗样式
:deep(.ant-table-row) {
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #e6f7ff !important;
  }
}
</style>
