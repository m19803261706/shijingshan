<template>
  <div class="category-manage p-4">
    <a-card>
      <!-- 工具栏 -->
      <div class="toolbar mb-4">
        <a-space>
          <a-button type="primary" @click="handleAdd(null)">
            <template #icon><PlusOutlined /></template>
            新增分类
          </a-button>
          <a-button @click="handleExpandAll">
            <template #icon><NodeExpandOutlined /></template>
            {{ expandAll ? '全部折叠' : '全部展开' }}
          </a-button>
          <a-button @click="handleRefresh" :loading="loading">
            <template #icon><ReloadOutlined /></template>
            刷新
          </a-button>
        </a-space>
      </div>

      <!-- 分类树表格 -->
      <a-table
        :columns="columns"
        :data-source="treeData"
        :loading="loading"
        :pagination="false"
        :expanded-row-keys="expandedKeys"
        row-key="id"
        size="middle"
        @expand="handleExpand"
      >
        <!-- 分类名称列 -->
        <template #name="{ record }">
          <span>{{ record.name }}</span>
          <a-tag v-if="!record.status" color="default" class="ml-2">已禁用</a-tag>
        </template>

        <!-- 状态列 -->
        <template #status="{ record }">
          <a-switch
            :checked="record.status === 1"
            checked-children="启用"
            un-checked-children="禁用"
            @change="handleToggleStatus(record)"
          />
        </template>

        <!-- 操作列 -->
        <template #action="{ record }">
          <a-space>
            <a-button type="link" size="small" @click="handleAdd(record)">
              <template #icon><PlusOutlined /></template>
              添加子分类
            </a-button>
            <a-button type="link" size="small" @click="handleEdit(record)">
              <template #icon><EditOutlined /></template>
              编辑
            </a-button>
            <a-popconfirm
              title="确定要删除该分类吗？"
              :description="record.children?.length ? '该分类下有子分类，将一并删除' : ''"
              @confirm="handleDelete(record)"
            >
              <a-button type="link" size="small" danger>
                <template #icon><DeleteOutlined /></template>
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 新增/编辑弹窗 -->
    <CategoryFormModal @register="registerModal" @success="handleRefresh" />
  </div>
</template>

<script lang="ts" name="adverse-category" setup>
/**
 * 事件分类管理页面
 * @description 树形结构展示和管理事件分类
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref, onMounted } from 'vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { useModal } from '/@/components/Modal';
import {
  getCategoryTree,
  deleteCategory,
  toggleCategoryStatus,
  CategoryItem,
} from '/@/api/adverse/category';
import CategoryFormModal from './CategoryFormModal.vue';
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  ReloadOutlined,
  NodeExpandOutlined,
} from '@ant-design/icons-vue';

const { createMessage } = useMessage();

// 注册弹窗
const [registerModal, { openModal }] = useModal();

// 加载状态
const loading = ref(false);
// 树形数据
const treeData = ref<CategoryItem[]>([]);
// 展开的行
const expandedKeys = ref<string[]>([]);
// 是否全部展开
const expandAll = ref(false);

/**
 * 表格列配置
 */
const columns = [
  {
    title: '分类名称',
    dataIndex: 'name',
    key: 'name',
    width: 300,
    slots: { customRender: 'name' },
  },
  {
    title: '分类编码',
    dataIndex: 'code',
    key: 'code',
    width: 150,
  },
  {
    title: '排序',
    dataIndex: 'sortOrder',
    key: 'sortOrder',
    width: 80,
    align: 'center',
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    align: 'center',
    slots: { customRender: 'status' },
  },
  {
    title: '备注',
    dataIndex: 'remark',
    key: 'remark',
    ellipsis: true,
  },
  {
    title: '操作',
    key: 'action',
    width: 280,
    fixed: 'right',
    slots: { customRender: 'action' },
  },
];

/**
 * 加载分类树数据
 */
async function loadData() {
  try {
    loading.value = true;
    const data = await getCategoryTree();
    treeData.value = data || [];

    // 初始展开第一级
    if (treeData.value.length > 0 && expandedKeys.value.length === 0) {
      expandedKeys.value = treeData.value.map((item) => item.id);
    }
  } catch (e) {
    console.error('加载分类数据失败', e);
    createMessage.error('加载分类数据失败');
  } finally {
    loading.value = false;
  }
}

/**
 * 刷新数据
 */
function handleRefresh() {
  loadData();
}

/**
 * 处理行展开
 */
function handleExpand(expanded: boolean, record: CategoryItem) {
  if (expanded) {
    expandedKeys.value = [...expandedKeys.value, record.id];
  } else {
    expandedKeys.value = expandedKeys.value.filter((key) => key !== record.id);
  }
}

/**
 * 全部展开/折叠
 */
function handleExpandAll() {
  if (expandAll.value) {
    expandedKeys.value = [];
  } else {
    expandedKeys.value = getAllKeys(treeData.value);
  }
  expandAll.value = !expandAll.value;
}

/**
 * 获取所有节点的 key
 */
function getAllKeys(data: CategoryItem[]): string[] {
  const keys: string[] = [];
  function traverse(items: CategoryItem[]) {
    items.forEach((item) => {
      keys.push(item.id);
      if (item.children && item.children.length > 0) {
        traverse(item.children);
      }
    });
  }
  traverse(data);
  return keys;
}

/**
 * 新增分类
 */
function handleAdd(parent: CategoryItem | null) {
  openModal(true, {
    isUpdate: false,
    record: {
      parentId: parent?.id || '',
      parentName: parent?.name || '',
    },
  });
}

/**
 * 编辑分类
 */
function handleEdit(record: CategoryItem) {
  openModal(true, {
    isUpdate: true,
    record: { ...record },
  });
}

/**
 * 删除分类
 */
async function handleDelete(record: CategoryItem) {
  try {
    await deleteCategory({ id: record.id });
    createMessage.success('删除成功');
    loadData();
  } catch (e) {
    console.error('删除失败', e);
  }
}

/**
 * 切换状态
 */
async function handleToggleStatus(record: CategoryItem) {
  try {
    const newStatus = record.status === 1 ? 0 : 1;
    await toggleCategoryStatus({ id: record.id, status: newStatus });
    createMessage.success(newStatus === 1 ? '已启用' : '已禁用');
    loadData();
  } catch (e) {
    console.error('切换状态失败', e);
  }
}

// 初始化
onMounted(() => {
  loadData();
});
</script>

<style scoped>
.category-manage {
  background: #f0f2f5;
  min-height: 100%;
}

.p-4 {
  padding: 16px;
}

.mb-4 {
  margin-bottom: 16px;
}

.ml-2 {
  margin-left: 8px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
