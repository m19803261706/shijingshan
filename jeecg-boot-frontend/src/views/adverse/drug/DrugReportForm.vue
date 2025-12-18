<template>
  <div class="drug-report-form p-4">
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
      <!-- 1. 报告基本信息区 -->
      <a-card title="一、报告基本信息" class="mb-4" :bordered="false">
        <BasicForm @register="registerReportBasicForm" />
      </a-card>

      <!-- 2. 患者基本信息区 -->
      <a-card title="二、患者基本信息" class="mb-4" :bordered="false">
        <BasicForm @register="registerPatientInfoForm" />
      </a-card>

      <!-- 3. 相关重要信息区 -->
      <a-card title="三、相关重要信息" class="mb-4" :bordered="false">
        <BasicForm @register="registerRelatedInfoForm" />
      </a-card>

      <!-- 4. 怀疑药品区 -->
      <a-card title="四、怀疑药品" class="mb-4" :bordered="false">
        <JVxeTable
          ref="suspectDrugTableRef"
          toolbar
          rowNumber
          rowSelection
          :maxHeight="300"
          :loading="suspectDrugLoading"
          :columns="suspectDrugColumns"
          :dataSource="suspectDrugDataSource"
          :disabled="isView"
          :toolbarConfig="{ btns: isView ? [] : ['add', 'remove'] }"
        />
      </a-card>

      <!-- 5. 并用药品区 -->
      <a-card title="五、并用药品" class="mb-4" :bordered="false">
        <JVxeTable
          ref="concomitantDrugTableRef"
          toolbar
          rowNumber
          rowSelection
          :maxHeight="300"
          :loading="concomitantDrugLoading"
          :columns="concomitantDrugColumns"
          :dataSource="concomitantDrugDataSource"
          :disabled="isView"
          :toolbarConfig="{ btns: isView ? [] : ['add', 'remove'] }"
        />
      </a-card>

      <!-- 6. 不良反应/事件信息区 -->
      <a-card title="六、不良反应/事件信息" class="mb-4" :bordered="false">
        <BasicForm @register="registerReactionInfoForm" />
      </a-card>

      <!-- 7. 不良反应/事件结果区 -->
      <a-card title="七、不良反应/事件结果" class="mb-4" :bordered="false">
        <BasicForm @register="registerReactionResultForm" />
      </a-card>

      <!-- 8. 停药或减量后反应区 -->
      <a-card title="八、停药或减量后反应" class="mb-4" :bordered="false">
        <BasicForm @register="registerStopDrugForm" />
      </a-card>

      <!-- 9. 对原患疾病的影响区 -->
      <a-card title="九、对原患疾病的影响" class="mb-4" :bordered="false">
        <BasicForm @register="registerDiseaseImpactForm" />
      </a-card>

      <!-- 10. 关联性评价区 -->
      <a-card title="十、关联性评价" class="mb-4" :bordered="false">
        <BasicForm @register="registerEvaluationForm" />
      </a-card>

      <!-- 11. 报告人信息区 -->
      <a-card title="十一、报告人信息" class="mb-4" :bordered="false">
        <BasicForm @register="registerReporterInfoForm" />
      </a-card>

      <!-- 12. 报告单位信息区 -->
      <a-card title="十二、报告单位信息" class="mb-4" :bordered="false">
        <BasicForm @register="registerReportUnitInfoForm" />
      </a-card>

      <!-- 13. 生产企业填写信息来源区（仅生产企业显示） -->
      <a-card title="十三、生产企业填写信息来源" class="mb-4" :bordered="false" v-if="showManufacturerInfo">
        <BasicForm @register="registerManufacturerInfoForm" />
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
 * 药品不良反应报告表单页
 * @description 按照国家标准药品不良反应/事件报告表 1:1 复刻
 * @author TC Agent
 * @since 2025-12-19
 */
import { ref, computed, onMounted, unref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { BasicForm, useForm } from '/@/components/Form';
import { JVxeTable } from '/@/components/jeecg/JVxeTable';
import { useMessage } from '/@/hooks/web/useMessage';
import {
  reportBasicFormSchema,
  patientInfoFormSchema,
  relatedInfoFormSchema,
  reactionInfoFormSchema,
  reactionResultFormSchema,
  stopDrugFormSchema,
  diseaseImpactFormSchema,
  evaluationFormSchema,
  reporterInfoFormSchema,
  reportUnitInfoFormSchema,
  manufacturerInfoFormSchema,
  suspectDrugColumns,
  concomitantDrugColumns,
} from './drug.data';
import {
  queryDrugReportDetailById,
  addDrugReport,
  editDrugReport,
  saveDrugReportDraft,
  submitDrugReport,
} from '/@/api/adverse/drug';

const route = useRoute();
const router = useRouter();
const { createMessage, createConfirm } = useMessage();

// 状态变量
const loading = ref(false);
const suspectDrugLoading = ref(false);
const concomitantDrugLoading = ref(false);

// 子表数据源
const suspectDrugDataSource = ref<any[]>([]);
const concomitantDrugDataSource = ref<any[]>([]);

// 子表引用
const suspectDrugTableRef = ref();
const concomitantDrugTableRef = ref();

// 从路由获取参数
const reportId = computed(() => route.query.id as string);
const isView = computed(() => route.query.mode === 'view');
const isEdit = computed(() => !!reportId.value && !isView.value);

// 页面标题
const pageTitle = computed(() => {
  if (isView.value) {
    return '查看药品不良反应报告';
  }
  if (isEdit.value) {
    return '编辑药品不良反应报告';
  }
  return '新增药品不良反应报告';
});

// 是否显示生产企业信息区（仅生产企业类型显示）
const showManufacturerInfo = ref(false);

// ========== 表单注册 ==========

// 1. 报告基本信息表单
const [registerReportBasicForm, { setFieldsValue: setReportBasicFields, validate: validateReportBasic, getFieldsValue: getReportBasicFields, setProps: setReportBasicProps }] = useForm({
  labelWidth: 120,
  schemas: reportBasicFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// 2. 患者基本信息表单
const [registerPatientInfoForm, { setFieldsValue: setPatientInfoFields, validate: validatePatientInfo, getFieldsValue: getPatientInfoFields, setProps: setPatientInfoProps }] = useForm({
  labelWidth: 140,
  schemas: patientInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// 3. 相关重要信息表单
const [registerRelatedInfoForm, { setFieldsValue: setRelatedInfoFields, validate: validateRelatedInfo, getFieldsValue: getRelatedInfoFields, setProps: setRelatedInfoProps }] = useForm({
  labelWidth: 80,
  schemas: relatedInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 4 },
});

// 4. 不良反应/事件信息表单
const [registerReactionInfoForm, { setFieldsValue: setReactionInfoFields, validate: validateReactionInfo, getFieldsValue: getReactionInfoFields, setProps: setReactionInfoProps }] = useForm({
  labelWidth: 160,
  schemas: reactionInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 12 },
});

// 5. 不良反应/事件结果表单
const [registerReactionResultForm, { setFieldsValue: setReactionResultFields, validate: validateReactionResult, getFieldsValue: getReactionResultFields, setProps: setReactionResultProps }] = useForm({
  labelWidth: 160,
  schemas: reactionResultFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 12 },
});

// 6. 停药或减量后反应表单
const [registerStopDrugForm, { setFieldsValue: setStopDrugFields, validate: validateStopDrug, getFieldsValue: getStopDrugFields, setProps: setStopDrugProps }] = useForm({
  labelWidth: 260,
  schemas: stopDrugFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 12 },
});

// 7. 对原患疾病的影响表单
const [registerDiseaseImpactForm, { setFieldsValue: setDiseaseImpactFields, validate: validateDiseaseImpact, getFieldsValue: getDiseaseImpactFields, setProps: setDiseaseImpactProps }] = useForm({
  labelWidth: 140,
  schemas: diseaseImpactFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 24 },
});

// 8. 关联性评价表单
const [registerEvaluationForm, { setFieldsValue: setEvaluationFields, validate: validateEvaluation, getFieldsValue: getEvaluationFields, setProps: setEvaluationProps }] = useForm({
  labelWidth: 120,
  schemas: evaluationFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// 9. 报告人信息表单
const [registerReporterInfoForm, { setFieldsValue: setReporterInfoFields, validate: validateReporterInfo, getFieldsValue: getReporterInfoFields, setProps: setReporterInfoProps }] = useForm({
  labelWidth: 100,
  schemas: reporterInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// 10. 报告单位信息表单
const [registerReportUnitInfoForm, { setFieldsValue: setReportUnitInfoFields, validate: validateReportUnitInfo, getFieldsValue: getReportUnitInfoFields, setProps: setReportUnitInfoProps }] = useForm({
  labelWidth: 120,
  schemas: reportUnitInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// 11. 生产企业填写信息来源表单
const [registerManufacturerInfoForm, { setFieldsValue: setManufacturerInfoFields, validate: validateManufacturerInfo, getFieldsValue: getManufacturerInfoFields, setProps: setManufacturerInfoProps }] = useForm({
  labelWidth: 100,
  schemas: manufacturerInfoFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 8 },
});

// ========== 初始化 ==========

onMounted(async () => {
  // 如果是编辑或查看模式，加载数据
  if (reportId.value) {
    await loadReportData();
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
  try {
    loading.value = true;
    suspectDrugLoading.value = true;
    concomitantDrugLoading.value = true;

    const data = await queryDrugReportDetailById({ id: reportId.value });

    if (data) {
      // 设置主表数据
      await setAllFormFields(data);

      // 设置子表数据
      suspectDrugDataSource.value = data.suspectDrugList || [];
      concomitantDrugDataSource.value = data.concomitantDrugList || [];

      // 根据报告单位类别显示生产企业信息区
      showManufacturerInfo.value = data.unitCategory === 'manufacture';
    }
  } catch (e) {
    console.error('加载报告数据失败', e);
    createMessage.error('加载报告数据失败');
  } finally {
    loading.value = false;
    suspectDrugLoading.value = false;
    concomitantDrugLoading.value = false;
  }
}

/**
 * 设置所有表单字段值
 */
async function setAllFormFields(data: any) {
  await setReportBasicFields(data);
  await setPatientInfoFields(data);
  await setRelatedInfoFields(data);
  await setReactionInfoFields(data);
  await setReactionResultFields(data);
  await setStopDrugFields(data);
  await setDiseaseImpactFields(data);
  await setEvaluationFields(data);
  await setReporterInfoFields(data);
  await setReportUnitInfoFields(data);
  await setManufacturerInfoFields(data);
}

/**
 * 设置表单禁用状态
 */
function setFormsDisabled(disabled: boolean) {
  setReportBasicProps({ disabled });
  setPatientInfoProps({ disabled });
  setRelatedInfoProps({ disabled });
  setReactionInfoProps({ disabled });
  setReactionResultProps({ disabled });
  setStopDrugProps({ disabled });
  setDiseaseImpactProps({ disabled });
  setEvaluationProps({ disabled });
  setReporterInfoProps({ disabled });
  setReportUnitInfoProps({ disabled });
  setManufacturerInfoProps({ disabled });
}

/**
 * 获取所有表单数据
 */
async function getAllFormData(): Promise<any> {
  // 验证所有必填表单
  await validateReportBasic();
  await validateReactionInfo();

  // 合并所有表单数据
  const formData = {
    ...getReportBasicFields(),
    ...getPatientInfoFields(),
    ...getRelatedInfoFields(),
    ...getReactionInfoFields(),
    ...getReactionResultFields(),
    ...getStopDrugFields(),
    ...getDiseaseImpactFields(),
    ...getEvaluationFields(),
    ...getReporterInfoFields(),
    ...getReportUnitInfoFields(),
    ...getManufacturerInfoFields(),
  };

  // 获取子表数据
  const suspectDrugList = suspectDrugTableRef.value?.getTableData() || [];
  const concomitantDrugList = concomitantDrugTableRef.value?.getTableData() || [];

  return {
    ...formData,
    suspectDrugList,
    concomitantDrugList,
  };
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
      await editDrugReport(formData);
    } else {
      await saveDrugReportDraft(formData);
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

        // 验证怀疑药品至少有一条
        if (!formData.suspectDrugList || formData.suspectDrugList.length === 0) {
          createMessage.warning('请至少添加一条怀疑药品');
          return;
        }

        if (isEdit.value) {
          formData.id = reportId.value;
          await editDrugReport(formData);
          // 编辑后提交
          await submitDrugReport({ id: reportId.value });
        } else {
          // 新增并提交
          const result = await addDrugReport(formData);
          if (result?.id) {
            await submitDrugReport({ id: result.id });
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
  router.push('/adverse/drug');
}
</script>

<style lang="less" scoped>
.drug-report-form {
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
