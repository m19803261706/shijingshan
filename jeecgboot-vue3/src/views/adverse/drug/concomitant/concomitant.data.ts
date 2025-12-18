/**
 * 常用并用药品配置 - 数据配置
 * <p>
 * 定义表格列、搜索表单、编辑表单的配置
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
import { BasicColumn, FormSchema } from '@/components/Table';

/**
 * 表格列配置
 */
export const columns: BasicColumn[] = [
  {
    title: '通用名称',
    dataIndex: 'genericName',
    width: 180,
  },
  {
    title: '商品名称',
    dataIndex: 'tradeName',
    width: 150,
  },
  {
    title: '生产厂家',
    dataIndex: 'manufacturer',
    width: 150,
  },
  {
    title: '批准文号',
    dataIndex: 'approvalNo',
    width: 120,
  },
  {
    title: '给药途径',
    dataIndex: 'route',
    width: 100,
  },
  {
    title: '剂型',
    dataIndex: 'dosageForm',
    width: 100,
  },
  {
    title: '规格',
    dataIndex: 'specification',
    width: 120,
  },
  {
    title: '用法用量',
    dataIndex: 'dosage',
    width: 120,
  },
  {
    title: '使用次数',
    dataIndex: 'useCount',
    width: 80,
    align: 'center',
    sorter: true,
    customRender: ({ text }) => {
      return text || 0;
    },
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    width: 160,
    sorter: true,
  },
];

/**
 * 搜索表单配置
 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '通用名称',
    field: 'genericName',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入通用名称',
    },
  },
  {
    label: '商品名称',
    field: 'tradeName',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入商品名称',
    },
  },
  {
    label: '生产厂家',
    field: 'manufacturer',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入生产厂家',
    },
  },
  {
    label: '批准文号',
    field: 'approvalNo',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      placeholder: '请输入批准文号',
    },
  },
];

/**
 * 新增/编辑表单配置
 */
export const formSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '通用名称',
    field: 'genericName',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入通用名称',
    },
  },
  {
    label: '商品名称',
    field: 'tradeName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入商品名称',
    },
  },
  {
    label: '生产厂家',
    field: 'manufacturer',
    component: 'Input',
    componentProps: {
      placeholder: '请输入生产厂家',
    },
  },
  {
    label: '批准文号',
    field: 'approvalNo',
    component: 'Input',
    componentProps: {
      placeholder: '请输入批准文号',
    },
  },
  {
    label: '给药途径',
    field: 'route',
    component: 'Input',
    componentProps: {
      placeholder: '请输入给药途径，如：口服、静脉注射',
    },
  },
  {
    label: '剂型',
    field: 'dosageForm',
    component: 'Input',
    componentProps: {
      placeholder: '请输入剂型，如：片剂、注射剂',
    },
  },
  {
    label: '规格',
    field: 'specification',
    component: 'Input',
    componentProps: {
      placeholder: '请输入规格',
    },
  },
  {
    label: '用法用量',
    field: 'dosage',
    component: 'Input',
    componentProps: {
      placeholder: '请输入用法用量',
    },
  },
];
