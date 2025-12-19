/**
 * 医疗器械不良事件器械科处理数据配置
 * @description 表格列、搜索表单配置
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #70
 */
import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';

/**
 * 报告状态映射
 */
export const processStatusOptions = [
  { label: '待处理', value: 'pending_process', color: 'processing' },
  { label: '待整改', value: 'pending_rectify', color: 'warning' },
  { label: '整改中', value: 'rectifying', color: 'blue' },
  { label: '已结案', value: 'closed', color: 'success' },
];

/**
 * 结案方式映射
 */
export const closeTypeOptions = [
  { label: '直接结案', value: 'direct', color: 'success' },
  { label: '整改结案', value: 'rectify', color: 'blue' },
];

/**
 * 整改状态映射
 */
export const rectifyStatusOptions = [
  { label: '待整改', value: 'pending', color: 'warning' },
  { label: '已提交', value: 'submitted', color: 'processing' },
  { label: '已通过', value: 'approved', color: 'success' },
  { label: '已退回', value: 'rejected', color: 'error' },
];

/**
 * 待处理列表表格列配置
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
    width: 180,
    ellipsis: true,
  },
  {
    title: '事件发生日期',
    dataIndex: 'eventOccurDate',
    width: 120,
  },
  {
    title: '审核通过时间',
    dataIndex: 'auditTime',
    width: 160,
  },
];

/**
 * 待确认列表表格列配置
 */
export const confirmingColumns: BasicColumn[] = [
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
    width: 150,
    ellipsis: true,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      const item = processStatusOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '处理人',
    dataIndex: 'processUserName',
    width: 100,
  },
  {
    title: '下发时间',
    dataIndex: 'processTime',
    width: 160,
  },
];

/**
 * 已结案列表表格列配置
 */
export const closedColumns: BasicColumn[] = [
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
    width: 150,
    ellipsis: true,
  },
  {
    title: '结案方式',
    dataIndex: 'closeType',
    width: 100,
    customRender: ({ text }) => {
      const item = closeTypeOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '结案人',
    dataIndex: 'closeUserName',
    width: 100,
  },
  {
    title: '结案时间',
    dataIndex: 'closeTime',
    width: 160,
  },
  {
    title: '结案意见',
    dataIndex: 'closeComment',
    width: 200,
    ellipsis: true,
  },
];

/**
 * 待处理搜索表单配置
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
 * 已结案搜索表单配置
 */
export const closedSearchFormSchema: FormSchema[] = [
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
    label: '结案方式',
    field: 'closeType',
    component: 'Select',
    componentProps: {
      options: closeTypeOptions.map((item) => ({ label: item.label, value: item.value })),
      placeholder: '请选择结案方式',
      allowClear: true,
    },
    colProps: { span: 6 },
  },
];

/**
 * 整改记录表格列配置
 */
export const rectifyHistoryColumns: BasicColumn[] = [
  {
    title: '整改要求',
    dataIndex: 'requirement',
    width: 200,
    ellipsis: true,
  },
  {
    title: '整改期限',
    dataIndex: 'deadline',
    width: 120,
  },
  {
    title: '下发人',
    dataIndex: 'requireUserName',
    width: 100,
  },
  {
    title: '下发时间',
    dataIndex: 'requireTime',
    width: 160,
  },
  {
    title: '整改措施',
    dataIndex: 'measures',
    width: 200,
    ellipsis: true,
  },
  {
    title: '完成情况',
    dataIndex: 'completion',
    width: 150,
    ellipsis: true,
  },
  {
    title: '提交时间',
    dataIndex: 'submitTime',
    width: 160,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      const item = rectifyStatusOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '确认意见',
    dataIndex: 'confirmComment',
    width: 150,
    ellipsis: true,
  },
];
