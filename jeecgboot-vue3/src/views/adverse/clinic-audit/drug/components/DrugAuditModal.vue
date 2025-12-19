<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'审核药品不良反应报告'"
    :width="650"
    @ok="handleSubmit"
  >
    <div v-if="reportData" class="mb-4">
      <a-descriptions bordered :column="2" size="small">
        <a-descriptions-item label="报告编号">{{ reportData.reportCode }}</a-descriptions-item>
        <a-descriptions-item label="患者姓名">{{ reportData.patientName }}</a-descriptions-item>
        <a-descriptions-item label="报告类型">
          <a-tag :color="getReportTypeColor(reportData.reportType)">
            {{ getReportTypeLabel(reportData.reportType) }}
          </a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="严重程度">
          <a-tag :color="getSeverityColor(reportData.severityType)">
            {{ getSeverityLabel(reportData.severityType) }}
          </a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="不良反应名称" :span="2">
          {{ reportData.reactionName }}
        </a-descriptions-item>
        <a-descriptions-item label="发生时间">{{ reportData.reactionTime }}</a-descriptions-item>
        <a-descriptions-item label="上报人">{{ reportData.createBy }}</a-descriptions-item>
      </a-descriptions>
    </div>

    <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
      <a-form-item label="审核结果" name="result">
        <a-radio-group v-model:value="formState.result" button-style="solid">
          <a-radio-button value="pass">
            <CheckCircleOutlined />
            通过
          </a-radio-button>
          <a-radio-button value="reject">
            <CloseCircleOutlined />
            退回
          </a-radio-button>
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
 * 药品不良反应审核弹窗
 * @description 临床科室审核操作弹窗，支持通过和退回
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/46
 */
import { ref, reactive, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { drugAuditPass, drugAuditReject } from '/@/api/adverse/drugAudit';
import { CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons-vue';

const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 表单引用
const formRef = ref();
// 报告数据
const reportData = ref<Recordable | null>(null);

// 表单状态
const formState = reactive({
  result: 'pass',
  comment: '',
});

// 表单验证规则
const rules = computed(() => ({
  result: [{ required: true, message: '请选择审核结果' }],
  comment: formState.result === 'reject' ? [{ required: true, message: '退回时必须填写退回原因' }] : [],
}));

// 注册弹窗
const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
  // 重置表单
  formState.result = 'pass';
  formState.comment = '';

  if (data?.record) {
    reportData.value = data.record;
  }
});

/**
 * 获取报告类型标签
 */
function getReportTypeLabel(type: string): string {
  const map: Record<string, string> = {
    first: '首次报告',
    follow_up: '跟踪报告',
  };
  return map[type] || type;
}

/**
 * 获取报告类型颜色
 */
function getReportTypeColor(type: string): string {
  const map: Record<string, string> = {
    first: 'processing',
    follow_up: 'warning',
  };
  return map[type] || 'default';
}

/**
 * 获取严重程度标签
 */
function getSeverityLabel(severity: string): string {
  const map: Record<string, string> = {
    new: '新的',
    serious: '严重',
    general: '一般',
  };
  return map[severity] || severity;
}

/**
 * 获取严重程度颜色
 */
function getSeverityColor(severity: string): string {
  const map: Record<string, string> = {
    new: 'blue',
    serious: 'red',
    general: 'default',
  };
  return map[severity] || 'default';
}

/**
 * 提交审核
 */
async function handleSubmit() {
  try {
    await formRef.value?.validate();

    setModalProps({ confirmLoading: true });

    const params = {
      id: reportData.value?.id,
      comment: formState.comment,
    };

    if (formState.result === 'pass') {
      await drugAuditPass(params);
      createMessage.success('审核通过');
    } else {
      await drugAuditReject(params as { id: string; comment: string });
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
