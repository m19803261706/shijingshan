/**
 * 药品不良反应报告 API
 * @description 药品不良反应报告相关接口
 * @author TC Agent
 * @since 2025-12-19
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 报告列表
  list = '/adverse/drugReport/list',
  myList = '/adverse/drugReport/myList',
  // 报告详情
  queryById = '/adverse/drugReport/queryById',
  queryDetailById = '/adverse/drugReport/queryDetailById',
  // 子表查询
  suspectDrugs = '/adverse/drugReport/suspectDrugs',
  concomitantDrugs = '/adverse/drugReport/concomitantDrugs',
  // 报告操作
  add = '/adverse/drugReport/add',
  edit = '/adverse/drugReport/edit',
  delete = '/adverse/drugReport/delete',
  deleteBatch = '/adverse/drugReport/deleteBatch',
  submit = '/adverse/drugReport/submit',
  saveDraft = '/adverse/drugReport/saveDraft',
  canEdit = '/adverse/drugReport/canEdit',
}

/**
 * 获取报告列表（分页）
 * @param params 查询参数
 */
export const getDrugReportList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 获取我的报告列表（分页）
 * @param params 查询参数
 */
export const getMyDrugReportList = (params) => {
  return defHttp.get({ url: Api.myList, params });
};

/**
 * 根据 ID 查询报告详情
 * @param params { id: string }
 */
export const queryDrugReportById = (params) => {
  return defHttp.get({ url: Api.queryById, params });
};

/**
 * 根据 ID 查询报告详情（含子表）
 * @param params { id: string }
 */
export const queryDrugReportDetailById = (params) => {
  return defHttp.get({ url: Api.queryDetailById, params });
};

/**
 * 查询怀疑药品列表
 * @param params { reportId: string }
 */
export const getSuspectDrugs = (params) => {
  return defHttp.get({ url: Api.suspectDrugs, params });
};

/**
 * 查询并用药品列表
 * @param params { reportId: string }
 */
export const getConcomitantDrugs = (params) => {
  return defHttp.get({ url: Api.concomitantDrugs, params });
};

/**
 * 新增报告（含子表）
 * @param data 报告数据（请求体）
 */
export const addDrugReport = (data) => {
  return defHttp.post({ url: Api.add, data });
};

/**
 * 编辑报告（含子表）
 * @param data 报告数据（请求体）
 */
export const editDrugReport = (data) => {
  return defHttp.post({ url: Api.edit, data });
};

/**
 * 保存草稿（含子表）
 * @param data 报告数据（请求体）
 */
export const saveDrugReportDraft = (data) => {
  return defHttp.post({ url: Api.saveDraft, data });
};

/**
 * 提交报告
 * @param id 报告ID
 */
export const submitDrugReport = (id: string) => {
  return defHttp.post({ url: `${Api.submit}?id=${id}` });
};

/**
 * 删除报告
 * @param id 报告ID
 */
export const deleteDrugReport = (id: string) => {
  return defHttp.delete({ url: `${Api.delete}?id=${id}` });
};

/**
 * 批量删除报告
 * @param ids 报告ID列表，逗号分隔
 */
export const batchDeleteDrugReport = (ids: string) => {
  return defHttp.delete({ url: `${Api.deleteBatch}?ids=${ids}` });
};

/**
 * 校验报告是否可编辑
 * @param params { id: string }
 */
export const canEditDrugReport = (params) => {
  return defHttp.get({ url: Api.canEdit, params });
};
