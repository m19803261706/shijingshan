<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="填写整改"
    :width="800"
    @ok="handleSubmit"
    okText="提交整改"
    :confirmLoading="loading"
  >
    <!-- 报告信息展示 -->
    <a-card title="报告信息" size="small" class="mb-4" v-if="reportData">
      <a-descriptions bordered :column="2" size="small">
        <a-descriptions-item label="报告编号">{{ reportData.reportCode }}</a-descriptions-item>
        <a-descriptions-item label="患者姓名">{{ reportData.patientName }}</a-descriptions-item>
        <a-descriptions-item label="不良反应名称">{{ reportData.reactionName }}</a-descriptions-item>
        <a-descriptions-item label="报告类型">
          <a-tag :color="getReportTypeColor(reportData.reportType)">
            {{ getReportTypeText(reportData.reportType) }}
          </a-tag>
        </a-descriptions-item>
      </a-descriptions>
    </a-card>

    <!-- 整改要求展示 -->
    <a-card title="整改要求" size="small" class="mb-4" v-if="rectifyData">
      <a-descriptions bordered :column="2" size="small">
        <a-descriptions-item label="整改期限">
          <span :class="isOverdue ? 'text-red' : ''">
            {{ rectifyData.deadline }}
            <a-tag v-if="isOverdue" color="red" size="small">已逾期</a-tag>
            <a-tag v-else-if="isNearDeadline" color="orange" size="small">即将到期</a-tag>
          </span>
        </a-descriptions-item>
        <a-descriptions-item label="整改次数">第 {{ rectifyData.rectifyRound || 1 }} 次</a-descriptions-item>
        <a-descriptions-item label="整改要求" :span="2">
          <div style="white-space: pre-wrap;">{{ rectifyData.requirement || '请根据药品不良反应情况进行整改' }}</div>
        </a-descriptions-item>
        <!-- 显示上一轮退回原因（新记录通过 prevRectifyId 关联上一轮，退回原因在 requireComment 中） -->
        <a-descriptions-item label="上一轮退回原因" :span="2" v-if="rectifyData.prevRectifyId">
          <a-alert type="warning" :message="rectifyData.requireComment || '未填写退回原因'" show-icon />
        </a-descriptions-item>
      </a-descriptions>
    </a-card>

    <!-- 整改表单 -->
    <a-form ref="formRef" :model="formState" :rules="rules" layout="vertical">
      <a-form-item label="整改措施" name="measures" required>
        <a-textarea
          v-model:value="formState.measures"
          :rows="5"
          placeholder="请详细描述采取的整改措施，包括但不限于：&#10;1. 问题原因分析&#10;2. 采取的纠正措施&#10;3. 预防措施"
          :maxlength="2000"
          showCount
        />
      </a-form-item>

      <a-form-item label="完成情况" name="completion" required>
        <a-textarea
          v-model:value="formState.completion"
          :rows="3"
          placeholder="请说明整改完成情况和效果"
          :maxlength="1000"
          showCount
        />
      </a-form-item>
    </a-form>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 药品不良反应整改表单弹窗
 * @description 临床科室人员填写整改措施并提交
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/55
 */
import { ref, reactive, computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { submitDrugRectify } from '/@/api/adverse/clinicRectify';
import dayjs from 'dayjs';

const emit = defineEmits(['success', 'register']);

const { createMessage } = useMessage();

// 表单引用
const formRef = ref();
// 报告数据
const reportData = ref<Recordable | null>(null);
// 整改记录数据
const rectifyData = ref<Recordable | null>(null);
// 加载状态
const loading = ref(false);

// 表单状态
const formState = reactive({
  rectifyId: '',
  measures: '',
  completion: '',
});

// 表单验证规则
const rules = {
  measures: [{ required: true, message: '请填写整改措施' }],
  completion: [{ required: true, message: '请填写完成情况' }],
};

/**
 * 报告类型映射
 */
const reportTypeMap: Record<string, { label: string; color: string }> = {
  first: { label: '首次报告', color: 'processing' },
  follow_up: { label: '跟踪报告', color: 'warning' },
};

/**
 * 获取报告类型文本
 */
function getReportTypeText(type: string): string {
  return reportTypeMap[type]?.label || type;
}

/**
 * 获取报告类型颜色
 */
function getReportTypeColor(type: string): string {
  return reportTypeMap[type]?.color || 'default';
}

/**
 * 是否已逾期
 */
const isOverdue = computed(() => {
  if (!rectifyData.value?.deadline) return false;
  return dayjs().isAfter(dayjs(rectifyData.value.deadline), 'day');
});

/**
 * 是否即将到期（3天内）
 */
const isNearDeadline = computed(() => {
  if (!rectifyData.value?.deadline) return false;
  const diff = dayjs(rectifyData.value.deadline).diff(dayjs(), 'day');
  return diff >= 0 && diff <= 3;
});

// 注册弹窗
const [registerModal, { closeModal }] = useModalInner(async (data) => {
  // 重置表单
  formState.rectifyId = '';
  formState.measures = '';
  formState.completion = '';
  formRef.value?.resetFields();

  if (data?.record) {
    reportData.value = data.record;
  }

  if (data?.rectify) {
    rectifyData.value = data.rectify;
    formState.rectifyId = data.rectify.id;

    // 如果是退回状态，加载之前填写的内容
    if (data.rectify.status === 'rejected') {
      formState.measures = data.rectify.measures || '';
      formState.completion = data.rectify.completion || '';
    }
  }
});

/**
 * 提交整改
 */
async function handleSubmit() {
  try {
    await formRef.value?.validate();

    if (!formState.rectifyId) {
      createMessage.error('整改记录ID缺失');
      return;
    }

    loading.value = true;

    await submitDrugRectify({
      rectifyId: formState.rectifyId,
      measures: formState.measures,
      completion: formState.completion,
    });

    createMessage.success('整改提交成功，等待审核确认');
    closeModal();
    emit('success');
  } catch (e) {
    console.error('提交整改失败', e);
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.mb-4 {
  margin-bottom: 16px;
}

.text-red {
  color: #ef4444;
}
</style>
