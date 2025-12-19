/**
 * 输血使用不良事件临床整改数据配置
 * @description 表格列、搜索表单配置
 * @author TC Agent
 * @since 2025-12-20
 * @see https://github.com/m19803261706/shijingshan/issues/87
 */
import { BasicColumn, FormSchema } from '/@/components/Table';
import { render } from '/@/utils/common/renderUtils';
import {
  eventNameOptions,
  severityLevelOptions,
  eventLevelOptions,
} from '../../blood/blood.data';

/**
 * 整改状态映射
 */
export const rectifyStatusOptions = [
  { label: '待整改', value: 'pending', color: 'warning' },
  { label: '已提交', value: 'submitted', color: 'processing' },
  { label: '已退回', value: 'rejected', color: 'error' },
  { label: '已通过', value: 'approved', color: 'success' },
];

/**
 * 待整改列表表格列配置
 */
export const pendingColumns: BasicColumn[] = [
  {
    title: '报告编号',
    dataIndex: 'reportCode',
    width: 160,
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
    width: 100,
    customRender: ({ text }) => {
      const item = eventLevelOptions.find((i) => i.value === text);
      return render.renderTag(item?.label || text, item?.color || 'default');
    },
  },
  {
    title: '患者姓名',
    dataIndex: 'patientName',
    width: 100,
  },
  {
    title: '整改期限',
    dataIndex: 'deadline',
    width: 160,
    slots: { customRender: 'deadline' },
  },
  {
    title: '整改状态',
    dataIndex: 'rectifyStatus',
    width: 100,
    slots: { customRender: 'rectifyStatus' },
  },
  {
    title: '涉及科室',
    dataIndex: 'involvedDept',
    width: 120,
  },
];

/**
 * 已提交列表表格列配置
 */
export const submittedColumns: BasicColumn[] = [
  {
    title: '报告编号',
    dataIndex: 'reportCode',
    width: 160,
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
    title: '患者姓名',
    dataIndex: 'patientName',
    width: 100,
  },
  {
    title: '整改状态',
    dataIndex: 'rectifyStatus',
    width: 100,
    slots: { customRender: 'rectifyStatus' },
  },
  {
    title: '提交时间',
    dataIndex: 'submitTime',
    width: 160,
  },
  {
    title: '确认结果',
    dataIndex: 'confirmResult',
    width: 120,
    slots: { customRender: 'confirmResult' },
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
