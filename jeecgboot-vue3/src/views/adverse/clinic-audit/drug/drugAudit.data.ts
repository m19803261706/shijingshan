/**
 * 药品不良反应临床科室审核数据配置
 * @description 表格列、搜索表单配置
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/46
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
 * 已审核状态映射（仅用于已审核列表）
 */
export const completedStatusOptions = [
  { label: '已通过', value: 'pending_process', color: 'success' },
  { label: '已退回', value: 'returned', color: 'warning' },
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
 * 待审核列表表格列配置
 */
export const pendingColumns: BasicColumn[] = [
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
    width: 120,
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
    title: '报告日期',
    dataIndex: 'reportDate',
    width: 120,
  },
  {
    title: '上报人',
    dataIndex: 'createBy',
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
    field: 'reportNo',
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
];

/**
 * 已审核搜索表单配置
 */
export const completedSearchFormSchema: FormSchema[] = [
  {
    label: '报告编号',
    field: 'reportNo',
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
