/**
 * 常用医疗器械配置 - 表格和表单配置
 * @author TC Agent
 * @since 2025-12-19
 */
import { BasicColumn, FormSchema } from '/@/components/Table';

/**
 * 表格列配置
 */
export const columns: BasicColumn[] = [
  {
    title: '产品名称',
    dataIndex: 'productName',
    width: 180,
  },
  {
    title: '商品名称',
    dataIndex: 'tradeName',
    width: 150,
  },
  {
    title: '注册证号',
    dataIndex: 'registrationNo',
    width: 150,
  },
  {
    title: '生产企业',
    dataIndex: 'manufacturerName',
    width: 180,
  },
  {
    title: '型号规格',
    dataIndex: 'modelSpec',
    width: 120,
  },
  {
    title: '使用次数',
    dataIndex: 'useCount',
    width: 80,
    align: 'center',
  },
  {
    title: '最后使用时间',
    dataIndex: 'lastUsedTime',
    width: 160,
  },
];

/**
 * 搜索表单配置
 */
export const searchFormSchema: FormSchema[] = [
  {
    label: '产品名称',
    field: 'productName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入产品名称',
    },
    colProps: { span: 6 },
  },
  {
    label: '生产企业',
    field: 'manufacturerName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入生产企业',
    },
    colProps: { span: 6 },
  },
  {
    label: '注册证号',
    field: 'registrationNo',
    component: 'Input',
    componentProps: {
      placeholder: '请输入注册证号',
    },
    colProps: { span: 6 },
  },
];

/**
 * 编辑表单配置
 */
export const formSchema: FormSchema[] = [
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
  {
    label: '产品名称',
    field: 'productName',
    component: 'Input',
    required: true,
    componentProps: {
      placeholder: '请输入产品名称',
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
    label: '注册证号',
    field: 'registrationNo',
    component: 'Input',
    componentProps: {
      placeholder: '请输入注册证号',
    },
  },
  {
    label: '生产企业',
    field: 'manufacturerName',
    component: 'Input',
    componentProps: {
      placeholder: '请输入生产企业名称',
    },
  },
  {
    label: '企业地址',
    field: 'manufacturerAddress',
    component: 'Input',
    componentProps: {
      placeholder: '请输入生产企业地址',
    },
  },
  {
    label: '联系方式',
    field: 'manufacturerContact',
    component: 'Input',
    componentProps: {
      placeholder: '请输入企业联系方式',
    },
  },
  {
    label: '型号规格',
    field: 'modelSpec',
    component: 'Input',
    componentProps: {
      placeholder: '请输入型号规格',
    },
  },
  {
    label: '产品编号',
    field: 'productCode',
    component: 'Input',
    componentProps: {
      placeholder: '请输入产品编号',
    },
  },
  {
    label: '产品批号',
    field: 'productBatch',
    component: 'Input',
    componentProps: {
      placeholder: '请输入产品批号',
    },
  },
];
