<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'处理输血使用不良事件报告'"
    :width="700"
    @ok="handleSubmit"
  >
    <div v-if="reportData" class="mb-4">
      <a-descriptions bordered :column="2" size="small">
        <a-descriptions-item label="报告编号">{{ reportData.reportCode }}</a-descriptions-item>
        <a-descriptions-item label="患者姓名">{{ reportData.patientName }}</a-descriptions-item>
        <a-descriptions-item label="严重程度">
          <a-tag :color="getSeverityColor(reportData.severityLevel)">
            {{ getSeverityLabel(reportData.severityLevel) }}
          </a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="事件等级">
          <a-tag :color="getEventLevelColor(reportData.eventLevel)">
            {{ getEventLevelLabel(reportData.eventLevel) }}
          </a-tag>
        </a-descriptions-item>
        <a-descriptions-item label="不良事件名称" :span="2">
          {{ getEventNameLabel(reportData.eventName) }}
        </a-descriptions-item>
        <a-descriptions-item label="涉及科室">{{ reportData.involvedDept }}</a-descriptions-item>
        <a-descriptions-item label="上报人">{{ reportData.createBy_dictText || reportData.createBy }}</a-descriptions-item>
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
      </template>
    </a-form>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 输血使用不良事件处理弹窗
 * @description 输血科处理操作弹窗，支持直接结案和要求整改
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/86
 */
import { ref, reactive, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { bloodProcessClose, bloodProcessRequireRectify } from '/@/api/adverse/bloodProcess';
import { CheckCircleOutlined, EditOutlined } from '@ant-design/icons-vue';
import dayjs, { Dayjs } from 'dayjs';
import {
  eventNameOptions,
  severityLevelOptions,
  eventLevelOptions,
} from '../../blood/blood.data';

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

  if (data?.record) {
    reportData.value = data.record;
  }
});

/**
 * 获取事件名称标签
 */
function getEventNameLabel(name: string): string {
  const item = eventNameOptions.find((i) => i.value === name);
  return item?.label || name || '-';
}

/**
 * 获取严重程度标签
 */
function getSeverityLabel(level: string): string {
  const item = severityLevelOptions.find((i) => i.value === level);
  return item?.label || level || '-';
}

/**
 * 获取严重程度颜色
 */
function getSeverityColor(level: string): string {
  const item = severityLevelOptions.find((i) => i.value === level);
  return item?.color || 'default';
}

/**
 * 获取事件等级标签
 */
function getEventLevelLabel(level: string): string {
  const item = eventLevelOptions.find((i) => i.value === level);
  return item?.label || level || '-';
}

/**
 * 获取事件等级颜色
 */
function getEventLevelColor(level: string): string {
  const item = eventLevelOptions.find((i) => i.value === level);
  return item?.color || 'default';
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
      await bloodProcessClose({
        id: reportData.value?.id,
        comment: formState.comment,
      });
      createMessage.success('结案成功');
    } else {
      // 要求整改
      await bloodProcessRequireRectify({
        id: reportData.value?.id,
        requirement: formState.requirement,
        deadline: formState.deadline ? formState.deadline.format('YYYY-MM-DD') : '',
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
