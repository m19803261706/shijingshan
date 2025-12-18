/**
 * 不良事件数据配置
 * @description 表格列、搜索表单、编辑表单配置
 * @author TC Agent
 * @since 2025-12-18
 */
import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

/**
 * 事件状态映射
 */
export const eventStatusOptions = [
  { label: '草稿', value: 'draft', color: 'default' },
  { label: '待审核', value: 'pending_audit', color: 'processing' },
  { label: '已退回', value: 'returned', color: 'warning' },
  { label: '待处理', value: 'pending_process', color: 'processing' },
  { label: '待整改', value: 'pending_rectify', color: 'warning' },
  { label: '整改中', value: 'rectifying', color: 'processing' },
  { label: '已结案', value: 'closed', color: 'success' },
];

/**
 * 事件级别映射
 */
export const eventLevelOptions = [
  { label: '一级（警告事件）', value: 'level_1', color: 'red' },
  { label: '二级（不良后果事件）', value: 'level_2', color: 'orange' },
  { label: '三级（未遂事件）', value: 'level_3', color: 'blue' },
  { label: '四级（隐患事件）', value: 'level_4', color: 'green' },
];

/**
 * 表格列配置
 */
export const columns: BasicColumn[] = [
  {
    title: '事件编号',
    dataIndex: 'eventCode',
    width: 150,
  },
  {
    title: '事件标题',
    dataIndex: 'title',
    width: 200,
  },
  {
    title: '事件分类',
    dataIndex: 'categoryId_dictText',
    width: 120,
  },
  {
    title: '事件级别',
    dataIndex: 'level',
    width: 140,
    customRender: ({ text }) => {
      const item = eventLevelOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      const item = eventStatusOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '发生时间',
    dataIndex: 'occurTime',
    width: 160,
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
    label: '事件编号',
    field: 'eventCode',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '事件标题',
    field: 'title',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    label: '事件状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: eventStatusOptions,
      placeholder: '请选择状态',
    },
    colProps: { span: 6 },
  },
  {
    label: '事件级别',
    field: 'level',
    component: 'Select',
    componentProps: {
      options: eventLevelOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择级别',
    },
    colProps: { span: 6 },
  },
];

/**
 * 事件表单配置
 */
export const eventFormSchema: FormSchema[] = [
  // ========== 基本信息 ==========
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '事件标题',
    field: 'title',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入事件标题',
    },
    colProps: { span: 24 },
  },
  {
    label: '事件分类',
    field: 'categoryId',
    component: 'TreeSelect',
    required: true,
    componentProps: {
      placeholder: '请选择事件分类',
      fieldNames: {
        label: 'title',
        key: 'key',
        value: 'key',
      },
      getPopupContainer: () => document.body,
    },
    colProps: { span: 12 },
  },
  {
    label: '事件级别',
    field: 'level',
    component: 'Select',
    required: true,
    componentProps: {
      options: eventLevelOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择事件级别',
    },
    colProps: { span: 12 },
  },
  {
    label: '发生时间',
    field: 'occurTime',
    component: 'DatePicker',
    required: true,
    componentProps: {
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      placeholder: '请选择发生时间',
      style: { width: '100%' },
    },
    colProps: { span: 12 },
  },
  {
    label: '发生地点',
    field: 'occurLocation',
    component: 'Input',
    componentProps: {
      placeholder: '请输入发生地点',
    },
    colProps: { span: 12 },
  },

  // ========== 患者信息 ==========
  {
    label: '患者姓名',
    field: 'patientName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入患者姓名（可选）',
    },
    colProps: { span: 8 },
  },
  {
    label: '患者性别',
    field: 'patientGender',
    component: 'JDictSelectTag',
    componentProps: {
      dictCode: 'sex',
      placeholder: '请选择性别',
    },
    colProps: { span: 8 },
  },
  {
    label: '患者年龄',
    field: 'patientAge',
    component: 'InputNumber',
    componentProps: {
      placeholder: '请输入年龄',
      min: 0,
      max: 200,
    },
    colProps: { span: 8 },
  },
  {
    label: '住院号',
    field: 'patientAdmissionNo',
    component: 'Input',
    componentProps: {
      placeholder: '请输入住院号（可选）',
    },
    colProps: { span: 8 },
  },
  {
    label: '床号',
    field: 'patientBedNo',
    component: 'Input',
    componentProps: {
      placeholder: '请输入床号（可选）',
    },
    colProps: { span: 8 },
  },

  // ========== 事件描述 ==========
  {
    label: '事件经过',
    field: 'description',
    component: 'InputTextArea',
    required: true,
    componentProps: {
      placeholder: '请详细描述事件经过',
      rows: 4,
    },
    colProps: { span: 24 },
  },
  {
    label: '原因分析',
    field: 'causeAnalysis',
    component: 'InputTextArea',
    componentProps: {
      placeholder: '请分析事件原因（可选）',
      rows: 3,
    },
    colProps: { span: 24 },
  },
  {
    label: '附件',
    field: 'attachments',
    component: 'JUpload',
    componentProps: {
      text: '上传附件',
      maxCount: 5,
    },
    colProps: { span: 24 },
  },
];
