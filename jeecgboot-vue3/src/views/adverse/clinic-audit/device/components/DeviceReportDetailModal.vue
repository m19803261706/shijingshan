<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="title"
    width="900px"
    :footer="null"
  >
    <a-descriptions bordered :column="2" size="small">
      <!-- A. 患者资料 -->
      <a-descriptions-item label="报告编号" :span="2">
        {{ reportData.reportCode }}
      </a-descriptions-item>
      <a-descriptions-item label="患者姓名">
        {{ reportData.patientName }}
      </a-descriptions-item>
      <a-descriptions-item label="年龄">
        {{ reportData.patientAge }}
      </a-descriptions-item>
      <a-descriptions-item label="性别">
        {{ reportData.patientGender === 'M' ? '男' : reportData.patientGender === 'F' ? '女' : '-' }}
      </a-descriptions-item>
      <a-descriptions-item label="预期治疗疾病或作用">
        {{ reportData.patientDisease }}
      </a-descriptions-item>

      <!-- B. 不良事件情况 -->
      <a-descriptions-item label="事件主要表现" :span="2">
        {{ reportData.eventManifestation }}
      </a-descriptions-item>
      <a-descriptions-item label="事件发生日期">
        {{ reportData.eventOccurDate }}
      </a-descriptions-item>
      <a-descriptions-item label="发现或者知悉时间">
        {{ reportData.eventDiscoverDate }}
      </a-descriptions-item>
      <a-descriptions-item label="器械使用场所" :span="2">
        {{ getDeviceUsePlaceText(reportData.deviceUsePlace) }}
        {{ reportData.deviceUsePlaceOther ? `(${reportData.deviceUsePlaceOther})` : '' }}
      </a-descriptions-item>
      <a-descriptions-item label="事件后果" :span="2">
        {{ getEventResultText(reportData) }}
      </a-descriptions-item>
      <a-descriptions-item label="事件陈述" :span="2">
        {{ reportData.eventStatement }}
      </a-descriptions-item>

      <!-- C. 医疗器械情况 -->
      <a-descriptions-item label="产品名称">
        {{ reportData.productName }}
      </a-descriptions-item>
      <a-descriptions-item label="商品名称">
        {{ reportData.tradeName }}
      </a-descriptions-item>
      <a-descriptions-item label="注册证号">
        {{ reportData.registrationNo }}
      </a-descriptions-item>
      <a-descriptions-item label="生产企业名称">
        {{ reportData.manufacturerName }}
      </a-descriptions-item>
      <a-descriptions-item label="型号规格">
        {{ reportData.modelSpec }}
      </a-descriptions-item>
      <a-descriptions-item label="产品批号">
        {{ reportData.productBatch }}
      </a-descriptions-item>
      <a-descriptions-item label="操作人类型">
        {{ getOperatorTypeText(reportData.operatorType) }}
      </a-descriptions-item>
      <a-descriptions-item label="有效期至">
        {{ reportData.validPeriodTo }}
      </a-descriptions-item>
      <a-descriptions-item label="事件发生初步原因分析" :span="2">
        {{ reportData.causeAnalysis }}
      </a-descriptions-item>
      <a-descriptions-item label="事件初步处理情况" :span="2">
        {{ reportData.initialHandling }}
      </a-descriptions-item>

      <!-- D. 不良事件评价 -->
      <a-descriptions-item label="时间先后顺序是否合理">
        {{ reportData.evalTimeSequence === 'yes' ? '是' : '否' }}
      </a-descriptions-item>
      <a-descriptions-item label="是否属于器械可能导致的伤害类型">
        {{ getEvalResultText(reportData.evalHarmType) }}
      </a-descriptions-item>
      <a-descriptions-item label="是否可用其他因素解释" :span="2">
        {{ getEvalResultText(reportData.evalExcludeOther) }}
      </a-descriptions-item>

      <!-- 报告人信息 -->
      <a-descriptions-item label="报告人类型">
        {{ getReporterTypeText(reportData.reporterType) }}
      </a-descriptions-item>
      <a-descriptions-item label="报告人签名">
        {{ reportData.reporterSignature }}
      </a-descriptions-item>
      <a-descriptions-item label="报告日期">
        {{ reportData.reportDate }}
      </a-descriptions-item>
      <a-descriptions-item label="状态">
        <a-tag :color="getStatusColor(reportData.status)">
          {{ getStatusText(reportData.status) }}
        </a-tag>
      </a-descriptions-item>
    </a-descriptions>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 医疗器械不良事件报告详情弹窗（审核页面版）
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #69
 */
import { ref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { queryDeviceReportById } from '/@/api/adverse/device';

defineProps({
  title: {
    type: String,
    default: '报告详情',
  },
});

const reportData = ref<any>({});

const [registerModal] = useModalInner(async (data) => {
  if (data?.id) {
    const result = await queryDeviceReportById({ id: data.id });
    reportData.value = result || {};
  } else if (data?.record) {
    reportData.value = data.record;
  }
});

/**
 * 器械使用场所选项
 */
const deviceUsePlaceOptions = [
  { label: '医疗机构', value: 'hospital' },
  { label: '家庭', value: 'home' },
  { label: '其它', value: 'other' },
];

/**
 * 操作人类型选项
 */
const operatorTypeOptions = [
  { label: '医务人员', value: 'medical_staff' },
  { label: '患者', value: 'patient' },
  { label: '其它', value: 'other' },
];

/**
 * 评价结果选项
 */
const evalResultOptions = [
  { label: '是', value: 'yes' },
  { label: '否', value: 'no' },
  { label: '无法确定', value: 'unknown' },
];

/**
 * 报告人类型选项
 */
const reporterTypeOptions = [
  { label: '医疗机构', value: 'hospital' },
  { label: '经营企业', value: 'enterprise' },
  { label: '生产企业', value: 'manufacturer' },
  { label: '使用者', value: 'user' },
  { label: '其他', value: 'other' },
];

/**
 * 报告状态选项
 */
const deviceReportStatusOptions = [
  { label: '草稿', value: 'draft', color: 'default' },
  { label: '待审核', value: 'pending_audit', color: 'processing' },
  { label: '已退回', value: 'returned', color: 'warning' },
  { label: '待处理', value: 'pending_process', color: 'processing' },
  { label: '待整改', value: 'pending_rectify', color: 'orange' },
  { label: '整改中', value: 'rectifying', color: 'cyan' },
  { label: '已结案', value: 'closed', color: 'success' },
];

/**
 * 获取器械使用场所文本
 */
function getDeviceUsePlaceText(value: string) {
  const item = deviceUsePlaceOptions.find((i) => i.value === value);
  return item?.label || value || '-';
}

/**
 * 获取事件后果文本
 */
function getEventResultText(data: any) {
  const results: string[] = [];
  if (data.resultDeath) results.push('死亡');
  if (data.resultLifeRisk) results.push('危及生命');
  if (data.resultHospitalization) results.push('导致或延长住院');
  if (data.resultPermanentDamage) results.push('机体功能结构永久性损伤');
  if (data.resultInterventionNeeded) results.push('可能导致永久性损伤');
  if (data.resultSurgicalAvoid) results.push('需内外科治疗避免永久损伤');
  if (data.resultOther) results.push(`其它(${data.resultOtherDesc || ''})`);
  return results.length > 0 ? results.join('、') : '-';
}

/**
 * 获取操作人类型文本
 */
function getOperatorTypeText(value: string) {
  const item = operatorTypeOptions.find((i) => i.value === value);
  return item?.label || value || '-';
}

/**
 * 获取评价结果文本
 */
function getEvalResultText(value: string) {
  const item = evalResultOptions.find((i) => i.value === value);
  return item?.label || value || '-';
}

/**
 * 获取报告人类型文本
 */
function getReporterTypeText(value: string) {
  const item = reporterTypeOptions.find((i) => i.value === value);
  return item?.label || value || '-';
}

/**
 * 获取状态文本
 */
function getStatusText(value: string) {
  const item = deviceReportStatusOptions.find((i) => i.value === value);
  return item?.label || value || '-';
}

/**
 * 获取状态颜色
 */
function getStatusColor(value: string) {
  const item = deviceReportStatusOptions.find((i) => i.value === value);
  return item?.color || 'default';
}
</script>
