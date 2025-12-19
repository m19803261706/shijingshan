<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'处理药品不良反应报告'"
    :width="700"
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
        <a-descriptions-item label="上报科室">{{ reportData.departmentId_dictText }}</a-descriptions-item>
      </a-descriptions>
    </div>

    <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
      <a-form-item label="处理方式" name="processType">
        <a-radio-group v-model:value="formState.processType" button-style="solid">
          <a-radio-button value="close">
            <CheckCircleOutlined />
            直接结案
          </a-radio-button>
          <a-radio-button value="rectify">
            <EditOutlined />
            要求整改
          </a-radio-button>
        </a-radio-group>
      </a-form-item>

      <!-- 直接结案 -->
      <template v-if="formState.processType === 'close'">
        <a-form-item label="结案意见" name="comment">
          <a-textarea
            v-model:value="formState.comment"
            :rows="4"
            placeholder="请填写结案意见（可选）"
          />
        </a-form-item>
      </template>

      <!-- 要求整改 -->
      <template v-if="formState.processType === 'rectify'">
        <a-form-item label="整改要求" name="requirement">
          <a-textarea
            v-model:value="formState.requirement"
            :rows="4"
            placeholder="请填写整改要求（必填）"
          />
        </a-form-item>
        <a-form-item label="整改期限" name="deadline">
          <a-date-picker
            v-model:value="formState.deadline"
            style="width: 100%"
            :disabledDate="disabledDate"
            placeholder="请选择整改期限"
          />
        </a-form-item>
        <a-form-item label="备注" name="rectifyComment">
          <a-textarea
            v-model:value="formState.rectifyComment"
            :rows="2"
            placeholder="请填写备注（可选）"
          />
        </a-form-item>
      </template>
    </a-form>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 药品不良反应处理弹窗
 * @description 药剂科处理操作弹窗，支持直接结案和要求整改
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/51
 */
import { ref, reactive, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { drugProcessClose, drugProcessRequireRectify } from '/@/api/adverse/drugProcess';
import { CheckCircleOutlined, EditOutlined } from '@ant-design/icons-vue';
import dayjs, { Dayjs } from 'dayjs';

const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 表单引用
const formRef = ref();
// 报告数据
const reportData = ref<Recordable | null>(null);

// 表单状态
const formState = reactive({
  processType: 'close',
  comment: '',
  requirement: '',
  deadline: null as Dayjs | null,
  rectifyComment: '',
});

// 表单验证规则
const rules = computed(() => ({
  processType: [{ required: true, message: '请选择处理方式' }],
  requirement: formState.processType === 'rectify' ? [{ required: true, message: '请填写整改要求' }] : [],
  deadline: formState.processType === 'rectify' ? [{ required: true, message: '请选择整改期限' }] : [],
}));

// 禁用今天之前的日期
const disabledDate = (current: Dayjs) => {
  return current && current < dayjs().startOf('day');
};

// 注册弹窗
const [registerModal, { closeModal, setModalProps }] = useModalInner(async (data) => {
  // 重置表单
  formState.processType = 'close';
  formState.comment = '';
  formState.requirement = '';
  formState.deadline = null;
  formState.rectifyComment = '';

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
 * 提交处理
 */
async function handleSubmit() {
  try {
    await formRef.value?.validate();

    setModalProps({ confirmLoading: true });

    if (formState.processType === 'close') {
      // 直接结案
      await drugProcessClose({
        id: reportData.value?.id,
        comment: formState.comment,
      });
      createMessage.success('结案成功');
    } else {
      // 要求整改
      await drugProcessRequireRectify({
        id: reportData.value?.id,
        requirement: formState.requirement,
        deadline: formState.deadline ? formState.deadline.format('YYYY-MM-DD') : '',
        comment: formState.rectifyComment,
      });
      createMessage.success('整改要求已下发');
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
