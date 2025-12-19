<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'报告详情 - ' + (reportData?.reportCode || '')"
    :width="1000"
    :footer="null"
  >
    <a-spin :spinning="loading">
      <template v-if="reportData">
        <!-- 基本信息 -->
        <a-card title="报告基本信息" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="报告编号">{{ reportData.reportCode }}</a-descriptions-item>
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
            <a-descriptions-item label="报告状态">
              <a-tag :color="getStatusColor(reportData.status)">
                {{ getStatusLabel(reportData.status) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="报告日期">{{ reportData.reportDate }}</a-descriptions-item>
            <a-descriptions-item label="上报人">{{ reportData.createBy }}</a-descriptions-item>
            <a-descriptions-item label="上报科室">{{ reportData.departmentName }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ reportData.createTime }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 患者信息 -->
        <a-card title="患者信息" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="4" size="small">
            <a-descriptions-item label="患者姓名">{{ reportData.patientName }}</a-descriptions-item>
            <a-descriptions-item label="性别">{{ reportData.patientGender === 'M' ? '男' : '女' }}</a-descriptions-item>
            <a-descriptions-item label="出生日期">{{ reportData.patientBirthDate }}</a-descriptions-item>
            <a-descriptions-item label="体重(kg)">{{ reportData.patientWeight }}</a-descriptions-item>
            <a-descriptions-item label="联系方式">{{ reportData.patientPhone }}</a-descriptions-item>
            <a-descriptions-item label="医院名称">{{ reportData.hospitalName }}</a-descriptions-item>
            <a-descriptions-item label="病历号">{{ reportData.medicalRecordNo }}</a-descriptions-item>
            <a-descriptions-item label="民族">{{ reportData.patientNationality }}</a-descriptions-item>
            <a-descriptions-item label="原患疾病" :span="4">{{ reportData.originalDisease }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 不良反应信息 -->
        <a-card title="不良反应/事件信息" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="3" size="small">
            <a-descriptions-item label="不良反应名称">{{ reportData.reactionName }}</a-descriptions-item>
            <a-descriptions-item label="发生时间">{{ reportData.reactionTime }}</a-descriptions-item>
            <a-descriptions-item label="结果">
              <a-tag :color="getResultColor(reportData.reactionResult)">
                {{ getResultLabel(reportData.reactionResult) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="过程描述" :span="3">{{ reportData.reactionDescription }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 怀疑药品 -->
        <a-card title="怀疑药品" :bordered="false" size="small" class="mb-3">
          <a-table
            :columns="drugColumns"
            :data-source="suspectDrugs"
            :pagination="false"
            size="small"
            bordered
          />
        </a-card>

        <!-- 并用药品 -->
        <a-card title="并用药品" :bordered="false" size="small" class="mb-3">
          <a-table
            :columns="drugColumns"
            :data-source="concomitantDrugs"
            :pagination="false"
            size="small"
            bordered
          />
        </a-card>

        <!-- 审核信息（如有） -->
        <a-card
          v-if="reportData.auditUserId"
          title="审核信息"
          :bordered="false"
          size="small"
          class="mb-3"
        >
          <a-descriptions :column="3" size="small">
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
 * 药品不良反应报告详情弹窗
 * @description 展示报告完整信息，包括主表和子表数据
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/46
 */
import { ref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { getDrugAuditDetail } from '/@/api/adverse/drugAudit';

// 加载状态
const loading = ref(false);
// 报告数据
const reportData = ref<Recordable | null>(null);
// 怀疑药品
const suspectDrugs = ref<Recordable[]>([]);
// 并用药品
const concomitantDrugs = ref<Recordable[]>([]);

// 药品表格列
const drugColumns = [
  { title: '通用名称', dataIndex: 'genericName', width: 150 },
  { title: '商品名称', dataIndex: 'tradeName', width: 120 },
  { title: '生产厂家', dataIndex: 'manufacturer', width: 120 },
  { title: '批号', dataIndex: 'batchNo', width: 100 },
  { title: '剂量', dataIndex: 'dosePerTime', width: 80 },
  { title: '单位', dataIndex: 'doseUnit', width: 60 },
  { title: '途径', dataIndex: 'route', width: 80 },
  { title: '用药原因', dataIndex: 'indication', width: 120 },
];

// 注册弹窗
const [registerModal] = useModalInner(async (data) => {
  if (data?.record) {
    loading.value = true;
    try {
      const result = await getDrugAuditDetail(data.record.id);
      if (result) {
        reportData.value = result.report || result;
        suspectDrugs.value = result.suspectDrugs || [];
        concomitantDrugs.value = result.concomitantDrugs || [];
      }
    } finally {
      loading.value = false;
    }
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
 * 获取状态标签
 */
function getStatusLabel(status: string): string {
  const map: Record<string, string> = {
    draft: '草稿',
    pending_audit: '待审核',
    returned: '已退回',
    pending_process: '待处理',
    closed: '已结案',
  };
  return map[status] || status;
}

/**
 * 获取状态颜色
 */
function getStatusColor(status: string): string {
  const map: Record<string, string> = {
    draft: 'default',
    pending_audit: 'processing',
    returned: 'warning',
    pending_process: 'processing',
    closed: 'success',
  };
  return map[status] || 'default';
}

/**
 * 获取结果标签
 */
function getResultLabel(result: string): string {
  const map: Record<string, string> = {
    cured: '痊愈',
    improved: '好转',
    not_improved: '未好转',
    unknown: '不详',
    sequela: '有后遗症',
    death: '死亡',
  };
  return map[result] || result;
}

/**
 * 获取结果颜色
 */
function getResultColor(result: string): string {
  const map: Record<string, string> = {
    cured: 'success',
    improved: 'processing',
    not_improved: 'warning',
    unknown: 'default',
    sequela: 'orange',
    death: 'error',
  };
  return map[result] || 'default';
}
</script>

<style scoped>
.mb-3 {
  margin-bottom: 12px;
}
</style>
