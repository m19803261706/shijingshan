/**
 * 医疗器械不良事件临床科室审核数据配置
 * @description 表格列、搜索表单配置
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #69
 */
import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

/**
 * 报告状态映射
 */
export const deviceReportStatusOptions = [
  { label: '草稿', value: 'draft', color: 'default' },
  { label: '待审核', value: 'pending_audit', color: 'processing' },
  { label: '已退回', value: 'returned', color: 'warning' },
  { label: '待处理', value: 'pending_process', color: 'processing' },
  { label: '待整改', value: 'pending_rectify', color: 'orange' },
  { label: '整改中', value: 'rectifying', color: 'cyan' },
  { label: '已结案', value: 'closed', color: 'success' },
];

/**
 * 已审核状态映射（仅用于已审核列表）
 */
export const completedStatusOptions = [
  { label: '待处理', value: 'pending_process', color: 'processing' },
  { label: '已退回', value: 'returned', color: 'warning' },
  { label: '待整改', value: 'pending_rectify', color: 'orange' },
  { label: '整改中', value: 'rectifying', color: 'cyan' },
  { label: '已结案', value: 'closed', color: 'success' },
];

/**
 * 器械使用场所映射
 */
export const deviceUsePlaceOptions = [
  { label: '医疗机构', value: 'hospital' },
  { label: '家庭', value: 'home' },
  { label: '其它', value: 'other' },
];

/**
 * 操作人类型映射
 */
export const operatorTypeOptions = [
  { label: '医务人员', value: 'medical_staff' },
  { label: '患者', value: 'patient' },
  { label: '其它', value: 'other' },
];

/**
 * 待审核列表表格列配置
 */
export const pendingColumns: BasicColumn[] = [
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
    title: '生产企业',
    dataIndex: 'manufacturerName',
    width: 150,
    ellipsis: true,
  },
  {
    title: '报告日期',
    dataIndex: 'reportDate',
    width: 120,
  },
  {
    title: '上报人',
    dataIndex: 'createBy_dictText',
    width: 100,
  },
  {
    title: '提交时间',
    dataIndex: 'createTime',
    width: 160,
  },
];

/**
 * 已审核列表表格列配置
 */
export const completedColumns: BasicColumn[] = [
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
    width: 180,
    ellipsis: true,
  },
  {
    title: '审核状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      const item = completedStatusOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '审核意见',
    dataIndex: 'auditComment',
    width: 200,
    ellipsis: true,
  },
  {
    title: '审核时间',
    dataIndex: 'auditTime',
    width: 160,
  },
];

/**
 * 待审核搜索表单配置
 */
export const pendingSearchFormSchema: FormSchema[] = [
  {
    label: '报告编号',
    field: 'reportCode',
    component: 'Input',
    componentProps: {
      placeholder: '请输入报告编号',
    },
    colProps: { span: 6 },
  },
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
    label: '产品名称',
    field: 'productName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入产品名称',
    },
    colProps: { span: 6 },
  },
];

/**
 * 已审核搜索表单配置
 */
export const completedSearchFormSchema: FormSchema[] = [
  {
    label: '报告编号',
    field: 'reportCode',
    component: 'Input',
    componentProps: {
      placeholder: '请输入报告编号',
    },
    colProps: { span: 6 },
  },
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
    label: '审核状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: completedStatusOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择状态',
      allowClear: true,
    },
    colProps: { span: 6 },
  },
];
