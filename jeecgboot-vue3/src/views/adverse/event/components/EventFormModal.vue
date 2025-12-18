<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :width="800"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <BasicForm @register="registerForm" />
    <template #footer>
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="default" @click="handleSaveDraft" :loading="loading" v-if="!isDetail">
        保存草稿
      </a-button>
      <a-button type="primary" @click="handleSubmit" :loading="loading" v-if="!isDetail">
        {{ isReturned ? '重新提交' : '提交' }}
      </a-button>
    </template>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 事件表单弹窗组件
 * @description 用于新增、编辑、查看事件
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form';
import { eventFormSchema } from '../event.data';
import { saveEventDraft, submitEvent, resubmitEvent, getCategoryTree } from '/@/api/adverse/event';
import { useMessage } from '/@/hooks/web/useMessage';

// 定义事件
const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 状态
const isUpdate = ref(false);
const isDetail = ref(false);
const isReturned = ref(false);
const loading = ref(false);
const recordData = ref<any>({});

// 表单配置
const [registerForm, { resetFields, setFieldsValue, validate, updateSchema }] = useForm({
  labelWidth: 100,
  schemas: eventFormSchema,
  showActionButtonGroup: false,
});

// 弹窗配置
const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  // 重置表单
  await resetFields();

  isUpdate.value = !!data?.isUpdate;
  isDetail.value = !!data?.isDetail;
  isReturned.value = data?.record?.status === 'returned';
  recordData.value = data?.record || {};

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

  // 如果是编辑或查看，填充数据
  if (unref(isUpdate) && data?.record) {
    await setFieldsValue({
      ...data.record,
    });
  }

  // 设置表单只读模式
  if (unref(isDetail)) {
    setModalProps({ showOkBtn: false });
  } else {
    setModalProps({ showOkBtn: false }); // 使用自定义 footer
  }
});

// 计算标题
const getTitle = computed(() => {
  if (unref(isDetail)) {
    return '查看事件详情';
  }
  if (unref(isReturned)) {
    return '重新提交事件';
  }
  return unref(isUpdate) ? '编辑事件' : '新增事件上报';
});

/**
 * 保存草稿
 */
async function handleSaveDraft() {
  try {
    loading.value = true;
    const values = await validate();
    await saveEventDraft(values, unref(isUpdate));
    createMessage.success('草稿保存成功');
    emit('success');
    closeModal();
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
      await resubmitEvent({ ...values, id: recordData.value.id });
      createMessage.success('重新提交成功');
    } else {
      // 正常提交
      await submitEvent(values);
      createMessage.success('提交成功');
    }

    emit('success');
    closeModal();
  } catch (e) {
    console.error('提交失败', e);
  } finally {
    loading.value = false;
  }
}

/**
 * 取消
 */
function handleCancel() {
  closeModal();
}
</script>
