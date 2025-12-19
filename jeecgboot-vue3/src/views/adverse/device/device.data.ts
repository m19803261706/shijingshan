/**
 * 医疗器械不良事件报告数据配置
 * @description 按照国标《可疑医疗器械不良事件报告表》1:1复刻
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #68
 */
import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

// ==================== 选项配置 ====================

/**
 * 报告状态映射
 */
export const deviceReportStatusOptions = [
  { label: '草稿', value: 'draft', color: 'default' },
  { label: '待审核', value: 'pending_audit', color: 'processing' },
  { label: '已退回', value: 'returned', color: 'warning' },
  { label: '待处理', value: 'pending_process', color: 'processing' },
  { label: '待整改', value: 'pending_rectify', color: 'orange' },
  { label: '整改中', value: 'rectifying', color: 'processing' },
  { label: '已结案', value: 'closed', color: 'success' },
];

/**
 * 性别选项
 */
export const genderOptions = [
  { label: '男', value: 'M' },
  { label: '女', value: 'F' },
];

/**
 * 器械使用场所选项
 */
export const deviceUsePlaceOptions = [
  { label: '医疗机构', value: 'hospital' },
  { label: '家庭', value: 'home' },
  { label: '其它', value: 'other' },
];

/**
 * 操作人类型选项
 */
export const operatorTypeOptions = [
  { label: '专业人员', value: 'professional' },
  { label: '非专业人员', value: 'non_professional' },
  { label: '患者', value: 'patient' },
  { label: '其它', value: 'other' },
];

/**
 * 报告人类型选项
 */
export const reporterTypeOptions = [
  { label: '医师', value: 'doctor' },
  { label: '技师', value: 'technician' },
  { label: '护士', value: 'nurse' },
  { label: '其他', value: 'other' },
];

/**
 * 评价结果选项（是/否/无法确定）
 */
export const evalResultOptions = [
  { label: '是', value: 'yes' },
  { label: '否', value: 'no' },
  { label: '无法确定', value: 'unknown' },
];

/**
 * 是/否选项
 */
export const yesNoOptions = [
  { label: '是', value: 'yes' },
  { label: '否', value: 'no' },
];

// ==================== 表格列配置 ====================

/**
 * 报告列表表格列配置
 */
export const columns: BasicColumn[] = [
  {
    title: '报告编号',
    dataIndex: 'reportCode',
    width: 150,
  },
  {
    title: '患者姓名',
    dataIndex: 'patientName',
    width: 100,
  },
  {
    title: '产品名称',
    dataIndex: 'productName',
    width: 150,
  },
  {
    title: '事件主要表现',
    dataIndex: 'eventManifestation',
    width: 200,
    ellipsis: true,
  },
  {
    title: '事件发生日期',
    dataIndex: 'eventOccurDate',
    width: 120,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      const item = deviceReportStatusOptions.find((i) => i.value === text);
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
    label: '产品名称',
    field: 'productName',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: deviceReportStatusOptions,
      placeholder: '请选择状态',
    },
    colProps: { span: 6 },
  },
];

// ==================== 表单 Schema 配置 ====================

/**
 * A. 患者资料表单
 */
export const patientInfoFormSchema: FormSchema[] = [
  {
    label: '1. 姓名',
    field: 'patientName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入患者姓名',
    },
    colProps: { span: 6 },
  },
  {
    label: '2. 年龄',
    field: 'patientAge',
    component: 'InputNumber',
    componentProps: {
      placeholder: '请输入年龄',
      min: 0,
      max: 150,
      style: { width: '100%' },
    },
    colProps: { span: 6 },
  },
  {
    label: '3. 性别',
    field: 'patientGender',
    component: 'RadioGroup',
    componentProps: {
      options: genderOptions,
    },
    colProps: { span: 6 },
  },
  {
    label: '4. 预期治疗疾病或作用',
    field: 'patientDisease',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请输入预期治疗疾病或作用',
      rows: 2,
    },
    colProps: { span: 24 },
  },
];

/**
 * B. 不良事件情况表单
 */
export const eventInfoFormSchema: FormSchema[] = [
  {
    label: '5. 事件主要表现',
    field: 'eventManifestation',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      placeholder: '请描述不良事件的主要表现',
      rows: 3,
    },
    colProps: { span: 24 },
  },
  {
    label: '6. 事件发生日期',
    field: 'eventOccurDate',
    component: 'DatePicker',
    required: true,
    componentProps: {
      placeholder: '请选择事件发生日期',
      valueFormat: 'YYYY-MM-DD',
      style: { width: '100%' },
    },
    colProps: { span: 8 },
  },
  {
    label: '7. 发现或者知悉时间',
    field: 'eventDiscoverDate',
    component: 'DatePicker',
    componentProps: {
      placeholder: '请选择发现时间',
      valueFormat: 'YYYY-MM-DD',
      style: { width: '100%' },
    },
    colProps: { span: 8 },
  },
  {
    label: '8. 医疗器械实际使用场所',
    field: 'deviceUsePlace',
    component: 'RadioGroup',
    componentProps: {
      options: deviceUsePlaceOptions,
    },
    colProps: { span: 24 },
  },
  {
    label: '其它场所说明',
    field: 'deviceUsePlaceOther',
    component: 'Input',
    componentProps: {
      placeholder: '请说明其它使用场所',
    },
    colProps: { span: 24 },
    ifShow: ({ values }) => values.deviceUsePlace === 'other',
  },
];

/**
 * B. 不良事件后果表单（9. 事件后果，多选）
 */
export const eventResultFormSchema: FormSchema[] = [
  {
    label: '死亡',
    field: 'resultDeath',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '死亡时间',
    field: 'resultDeathTime',
    component: 'DatePicker',
    componentProps: {
      placeholder: '请选择死亡时间',
      showTime: true,
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      style: { width: '100%' },
    },
    colProps: { span: 6 },
    ifShow: ({ values }) => values.resultDeath === true || values.resultDeath === 1,
  },
  {
    label: '危及生命',
    field: 'resultLifeRisk',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '导致或延长住院',
    field: 'resultHospitalization',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '机体功能结构永久性损伤',
    field: 'resultPermanentDamage',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '可能导致机体功能结构永久性损伤',
    field: 'resultInterventionNeeded',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '需要内、外科治疗避免上述永久损伤',
    field: 'resultSurgicalAvoid',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '其它',
    field: 'resultOther',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '其它后果描述',
    field: 'resultOtherDesc',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请描述其它后果',
      rows: 2,
    },
    colProps: { span: 24 },
    ifShow: ({ values }) => values.resultOther === true || values.resultOther === 1,
  },
];

/**
 * B. 事件陈述表单（10. 事件陈述）
 */
export const eventStatementFormSchema: FormSchema[] = [
  {
    label: '10. 事件陈述',
    field: 'eventStatement',
    component: 'InputTextArea',
    componentProps: {
      placeholder:
        '至少包括器械使用时间、使用目的、不良事件情况、对受害者影响、采取的治疗措施、器械联合使用情况',
      rows: 5,
    },
    colProps: { span: 24 },
  },
];

/**
 * C. 医疗器械情况表单
 */
export const deviceInfoFormSchema: FormSchema[] = [
  {
    label: '11. 产品名称',
    field: 'productName',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入产品名称',
    },
    colProps: { span: 8 },
  },
  {
    label: '12. 商品名称',
    field: 'tradeName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入商品名称',
    },
    colProps: { span: 8 },
  },
  {
    label: '13. 注册证号',
    field: 'registrationNo',
    component: 'Input',
    componentProps: {
      placeholder: '请输入注册证号',
    },
    colProps: { span: 8 },
  },
  {
    label: '14. 生产企业名称',
    field: 'manufacturerName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入生产企业名称',
    },
    colProps: { span: 8 },
  },
  {
    label: '生产企业地址',
    field: 'manufacturerAddress',
    component: 'Input',
    componentProps: {
      placeholder: '请输入生产企业地址',
    },
    colProps: { span: 8 },
  },
  {
    label: '企业联系方式',
    field: 'manufacturerContact',
    component: 'Input',
    componentProps: {
      placeholder: '请输入企业联系方式',
    },
    colProps: { span: 8 },
  },
  {
    label: '15. 型号规格',
    field: 'modelSpec',
    component: 'Input',
    componentProps: {
      placeholder: '请输入型号规格',
    },
    colProps: { span: 8 },
  },
  {
    label: '产品编号',
    field: 'productCode',
    component: 'Input',
    componentProps: {
      placeholder: '请输入产品编号',
    },
    colProps: { span: 8 },
  },
  {
    label: '产品批号',
    field: 'productBatch',
    component: 'Input',
    componentProps: {
      placeholder: '请输入产品批号',
    },
    colProps: { span: 8 },
  },
  {
    label: '16. 操作人',
    field: 'operatorType',
    component: 'RadioGroup',
    componentProps: {
      options: operatorTypeOptions,
    },
    colProps: { span: 24 },
  },
  {
    label: '其它操作人说明',
    field: 'operatorTypeOther',
    component: 'Input',
    componentProps: {
      placeholder: '请说明其它操作人类型',
    },
    colProps: { span: 24 },
    ifShow: ({ values }) => values.operatorType === 'other',
  },
  {
    label: '17. 有效期至',
    field: 'validPeriodTo',
    component: 'DatePicker',
    componentProps: {
      placeholder: '请选择有效期',
      valueFormat: 'YYYY-MM-DD',
      style: { width: '100%' },
    },
    colProps: { span: 8 },
  },
  {
    label: '生产日期',
    field: 'productionDate',
    component: 'DatePicker',
    componentProps: {
      placeholder: '请选择生产日期',
      valueFormat: 'YYYY-MM-DD',
      style: { width: '100%' },
    },
    colProps: { span: 8 },
  },
  {
    label: '19. 停用日期',
    field: 'deactivateDate',
    component: 'DatePicker',
    componentProps: {
      placeholder: '请选择停用日期',
      valueFormat: 'YYYY-MM-DD',
      style: { width: '100%' },
    },
    colProps: { span: 8 },
  },
  {
    label: '20. 植入日期（若植入）',
    field: 'implantDate',
    component: 'DatePicker',
    componentProps: {
      placeholder: '请选择植入日期',
      valueFormat: 'YYYY-MM-DD',
      style: { width: '100%' },
    },
    colProps: { span: 8 },
  },
];

/**
 * C. 事件发生初步原因分析和处理情况表单
 */
export const causeAnalysisFormSchema: FormSchema[] = [
  {
    label: '21. 事件发生初步原因分析',
    field: 'causeAnalysis',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请描述事件发生的初步原因分析',
      rows: 3,
    },
    colProps: { span: 24 },
  },
  {
    label: '22. 事件初步处理情况',
    field: 'initialHandling',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请描述事件的初步处理情况',
      rows: 3,
    },
    colProps: { span: 24 },
  },
];

/**
 * C. 事件报告状态表单（23. 事件报告状态）
 */
export const reportStatusFormSchema: FormSchema[] = [
  {
    label: '已通知使用单位',
    field: 'reportStatusUseUnit',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '已通知经营企业',
    field: 'reportStatusBusiness',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '已通知生产企业',
    field: 'reportStatusManufacturer',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
  {
    label: '已通知药监部门',
    field: 'reportStatusFda',
    component: 'Checkbox',
    colProps: { span: 6 },
  },
];

/**
 * D. 不良事件评价表单（三个必填评价问题）
 */
export const evaluationFormSchema: FormSchema[] = [
  {
    label: '*1. 医疗器械与不良事件发生在时间先后顺序上是否合理',
    field: 'evalTimeSequence',
    component: 'RadioGroup',
    required: true,
    componentProps: {
      options: [
        { label: '是', value: 'yes' },
        { label: '否', value: 'no' },
      ],
    },
    colProps: { span: 24 },
  },
  {
    label: '*2. 伤害事件是否属于该医疗器械可能导致的伤害类型',
    field: 'evalHarmType',
    component: 'RadioGroup',
    required: true,
    componentProps: {
      options: evalResultOptions,
    },
    colProps: { span: 24 },
  },
  {
    label: '*3. 伤害事件是否可以用合并用药和/或械的作用、患者病情或其他非医疗器械因素来解释',
    field: 'evalExcludeOther',
    component: 'RadioGroup',
    required: true,
    componentProps: {
      options: evalResultOptions,
    },
    colProps: { span: 24 },
  },
];

/**
 * 报告人信息表单
 */
export const reporterInfoFormSchema: FormSchema[] = [
  {
    label: '报告人类型',
    field: 'reporterType',
    component: 'RadioGroup',
    componentProps: {
      options: reporterTypeOptions,
    },
    colProps: { span: 24 },
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
