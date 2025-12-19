<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'报告详情 - ' + (reportData?.reportCode || '')"
    :width="900"
    :footer="null"
  >
    <a-spin :spinning="loading">
      <template v-if="reportData">
        <!-- A. 患者资料 -->
        <a-card title="A. 患者资料" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="3" size="small" bordered>
            <a-descriptions-item label="患者姓名">{{ reportData.patientName || '-' }}</a-descriptions-item>
            <a-descriptions-item label="性别">{{ getGenderLabel(reportData.patientGender) }}</a-descriptions-item>
            <a-descriptions-item label="年龄">{{ reportData.patientAge ? reportData.patientAge + '岁' : '-' }}</a-descriptions-item>
            <a-descriptions-item label="临床诊断" :span="3">{{ reportData.clinicalDiagnosis || '-' }}</a-descriptions-item>
            <a-descriptions-item label="病案号">{{ reportData.medicalRecordNo || '-' }}</a-descriptions-item>
            <a-descriptions-item label="涉及科室" :span="2">{{ reportData.involvedDept || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- B. 不良事件情况 -->
        <a-card title="B. 不良事件情况" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="1" size="small" bordered>
            <a-descriptions-item label="事件发生场所">{{ formatEventPlace(reportData.eventPlace) }}</a-descriptions-item>
            <a-descriptions-item v-if="reportData.eventPlaceOther" label="其他场所说明">
              {{ reportData.eventPlaceOther }}
            </a-descriptions-item>
            <a-descriptions-item label="事件描述">{{ reportData.eventDescription || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- C. 不良事件内容 -->
        <a-card title="C. 不良事件内容" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="3" size="small" bordered>
            <a-descriptions-item label="不良事件名称">
              <a-tag :color="getEventNameColor(reportData.eventName)">
                {{ getEventNameLabel(reportData.eventName) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item v-if="reportData.eventNameOther" label="其他事件名称">
              {{ reportData.eventNameOther }}
            </a-descriptions-item>
            <a-descriptions-item label="输血事件类型">
              {{ getTransfusionEventLabel(reportData.transfusionEvent) }}
            </a-descriptions-item>
            <a-descriptions-item v-if="reportData.transfusionEventOther" label="其他输血事件">
              {{ reportData.transfusionEventOther }}
            </a-descriptions-item>
            <a-descriptions-item label="涉及人员" :span="3">{{ reportData.involvedStaff || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- D. 不良事件等级 -->
        <a-card title="D. 不良事件等级" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="2" size="small" bordered>
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
          </a-descriptions>
        </a-card>

        <!-- E. 事件处理与分析 -->
        <a-card title="E. 事件处理与分析" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="1" size="small" bordered>
            <a-descriptions-item label="事件处理措施">{{ reportData.handlingMeasures || '-' }}</a-descriptions-item>
            <a-descriptions-item label="导致事件的可能原因">{{ reportData.possibleCauses || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- F. 改进措施 -->
        <a-card title="F. 改进措施" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="1" size="small" bordered>
            <a-descriptions-item label="改进措施">{{ reportData.improvementMeasures || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- G. 选填项目 -->
        <a-card title="G. 选填项目" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="3" size="small" bordered>
            <a-descriptions-item label="报告人类型">{{ formatPersonType(reportData.reporterType) }}</a-descriptions-item>
            <a-descriptions-item label="当事人类别">{{ formatPersonType(reportData.partyType) }}</a-descriptions-item>
            <a-descriptions-item label="职称">{{ getProfessionalTitleLabel(reportData.professionalTitle) }}</a-descriptions-item>
            <a-descriptions-item label="报告人签名">{{ reportData.reporterSignature || '-' }}</a-descriptions-item>
            <a-descriptions-item label="科室名称">{{ reportData.deptName || '-' }}</a-descriptions-item>
            <a-descriptions-item label="联系电话">{{ reportData.contactPhone || '-' }}</a-descriptions-item>
            <a-descriptions-item label="Email">{{ reportData.email || '-' }}</a-descriptions-item>
            <a-descriptions-item label="报告日期">{{ reportData.reportDate || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 报告状态信息 -->
        <a-card title="报告状态" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="3" size="small" bordered>
            <a-descriptions-item label="当前状态">
              <a-tag :color="getStatusColor(reportData.status)">
                {{ getStatusLabel(reportData.status) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="上报人">{{ reportData.createBy_dictText || reportData.createBy }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ reportData.createTime }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 审核信息（如有） -->
        <a-card
          v-if="reportData.auditUserId"
          title="审核信息"
          :bordered="false"
          size="small"
          class="mb-3"
        >
          <a-descriptions :column="3" size="small" bordered>
            <a-descriptions-item label="审核人">{{ reportData.auditUserName }}</a-descriptions-item>
            <a-descriptions-item label="审核时间">{{ reportData.auditTime }}</a-descriptions-item>
            <a-descriptions-item label="审核意见">{{ reportData.auditComment || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>
      </template>
    </a-spin>
  </BasicModal>
</template>

<script lang="ts" setup>
/**
 * 输血使用不良事件报告详情弹窗
 * @description 展示报告完整信息
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/85
 */
import { ref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { getBloodAuditDetail } from '/@/api/adverse/bloodAudit';
import {
  eventPlaceOptions,
  eventNameOptions,
  transfusionEventOptions,
  severityLevelOptions,
  eventLevelOptions,
  personTypeOptions,
  professionalTitleOptions,
  bloodReportStatusOptions,
  genderOptions,
} from '../../../blood/blood.data';

// 加载状态
const loading = ref(false);
// 报告数据
const reportData = ref<Recordable | null>(null);

// 注册弹窗
const [registerModal] = useModalInner(async (data) => {
  if (data?.record) {
    loading.value = true;
    try {
      const result = await getBloodAuditDetail(data.record.id);
      reportData.value = result || data.record;
    } finally {
      loading.value = false;
    }
  }
});

// ========== 标签转换函数 ==========

function getGenderLabel(gender: string): string {
  const item = genderOptions.find((i) => i.value === gender);
  return item?.label || gender || '-';
}

function formatEventPlace(places: string): string {
  if (!places) return '-';
  const placeArray = places.split(',');
  return placeArray
    .map((p) => {
      const item = eventPlaceOptions.find((i) => i.value === p);
      return item?.label || p;
    })
    .join('、');
}

function getEventNameLabel(name: string): string {
  const item = eventNameOptions.find((i) => i.value === name);
  return item?.label || name || '-';
}

function getEventNameColor(name: string): string {
  const item = eventNameOptions.find((i) => i.value === name);
  return item?.color || 'default';
}

function getTransfusionEventLabel(event: string): string {
  const item = transfusionEventOptions.find((i) => i.value === event);
  return item?.label || event || '-';
}

function getSeverityLabel(level: string): string {
  const item = severityLevelOptions.find((i) => i.value === level);
  return item?.label || level || '-';
}

function getSeverityColor(level: string): string {
  const item = severityLevelOptions.find((i) => i.value === level);
  return item?.color || 'default';
}

function getEventLevelLabel(level: string): string {
  const item = eventLevelOptions.find((i) => i.value === level);
  return item?.label || level || '-';
}

function getEventLevelColor(level: string): string {
  const item = eventLevelOptions.find((i) => i.value === level);
  return item?.color || 'default';
}

function formatPersonType(types: string): string {
  if (!types) return '-';
  const typeArray = types.split(',');
  return typeArray
    .map((t) => {
      const item = personTypeOptions.find((i) => i.value === t);
      return item?.label || t;
    })
    .join('、');
}

function getProfessionalTitleLabel(title: string): string {
  const item = professionalTitleOptions.find((i) => i.value === title);
  return item?.label || title || '-';
}

function getStatusLabel(status: string): string {
  const item = bloodReportStatusOptions.find((i) => i.value === status);
  return item?.label || status || '-';
}

function getStatusColor(status: string): string {
  const item = bloodReportStatusOptions.find((i) => i.value === status);
  return item?.color || 'default';
}
</script>

<style scoped>
.mb-3 {
  margin-bottom: 12px;
}
</style>
