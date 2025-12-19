<template>
  <div class="device-report-form p-4">
    <!-- 页面标题 -->
    <div class="page-header mb-4">
      <a-page-header
        :title="pageTitle"
        :sub-title="isView ? '只读模式' : ''"
        @back="handleBack"
      >
        <template #extra>
          <a-space>
            <a-button @click="handleBack">返回</a-button>
            <a-button type="default" @click="handleSaveDraft" :loading="loading" v-if="!isView">
              保存草稿
            </a-button>
            <a-button type="primary" @click="handleSubmit" :loading="loading" v-if="!isView">
              提交报告
            </a-button>
          </a-space>
        </template>
      </a-page-header>
    </div>

    <a-spin :spinning="loading">
      <!-- A. 患者资料 -->
      <a-card title="A. 患者资料" class="mb-4" :bordered="false">
        <BasicForm @register="registerPatientInfoForm" />
      </a-card>

      <!-- B. 不良事件情况 -->
      <a-card title="B. 不良事件情况" class="mb-4" :bordered="false">
        <!-- 事件基本信息 -->
        <BasicForm @register="registerEventInfoForm" />

        <!-- 9. 事件后果 -->
        <a-divider orientation="left" style="margin: 16px 0 8px 0; font-size: 13px;">
          9. 事件后果（可多选）
        </a-divider>
        <BasicForm @register="registerEventResultForm" />

        <!-- 10. 事件陈述 -->
        <a-divider orientation="left" style="margin: 16px 0 8px 0; font-size: 13px;">
          10. 事件陈述（至少包括器械使用时间、使用目的、不良事件情况、对受害者影响、采取的治疗措施、器械联合使用情况）
        </a-divider>
        <BasicForm @register="registerEventStatementForm" />
      </a-card>

      <!-- C. 医疗器械情况 -->
      <a-card class="mb-4" :bordered="false">
        <template #title>
          <div class="card-title-with-action">
            <span>C. 医疗器械情况</span>
            <a-button
              type="primary"
              size="small"
              preIcon="ant-design:search-outlined"
              @click="handleSelectDevice"
              v-if="!isView"
            >
              选择常用器械
            </a-button>
          </div>
        </template>
        <BasicForm @register="registerDeviceInfoForm" />

        <!-- 21-22. 原因分析和处理情况 -->
        <a-divider style="margin: 16px 0 8px 0;" />
        <BasicForm @register="registerCauseAnalysisForm" />

        <!-- 23. 事件报告状态 -->
        <a-divider orientation="left" style="margin: 16px 0 8px 0; font-size: 13px;">
          23. 事件报告状态
        </a-divider>
        <BasicForm @register="registerReportStatusForm" />
      </a-card>

      <!-- D. 不良事件评价 -->
      <a-card title="D. 不良事件评价" class="mb-4" :bordered="false">
        <a-alert
          message="以下为必填评价问题，请根据实际情况如实填写"
          type="info"
          show-icon
          style="margin-bottom: 16px;"
        />
        <BasicForm @register="registerEvaluationForm" />
      </a-card>

      <!-- 报告人信息 -->
      <a-card title="报告人信息" class="mb-4" :bordered="false">
        <BasicForm @register="registerReporterInfoForm" />
      </a-card>
    </a-spin>

    <!-- 器械选择弹窗 -->
    <DeviceSelectModal @register="registerDeviceSelectModal" @select="handleDeviceSelected" />

    <!-- 底部操作栏 -->
    <div class="form-footer">
      <a-space>
        <a-button @click="handleBack">返回</a-button>
        <a-button type="default" @click="handleSaveDraft" :loading="loading" v-if="!isView">
          保存草稿
        </a-button>
        <a-button type="primary" @click="handleSubmit" :loading="loading" v-if="!isView">
          提交报告
        </a-button>
      </a-space>
    </div>
  </div>
</template>

<script lang="ts" setup>
/**
 * 医疗器械不良事件报告表单页
 * @description 按照国标《可疑医疗器械不良事件报告表》1:1 复刻
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #68
 */
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { BasicForm, useForm } from '/@/components/Form';
import { useModal } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import DeviceSelectModal from './components/DeviceSelectModal.vue';
import {
  patientInfoFormSchema,
  eventInfoFormSchema,
  eventResultFormSchema,
  eventStatementFormSchema,
  deviceInfoFormSchema,
  causeAnalysisFormSchema,
  reportStatusFormSchema,
  evaluationFormSchema,
  reporterInfoFormSchema,
} from './device.data';
import {
  queryDeviceReportById,
  addDeviceReport,
  editDeviceReport,
  saveDeviceReportDraft,
  submitDeviceReport,
} from '/@/api/adverse/device';
import { saveDeviceConfigIfNotExists } from '/@/api/adverse/deviceConfig';

const route = useRoute();
const router = useRouter();
const { createMessage, createConfirm } = useMessage();

// 状态变量
const loading = ref(false);

// 器械选择弹窗
const [registerDeviceSelectModal, { openModal: openDeviceSelectModal }] = useModal();

// 从路由获取参数
const reportId = computed(() => route.query.id as string);
const isView = computed(() => route.query.mode === 'view');
const isEdit = computed(() => !!reportId.value && !isView.value);

// 页面标题
const pageTitle = computed(() => {
  if (isView.value) {
    return '查看医疗器械不良事件报告';
  }
  if (isEdit.value) {
    return '编辑医疗器械不良事件报告';
  }
  return '新增医疗器械不良事件报告';
});

// ========== 表单注册 ==========

// A. 患者资料表单
const [registerPatientInfoForm, { setFieldsValue: setPatientInfoFields, getFieldsValue: getPatientInfoFields, setProps: setPatientInfoProps }] = useForm({
  labelWidth: 180,
  schemas: patientInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// B. 不良事件情况表单
const [registerEventInfoForm, { setFieldsValue: setEventInfoFields, validate: validateEventInfo, getFieldsValue: getEventInfoFields, setProps: setEventInfoProps }] = useForm({
  labelWidth: 180,
  schemas: eventInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// B. 事件后果表单
const [registerEventResultForm, { setFieldsValue: setEventResultFields, getFieldsValue: getEventResultFields, setProps: setEventResultProps }] = useForm({
  labelWidth: 200,
  schemas: eventResultFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 6 },
});

// B. 事件陈述表单
const [registerEventStatementForm, { setFieldsValue: setEventStatementFields, getFieldsValue: getEventStatementFields, setProps: setEventStatementProps }] = useForm({
  labelWidth: 120,
  schemas: eventStatementFormSchema,
  showActionButtonGroup: false,
});

// C. 医疗器械情况表单
const [registerDeviceInfoForm, { setFieldsValue: setDeviceInfoFields, validate: validateDeviceInfo, getFieldsValue: getDeviceInfoFields, setProps: setDeviceInfoProps }] = useForm({
  labelWidth: 180,
  schemas: deviceInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// C. 原因分析和处理情况表单
const [registerCauseAnalysisForm, { setFieldsValue: setCauseAnalysisFields, getFieldsValue: getCauseAnalysisFields, setProps: setCauseAnalysisProps }] = useForm({
  labelWidth: 200,
  schemas: causeAnalysisFormSchema,
  showActionButtonGroup: false,
});

// C. 事件报告状态表单
const [registerReportStatusForm, { setFieldsValue: setReportStatusFields, getFieldsValue: getReportStatusFields, setProps: setReportStatusProps }] = useForm({
  labelWidth: 120,
  schemas: reportStatusFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 6 },
});

// D. 不良事件评价表单
const [registerEvaluationForm, { setFieldsValue: setEvaluationFields, validate: validateEvaluation, getFieldsValue: getEvaluationFields, setProps: setEvaluationProps }] = useForm({
  labelWidth: 500,
  schemas: evaluationFormSchema,
  showActionButtonGroup: false,
});

// 报告人信息表单
const [registerReporterInfoForm, { setFieldsValue: setReporterInfoFields, getFieldsValue: getReporterInfoFields, setProps: setReporterInfoProps }] = useForm({
  labelWidth: 120,
  schemas: reporterInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// ========== 初始化 ==========

/**
 * 设置默认测试数据
 */
function setDefaultTestData() {
  // A. 患者资料
  setPatientInfoFields({
    patientName: '李四',
    patientAge: 55,
    patientGender: 'M',
    patientDisease: '高血压、冠心病',
  });

  // B. 不良事件情况
  setEventInfoFields({
    eventManifestation: '使用血压计测量时，显示屏出现异常数值，且设备发出异常警报声',
    eventOccurDate: new Date().toISOString().slice(0, 10),
    eventDiscoverDate: new Date().toISOString().slice(0, 10),
    deviceUsePlace: 'hospital',
  });

  // B. 事件后果
  setEventResultFields({
    resultDeath: false,
    resultLifeRisk: false,
    resultHospitalization: false,
    resultPermanentDamage: false,
    resultInterventionNeeded: false,
    resultSurgicalAvoid: false,
    resultOther: true,
    resultOtherDesc: '设备故障导致无法正常测量血压',
  });

  // B. 事件陈述
  setEventStatementFields({
    eventStatement: '患者于2025年12月19日上午9:00使用医院提供的电子血压计进行血压测量，在测量过程中设备显示屏出现花屏现象，同时发出持续的警报声。护士立即停止使用该设备，更换其他血压计为患者完成血压测量。患者未受到任何身体伤害。已将该设备送至设备科进行检修。',
  });

  // C. 医疗器械情况
  setDeviceInfoFields({
    productName: '电子血压计',
    tradeName: 'XY-2000',
    registrationNo: '京械注准20200001号',
    manufacturerName: '北京医疗器械有限公司',
    manufacturerAddress: '北京市海淀区XX路XX号',
    manufacturerContact: '010-12345678',
    modelSpec: 'XY-2000型',
    productCode: 'SN20251201001',
    productBatch: '202511',
    operatorType: 'professional',
    validPeriodTo: '2027-12-31',
    productionDate: '2023-12-01',
  });

  // C. 原因分析和处理情况
  setCauseAnalysisFields({
    causeAnalysis: '初步判断为设备内部电路故障，可能与长期使用导致的元器件老化有关',
    initialHandling: '已将设备送至设备科进行检修，并更换其他血压计继续使用',
  });

  // C. 事件报告状态
  setReportStatusFields({
    reportStatusUseUnit: true,
    reportStatusBusiness: false,
    reportStatusManufacturer: true,
    reportStatusFda: false,
  });

  // D. 不良事件评价
  setEvaluationFields({
    evalTimeSequence: 'yes',
    evalHarmType: 'yes',
    evalExcludeOther: 'no',
  });

  // 报告人信息
  setReporterInfoFields({
    reporterType: 'nurse',
    reporterSignature: '王护士',
    reportDate: new Date().toISOString().slice(0, 10),
  });
}

onMounted(async () => {
  // 如果是编辑或查看模式，加载数据
  if (reportId.value) {
    await loadReportData();
  } else {
    // 新增模式：设置默认测试数据
    setDefaultTestData();
  }

  // 查看模式设置表单只读
  if (isView.value) {
    setFormsDisabled(true);
  }
});

/**
 * 加载报告数据
 */
async function loadReportData() {
  loading.value = true;

  try {
    const data = await queryDeviceReportById({ id: reportId.value });

    if (data) {
      await setAllFormFields(data);
    }
  } catch (e) {
    console.error('加载报告数据失败', e);
    createMessage.error('加载报告数据失败');
  } finally {
    loading.value = false;
  }
}

/**
 * 将 Integer 值转换为 Boolean (用于 Checkbox 组件显示)
 */
function integerToBoolean(value: any): boolean {
  return value === 1 || value === '1' || value === true;
}

/**
 * 设置所有表单字段值
 */
async function setAllFormFields(data: any) {
  await setPatientInfoFields(data);
  await setEventInfoFields(data);

  // 转换 Integer → Boolean（用于 Checkbox 组件显示）
  const resultData = { ...data };
  const checkboxFields = [
    'resultDeath', 'resultLifeRisk', 'resultHospitalization',
    'resultPermanentDamage', 'resultInterventionNeeded', 'resultSurgicalAvoid', 'resultOther',
    'reportStatusUseUnit', 'reportStatusBusiness', 'reportStatusManufacturer', 'reportStatusFda',
  ];
  checkboxFields.forEach((field) => {
    if (resultData[field] !== undefined && resultData[field] !== null) {
      resultData[field] = integerToBoolean(resultData[field]);
    }
  });

  await setEventResultFields(resultData);
  await setEventStatementFields(data);
  await setDeviceInfoFields(data);
  await setCauseAnalysisFields(data);
  await setReportStatusFields(resultData);
  await setEvaluationFields(data);
  await setReporterInfoFields(data);
}

/**
 * 设置表单禁用状态
 */
function setFormsDisabled(disabled: boolean) {
  setPatientInfoProps({ disabled });
  setEventInfoProps({ disabled });
  setEventResultProps({ disabled });
  setEventStatementProps({ disabled });
  setDeviceInfoProps({ disabled });
  setCauseAnalysisProps({ disabled });
  setReportStatusProps({ disabled });
  setEvaluationProps({ disabled });
  setReporterInfoProps({ disabled });
}

/**
 * 将 Boolean 值转换为 Integer (用于后端 Integer 类型字段)
 */
function booleanToInteger(value: any): number {
  if (value === true || value === 1 || value === '1') {
    return 1;
  }
  return 0;
}

/**
 * 获取所有表单数据
 */
async function getAllFormData(): Promise<any> {
  // 验证必填表单
  await validateEventInfo();
  await validateDeviceInfo();
  await validateEvaluation();

  // 合并所有表单数据
  const formData = {
    ...getPatientInfoFields(),
    ...getEventInfoFields(),
    ...getEventResultFields(),
    ...getEventStatementFields(),
    ...getDeviceInfoFields(),
    ...getCauseAnalysisFields(),
    ...getReportStatusFields(),
    ...getEvaluationFields(),
    ...getReporterInfoFields(),
  };

  // 转换 Checkbox 字段：Boolean → Integer
  const checkboxFields = [
    'resultDeath', 'resultLifeRisk', 'resultHospitalization',
    'resultPermanentDamage', 'resultInterventionNeeded', 'resultSurgicalAvoid', 'resultOther',
    'reportStatusUseUnit', 'reportStatusBusiness', 'reportStatusManufacturer', 'reportStatusFda',
  ];
  checkboxFields.forEach((field) => {
    if (formData[field] !== undefined && formData[field] !== null) {
      formData[field] = booleanToInteger(formData[field]);
    }
  });

  return formData;
}

/**
 * 保存草稿
 */
async function handleSaveDraft() {
  try {
    loading.value = true;

    const formData = await getAllFormData();

    if (isEdit.value) {
      formData.id = reportId.value;
      await editDeviceReport(formData);
    } else {
      await saveDeviceReportDraft(formData);
    }

    createMessage.success('草稿保存成功');
    handleBack();
  } catch (e: any) {
    console.error('保存草稿失败', e);
    if (e?.errorFields) {
      createMessage.warning('请填写必填项');
    } else {
      createMessage.error('保存草稿失败');
    }
  } finally {
    loading.value = false;
  }
}

/**
 * 提交报告
 */
async function handleSubmit() {
  createConfirm({
    iconType: 'info',
    title: '确认提交',
    content: '确定要提交此报告吗？提交后将进入审核流程，您将无法再次编辑。',
    onOk: async () => {
      try {
        loading.value = true;

        const formData = await getAllFormData();

        // 自动保存器械到常用配置库（如果不存在）
        await autoSaveDeviceConfig(formData);

        if (isEdit.value) {
          formData.id = reportId.value;
          await editDeviceReport(formData);
          await submitDeviceReport(reportId.value);
        } else {
          const result = await addDeviceReport(formData);
          if (result?.id) {
            await submitDeviceReport(result.id);
          }
        }

        createMessage.success('报告提交成功');
        handleBack();
      } catch (e: any) {
        console.error('提交报告失败', e);
        if (e?.errorFields) {
          createMessage.warning('请填写必填项');
        } else {
          createMessage.error('提交报告失败');
        }
      } finally {
        loading.value = false;
      }
    },
  });
}

/**
 * 返回列表页
 */
function handleBack() {
  router.push('/adverse/device');
}

// ========== 器械选择功能 ==========

/**
 * 打开器械选择弹窗
 */
function handleSelectDevice() {
  openDeviceSelectModal(true);
}

/**
 * 处理选择的器械数据
 * @param device 选中的器械信息
 */
function handleDeviceSelected(device: any) {
  // 将选中的器械信息填充到表单
  setDeviceInfoFields({
    productName: device.productName,
    tradeName: device.tradeName,
    registrationNo: device.registrationNo,
    manufacturerName: device.manufacturerName,
    manufacturerAddress: device.manufacturerAddress,
    manufacturerContact: device.manufacturerContact,
    modelSpec: device.modelSpec,
    productCode: device.productCode,
    productBatch: device.productBatch,
  });

  createMessage.success('已填充器械信息');
}

/**
 * 自动保存常用器械配置
 * @param formData 表单数据
 */
async function autoSaveDeviceConfig(formData: any) {
  // 如果有器械产品名称，则尝试保存到常用配置
  if (formData.productName) {
    try {
      await saveDeviceConfigIfNotExists({
        productName: formData.productName,
        tradeName: formData.tradeName,
        registrationNo: formData.registrationNo,
        manufacturerName: formData.manufacturerName,
        manufacturerAddress: formData.manufacturerAddress,
        manufacturerContact: formData.manufacturerContact,
        modelSpec: formData.modelSpec,
        productCode: formData.productCode,
        productBatch: formData.productBatch,
      });
    } catch (e) {
      // 保存常用配置失败不影响主流程
      console.warn('保存常用器械配置失败:', e);
    }
  }
}
</script>

<style lang="less" scoped>
.device-report-form {
  background-color: #f0f2f5;
  min-height: 100vh;

  .page-header {
    background-color: #fff;
    margin: -16px -16px 16px -16px;
    padding: 16px;
  }

  :deep(.ant-card) {
    .ant-card-head {
      background-color: #fafafa;
      min-height: 40px;

      .ant-card-head-title {
        padding: 8px 0;
        font-weight: 600;
        font-size: 14px;
      }
    }

    .ant-card-body {
      padding: 16px 24px;
    }
  }

  .form-footer {
    position: sticky;
    bottom: 0;
    background-color: #fff;
    padding: 16px;
    margin: 0 -16px -16px -16px;
    box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
    text-align: center;
  }
}

.p-4 {
  padding: 16px;
}

.mb-4 {
  margin-bottom: 16px;
}

// 卡片标题带操作按钮样式
.card-title-with-action {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
</style>
