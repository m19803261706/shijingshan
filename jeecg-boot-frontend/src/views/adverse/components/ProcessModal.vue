<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="modalTitle"
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
      <!-- 处理模式 -->
      <template v-if="mode === 'process'">
        <a-form-item label="处理方式" name="action">
          <a-radio-group v-model:value="formState.action">
            <a-radio value="require_rectify">
              <span style="color: #fa8c16;">要求整改</span>
            </a-radio>
            <a-radio value="close">
              <span style="color: #52c41a;">直接结案</span>
            </a-radio>
          </a-radio-group>
        </a-form-item>

        <a-form-item v-if="formState.action === 'require_rectify'" label="整改期限" name="deadline">
          <a-date-picker
            v-model:value="formState.deadline"
            format="YYYY-MM-DD"
            valueFormat="YYYY-MM-DD"
            placeholder="请选择整改期限"
            style="width: 100%;"
          />
        </a-form-item>
      </template>

      <!-- 确认模式 -->
      <template v-if="mode === 'confirm'">
        <a-form-item label="确认结果" name="action">
          <a-radio-group v-model:value="formState.action">
            <a-radio value="confirm_rectify">
              <span style="color: #52c41a;">确认整改</span>
            </a-radio>
            <a-radio value="reject_rectify">
              <span style="color: #ff4d4f;">退回整改</span>
            </a-radio>
          </a-radio-group>
        </a-form-item>
      </template>

      <a-form-item label="处理意见" name="comment">
        <a-textarea
          v-model:value="formState.comment"
          :rows="4"
          :placeholder="getCommentPlaceholder()"
        />
      </a-form-item>
    </a-form>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 处理弹窗
 * @description 职能科室处理操作弹窗，支持要求整改、直接结案、确认整改、退回整改
 * @author TC Agent
 * @since 2025-12-18
 */
import { ref, reactive, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { requireRectify, closeEvent, confirmRectify, rejectRectify } from '/@/api/adverse/process';

const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 表单引用
const formRef = ref();
// 事件数据
const eventData = ref<Recordable | null>(null);
// 模式：process（处理）或 confirm（确认）
const mode = ref<'process' | 'confirm'>('process');

// 弹窗标题
const modalTitle = computed(() => {
  return mode.value === 'process' ? '处理事件' : '确认整改';
});

// 表单状态
const formState = reactive({
  action: 'require_rectify',
  comment: '',
  deadline: null as string | null,
});

// 表单验证规则
const rules = computed(() => {
  const baseRules: any = {
    action: [{ required: true, message: '请选择操作' }],
  };

  // 退回整改时必须填写意见
  if (formState.action === 'reject_rectify') {
    baseRules.comment = [{ required: true, message: '退回时必须填写原因' }];
  }

  // 要求整改时必须设置期限
  if (formState.action === 'require_rectify') {
    baseRules.deadline = [{ required: true, message: '请设置整改期限' }];
  }

  return baseRules;
});

// 注册弹窗
const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
  // 设置模式
  mode.value = data?.mode || 'process';

  // 重置表单
  formState.action = mode.value === 'process' ? 'require_rectify' : 'confirm_rectify';
  formState.comment = '';
  formState.deadline = null;

  if (data?.record) {
    eventData.value = data.record;
  }
});

/**
 * 获取意见输入框占位符
 */
function getCommentPlaceholder() {
  switch (formState.action) {
    case 'require_rectify':
      return '请填写整改要求（可选）';
    case 'close':
      return '请填写结案意见（可选）';
    case 'confirm_rectify':
      return '请填写确认意见（可选）';
    case 'reject_rectify':
      return '请填写退回原因（必填）';
    default:
      return '请填写处理意见';
  }
}

/**
 * 提交处理
 */
async function handleSubmit() {
  try {
    await formRef.value?.validate();

    setModalProps({ confirmLoading: true });

    const params = {
      id: eventData.value?.id,
      comment: formState.comment,
      deadline: formState.deadline,
    };

    switch (formState.action) {
      case 'require_rectify':
        await requireRectify(params);
        createMessage.success('已发送整改要求');
        break;
      case 'close':
        await closeEvent(params);
        createMessage.success('事件已结案');
        break;
      case 'confirm_rectify':
        await confirmRectify(params);
        createMessage.success('整改已确认');
        break;
      case 'reject_rectify':
        await rejectRectify(params);
        createMessage.success('整改已退回');
        break;
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
