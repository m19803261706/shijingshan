/**
 * 药品不良反应报告数据配置
 * @description 表格列、搜索表单配置
 * @author TC Agent
 * @since 2025-12-19
 */
import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

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
