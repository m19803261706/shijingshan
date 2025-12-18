<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="isUpdate ? '编辑分类' : '新增分类'"
    :width="600"
    @ok="handleSubmit"
    :confirmLoading="loading"
  >
    <a-form
      ref="formRef"
      :model="formState"
      :rules="rules"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 18 }"
    >
      <a-form-item label="父级分类">
        <a-input v-model:value="formState.parentName" disabled placeholder="顶级分类" />
      </a-form-item>

      <a-form-item label="分类名称" name="name" required>
        <a-input v-model:value="formState.name" placeholder="请输入分类名称" />
      </a-form-item>

      <a-form-item label="分类编码" name="code" required>
        <a-input v-model:value="formState.code" placeholder="请输入分类编码（英文或数字）" />
      </a-form-item>

      <a-form-item label="排序" name="sortOrder">
        <a-input-number
          v-model:value="formState.sortOrder"
          :min="0"
          :max="9999"
          placeholder="请输入排序号"
          style="width: 100%"
        />
      </a-form-item>

      <a-form-item label="状态" name="status">
        <a-radio-group v-model:value="formState.status">
          <a-radio :value="1">启用</a-radio>
          <a-radio :value="0">禁用</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="备注" name="remark">
        <a-textarea
          v-model:value="formState.remark"
          placeholder="请输入备注信息"
          :rows="3"
        />
      </a-form-item>
    </a-form>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 分类表单弹窗
 * @description 新增和编辑分类
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref, reactive } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { saveCategory, editCategory } from '/@/api/adverse/category';

const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 表单引用
const formRef = ref();
// 是否更新模式
const isUpdate = ref(false);
// 加载状态
const loading = ref(false);

// 表单状态
const formState = reactive({
  id: '',
  parentId: '',
  parentName: '',
  name: '',
  code: '',
  sortOrder: 0,
  status: 1,
  remark: '',
});

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入分类名称' },
    { max: 50, message: '分类名称不能超过50个字符' },
  ],
  code: [
    { required: true, message: '请输入分类编码' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '编码只能包含英文、数字和下划线' },
    { max: 50, message: '分类编码不能超过50个字符' },
  ],
};

// 注册弹窗
const [registerModal, { closeModal }] = useModalInner(async (data) => {
  // 重置表单
  formRef.value?.resetFields();

  isUpdate.value = !!data?.isUpdate;

  if (data?.record) {
    // 填充表单数据
    formState.id = data.record.id || '';
    formState.parentId = data.record.parentId || '';
    formState.parentName = data.record.parentName || '顶级分类';
    formState.name = data.record.name || '';
    formState.code = data.record.code || '';
    formState.sortOrder = data.record.sortOrder ?? 0;
    formState.status = data.record.status ?? 1;
    formState.remark = data.record.remark || '';
  } else {
    // 新增时清空
    formState.id = '';
    formState.parentId = '';
    formState.parentName = '顶级分类';
    formState.name = '';
    formState.code = '';
    formState.sortOrder = 0;
    formState.status = 1;
    formState.remark = '';
  }
});

/**
 * 提交表单
 */
async function handleSubmit() {
  try {
    await formRef.value?.validate();

    loading.value = true;

    const params = {
      id: formState.id || undefined,
      parentId: formState.parentId || undefined,
      name: formState.name,
      code: formState.code,
      sortOrder: formState.sortOrder,
      status: formState.status,
      remark: formState.remark,
    };

    if (isUpdate.value) {
      await editCategory(params);
      createMessage.success('编辑成功');
    } else {
      await saveCategory(params);
      createMessage.success('新增成功');
    }

    closeModal();
    emit('success');
  } catch (e) {
    console.error('提交失败', e);
  } finally {
    loading.value = false;
  }
}
</script>
