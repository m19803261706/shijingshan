<!--
 * 常用医疗器械配置 - 编辑弹窗
 * @author TC Agent
 * @since 2025-12-19
-->
<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="title"
    :width="700"
    @ok="handleSubmit"
    :confirmLoading="loading"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 常用医疗器械配置编辑弹窗
 */
import { ref, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { BasicForm, useForm } from '/@/components/Form';
import { useMessage } from '/@/hooks/web/useMessage';
import { formSchema } from './config.data';
import { addDeviceConfig, editDeviceConfig } from '/@/api/adverse/deviceConfig';

// 事件
const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 是否更新
const isUpdate = ref(false);
// 加载状态
const loading = ref(false);

// 弹窗标题
const title = computed(() => (isUpdate.value ? '编辑常用器械配置' : '新增常用器械配置'));

// 表单配置
const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
  labelWidth: 100,
  schemas: formSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 24 },
});

// 弹窗配置
const [registerModal, { closeModal }] = useModalInner(async (data) => {
  // 重置表单
  await resetFields();

  isUpdate.value = !!data?.isUpdate;

  if (isUpdate.value && data.record) {
    setFieldsValue({
      ...data.record,
    });
  }
});

/**
 * 提交表单
 */
async function handleSubmit() {
  try {
    const values = await validate();
    loading.value = true;

    if (isUpdate.value) {
      await editDeviceConfig(values);
      createMessage.success('编辑成功');
    } else {
      await addDeviceConfig(values);
      createMessage.success('新增成功');
    }

    closeModal();
    emit('success');
  } catch (error) {
    console.error('提交失败:', error);
  } finally {
    loading.value = false;
  }
}
</script>
