<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="'报告详情 - ' + (reportData?.reportCode || '')"
    :width="1100"
    :footer="null"
  >
    <a-spin :spinning="loading">
      <template v-if="reportData">
        <!-- 1. 报告基本信息 -->
        <a-card title="一、报告基本信息" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="4" size="small" bordered>
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
            <a-descriptions-item label="报告单位类别">{{ getUnitCategoryLabel(reportData.unitCategory) }}</a-descriptions-item>
            <a-descriptions-item label="报告状态">
              <a-tag :color="getStatusColor(reportData.status)">
                {{ getStatusLabel(reportData.status) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="报告日期">{{ reportData.reportDate }}</a-descriptions-item>
            <a-descriptions-item label="上报人">{{ reportData.createBy_dictText || reportData.createBy }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ reportData.createTime }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 2. 患者基本信息 -->
        <a-card title="二、患者信息" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="4" size="small" bordered>
            <a-descriptions-item label="患者姓名">{{ reportData.patientName || '-' }}</a-descriptions-item>
            <a-descriptions-item label="性别">{{ getGenderLabel(reportData.patientGender) }}</a-descriptions-item>
            <a-descriptions-item label="出生日期">{{ reportData.patientBirthDate || '-' }}</a-descriptions-item>
            <a-descriptions-item label="民族">{{ reportData.patientNationality || '-' }}</a-descriptions-item>
            <a-descriptions-item label="体重(kg)">{{ reportData.patientWeight || '-' }}</a-descriptions-item>
            <a-descriptions-item label="联系方式">{{ reportData.patientPhone || '-' }}</a-descriptions-item>
            <a-descriptions-item label="医院名称">{{ reportData.hospitalName || '-' }}</a-descriptions-item>
            <a-descriptions-item label="病历号/门诊号">{{ reportData.medicalRecordNo || '-' }}</a-descriptions-item>
            <a-descriptions-item label="原患疾病" :span="4">{{ reportData.originalDisease || '-' }}</a-descriptions-item>
            <a-descriptions-item label="既往药品不良反应">{{ getYesNoLabel(reportData.historyAdr) }}</a-descriptions-item>
            <a-descriptions-item label="既往不良反应详情" :span="3">{{ reportData.historyAdrDetail || '-' }}</a-descriptions-item>
            <a-descriptions-item label="家族药品不良反应">{{ getYesNoLabel(reportData.familyAdr) }}</a-descriptions-item>
            <a-descriptions-item label="家族不良反应详情" :span="3">{{ reportData.familyAdrDetail || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 3. 相关重要信息 -->
        <a-card title="三、相关重要信息" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="6" size="small" bordered>
            <a-descriptions-item label="吸烟史">{{ getBoolLabel(reportData.hasSmoking) }}</a-descriptions-item>
            <a-descriptions-item label="饮酒史">{{ getBoolLabel(reportData.hasDrinking) }}</a-descriptions-item>
            <a-descriptions-item label="妊娠期">{{ getBoolLabel(reportData.isPregnant) }}</a-descriptions-item>
            <a-descriptions-item label="肝病史">{{ getBoolLabel(reportData.hasLiverDisease) }}</a-descriptions-item>
            <a-descriptions-item label="肾病史">{{ getBoolLabel(reportData.hasKidneyDisease) }}</a-descriptions-item>
            <a-descriptions-item label="过敏史">{{ getBoolLabel(reportData.hasAllergy) }}</a-descriptions-item>
            <a-descriptions-item label="其他重要信息" :span="6">{{ reportData.otherHistory || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 4. 怀疑药品 -->
        <a-card title="四、怀疑药品" :bordered="false" size="small" class="mb-3">
          <a-table
            :columns="suspectDrugColumns"
            :data-source="suspectDrugs"
            :pagination="false"
            size="small"
            bordered
            :scroll="{ x: 1200 }"
          >
            <template #emptyText>
              <a-empty description="暂无怀疑药品" />
            </template>
          </a-table>
        </a-card>

        <!-- 5. 并用药品 -->
        <a-card title="五、并用药品" :bordered="false" size="small" class="mb-3">
          <a-table
            :columns="concomitantDrugColumns"
            :data-source="concomitantDrugs"
            :pagination="false"
            size="small"
            bordered
            :scroll="{ x: 1200 }"
          >
            <template #emptyText>
              <a-empty description="暂无并用药品" />
            </template>
          </a-table>
        </a-card>

        <!-- 6. 不良反应/事件信息 -->
        <a-card title="六、不良反应/事件信息" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="3" size="small" bordered>
            <a-descriptions-item label="不良反应名称">{{ reportData.reactionName || '-' }}</a-descriptions-item>
            <a-descriptions-item label="发生时间">{{ reportData.reactionTime || '-' }}</a-descriptions-item>
            <a-descriptions-item label="不良反应结果">
              <a-tag v-if="reportData.reactionResult" :color="getResultColor(reportData.reactionResult)">
                {{ getResultLabel(reportData.reactionResult) }}
              </a-tag>
              <span v-else>-</span>
            </a-descriptions-item>
            <a-descriptions-item label="过程描述" :span="3">{{ reportData.reactionDescription || '-' }}</a-descriptions-item>
          </a-descriptions>

          <!-- 结构化过程描述 -->
          <a-divider orientation="left" style="font-size: 12px;">结构化过程描述</a-divider>
          <a-descriptions :column="4" size="small" bordered>
            <a-descriptions-item label="患者因">{{ reportData.patientReason || '-' }}</a-descriptions-item>
            <a-descriptions-item label="发病时间">
              {{ reportData.onsetYear ? `${reportData.onsetYear}年${reportData.onsetMonth || ''}月${reportData.onsetDay || ''}日${reportData.onsetHour || ''}时` : '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="给药途径">{{ reportData.routeType || '-' }}</a-descriptions-item>
            <a-descriptions-item label="药物名称">{{ reportData.drugNameDesc || '-' }}</a-descriptions-item>
            <a-descriptions-item label="剂量">{{ reportData.doseAmount ? `${reportData.doseAmount} ${reportData.doseType || ''}` : '-' }}</a-descriptions-item>
            <a-descriptions-item label="溶媒">{{ reportData.solventName ? `${reportData.solventName} ${reportData.solventVolume || ''}ml` : '-' }}</a-descriptions-item>
            <a-descriptions-item label="输入时间">{{ reportData.infusionDuration ? `${reportData.infusionDuration}${getTimeUnitLabel(reportData.infusionTimeUnit)}` : '-' }}</a-descriptions-item>
            <a-descriptions-item label="输入量">{{ reportData.infusedVolume ? `${reportData.infusedVolume}ml` : '-' }}</a-descriptions-item>
            <a-descriptions-item label="出现症状" :span="2">{{ reportData.symptoms || '-' }}</a-descriptions-item>
            <a-descriptions-item label="治疗方法" :span="2">{{ reportData.treatmentMethods || '-' }}</a-descriptions-item>
            <a-descriptions-item label="恢复时间">{{ reportData.recoveryDuration ? `${reportData.recoveryDuration}${getTimeUnitLabel(reportData.recoveryTimeUnit)}` : '-' }}</a-descriptions-item>
            <a-descriptions-item label="症状转归">{{ getSymptomOutcomeLabel(reportData.symptomOutcome) }}</a-descriptions-item>
            <a-descriptions-item label="后续用药" :span="2">{{ getSubsequentUsageLabel(reportData.subsequentUsage) }}</a-descriptions-item>
          </a-descriptions>

          <!-- 后遗症/死亡信息 -->
          <template v-if="reportData.reactionResult === 'sequela'">
            <a-divider orientation="left" style="font-size: 12px;">后遗症信息</a-divider>
            <a-descriptions :column="1" size="small" bordered>
              <a-descriptions-item label="后遗症表现">{{ reportData.sequelaDescription || '-' }}</a-descriptions-item>
            </a-descriptions>
          </template>
          <template v-if="reportData.reactionResult === 'death'">
            <a-divider orientation="left" style="font-size: 12px;">死亡信息</a-divider>
            <a-descriptions :column="2" size="small" bordered>
              <a-descriptions-item label="死亡时间">{{ reportData.deathTime || '-' }}</a-descriptions-item>
              <a-descriptions-item label="直接死因">{{ reportData.deathCause || '-' }}</a-descriptions-item>
            </a-descriptions>
          </template>
        </a-card>

        <!-- 7. 停药/减量后反应 -->
        <a-card title="七、停药/减量后反应及再次使用情况" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="2" size="small" bordered>
            <a-descriptions-item label="停药或减量后反应是否消失或减轻">{{ getStopDrugReactionLabel(reportData.stopDrugReaction) }}</a-descriptions-item>
            <a-descriptions-item label="再次使用后是否再次出现同样反应">{{ getRechallengeReactionLabel(reportData.rechallengeReaction) }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 8. 对原患疾病的影响 -->
        <a-card title="八、对原患疾病的影响" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="1" size="small" bordered>
            <a-descriptions-item label="对原患疾病的影响">{{ getDiseaseImpactLabel(reportData.diseaseImpact) }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 9. 关联性评价 -->
        <a-card title="九、关联性评价" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="3" size="small" bordered>
            <a-descriptions-item label="报告人评价">{{ getEvaluationLabel(reportData.reporterEvaluation) }}</a-descriptions-item>
            <a-descriptions-item label="报告单位评价">{{ getEvaluationLabel(reportData.unitEvaluation) }}</a-descriptions-item>
            <a-descriptions-item label="评价人签名">{{ reportData.unitEvaluatorName || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 10. 报告人信息 -->
        <a-card title="十、报告人信息" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="4" size="small" bordered>
            <a-descriptions-item label="报告人电话">{{ reportData.reporterPhone || '-' }}</a-descriptions-item>
            <a-descriptions-item label="职业">{{ getProfessionLabel(reportData.reporterProfession) }}</a-descriptions-item>
            <a-descriptions-item label="电子邮箱">{{ reportData.reporterEmail || '-' }}</a-descriptions-item>
            <a-descriptions-item label="报告人签名">{{ reportData.reporterSignature || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 11. 报告单位信息 -->
        <a-card title="十一、报告单位信息" :bordered="false" size="small" class="mb-3">
          <a-descriptions :column="4" size="small" bordered>
            <a-descriptions-item label="报告单位名称">{{ reportData.unitName || '-' }}</a-descriptions-item>
            <a-descriptions-item label="联系人">{{ reportData.unitContact || '-' }}</a-descriptions-item>
            <a-descriptions-item label="单位电话">{{ reportData.unitPhone || '-' }}</a-descriptions-item>
            <a-descriptions-item label="信息来源">{{ getInfoSourceLabel(reportData.infoSource) }}</a-descriptions-item>
            <a-descriptions-item label="备注" :span="4">{{ reportData.remark || '-' }}</a-descriptions-item>
          </a-descriptions>
        </a-card>

        <!-- 12. 审核信息（如有） -->
        <a-card
          v-if="reportData.auditUserId"
          title="十二、审核信息"
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

// 怀疑药品表格列
const suspectDrugColumns = [
  { title: '批准文号', dataIndex: 'approvalNo', width: 120 },
  { title: '商品名称', dataIndex: 'tradeName', width: 120 },
  { title: '通用名称(含剂型)', dataIndex: 'genericName', width: 150 },
  { title: '生产厂家', dataIndex: 'manufacturer', width: 120 },
  { title: '生产批号', dataIndex: 'batchNo', width: 100 },
  { title: '单次剂量', dataIndex: 'dosePerTime', width: 80 },
  { title: '单位', dataIndex: 'doseUnit', width: 60 },
  { title: '给药途径', dataIndex: 'route', width: 80 },
  { title: '日次数', dataIndex: 'frequency', width: 70 },
  { title: '用药开始时间', dataIndex: 'startDate', width: 110 },
  { title: '用药截止时间', dataIndex: 'endDate', width: 110 },
  { title: '用药原因', dataIndex: 'indication', width: 120 },
];

// 并用药品表格列
const concomitantDrugColumns = [...suspectDrugColumns];

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

// ========== 标签转换函数 ==========

function getReportTypeLabel(type: string): string {
  const map: Record<string, string> = { first: '首次报告', follow_up: '跟踪报告' };
  return map[type] || type || '-';
}

function getReportTypeColor(type: string): string {
  const map: Record<string, string> = { first: 'processing', follow_up: 'warning' };
  return map[type] || 'default';
}

function getSeverityLabel(severity: string): string {
  const map: Record<string, string> = { new: '新的', serious: '严重', general: '一般' };
  return map[severity] || severity || '-';
}

function getSeverityColor(severity: string): string {
  const map: Record<string, string> = { new: 'blue', serious: 'red', general: 'default' };
  return map[severity] || 'default';
}

function getStatusLabel(status: string): string {
  const map: Record<string, string> = {
    draft: '草稿', pending_audit: '待审核', returned: '已退回',
    pending_process: '待处理', closed: '已结案',
  };
  return map[status] || status || '-';
}

function getStatusColor(status: string): string {
  const map: Record<string, string> = {
    draft: 'default', pending_audit: 'processing', returned: 'warning',
    pending_process: 'processing', closed: 'success',
  };
  return map[status] || 'default';
}

function getUnitCategoryLabel(category: string): string {
  const map: Record<string, string> = {
    hospital: '医疗机构', business: '经营企业', manufacture: '生产企业',
    personal: '个人', other: '其他',
  };
  return map[category] || category || '-';
}

function getGenderLabel(gender: string): string {
  const map: Record<string, string> = { M: '男', F: '女' };
  return map[gender] || gender || '-';
}

function getYesNoLabel(value: string): string {
  const map: Record<string, string> = { yes: '有', no: '无', unknown: '不详' };
  return map[value] || value || '-';
}

function getBoolLabel(value: number | null | undefined): string {
  if (value === 1) return '是';
  if (value === 0) return '否';
  return '-';
}

function getResultLabel(result: string): string {
  const map: Record<string, string> = {
    cured: '痊愈', improved: '好转', not_improved: '未好转',
    unknown: '不详', sequela: '有后遗症', death: '死亡',
  };
  return map[result] || result || '-';
}

function getResultColor(result: string): string {
  const map: Record<string, string> = {
    cured: 'success', improved: 'processing', not_improved: 'warning',
    unknown: 'default', sequela: 'orange', death: 'error',
  };
  return map[result] || 'default';
}

function getTimeUnitLabel(unit: string): string {
  const map: Record<string, string> = { day: '天', hour: '小时', minute: '分钟' };
  return map[unit] || unit || '';
}

function getSymptomOutcomeLabel(outcome: string): string {
  const map: Record<string, string> = {
    improved: '逐渐有所好转', no_change: '无明显好转', worsened: '进一步加重',
  };
  return map[outcome] || outcome || '-';
}

function getSubsequentUsageLabel(usage: string): string {
  const map: Record<string, string> = {
    discontinued: '未再继续使用该药', continued: '继续使用该药',
  };
  return map[usage] || usage || '-';
}

function getStopDrugReactionLabel(reaction: string): string {
  const map: Record<string, string> = {
    yes: '是', no: '否', unknown: '不明', not_stop: '未停药或未减量',
  };
  return map[reaction] || reaction || '-';
}

function getRechallengeReactionLabel(reaction: string): string {
  const map: Record<string, string> = {
    yes: '是', no: '否', unknown: '不明', not_use: '未再使用',
  };
  return map[reaction] || reaction || '-';
}

function getDiseaseImpactLabel(impact: string): string {
  const map: Record<string, string> = {
    none: '不明显', prolonged: '病程延长', aggravated: '病情加重',
    sequela: '导致后遗症', death: '导致死亡',
  };
  return map[impact] || impact || '-';
}

function getEvaluationLabel(evaluation: string): string {
  const map: Record<string, string> = {
    definite: '肯定', probable: '很可能', possible: '可能',
    unlikely: '可能无关', pending: '待评价', unable: '无法评价',
  };
  return map[evaluation] || evaluation || '-';
}

function getProfessionLabel(profession: string): string {
  const map: Record<string, string> = {
    doctor: '医生', pharmacist: '药师', nurse: '护士', other: '其他',
  };
  return map[profession] || profession || '-';
}

function getInfoSourceLabel(source: string): string {
  const map: Record<string, string> = {
    hospital: '医疗机构', business: '经营企业', personal: '个人',
    literature: '文献报道', study: '上市后研究', other: '其他',
  };
  return map[source] || source || '-';
}
</script>

<style scoped>
.mb-3 {
  margin-bottom: 12px;
}
</style>
