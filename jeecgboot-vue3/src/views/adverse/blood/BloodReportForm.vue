<template>
  <div class="blood-report-form p-4">
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
        <BasicForm @register="registerEventInfoForm" />
      </a-card>

      <!-- C. 不良事件内容 -->
      <a-card title="C. 不良事件内容" class="mb-4" :bordered="false">
        <BasicForm @register="registerEventContentForm" />
      </a-card>

      <!-- D. 不良事件等级 -->
      <a-card title="D. 不良事件等级" class="mb-4" :bordered="false">
        <BasicForm @register="registerEventLevelForm" />
      </a-card>

      <!-- E. 事件处理与分析 -->
      <a-card title="E. 事件处理与分析" class="mb-4" :bordered="false">
        <BasicForm @register="registerHandlingForm" />
      </a-card>

      <!-- F. 改进措施 -->
      <a-card title="F. 改进措施" class="mb-4" :bordered="false">
        <BasicForm @register="registerImprovementForm" />
      </a-card>

      <!-- G. 选填项目 -->
      <a-card title="G. 选填项目" class="mb-4" :bordered="false">
        <BasicForm @register="registerOptionalForm" />
      </a-card>
    </a-spin>

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
 * 输血使用不良事件上报表单页
 * @description 按照输血使用不良事件报告表结构创建
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/83
 */
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { BasicForm, useForm } from '/@/components/Form';
import { useMessage } from '/@/hooks/web/useMessage';
import {
  patientInfoFormSchema,
  eventInfoFormSchema,
  eventContentFormSchema,
  eventLevelFormSchema,
  handlingFormSchema,
  improvementFormSchema,
  optionalFormSchema,
} from './blood.data';
import {
  queryBloodReportById,
  addBloodReport,
  editBloodReport,
  saveBloodReportDraft,
  submitBloodReport,
} from '/@/api/adverse/blood';

const route = useRoute();
const router = useRouter();
const { createMessage, createConfirm } = useMessage();

// 状态变量
const loading = ref(false);

// 从路由获取参数
const reportId = computed(() => route.query.id as string);
const isView = computed(() => route.query.mode === 'view');
const isEdit = computed(() => !!reportId.value && !isView.value);

// 页面标题
const pageTitle = computed(() => {
  if (isView.value) {
    return '查看输血使用不良事件报告';
  }
  if (isEdit.value) {
    return '编辑输血使用不良事件报告';
  }
  return '新增输血使用不良事件报告';
});

// ========== 表单注册 ==========

// A. 患者资料表单
const [
  registerPatientInfoForm,
  {
    setFieldsValue: setPatientInfoFields,
    validate: validatePatientInfo,
    getFieldsValue: getPatientInfoFields,
    setProps: setPatientInfoProps,
  },
] = useForm({
  labelWidth: 120,
  schemas: patientInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// B. 不良事件情况表单
const [
  registerEventInfoForm,
  {
    setFieldsValue: setEventInfoFields,
    validate: validateEventInfo,
    getFieldsValue: getEventInfoFields,
    setProps: setEventInfoProps,
  },
] = useForm({
  labelWidth: 120,
  schemas: eventInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 24 },
});

// C. 不良事件内容表单
const [
  registerEventContentForm,
  {
    setFieldsValue: setEventContentFields,
    validate: validateEventContent,
    getFieldsValue: getEventContentFields,
    setProps: setEventContentProps,
  },
] = useForm({
  labelWidth: 120,
  schemas: eventContentFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// D. 不良事件等级表单
const [
  registerEventLevelForm,
  {
    setFieldsValue: setEventLevelFields,
    validate: validateEventLevel,
    getFieldsValue: getEventLevelFields,
    setProps: setEventLevelProps,
  },
] = useForm({
  labelWidth: 120,
  schemas: eventLevelFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 12 },
});

// E. 事件处理与分析表单
const [
  registerHandlingForm,
  {
    setFieldsValue: setHandlingFields,
    validate: validateHandling,
    getFieldsValue: getHandlingFields,
    setProps: setHandlingProps,
  },
] = useForm({
  labelWidth: 140,
  schemas: handlingFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 24 },
});

// F. 改进措施表单
const [
  registerImprovementForm,
  {
    setFieldsValue: setImprovementFields,
    validate: validateImprovement,
    getFieldsValue: getImprovementFields,
    setProps: setImprovementProps,
  },
] = useForm({
  labelWidth: 100,
  schemas: improvementFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 24 },
});

// G. 选填项目表单
const [
  registerOptionalForm,
  {
    setFieldsValue: setOptionalFields,
    validate: validateOptional,
    getFieldsValue: getOptionalFields,
    setProps: setOptionalProps,
  },
] = useForm({
  labelWidth: 100,
  schemas: optionalFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// ========== 初始化 ==========

/**
 * 设置默认测试数据（方便测试）
 */
function setDefaultTestData() {
  // A. 患者资料
  setPatientInfoFields({
    patientName: '测试患者',
    patientGender: 'M',
    patientAge: 45,
    clinicalDiagnosis: '贫血',
    medicalRecordNo: 'MR' + new Date().getTime(),
    involvedDept: '内科',
  });

  // B. 不良事件情况
  setEventInfoFields({
    eventPlace: ['ward'],
    eventDescription: '患者在输血过程中出现发热、寒颤症状',
  });

  // C. 不良事件内容
  setEventContentFields({
    eventName: 'febrile_reaction',
    involvedStaff: '护士张三、医生李四',
  });

  // D. 不良事件等级
  setEventLevelFields({
    severityLevel: 'B',
    eventLevel: 'III',
  });

  // E. 事件处理与分析
  setHandlingFields({
    handlingMeasures: '立即停止输血，给予对症治疗，密切监测患者生命体征',
    possibleCauses: '患者可能存在输血敏感体质',
  });

  // F. 改进措施
  setImprovementFields({
    improvementMeasures: '加强输血前评估，完善输血观察流程',
  });

  // G. 选填项目
  setOptionalFields({
    reporterType: ['nurse'],
    partyType: ['patient'],
    professionalTitle: 'nurse_practitioner',
    reporterSignature: '张护士',
    deptName: '内科',
    contactPhone: '13800138000',
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
    const data = await queryBloodReportById(reportId.value);

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
 * 设置所有表单字段值
 */
async function setAllFormFields(data: any) {
  // 处理多选字段（逗号分隔字符串转数组）
  const processedData = { ...data };

  // eventPlace 字段处理
  if (processedData.eventPlace && typeof processedData.eventPlace === 'string') {
    processedData.eventPlace = processedData.eventPlace.split(',');
  }

  // reporterType 字段处理
  if (processedData.reporterType && typeof processedData.reporterType === 'string') {
    processedData.reporterType = processedData.reporterType.split(',');
  }

  // partyType 字段处理
  if (processedData.partyType && typeof processedData.partyType === 'string') {
    processedData.partyType = processedData.partyType.split(',');
  }

  await setPatientInfoFields(processedData);
  await setEventInfoFields(processedData);
  await setEventContentFields(processedData);
  await setEventLevelFields(processedData);
  await setHandlingFields(processedData);
  await setImprovementFields(processedData);
  await setOptionalFields(processedData);
}

/**
 * 设置表单禁用状态
 */
function setFormsDisabled(disabled: boolean) {
  setPatientInfoProps({ disabled });
  setEventInfoProps({ disabled });
  setEventContentProps({ disabled });
  setEventLevelProps({ disabled });
  setHandlingProps({ disabled });
  setImprovementProps({ disabled });
  setOptionalProps({ disabled });
}

/**
 * 获取所有表单数据
 */
async function getAllFormData(): Promise<any> {
  // 验证必填表单
  await validatePatientInfo();
  await validateEventInfo();
  await validateEventContent();
  await validateEventLevel();

  // 合并所有报告数据
  const formData = {
    ...getPatientInfoFields(),
    ...getEventInfoFields(),
    ...getEventContentFields(),
    ...getEventLevelFields(),
    ...getHandlingFields(),
    ...getImprovementFields(),
    ...getOptionalFields(),
  };

  // 处理多选字段（数组转逗号分隔字符串）
  if (Array.isArray(formData.eventPlace)) {
    formData.eventPlace = formData.eventPlace.join(',');
  }

  if (Array.isArray(formData.reporterType)) {
    formData.reporterType = formData.reporterType.join(',');
  }

  if (Array.isArray(formData.partyType)) {
    formData.partyType = formData.partyType.join(',');
  }

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
      // 编辑模式：设置报告ID
      formData.id = reportId.value;
      await editBloodReport(formData);
    } else {
      await saveBloodReportDraft(formData);
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

        if (isEdit.value) {
          // 编辑模式：设置报告ID并保存
          formData.id = reportId.value;
          await editBloodReport(formData);
          // 编辑后提交
          await submitBloodReport(reportId.value);
        } else {
          // 新增报告
          const result = await addBloodReport(formData);
          if (result?.id) {
            await submitBloodReport(result.id);
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
  router.push('/adverse/blood');
}
</script>

<style lang="less" scoped>
.blood-report-form {
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
</style>
