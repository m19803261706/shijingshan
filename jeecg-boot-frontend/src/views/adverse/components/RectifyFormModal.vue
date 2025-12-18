<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'填写整改'"
    :width="800"
    @ok="handleSubmit"
    :okText="submitType === 'draft' ? '保存草稿' : '提交整改'"
  >
    <!-- 整改要求展示 -->
    <a-card title="整改要求" size="small" class="mb-4" v-if="eventData">
      <a-descriptions bordered :column="2" size="small">
        <a-descriptions-item label="事件编号">{{ eventData.eventCode }}</a-descriptions-item>
        <a-descriptions-item label="事件标题">{{ eventData.title }}</a-descriptions-item>
        <a-descriptions-item label="整改期限">
          <span :class="isOverdue ? 'text-red' : ''">
            {{ eventData.deadline }}
            <a-tag v-if="isOverdue" color="red">已逾期</a-tag>
          </span>
        </a-descriptions-item>
        <a-descriptions-item label="整改次数">第 {{ (eventData.rectifyCount || 0) + 1 }} 次</a-descriptions-item>
        <a-descriptions-item label="整改要求" :span="2">
          <div style="white-space: pre-wrap;">{{ eventData.rectifyRequirement || '无具体要求' }}</div>
        </a-descriptions-item>
      </a-descriptions>
    </a-card>

    <!-- 整改表单 -->
    <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
      <a-form-item label="整改措施" name="measures" required>
        <a-textarea
          v-model:value="formState.measures"
          :rows="5"
          placeholder="请详细描述采取的整改措施"
        />
      </a-form-item>

      <a-form-item label="整改结果说明" name="result">
        <a-textarea
          v-model:value="formState.result"
          :rows="3"
          placeholder="请说明整改后的效果和结果"
        />
      </a-form-item>

      <a-form-item label="附件" name="attachments">
        <JUpload v-model:value="formState.attachments" :maxCount="5" text="上传整改证明材料" />
      </a-form-item>
    </a-form>

    <!-- 底部操作按钮 -->
    <template #footer>
      <a-button @click="closeModal">取消</a-button>
      <a-button type="default" @click="handleSaveDraft" :loading="loading">保存草稿</a-button>
      <a-button type="primary" @click="handleSubmitRectify" :loading="loading">提交整改</a-button>
    </template>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 整改表单弹窗
 * @description 填写整改措施和结果
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref, reactive, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { saveRectifyDraft, submitRectify, getRectifyDetail } from '/@/api/adverse/rectify';
import JUpload from '/@/components/Form/src/jeecg/components/JUpload.vue';
import dayjs from 'dayjs';

const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 表单引用
const formRef = ref();
// 事件数据
const eventData = ref<Recordable | null>(null);
// 加载状态
const loading = ref(false);
// 提交类型
const submitType = ref<'draft' | 'submit'>('submit');

// 表单状态
const formState = reactive({
  id: '',
  eventId: '',
  measures: '',
  result: '',
  attachments: '',
});

// 表单验证规则
const rules = {
  measures: [{ required: true, message: '请填写整改措施' }],
};

// 是否已逾期
const isOverdue = computed(() => {
  if (!eventData.value?.deadline) return false;
  return dayjs().isAfter(dayjs(eventData.value.deadline), 'day');
});

// 注册弹窗
const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
  // 重置表单
  formState.id = '';
  formState.eventId = '';
  formState.measures = '';
  formState.result = '';
  formState.attachments = '';

  if (data?.record) {
    eventData.value = data.record;
    formState.eventId = data.record.id;

    // 如果有草稿，加载草稿内容
    if (data.record.rectifyDraftId) {
      try {
        const detail = await getRectifyDetail({ id: data.record.rectifyDraftId });
        if (detail) {
          formState.id = detail.id;
          formState.measures = detail.measures || '';
          formState.result = detail.result || '';
          formState.attachments = detail.attachments || '';
        }
      } catch (e) {
        console.error('加载草稿失败', e);
      }
    }
  }
});

/**
 * 保存草稿
 */
async function handleSaveDraft() {
  try {
    loading.value = true;
    submitType.value = 'draft';

    await saveRectifyDraft({
      id: formState.id || undefined,
      eventId: formState.eventId,
      measures: formState.measures,
      result: formState.result,
      attachments: formState.attachments,
    });

    createMessage.success('草稿保存成功');
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
}

/**
 * 提交整改
 */
async function handleSubmitRectify() {
  try {
    await formRef.value?.validate();

    loading.value = true;
    submitType.value = 'submit';

    await submitRectify({
      id: formState.id || undefined,
      eventId: formState.eventId,
      measures: formState.measures,
      result: formState.result,
      attachments: formState.attachments,
    });

    createMessage.success('整改提交成功');
    closeModal();
    emit('success');
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
}

/**
 * 提交（兼容 ok 事件）
 */
function handleSubmit() {
  if (submitType.value === 'draft') {
    handleSaveDraft();
  } else {
    handleSubmitRectify();
  }
}
</script>

<style scoped>
.mb-4 {
  margin-bottom: 16px;
}

.text-red {
  color: #ff4d4f;
}
</style>
