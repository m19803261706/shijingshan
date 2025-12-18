<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'审核事件'"
    :width="600"
    @ok="handleSubmit"
  >
    <div v-if="eventData" class="mb-4">
      <a-descriptions bordered :column="2" size="small">
        <a-descriptions-item label="事件编号">{{ eventData.eventCode }}</a-descriptions-item>
        <a-descriptions-item label="事件标题">{{ eventData.title }}</a-descriptions-item>
        <a-descriptions-item label="事件分类">{{ eventData.categoryId_dictText }}</a-descriptions-item>
        <a-descriptions-item label="事件级别">{{ eventData.level }}</a-descriptions-item>
      </a-descriptions>
    </div>

    <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
      <a-form-item label="审核结果" name="result">
        <a-radio-group v-model:value="formState.result">
          <a-radio value="approve">
            <span style="color: #52c41a;">通过</span>
          </a-radio>
          <a-radio value="reject">
            <span style="color: #ff4d4f;">退回</span>
          </a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="审核意见" name="comment">
        <a-textarea
          v-model:value="formState.comment"
          :rows="4"
          :placeholder="formState.result === 'reject' ? '请填写退回原因（必填）' : '请填写审核意见（可选）'"
        />
      </a-form-item>
    </a-form>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 审核弹窗
 * @description 科室审核操作弹窗，支持通过和退回
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref, reactive, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { approveEvent, rejectEvent } from '/@/api/adverse/audit';

const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 表单引用
const formRef = ref();
// 事件数据
const eventData = ref<Recordable | null>(null);

// 表单状态
const formState = reactive({
  result: 'approve',
  comment: '',
});

// 表单验证规则
const rules = computed(() => ({
  result: [{ required: true, message: '请选择审核结果' }],
  comment: formState.result === 'reject' ? [{ required: true, message: '退回时必须填写原因' }] : [],
}));

// 注册弹窗
const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
  // 重置表单
  formState.result = 'approve';
  formState.comment = '';

  if (data?.record) {
    eventData.value = data.record;
  }
});

/**
 * 提交审核
 */
async function handleSubmit() {
  try {
    await formRef.value?.validate();

    setModalProps({ confirmLoading: true });

    const params = {
      id: eventData.value?.id,
      comment: formState.comment,
    };

    if (formState.result === 'approve') {
      await approveEvent(params);
      createMessage.success('审核通过');
    } else {
      await rejectEvent(params);
      createMessage.success('已退回');
    }

    closeModal();
    emit('success');
  } catch (e) {
    console.error(e);
  } finally {
    setModalProps({ confirmLoading: false });
  }
}
</script>

<style scoped>
.mb-4 {
  margin-bottom: 16px;
}
</style>
