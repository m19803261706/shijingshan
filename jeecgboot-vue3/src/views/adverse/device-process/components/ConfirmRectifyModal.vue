<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'确认整改结果'"
    :width="800"
    @ok="handleSubmit"
  >
    <!-- 整改信息 -->
    <div v-if="rectifyData" class="mb-4">
      <a-descriptions bordered :column="2" size="small" title="整改信息">
        <a-descriptions-item label="整改要求" :span="2">
          {{ rectifyData.requirement }}
        </a-descriptions-item>
        <a-descriptions-item label="整改期限">{{ rectifyData.deadline }}</a-descriptions-item>
        <a-descriptions-item label="下发时间">{{ rectifyData.requireTime }}</a-descriptions-item>
        <a-descriptions-item label="整改措施" :span="2">
          {{ rectifyData.measures || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="完成情况" :span="2">
          {{ rectifyData.completion || '-' }}
        </a-descriptions-item>
        <a-descriptions-item label="提交人">{{ rectifyData.submitUserName }}</a-descriptions-item>
        <a-descriptions-item label="提交时间">{{ rectifyData.submitTime }}</a-descriptions-item>
      </a-descriptions>
    </div>

    <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
      <a-form-item label="确认结果" name="result">
        <a-radio-group v-model:value="formState.result" button-style="solid">
          <a-radio-button value="approve">
            <CheckCircleOutlined />
            通过
          </a-radio-button>
          <a-radio-button value="reject">
            <CloseCircleOutlined />
            退回
          </a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="确认意见" name="comment">
        <a-textarea
          v-model:value="formState.comment"
          :rows="4"
          :placeholder="formState.result === 'reject' ? '请填写退回原因（必填）' : '请填写确认意见（可选）'"
        />
      </a-form-item>
    </a-form>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 确认整改弹窗
 * @description 器械科确认整改结果，支持通过和退回
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #70
 */
import { ref, reactive, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { getDeviceRectifyHistory, deviceProcessConfirmRectify, deviceProcessRejectRectify } from '/@/api/adverse/deviceProcess';
import { CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons-vue';

const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 表单引用
const formRef = ref();
// 报告数据
const reportData = ref<Recordable | null>(null);
// 整改数据
const rectifyData = ref<Recordable | null>(null);

// 表单状态
const formState = reactive({
  result: 'approve',
  comment: '',
});

// 表单验证规则
const rules = computed(() => ({
  result: [{ required: true, message: '请选择确认结果' }],
  comment: formState.result === 'reject' ? [{ required: true, message: '退回时必须填写退回原因' }] : [],
}));

// 注册弹窗
const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
  // 重置表单
  formState.result = 'approve';
  formState.comment = '';

  if (data?.record) {
    reportData.value = data.record;
    // 加载最新整改记录
    const rectifyList = await getDeviceRectifyHistory(data.record.id);
    if (rectifyList && rectifyList.length > 0) {
      // 获取最新的已提交整改记录
      rectifyData.value = rectifyList.find((item: Recordable) => item.status === 'submitted') || rectifyList[0];
    }
  }
});

/**
 * 提交确认
 */
async function handleSubmit() {
  try {
    await formRef.value?.validate();

    if (!rectifyData.value) {
      createMessage.error('未找到整改记录');
      return;
    }

    setModalProps({ confirmLoading: true });

    if (formState.result === 'approve') {
      await deviceProcessConfirmRectify({
        rectifyId: rectifyData.value.id,
        comment: formState.comment,
      });
      createMessage.success('整改确认通过，报告已结案');
    } else {
      await deviceProcessRejectRectify({
        rectifyId: rectifyData.value.id,
        comment: formState.comment,
      });
      createMessage.success('整改已退回');
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
