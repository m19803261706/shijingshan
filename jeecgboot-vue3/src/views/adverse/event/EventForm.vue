<template>
  <PageWrapper :title="getTitle" @back="handleBack" contentBackground contentClass="p-4">
    <a-card>
      <BasicForm @register="registerForm" />
    </a-card>

    <!-- 底部操作按钮 -->
    <template #rightFooter>
      <a-button @click="handleBack">返回</a-button>
      <a-button type="default" @click="handleSaveDraft" :loading="loading">
        保存草稿
      </a-button>
      <a-button type="primary" @click="handleSubmit" :loading="loading">
        {{ isReturned ? '重新提交' : '提交' }}
      </a-button>
    </template>
  </PageWrapper>
</template>

<script lang="ts" setup>
/**
 * 事件表单页面
 * @description 用于新增、编辑事件（独立页面形式）
 * @author TC Agent
 * @since 2025-12-19
 */
import { ref, computed, unref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { PageWrapper } from '/@/components/Page';
import { BasicForm, useForm } from '/@/components/Form';
import { eventFormSchema } from './event.data';
import { saveEventDraft, submitEvent, resubmitEvent, getCategoryTree, queryEventById } from '/@/api/adverse/event';
import { useMessage } from '/@/hooks/web/useMessage';

const route = useRoute();
const router = useRouter();
const { createMessage } = useMessage();

// 状态
const isUpdate = ref(false);
const isReturned = ref(false);
const loading = ref(false);
const recordId = ref('');

// 表单配置
const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
  labelWidth: 120,
  schemas: eventFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 24 },
});

// 计算标题
const getTitle = computed(() => {
  if (unref(isReturned)) {
    return '重新提交事件';
  }
  return unref(isUpdate) ? '编辑事件' : '新增事件上报';
});

/**
 * 初始化页面
 */
onMounted(async () => {
  // 从路由参数获取数据
  const id = route.query.id as string;
  const status = route.query.status as string;

  if (id) {
    recordId.value = id;
    isUpdate.value = true;
    isReturned.value = status === 'returned';

    // 加载事件详情
    try {
      const detail = await queryEventById({ id });
      if (detail) {
        await setFieldsValue(detail);
      }
    } catch (e) {
      console.error('加载事件详情失败', e);
      createMessage.error('加载事件详情失败');
    }
  }

  // 加载分类树
  try {
    const categoryTree = await getCategoryTree();
    updateSchema({
      field: 'categoryId',
      componentProps: {
        treeData: categoryTree,
      },
    });
  } catch (e) {
    console.error('加载分类树失败', e);
  }
});

/**
 * 返回列表页
 */
function handleBack() {
  router.push('/adverse/event');
}

/**
 * 保存草稿
 */
async function handleSaveDraft() {
  try {
    loading.value = true;
    const values = await validate();
    if (unref(isUpdate)) {
      values.id = recordId.value;
    }
    await saveEventDraft(values, unref(isUpdate));
    createMessage.success('草稿保存成功');
    handleBack();
  } catch (e) {
    console.error('保存草稿失败', e);
  } finally {
    loading.value = false;
  }
}

/**
 * 提交事件
 */
async function handleSubmit() {
  try {
    loading.value = true;
    const values = await validate();

    if (unref(isReturned)) {
      // 重新提交
      await resubmitEvent({ ...values, id: recordId.value });
      createMessage.success('重新提交成功');
    } else {
      // 正常提交
      if (unref(isUpdate)) {
        values.id = recordId.value;
      }
      await submitEvent(values);
      createMessage.success('提交成功');
    }

    handleBack();
  } catch (e) {
    console.error('提交失败', e);
  } finally {
    loading.value = false;
  }
}
</script>
