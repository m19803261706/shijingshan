<!--
 * 常用并用药品配置 - 编辑弹窗
 * <p>
 * 用于新增和编辑常用并用药品信息
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
    :width="600"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 常用并用药品编辑弹窗组件
 */
import { ref, computed, unref } from 'vue';
import { BasicModal, useModalInner } from '@/components/Modal';
import { BasicForm, useForm } from '@/components/Form';
import { useMessage } from '@/hooks/web/useMessage';
import { formSchema } from './concomitant.data';
import { addConcomitant, editConcomitant } from '@/api/adverse/concomitant';

// 组件名称
defineOptions({ name: 'ConcomitantModal' });

// 事件定义
const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 是否编辑模式
const isUpdate = ref(false);

// 弹窗标题
const title = computed(() => (unref(isUpdate) ? '编辑并用药品配置' : '新增并用药品配置'));

// 表单配置
const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
  labelWidth: 100,
  schemas: formSchema,
  showActionButtonGroup: false,
});

// 弹窗配置
const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
  // 重置表单
  await resetFields();
  isUpdate.value = !!data?.isUpdate;

  // 编辑模式时填充数据
  if (unref(isUpdate) && data?.record) {
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
    // 表单验证
    const values = await validate();
    setModalProps({ confirmLoading: true });

    // 调用新增或编辑接口
    if (unref(isUpdate)) {
      await editConcomitant(values);
      createMessage.success('编辑成功');
    } else {
      await addConcomitant(values);
      createMessage.success('新增成功');
    }

    // 关闭弹窗并通知列表刷新
    closeModal();
    emit('success');
  } catch (error) {
    console.error('提交失败:', error);
  } finally {
    setModalProps({ confirmLoading: false });
  }
}
</script>
