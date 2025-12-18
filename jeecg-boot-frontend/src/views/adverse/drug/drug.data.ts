/**
 * 药品不良反应报告数据配置
 * @description 表格列、搜索表单、报告表单配置
 * @author TC Agent
 * @since 2025-12-19
 */
import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';
import { JVxeColumn, JVxeTypes } from '/@/components/jeecg/JVxeTable/types';

/**
 * 报告类型映射
 */
export const reportTypeOptions = [
  { label: '首次报告', value: 'first', color: 'processing' },
  { label: '跟踪报告', value: 'follow_up', color: 'warning' },
];

/**
 * 严重程度映射
 */
export const severityTypeOptions = [
  { label: '新的', value: 'new', color: 'blue' },
  { label: '严重', value: 'serious', color: 'red' },
  { label: '一般', value: 'general', color: 'default' },
];

/**
 * 报告状态映射
 */
export const drugReportStatusOptions = [
  { label: '草稿', value: 'draft', color: 'default' },
  { label: '待审核', value: 'pending_audit', color: 'processing' },
  { label: '已退回', value: 'returned', color: 'warning' },
  { label: '待处理', value: 'pending_process', color: 'processing' },
  { label: '已结案', value: 'closed', color: 'success' },
];

/**
 * 不良反应结果映射
 */
export const reactionResultOptions = [
  { label: '痊愈', value: 'cured', color: 'success' },
  { label: '好转', value: 'improved', color: 'processing' },
  { label: '未好转', value: 'not_improved', color: 'warning' },
  { label: '不详', value: 'unknown', color: 'default' },
  { label: '有后遗症', value: 'sequela', color: 'orange' },
  { label: '死亡', value: 'death', color: 'error' },
];

/**
 * 表格列配置
 */
export const columns: BasicColumn[] = [
  {
    title: '报告编号',
    dataIndex: 'reportCode',
    width: 150,
  },
  {
    title: '报告类型',
    dataIndex: 'reportType',
    width: 100,
    customRender: ({ text }) => {
      const item = reportTypeOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '严重程度',
    dataIndex: 'severityType',
    width: 100,
    customRender: ({ text }) => {
      const item = severityTypeOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '患者姓名',
    dataIndex: 'patientName',
    width: 100,
  },
  {
    title: '不良反应名称',
    dataIndex: 'reactionName',
    width: 150,
  },
  {
    title: '发生时间',
    dataIndex: 'reactionTime',
    width: 160,
  },
  {
    title: '结果',
    dataIndex: 'reactionResult',
    width: 100,
    customRender: ({ text }) => {
      const item = reactionResultOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      const item = drugReportStatusOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '报告日期',
    dataIndex: 'reportDate',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 160,
  },
];

/**
 * 搜索表单配置
 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '报告编号',
    field: 'reportCode',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '患者姓名',
    field: 'patientName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '不良反应名称',
    field: 'reactionName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: drugReportStatusOptions,
      placeholder: '请选择状态',
    },
    colProps: { span: 6 },
  },
  {
    label: '报告类型',
    field: 'reportType',
    component: 'Select',
    componentProps: {
      options: reportTypeOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择类型',
    },
    colProps: { span: 6 },
  },
  {
    label: '严重程度',
    field: 'severityType',
    component: 'Select',
    componentProps: {
      options: severityTypeOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择程度',
    },
    colProps: { span: 6 },
  },
];

// ========== 表单相关选项配置 ==========

/**
 * 报告单位类别选项
 */
export const unitCategoryOptions = [
  { label: '医疗机构', value: 'hospital' },
  { label: '经营企业', value: 'business' },
  { label: '生产企业', value: 'manufacture' },
  { label: '个人', value: 'personal' },
  { label: '其他', value: 'other' },
];

/**
 * 性别选项
 */
export const genderOptions = [
  { label: '男', value: 'M' },
  { label: '女', value: 'F' },
];

/**
 * 是/否/不详选项
 */
export const yesNoUnknownOptions = [
  { label: '有', value: 'yes' },
  { label: '无', value: 'no' },
  { label: '不详', value: 'unknown' },
];

/**
 * 停药/减量后反应选项
 */
export const stopDrugReactionOptions = [
  { label: '是', value: 'yes' },
  { label: '否', value: 'no' },
  { label: '不明', value: 'unknown' },
  { label: '未停药或未减量', value: 'not_stop' },
];

/**
 * 再次使用后反应选项
 */
export const rechallengeReactionOptions = [
  { label: '是', value: 'yes' },
  { label: '否', value: 'no' },
  { label: '不明', value: 'unknown' },
  { label: '未再使用', value: 'not_use' },
];

/**
 * 对原患疾病影响选项
 */
export const diseaseImpactOptions = [
  { label: '不明显', value: 'none' },
  { label: '病程延长', value: 'prolonged' },
  { label: '病情加重', value: 'aggravated' },
  { label: '导致后遗症', value: 'sequela' },
  { label: '导致死亡', value: 'death' },
];

/**
 * 关联性评价选项
 */
export const evaluationOptions = [
  { label: '肯定', value: 'definite' },
  { label: '很可能', value: 'probable' },
  { label: '可能', value: 'possible' },
  { label: '可能无关', value: 'unlikely' },
  { label: '待评价', value: 'pending' },
  { label: '无法评价', value: 'unable' },
];

/**
 * 报告人职业选项
 */
export const professionOptions = [
  { label: '医生', value: 'doctor' },
  { label: '药师', value: 'pharmacist' },
  { label: '护士', value: 'nurse' },
  { label: '其他', value: 'other' },
];

/**
 * 信息来源选项（生产企业填写）
 */
export const infoSourceOptions = [
  { label: '医疗机构', value: 'hospital' },
  { label: '经营企业', value: 'business' },
  { label: '个人', value: 'personal' },
  { label: '文献报道', value: 'literature' },
  { label: '上市后研究', value: 'study' },
  { label: '其他', value: 'other' },
];

/**
 * 给药途径选项
 */
export const routeOptions = [
  { label: '口服', value: 'oral' },
  { label: '静脉注射', value: 'iv' },
  { label: '肌肉注射', value: 'im' },
  { label: '皮下注射', value: 'sc' },
  { label: '外用', value: 'topical' },
  { label: '吸入', value: 'inhalation' },
  { label: '其他', value: 'other' },
];

/**
 * 剂量单位选项
 */
export const doseUnitOptions = [
  { label: 'g', value: 'g' },
  { label: 'mg', value: 'mg' },
  { label: 'μg', value: 'ug' },
  { label: 'ml', value: 'ml' },
  { label: 'IU', value: 'IU' },
  { label: '片', value: 'tablet' },
  { label: '粒', value: 'capsule' },
  { label: '支', value: 'ampoule' },
];

// ========== 药品不良反应报告表单 Schema ==========

/**
 * 1. 报告基本信息区
 */
export const reportBasicFormSchema: FormSchema[] = [
  {
    label: '报告类型',
    field: 'reportType',
    component: 'RadioGroup',
    required: true,
    defaultValue: 'first',
    componentProps: {
      options: reportTypeOptions.map((item) => ({ label: item.label, value: item.value })),
    },
    colProps: { span: 8 },
  },
  {
    label: '严重程度',
    field: 'severityType',
    component: 'RadioGroup',
    required: true,
    defaultValue: 'general',
    componentProps: {
      options: severityTypeOptions.map((item) => ({ label: item.label, value: item.value })),
    },
    colProps: { span: 8 },
  },
  {
    label: '报告单位类别',
    field: 'unitCategory',
    component: 'RadioGroup',
    required: true,
    defaultValue: 'hospital',
    componentProps: {
      options: unitCategoryOptions,
    },
    colProps: { span: 8 },
  },
  {
    label: '报告编号',
    field: 'reportCode',
    component: 'Input',
    componentProps: {
      disabled: true,
      placeholder: '系统自动生成',
    },
    colProps: { span: 8 },
  },
];

/**
 * 2. 患者基本信息区
 */
export const patientInfoFormSchema: FormSchema[] = [
  {
    label: '患者姓名',
    field: 'patientName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入患者姓名',
    },
    colProps: { span: 6 },
  },
  {
    label: '性别',
    field: 'patientGender',
    component: 'RadioGroup',
    componentProps: {
      options: genderOptions,
    },
    colProps: { span: 6 },
  },
  {
    label: '出生日期',
    field: 'patientBirthDate',
    component: 'DatePicker',
    componentProps: {
      placeholder: '请选择出生日期',
      valueFormat: 'YYYY-MM-DD',
      style: { width: '100%' },
    },
    colProps: { span: 6 },
  },
  {
    label: '民族',
    field: 'patientNationality',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'nationality',
      placeholder: '请选择民族',
    },
    colProps: { span: 6 },
  },
  {
    label: '体重(kg)',
    field: 'patientWeight',
    component: 'InputNumber',
    componentProps: {
      placeholder: '请输入体重',
      min: 0,
      max: 500,
      precision: 2,
      style: { width: '100%' },
    },
    colProps: { span: 6 },
  },
  {
    label: '联系方式',
    field: 'patientPhone',
    component: 'Input',
    componentProps: {
      placeholder: '请输入联系电话',
    },
    colProps: { span: 6 },
  },
  {
    label: '医院名称',
    field: 'hospitalName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入医院名称',
    },
    colProps: { span: 6 },
  },
  {
    label: '病历号/门诊号',
    field: 'medicalRecordNo',
    component: 'Input',
    componentProps: {
      placeholder: '请输入病历号或门诊号',
    },
    colProps: { span: 6 },
  },
  {
    label: '原患疾病',
    field: 'originalDisease',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入原患疾病',
      rows: 2,
    },
    colProps: { span: 24 },
  },
  {
    label: '既往药品不良反应',
    field: 'historyAdr',
    component: 'RadioGroup',
    componentProps: {
      options: yesNoUnknownOptions,
    },
    colProps: { span: 8 },
  },
  {
    label: '既往不良反应详情',
    field: 'historyAdrDetail',
    component: 'Input',
    componentProps: {
      placeholder: '若有，请详细描述',
    },
    colProps: { span: 16 },
    ifShow: ({ values }) => values.historyAdr === 'yes',
  },
  {
    label: '家族药品不良反应',
    field: 'familyAdr',
    component: 'RadioGroup',
    componentProps: {
      options: yesNoUnknownOptions,
    },
    colProps: { span: 8 },
  },
  {
    label: '家族不良反应详情',
    field: 'familyAdrDetail',
    component: 'Input',
    componentProps: {
      placeholder: '若有，请详细描述',
    },
    colProps: { span: 16 },
    ifShow: ({ values }) => values.familyAdr === 'yes',
  },
];

/**
 * 3. 相关重要信息区（勾选框）
 */
export const relatedInfoFormSchema: FormSchema[] = [
  {
    label: '吸烟史',
    field: 'hasSmoking',
    component: 'Checkbox',
    colProps: { span: 4 },
  },
  {
    label: '饮酒史',
    field: 'hasDrinking',
    component: 'Checkbox',
    colProps: { span: 4 },
  },
  {
    label: '妊娠期',
    field: 'isPregnant',
    component: 'Checkbox',
    colProps: { span: 4 },
  },
  {
    label: '肝病史',
    field: 'hasLiverDisease',
    component: 'Checkbox',
    colProps: { span: 4 },
  },
  {
    label: '肾病史',
    field: 'hasKidneyDisease',
    component: 'Checkbox',
    colProps: { span: 4 },
  },
  {
    label: '过敏史',
    field: 'hasAllergy',
    component: 'Checkbox',
    colProps: { span: 4 },
  },
  {
    label: '其他病史',
    field: 'otherHistory',
    component: 'Input',
    componentProps: {
      placeholder: '如有其他重要信息请填写',
    },
    colProps: { span: 24 },
  },
];

/**
 * 4. 不良反应/事件信息区
 */
export const reactionInfoFormSchema: FormSchema[] = [
  {
    label: '不良反应/事件名称',
    field: 'reactionName',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入不良反应/事件名称',
    },
    colProps: { span: 12 },
  },
  {
    label: '发生时间',
    field: 'reactionTime',
    component: 'DatePicker',
    required: true,
    componentProps: {
      placeholder: '请选择发生时间',
      showTime: true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      style: { width: '100%' },
    },
    colProps: { span: 12 },
  },
  {
    label: '不良反应/事件过程描述',
    field: 'reactionDescription',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      placeholder: '请描述不良反应/事件的症状、体征、临床检验结果及处理情况',
      rows: 4,
    },
    colProps: { span: 24 },
  },
];

/**
 * 5. 不良反应/事件结果区
 */
export const reactionResultFormSchema: FormSchema[] = [
  {
    label: '不良反应/事件结果',
    field: 'reactionResult',
    component: 'RadioGroup',
    componentProps: {
      options: reactionResultOptions.map((item) => ({ label: item.label, value: item.value })),
    },
    colProps: { span: 24 },
  },
  {
    label: '后遗症表现',
    field: 'sequelaDescription',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请描述后遗症的具体表现',
      rows: 2,
    },
    colProps: { span: 24 },
    ifShow: ({ values }) => values.reactionResult === 'sequela',
  },
  {
    label: '死亡时间',
    field: 'deathTime',
    component: 'DatePicker',
    componentProps: {
      placeholder: '请选择死亡时间',
      showTime: true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      style: { width: '100%' },
    },
    colProps: { span: 12 },
    ifShow: ({ values }) => values.reactionResult === 'death',
  },
  {
    label: '直接死因',
    field: 'deathCause',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请描述直接死亡原因',
      rows: 2,
    },
    colProps: { span: 12 },
    ifShow: ({ values }) => values.reactionResult === 'death',
  },
];

/**
 * 6. 停药或减量后反应区
 */
export const stopDrugFormSchema: FormSchema[] = [
  {
    label: '停药或减量后反应是否消失或减轻',
    field: 'stopDrugReaction',
    component: 'RadioGroup',
    componentProps: {
      options: stopDrugReactionOptions,
    },
    colProps: { span: 12 },
  },
  {
    label: '再次使用后是否再次出现同样反应',
    field: 'rechallengeReaction',
    component: 'RadioGroup',
    componentProps: {
      options: rechallengeReactionOptions,
    },
    colProps: { span: 12 },
  },
];

/**
 * 7. 对原患疾病的影响区
 */
export const diseaseImpactFormSchema: FormSchema[] = [
  {
    label: '对原患疾病的影响',
    field: 'diseaseImpact',
    component: 'RadioGroup',
    componentProps: {
      options: diseaseImpactOptions,
    },
    colProps: { span: 24 },
  },
];

/**
 * 8. 关联性评价区
 */
export const evaluationFormSchema: FormSchema[] = [
  {
    label: '报告人评价',
    field: 'reporterEvaluation',
    component: 'Select',
    componentProps: {
      options: evaluationOptions,
      placeholder: '请选择',
    },
    colProps: { span: 8 },
  },
  {
    label: '报告单位评价',
    field: 'unitEvaluation',
    component: 'Select',
    componentProps: {
      options: evaluationOptions,
      placeholder: '请选择',
    },
    colProps: { span: 8 },
  },
  {
    label: '评价人签名',
    field: 'unitEvaluatorName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入评价人姓名',
    },
    colProps: { span: 8 },
  },
];

/**
 * 9. 报告人信息区
 */
export const reporterInfoFormSchema: FormSchema[] = [
  {
    label: '报告人电话',
    field: 'reporterPhone',
    component: 'Input',
    componentProps: {
      placeholder: '请输入联系电话',
    },
    colProps: { span: 8 },
  },
  {
    label: '职业',
    field: 'reporterProfession',
    component: 'Select',
    componentProps: {
      options: professionOptions,
      placeholder: '请选择职业',
    },
    colProps: { span: 8 },
  },
  {
    label: '电子邮箱',
    field: 'reporterEmail',
    component: 'Input',
    componentProps: {
      placeholder: '请输入电子邮箱',
    },
    colProps: { span: 8 },
  },
  {
    label: '报告人签名',
    field: 'reporterSignature',
    component: 'Input',
    componentProps: {
      placeholder: '请输入报告人姓名',
    },
    colProps: { span: 8 },
  },
];

/**
 * 10. 报告单位信息区
 */
export const reportUnitInfoFormSchema: FormSchema[] = [
  {
    label: '报告单位名称',
    field: 'unitName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入报告单位名称',
    },
    colProps: { span: 8 },
  },
  {
    label: '联系人',
    field: 'unitContact',
    component: 'Input',
    componentProps: {
      placeholder: '请输入联系人姓名',
    },
    colProps: { span: 8 },
  },
  {
    label: '单位电话',
    field: 'unitPhone',
    component: 'Input',
    componentProps: {
      placeholder: '请输入单位电话',
    },
    colProps: { span: 8 },
  },
  {
    label: '报告日期',
    field: 'reportDate',
    component: 'DatePicker',
    componentProps: {
      placeholder: '请选择报告日期',
      valueFormat: 'YYYY-MM-DD',
      style: { width: '100%' },
    },
    colProps: { span: 8 },
  },
];

/**
 * 11. 生产企业填写信息来源区
 */
export const manufacturerInfoFormSchema: FormSchema[] = [
  {
    label: '信息来源',
    field: 'infoSource',
    component: 'Select',
    componentProps: {
      options: infoSourceOptions,
      placeholder: '请选择信息来源',
    },
    colProps: { span: 8 },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入备注信息',
      rows: 2,
    },
    colProps: { span: 16 },
  },
];

// ========== 怀疑药品/并用药品可编辑表格列配置 ==========

/**
 * 怀疑药品表格列配置
 */
export const suspectDrugColumns: JVxeColumn[] = [
  {
    title: '批准文号',
    key: 'approvalNo',
    width: 140,
    type: JVxeTypes.input,
    placeholder: '请输入批准文号',
  },
  {
    title: '商品名称',
    key: 'tradeName',
    width: 140,
    type: JVxeTypes.input,
    placeholder: '请输入商品名称',
  },
  {
    title: '通用名称(含剂型)',
    key: 'genericName',
    width: 160,
    type: JVxeTypes.input,
    placeholder: '请输入通用名称',
    validateRules: [{ required: true, message: '请输入通用名称' }],
  },
  {
    title: '生产厂家',
    key: 'manufacturer',
    width: 140,
    type: JVxeTypes.input,
    placeholder: '请输入生产厂家',
  },
  {
    title: '生产批号',
    key: 'batchNo',
    width: 120,
    type: JVxeTypes.input,
    placeholder: '请输入批号',
  },
  {
    title: '单次剂量',
    key: 'dosePerTime',
    width: 100,
    type: JVxeTypes.input,
    placeholder: '剂量',
  },
  {
    title: '单位',
    key: 'doseUnit',
    width: 80,
    type: JVxeTypes.select,
    options: doseUnitOptions,
    placeholder: '单位',
  },
  {
    title: '给药途径',
    key: 'route',
    width: 100,
    type: JVxeTypes.select,
    options: routeOptions,
    placeholder: '途径',
  },
  {
    title: '日次数',
    key: 'frequency',
    width: 80,
    type: JVxeTypes.input,
    placeholder: '次数',
  },
  {
    title: '用药开始时间',
    key: 'startDate',
    width: 130,
    type: JVxeTypes.date,
    placeholder: '开始日期',
  },
  {
    title: '用药截止时间',
    key: 'endDate',
    width: 130,
    type: JVxeTypes.date,
    placeholder: '截止日期',
  },
  {
    title: '用药原因',
    key: 'indication',
    width: 140,
    type: JVxeTypes.input,
    placeholder: '请输入用药原因',
  },
];

/**
 * 并用药品表格列配置
 * 结构与怀疑药品相同
 */
export const concomitantDrugColumns: JVxeColumn[] = [
  {
    title: '批准文号',
    key: 'approvalNo',
    width: 140,
    type: JVxeTypes.input,
    placeholder: '请输入批准文号',
  },
  {
    title: '商品名称',
    key: 'tradeName',
    width: 140,
    type: JVxeTypes.input,
    placeholder: '请输入商品名称',
  },
  {
    title: '通用名称(含剂型)',
    key: 'genericName',
    width: 160,
    type: JVxeTypes.input,
    placeholder: '请输入通用名称',
  },
  {
    title: '生产厂家',
    key: 'manufacturer',
    width: 140,
    type: JVxeTypes.input,
    placeholder: '请输入生产厂家',
  },
  {
    title: '生产批号',
    key: 'batchNo',
    width: 120,
    type: JVxeTypes.input,
    placeholder: '请输入批号',
  },
  {
    title: '单次剂量',
    key: 'dosePerTime',
    width: 100,
    type: JVxeTypes.input,
    placeholder: '剂量',
  },
  {
    title: '单位',
    key: 'doseUnit',
    width: 80,
    type: JVxeTypes.select,
    options: doseUnitOptions,
    placeholder: '单位',
  },
  {
    title: '给药途径',
    key: 'route',
    width: 100,
    type: JVxeTypes.select,
    options: routeOptions,
    placeholder: '途径',
  },
  {
    title: '日次数',
    key: 'frequency',
    width: 80,
    type: JVxeTypes.input,
    placeholder: '次数',
  },
  {
    title: '用药开始时间',
    key: 'startDate',
    width: 130,
    type: JVxeTypes.date,
    placeholder: '开始日期',
  },
  {
    title: '用药截止时间',
    key: 'endDate',
    width: 130,
    type: JVxeTypes.date,
    placeholder: '截止日期',
  },
  {
    title: '用药原因',
    key: 'indication',
    width: 140,
    type: JVxeTypes.input,
    placeholder: '请输入用药原因',
  },
];
