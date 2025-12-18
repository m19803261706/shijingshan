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
        >
          <!-- 快速选择按钮插槽 -->
          <template #action_suspect="{ row, rowIndex }">
            <a-button
              type="link"
              size="small"
              :disabled="isView"
              @click="openSuspectSelect(row, rowIndex)"
            >
              选择
            </a-button>
          </template>
        </JVxeTable>
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
        >
          <!-- 快速选择按钮插槽 -->
          <template #action_concomitant="{ row, rowIndex }">
            <a-button
              type="link"
              size="small"
              :disabled="isView"
              @click="openConcomitantSelect(row, rowIndex)"
            >
              选择
            </a-button>
          </template>
        </JVxeTable>
      </a-card>

      <!-- 6. 不良反应/事件信息区 -->
      <a-card title="六、不良反应/事件信息" class="mb-4" :bordered="false">
        <!-- 基本信息：事件名称和发生时间 -->
        <BasicForm @register="registerReactionBasicForm" />

        <!-- 事件过程描述（1:1复刻国标表单格式） -->
        <a-divider orientation="left" style="margin: 16px 0 8px 0; font-size: 13px;">
          不良反应/事件过程描述（包括症状、体征、临床检验等）及处理情况（可附页）：
        </a-divider>
        <BasicForm @register="registerReactionProcessForm" />
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

    <!-- 怀疑药品选择弹窗 -->
    <DrugSelectModal
      @register="registerSuspectModal"
      type="suspect"
      @select="onSuspectSelect"
    />

    <!-- 并用药品选择弹窗 -->
    <DrugSelectModal
      @register="registerConcomitantModal"
      type="concomitant"
      @select="onConcomitantSelect"
    />
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
import { useModal } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import DrugSelectModal from './components/DrugSelectModal.vue';
import {
  reportBasicFormSchema,
  patientInfoFormSchema,
  relatedInfoFormSchema,
  reactionBasicFormSchema,
  reactionProcessFormSchema,
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

// ========== 药品选择弹窗 ==========

// 怀疑药品选择弹窗
const [registerSuspectModal, { openModal: openSuspectModal }] = useModal();
// 并用药品选择弹窗
const [registerConcomitantModal, { openModal: openConcomitantModal }] = useModal();

// 当前选择的行索引
const currentSuspectRowIndex = ref<number>(-1);
const currentConcomitantRowIndex = ref<number>(-1);

/**
 * 打开怀疑药品选择弹窗
 * @param row 当前行数据
 * @param rowIndex 当前行索引
 */
function openSuspectSelect(row: any, rowIndex: number) {
  currentSuspectRowIndex.value = rowIndex;
  openSuspectModal(true, {});
}

/**
 * 打开并用药品选择弹窗
 * @param row 当前行数据
 * @param rowIndex 当前行索引
 */
function openConcomitantSelect(row: any, rowIndex: number) {
  currentConcomitantRowIndex.value = rowIndex;
  openConcomitantModal(true, {});
}

/**
 * 怀疑药品选择回调
 * @param drug 选择的药品信息
 */
function onSuspectSelect(drug: any) {
  const rowIndex = currentSuspectRowIndex.value;
  if (rowIndex >= 0 && suspectDrugTableRef.value) {
    // 获取当前行数据并更新
    const tableData = suspectDrugTableRef.value.getTableData();
    if (tableData && tableData[rowIndex]) {
      // 将选择的药品信息填充到当前行（包含所有可用字段）
      Object.assign(tableData[rowIndex], {
        approvalNo: drug.approvalNo,
        tradeName: drug.tradeName,
        genericName: drug.genericName,
        manufacturer: drug.manufacturer,
        route: drug.route,
        dosageForm: drug.dosageForm,
        specification: drug.specification,
        dosage: drug.dosage,
        // 新增字段：生产批号、单次剂量、剂量单位、用药频次、用药原因
        batchNo: drug.batchNo,
        dosePerTime: drug.dosePerTime,
        doseUnit: drug.doseUnit,
        frequency: drug.frequency,
        indication: drug.indication,
      });
      // 更新数据源触发重新渲染
      suspectDrugDataSource.value = [...tableData];
    }
  }
  currentSuspectRowIndex.value = -1;
}

/**
 * 并用药品选择回调
 * @param drug 选择的药品信息
 */
function onConcomitantSelect(drug: any) {
  const rowIndex = currentConcomitantRowIndex.value;
  if (rowIndex >= 0 && concomitantDrugTableRef.value) {
    // 获取当前行数据并更新
    const tableData = concomitantDrugTableRef.value.getTableData();
    if (tableData && tableData[rowIndex]) {
      // 将选择的药品信息填充到当前行（包含所有可用字段）
      Object.assign(tableData[rowIndex], {
        approvalNo: drug.approvalNo,
        tradeName: drug.tradeName,
        genericName: drug.genericName,
        manufacturer: drug.manufacturer,
        route: drug.route,
        dosageForm: drug.dosageForm,
        specification: drug.specification,
        dosage: drug.dosage,
        // 新增字段：生产批号、单次剂量、剂量单位、用药频次、用药原因
        batchNo: drug.batchNo,
        dosePerTime: drug.dosePerTime,
        doseUnit: drug.doseUnit,
        frequency: drug.frequency,
        indication: drug.indication,
      });
      // 更新数据源触发重新渲染
      concomitantDrugDataSource.value = [...tableData];
    }
  }
  currentConcomitantRowIndex.value = -1;
}

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

// 4. 不良反应/事件基本信息表单
const [registerReactionBasicForm, { setFieldsValue: setReactionBasicFields, validate: validateReactionBasic, getFieldsValue: getReactionBasicFields, setProps: setReactionBasicProps }] = useForm({
  labelWidth: 180,
  schemas: reactionBasicFormSchema,
  showActionButtonGroup: false,
  baseColProps: { span: 12 },
});

// 4.5 不良反应/事件过程描述表单（1:1复刻国标格式）
const [registerReactionProcessForm, { setFieldsValue: setReactionProcessFields, validate: validateReactionProcess, getFieldsValue: getReactionProcessFields, setProps: setReactionProcessProps }] = useForm({
  labelWidth: 70,
  schemas: reactionProcessFormSchema,
  showActionButtonGroup: false,
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

/**
 * 设置默认测试数据（方便测试）
 */
function setDefaultTestData() {
  // 1. 报告基本信息
  setReportBasicFields({
    reportType: 'first',
    severityType: 'general',
    unitCategory: 'hospital',
  });

  // 2. 患者基本信息
  setPatientInfoFields({
    patientName: '张三',
    patientGender: 'M',
    patientBirthDate: '1990-01-15',
    patientNationality: 'han',
    patientWeight: 70,
    patientPhone: '13800138000',
    hospitalName: '北京市石景山医院',
    medicalRecordNo: 'MR202512190001',
    originalDisease: '高血压、糖尿病',
    historyAdr: 'no',
    familyAdr: 'unknown',
  });

  // 3. 相关重要信息（勾选项）- 后端字段类型为 Integer (0-否 1-是)
  setRelatedInfoFields({
    hasSmoking: 0,
    hasDrinking: 0,
    isPregnant: 0,  // 注意：后端字段名是 isPregnant
    hasLiverDisease: 0,
    hasKidneyDisease: 0,
    hasAllergy: 0,
    otherHistory: '',
  });

  // 4. 不良反应基本信息
  setReactionBasicFields({
    reactionName: '皮疹、瘙痒',
    reactionTime: new Date().toISOString().slice(0, 10) + ' 09:30:00',
  });

  // 5. 不良反应过程描述
  setReactionProcessFields({
    reactionProcess: '患者于2025年12月19日上午9点服用阿莫西林胶囊后约30分钟出现全身皮疹、瘙痒症状，无呼吸困难、胸闷等不适。立即停药并给予抗过敏治疗（氯雷他定片10mg口服），症状逐渐缓解。',
  });

  // 6. 不良反应结果
  setReactionResultFields({
    reactionResult: 'improved',
    deathTime: undefined,
    deathCause: '',
  });

  // 7. 停药/减量后反应
  setStopDrugFields({
    stopDrugReaction: 'yes',
    rechallengeReaction: 'not_use',
  });

  // 8. 对原患疾病影响
  setDiseaseImpactFields({
    diseaseImpact: 'none',
  });

  // 9. 关联性评价
  setEvaluationFields({
    reporterEvaluation: 'probable',
    unitEvaluation: 'possible',
  });

  // 10. 报告人信息
  setReporterInfoFields({
    reporterName: '李医生',
    reporterPhone: '010-12345678',
    reporterEmail: 'doctor.li@hospital.com',
    reporterProfession: 'doctor',
    reportDate: new Date().toISOString().slice(0, 10),
  });

  // 11. 报告单位信息
  setReportUnitInfoFields({
    reportUnitName: '北京市石景山医院',
    reportUnitContact: '王主任',
    reportUnitPhone: '010-88888888',
  });

  // 12. 生产企业信息（可选）
  setManufacturerInfoFields({
    manufacturerInfoSource: 'hospital',
  });

  // 13. 怀疑药品测试数据
  suspectDrugDataSource.value = [
    {
      genericName: '阿莫西林胶囊',
      tradeName: '阿莫仙',
      manufacturer: '珠海联邦制药股份有限公司',
      approvalNo: '国药准字H44021518',
      batchNo: '20251201',
      dosageForm: '胶囊剂',
      specification: '0.5g*24粒',
      route: 'oral',
      dosePerTime: '0.5',
      doseUnit: 'g',
      frequency: '每日3次',
      startDate: '2025-12-18',
      endDate: '2025-12-19',
      indication: '上呼吸道感染',
    },
  ];

  // 14. 并用药品测试数据
  concomitantDrugDataSource.value = [
    {
      genericName: '二甲双胍片',
      tradeName: '格华止',
      manufacturer: '中美上海施贵宝制药有限公司',
      approvalNo: '国药准字H20023370',
      batchNo: '20251101',
      dosageForm: '片剂',
      specification: '0.5g*20片',
      route: 'oral',
      dosePerTime: '0.5',
      doseUnit: 'g',
      frequency: '每日2次',
      startDate: '2025-01-01',
      indication: '糖尿病',
    },
  ];
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
  try {
    loading.value = true;
    suspectDrugLoading.value = true;
    concomitantDrugLoading.value = true;

    const data = await queryDrugReportDetailById({ id: reportId.value });

    if (data) {
      // 后端返回结构: { report: {...}, suspectDrugs: [...], concomitantDrugs: [...] }
      // 提取主表数据（兼容两种数据结构）
      const reportData = data.report || data;

      // 设置主表数据
      await setAllFormFields(reportData);

      // 设置子表数据（兼容两种字段名）
      suspectDrugDataSource.value = data.suspectDrugs || data.suspectDrugList || [];
      concomitantDrugDataSource.value = data.concomitantDrugs || data.concomitantDrugList || [];

      // 根据报告单位类别显示生产企业信息区
      showManufacturerInfo.value = reportData.unitCategory === 'manufacture';
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
 * 将 Integer 值转换为 Boolean (用于 Checkbox 组件显示)
 * @param value Integer 或 Boolean 值
 * @returns Boolean
 */
function integerToBoolean(value: any): boolean {
  return value === 1 || value === '1' || value === true;
}

/**
 * 设置所有表单字段值
 */
async function setAllFormFields(data: any) {
  await setReportBasicFields(data);
  await setPatientInfoFields(data);

  // 转换 Integer → Boolean（用于 Checkbox 组件显示）
  const relatedInfoData = { ...data };
  const integerFields = ['hasSmoking', 'hasDrinking', 'isPregnant', 'hasLiverDisease', 'hasKidneyDisease', 'hasAllergy'];
  integerFields.forEach((field) => {
    if (relatedInfoData[field] !== undefined && relatedInfoData[field] !== null) {
      relatedInfoData[field] = integerToBoolean(relatedInfoData[field]);
    }
  });
  await setRelatedInfoFields(relatedInfoData);

  await setReactionBasicFields(data);
  await setReactionProcessFields(data);
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
  setReactionBasicProps({ disabled });
  setReactionProcessProps({ disabled });
  setReactionResultProps({ disabled });
  setStopDrugProps({ disabled });
  setDiseaseImpactProps({ disabled });
  setEvaluationProps({ disabled });
  setReporterInfoProps({ disabled });
  setReportUnitInfoProps({ disabled });
  setManufacturerInfoProps({ disabled });
}

/**
 * 将 Boolean 值转换为 Integer (用于后端 Integer 类型字段)
 * @param value Boolean 或 Integer 值
 * @returns 0 或 1
 */
function booleanToInteger(value: any): number {
  if (value === true || value === 1 || value === '1') {
    return 1;
  }
  return 0;
}

/**
 * 获取所有表单数据
 * 返回符合后端 DrugAdverseReportVO 结构的数据
 */
async function getAllFormData(): Promise<any> {
  // 验证所有必填表单
  await validateReportBasic();
  await validateReactionBasic();

  // 合并所有报告主表数据
  const report = {
    ...getReportBasicFields(),
    ...getPatientInfoFields(),
    ...getRelatedInfoFields(),
    ...getReactionBasicFields(),
    ...getReactionProcessFields(),
    ...getReactionResultFields(),
    ...getStopDrugFields(),
    ...getDiseaseImpactFields(),
    ...getEvaluationFields(),
    ...getReporterInfoFields(),
    ...getReportUnitInfoFields(),
    ...getManufacturerInfoFields(),
  };

  // 转换 Checkbox 字段：Boolean → Integer（后端 Integer 类型字段）
  const integerFields = ['hasSmoking', 'hasDrinking', 'isPregnant', 'hasLiverDisease', 'hasKidneyDisease', 'hasAllergy'];
  integerFields.forEach((field) => {
    if (report[field] !== undefined && report[field] !== null) {
      report[field] = booleanToInteger(report[field]);
    }
  });

  // 获取子表数据
  const suspectDrugs = suspectDrugTableRef.value?.getTableData() || [];
  const concomitantDrugs = concomitantDrugTableRef.value?.getTableData() || [];

  // 返回符合后端 VO 结构的数据
  return {
    report,
    suspectDrugs,
    concomitantDrugs,
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
      // 编辑模式：设置报告ID
      formData.report.id = reportId.value;
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
        if (!formData.suspectDrugs || formData.suspectDrugs.length === 0) {
          createMessage.warning('请至少添加一条怀疑药品');
          return;
        }

        if (isEdit.value) {
          // 编辑模式：设置报告ID
          formData.report.id = reportId.value;
          await editDrugReport(formData);
          // 编辑后提交
          await submitDrugReport(reportId.value);
        } else {
          // 新增并提交（后端返回 DrugAdverseReport 对象）
          const result = await addDrugReport(formData);
          if (result?.id) {
            await submitDrugReport(result.id);
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
