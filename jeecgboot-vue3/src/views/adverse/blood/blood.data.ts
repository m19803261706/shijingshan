/**
 * 输血使用不良事件报告数据配置
 * @description 表格列、搜索表单、报告表单配置
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/82
 */
import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

// ========== 选项配置 ==========

/**
 * 事件发生场所选项
 * 对应字典：blood_event_place
 */
export const eventPlaceOptions = [
  { label: '门诊', value: 'outpatient' },
  { label: '急诊', value: 'emergency' },
  { label: '病区', value: 'ward' },
  { label: '手术室', value: 'operating_room' },
  { label: '血透室', value: 'dialysis_room' },
  { label: 'ICU', value: 'icu' },
  { label: '其他', value: 'other' },
];

/**
 * 不良事件名称选项
 * 对应字典：blood_event_name
 */
export const eventNameOptions = [
  { label: '输血反应', value: 'transfusion_reaction', color: 'orange' },
  { label: '溶血反应', value: 'hemolytic_reaction', color: 'red' },
  { label: '过敏反应', value: 'allergic_reaction', color: 'warning' },
  { label: '发热反应', value: 'febrile_reaction', color: 'processing' },
  { label: '细菌污染', value: 'bacterial_contamination', color: 'error' },
  { label: 'TRALI', value: 'trali', color: 'red' },
  { label: 'TA-GVHD', value: 'ta_gvhd', color: 'red' },
  { label: '其他', value: 'other', color: 'default' },
];

/**
 * 输血事件类型选项
 * 对应字典：blood_transfusion_event
 */
export const transfusionEventOptions = [
  { label: '输错血型', value: 'wrong_blood_type', color: 'red' },
  { label: '输错患者', value: 'wrong_patient', color: 'red' },
  { label: '输错血液成分', value: 'wrong_component', color: 'orange' },
  { label: '输注过期血液', value: 'expired_blood', color: 'warning' },
  { label: '其他', value: 'other', color: 'default' },
];

/**
 * 严重程度选项
 * 对应字典：blood_severity_level
 */
export const severityLevelOptions = [
  { label: 'A-轻微', value: 'A', color: 'success' },
  { label: 'B-一般', value: 'B', color: 'processing' },
  { label: 'C-严重', value: 'C', color: 'warning' },
  { label: 'D-灾难', value: 'D', color: 'error' },
];

/**
 * 事件等级选项
 * 对应字典：blood_event_level
 */
export const eventLevelOptions = [
  { label: 'I级', value: 'I', color: 'error' },
  { label: 'II级', value: 'II', color: 'warning' },
  { label: 'III级', value: 'III', color: 'processing' },
  { label: 'IV级', value: 'IV', color: 'default' },
];

/**
 * 报告人/当事人类型选项
 * 对应字典：blood_person_type
 */
export const personTypeOptions = [
  { label: '患者', value: 'patient' },
  { label: '患者家属', value: 'family' },
  { label: '护士', value: 'nurse' },
  { label: '医生', value: 'doctor' },
  { label: '其他职员', value: 'other_staff' },
  { label: '其他', value: 'other' },
];

/**
 * 职称选项
 * 对应字典：blood_professional_title
 */
export const professionalTitleOptions = [
  { label: '医师', value: 'physician' },
  { label: '护师', value: 'nurse_practitioner' },
  { label: '技师', value: 'technician' },
  { label: '其他', value: 'other' },
];

/**
 * 报告状态选项
 * 对应字典：blood_report_status
 */
export const bloodReportStatusOptions = [
  { label: '草稿', value: 'draft', color: 'default' },
  { label: '待审核', value: 'pending_audit', color: 'processing' },
  { label: '已退回', value: 'returned', color: 'warning' },
  { label: '待处理', value: 'pending_process', color: 'processing' },
  { label: '待整改', value: 'pending_rectify', color: 'orange' },
  { label: '整改中', value: 'rectifying', color: 'cyan' },
  { label: '已结案', value: 'closed', color: 'success' },
];

/**
 * 性别选项
 */
export const genderOptions = [
  { label: '男', value: 'M' },
  { label: '女', value: 'F' },
];

// ========== 表格列配置 ==========

/**
 * 列表表格列配置
 */
export const columns: BasicColumn[] = [
  {
    title: '报告编号',
    dataIndex: 'reportCode',
    width: 160,
  },
  {
    title: '患者姓名',
    dataIndex: 'patientName',
    width: 100,
  },
  {
    title: '病案号',
    dataIndex: 'medicalRecordNo',
    width: 120,
  },
  {
    title: '不良事件名称',
    dataIndex: 'eventName',
    width: 120,
    customRender: ({ text }) => {
      const item = eventNameOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '严重程度',
    dataIndex: 'severityLevel',
    width: 100,
    customRender: ({ text }) => {
      const item = severityLevelOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '事件等级',
    dataIndex: 'eventLevel',
    width: 90,
    customRender: ({ text }) => {
      const item = eventLevelOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '涉及科室',
    dataIndex: 'involvedDept',
    width: 120,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      const item = bloodReportStatusOptions.find((i) => i.value === text);
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
    label: '不良事件名称',
    field: 'eventName',
    component: 'Select',
    componentProps: {
      options: eventNameOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择事件名称',
    },
    colProps: { span: 6 },
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: bloodReportStatusOptions,
      placeholder: '请选择状态',
    },
    colProps: { span: 6 },
  },
  {
    label: '严重程度',
    field: 'severityLevel',
    component: 'Select',
    componentProps: {
      options: severityLevelOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择严重程度',
    },
    colProps: { span: 6 },
  },
  {
    label: '事件等级',
    field: 'eventLevel',
    component: 'Select',
    componentProps: {
      options: eventLevelOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择事件等级',
    },
    colProps: { span: 6 },
  },
];

// ========== 表单 Schema ==========

/**
 * A. 患者资料表单
 */
export const patientInfoFormSchema: FormSchema[] = [
  {
    label: '患者姓名',
    field: 'patientName',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入患者姓名',
    },
    colProps: { span: 8 },
  },
  {
    label: '性别',
    field: 'patientGender',
    component: 'RadioGroup',
    componentProps: {
      options: genderOptions,
    },
    colProps: { span: 8 },
  },
  {
    label: '年龄',
    field: 'patientAge',
    component: 'InputNumber',
    componentProps: {
      placeholder: '请输入年龄',
      min: 0,
      max: 150,
      style: { width: '100%' },
      addonAfter: '岁',
    },
    colProps: { span: 8 },
  },
  {
    label: '临床诊断',
    field: 'clinicalDiagnosis',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入临床诊断',
      rows: 2,
    },
    colProps: { span: 24 },
  },
  {
    label: '病案号',
    field: 'medicalRecordNo',
    component: 'Input',
    componentProps: {
      placeholder: '请输入病案号',
    },
    colProps: { span: 8 },
  },
  {
    label: '涉及科室',
    field: 'involvedDept',
    component: 'Input',
    componentProps: {
      placeholder: '请输入涉及科室',
    },
    colProps: { span: 8 },
  },
];

/**
 * B. 不良事件情况表单
 */
export const eventInfoFormSchema: FormSchema[] = [
  {
    label: '事件发生场所',
    field: 'eventPlace',
    component: 'CheckboxGroup',
    componentProps: {
      options: eventPlaceOptions,
    },
    colProps: { span: 24 },
  },
  {
    label: '其他场所说明',
    field: 'eventPlaceOther',
    component: 'Input',
    componentProps: {
      placeholder: '若选择其他，请说明具体场所',
    },
    colProps: { span: 24 },
    ifShow: ({ values }) => {
      const places = values.eventPlace || [];
      return Array.isArray(places) ? places.includes('other') : places === 'other';
    },
  },
  {
    label: '事件描述',
    field: 'eventDescription',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      placeholder: '请详细描述事件经过',
      rows: 4,
    },
    colProps: { span: 24 },
  },
];

/**
 * C. 不良事件内容表单
 */
export const eventContentFormSchema: FormSchema[] = [
  {
    label: '不良事件名称',
    field: 'eventName',
    component: 'Select',
    required: true,
    componentProps: {
      options: eventNameOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择不良事件名称',
    },
    colProps: { span: 8 },
  },
  {
    label: '其他事件名称',
    field: 'eventNameOther',
    component: 'Input',
    componentProps: {
      placeholder: '若选择其他，请说明',
    },
    colProps: { span: 8 },
    ifShow: ({ values }) => values.eventName === 'other',
  },
  {
    label: '输血事件类型',
    field: 'transfusionEvent',
    component: 'Select',
    componentProps: {
      options: transfusionEventOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择输血事件类型',
    },
    colProps: { span: 8 },
  },
  {
    label: '其他输血事件',
    field: 'transfusionEventOther',
    component: 'Input',
    componentProps: {
      placeholder: '若选择其他，请说明',
    },
    colProps: { span: 8 },
    ifShow: ({ values }) => values.transfusionEvent === 'other',
  },
  {
    label: '涉及人员',
    field: 'involvedStaff',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入涉及人员信息',
      rows: 2,
    },
    colProps: { span: 24 },
  },
];

/**
 * D. 不良事件等级表单
 */
export const eventLevelFormSchema: FormSchema[] = [
  {
    label: '严重程度',
    field: 'severityLevel',
    component: 'RadioGroup',
    required: true,
    componentProps: {
      options: severityLevelOptions.map((item) => ({ label: item.label, value: item.value })),
    },
    colProps: { span: 12 },
  },
  {
    label: '事件等级',
    field: 'eventLevel',
    component: 'RadioGroup',
    required: true,
    componentProps: {
      options: eventLevelOptions.map((item) => ({ label: item.label, value: item.value })),
    },
    colProps: { span: 12 },
  },
];

/**
 * E. 事件处理与分析表单
 */
export const handlingFormSchema: FormSchema[] = [
  {
    label: '事件处理措施',
    field: 'handlingMeasures',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请描述事件处理措施',
      rows: 3,
    },
    colProps: { span: 24 },
  },
  {
    label: '导致事件的可能原因',
    field: 'possibleCauses',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请分析导致事件的可能原因',
      rows: 3,
    },
    colProps: { span: 24 },
  },
];

/**
 * F. 改进措施表单
 */
export const improvementFormSchema: FormSchema[] = [
  {
    label: '改进措施',
    field: 'improvementMeasures',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入改进措施',
      rows: 4,
    },
    colProps: { span: 24 },
  },
];

/**
 * G. 选填项目表单
 */
export const optionalFormSchema: FormSchema[] = [
  {
    label: '报告人类型',
    field: 'reporterType',
    component: 'CheckboxGroup',
    componentProps: {
      options: personTypeOptions,
    },
    colProps: { span: 24 },
  },
  {
    label: '当事人类别',
    field: 'partyType',
    component: 'CheckboxGroup',
    componentProps: {
      options: personTypeOptions,
    },
    colProps: { span: 24 },
  },
  {
    label: '职称',
    field: 'professionalTitle',
    component: 'Select',
    componentProps: {
      options: professionalTitleOptions,
      placeholder: '请选择职称',
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
  {
    label: '科室名称',
    field: 'deptName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入科室名称',
    },
    colProps: { span: 8 },
  },
  {
    label: '联系电话',
    field: 'contactPhone',
    component: 'Input',
    componentProps: {
      placeholder: '请输入联系电话',
    },
    colProps: { span: 8 },
  },
  {
    label: 'Email',
    field: 'email',
    component: 'Input',
    componentProps: {
      placeholder: '请输入Email',
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

// ========== 审核/处理相关表单 ==========

/**
 * 审核表单配置
 */
export const auditFormSchema: FormSchema[] = [
  {
    label: '审核意见',
    field: 'comment',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入审核意见（选填）',
      rows: 3,
    },
    colProps: { span: 24 },
  },
];

/**
 * 退回表单配置
 */
export const rejectFormSchema: FormSchema[] = [
  {
    label: '退回原因',
    field: 'comment',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      placeholder: '请输入退回原因',
      rows: 3,
    },
    colProps: { span: 24 },
  },
];

/**
 * 直接结案表单配置
 */
export const closeFormSchema: FormSchema[] = [
  {
    label: '结案意见',
    field: 'comment',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入结案意见（选填）',
      rows: 3,
    },
    colProps: { span: 24 },
  },
];

/**
 * 要求整改表单配置
 */
export const requireRectifyFormSchema: FormSchema[] = [
  {
    label: '整改要求',
    field: 'requirement',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      placeholder: '请输入整改要求',
      rows: 4,
    },
    colProps: { span: 24 },
  },
  {
    label: '整改期限',
    field: 'deadline',
    component: 'DatePicker',
    required: true,
    componentProps: {
      placeholder: '请选择整改期限',
      valueFormat: 'YYYY-MM-DD',
      style: { width: '100%' },
    },
    colProps: { span: 12 },
  },
];

/**
 * 提交整改表单配置
 */
export const submitRectifyFormSchema: FormSchema[] = [
  {
    label: '整改措施',
    field: 'measures',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      placeholder: '请输入整改措施',
      rows: 4,
    },
    colProps: { span: 24 },
  },
  {
    label: '完成情况',
    field: 'completion',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      placeholder: '请输入完成情况',
      rows: 4,
    },
    colProps: { span: 24 },
  },
];

/**
 * 确认整改表单配置
 */
export const confirmRectifyFormSchema: FormSchema[] = [
  {
    label: '确认意见',
    field: 'comment',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入确认意见（选填）',
      rows: 3,
    },
    colProps: { span: 24 },
  },
];

/**
 * 退回整改表单配置
 */
export const rejectRectifyFormSchema: FormSchema[] = [
  {
    label: '退回原因',
    field: 'comment',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      placeholder: '请输入退回原因',
      rows: 3,
    },
    colProps: { span: 24 },
  },
];
