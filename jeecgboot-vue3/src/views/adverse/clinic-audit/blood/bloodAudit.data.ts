/**
 * 输血使用不良事件审核数据配置
 * @description 审核列表表格列和搜索表单配置
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/85
 */
import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';
import {
  eventNameOptions,
  severityLevelOptions,
  eventLevelOptions,
  bloodReportStatusOptions,
} from '../../blood/blood.data';

/**
 * 待审核列表表格列配置
 */
export const pendingColumns: BasicColumn[] = [
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
    title: '上报人',
    dataIndex: 'createBy_dictText',
    width: 100,
  },
  {
    title: '上报时间',
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
    width: 160,
  },
  {
    title: '患者姓名',
    dataIndex: 'patientName',
    width: 100,
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
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender: ({ text }) => {
      const item = bloodReportStatusOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '审核人',
    dataIndex: 'auditUserName',
    width: 100,
  },
  {
    title: '审核时间',
    dataIndex: 'auditTime',
    width: 160,
  },
];

/**
 * 待审核列表搜索表单配置
 */
export const pendingSearchFormSchema: FormSchema[] = [
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
];

/**
 * 已审核列表搜索表单配置
 */
export const completedSearchFormSchema: FormSchema[] = [
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
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: [
        { label: '已通过', value: 'pending_process' },
        { label: '已退回', value: 'returned' },
      ],
      placeholder: '请选择状态',
    },
    colProps: { span: 6 },
  },
];
