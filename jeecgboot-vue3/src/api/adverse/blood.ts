/**
 * 输血使用不良事件报告 API
 * @description 输血使用不良事件报告相关接口
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/81
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 报告列表
  list = '/adverse/blood/report/list',
  myList = '/adverse/blood/report/myList',
  // 报告详情
  queryById = '/adverse/blood/report/queryById',
  // 报告操作
  add = '/adverse/blood/report/add',
  edit = '/adverse/blood/report/edit',
  delete = '/adverse/blood/report/delete',
  deleteBatch = '/adverse/blood/report/deleteBatch',
  submit = '/adverse/blood/report/submit',
  saveDraft = '/adverse/blood/report/saveDraft',
  canEdit = '/adverse/blood/report/canEdit',
  // 流转历史
  flowHistory = '/adverse/blood/report/flowHistory',
  // 整改历史
  rectifyHistory = '/adverse/blood/report/rectifyHistory',
}

/**
 * 获取报告列表（分页）
 * @param params 查询参数
 */
export const getBloodReportList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 获取我的报告列表（分页）
 * @param params 查询参数
 */
export const getMyBloodReportList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.myList, params });
};

/**
 * 根据 ID 查询报告详情
 * @param id 报告ID
 */
export const queryBloodReportById = (id: string) => {
  return defHttp.get({ url: Api.queryById, params: { id } });
};

/**
 * 新增报告
 * @param data 报告数据（请求体）
 */
export const addBloodReport = (data: Record<string, any>) => {
  return defHttp.post({ url: Api.add, data });
};

/**
 * 编辑报告
 * @param data 报告数据（请求体）
 */
export const editBloodReport = (data: Record<string, any>) => {
  return defHttp.post({ url: Api.edit, data });
};

/**
 * 保存草稿
 * @param data 报告数据（请求体）
 */
export const saveBloodReportDraft = (data: Record<string, any>) => {
  return defHttp.post({ url: Api.saveDraft, data });
};

/**
 * 提交报告
 * @param id 报告ID
 */
export const submitBloodReport = (id: string) => {
  return defHttp.post({ url: `${Api.submit}?id=${id}` });
};

/**
 * 删除报告
 * @param id 报告ID
 */
export const deleteBloodReport = (id: string) => {
  return defHttp.delete({ url: `${Api.delete}?id=${id}` });
};

/**
 * 批量删除报告
 * @param ids 报告ID列表，逗号分隔
 */
export const batchDeleteBloodReport = (ids: string) => {
  return defHttp.delete({ url: `${Api.deleteBatch}?ids=${ids}` });
};

/**
 * 校验报告是否可编辑
 * @param id 报告ID
 */
export const canEditBloodReport = (id: string) => {
  return defHttp.get({ url: Api.canEdit, params: { id } });
};

/**
 * 获取流转历史
 * @param reportId 报告ID
 */
export const getBloodFlowHistory = (reportId: string) => {
  return defHttp.get({ url: Api.flowHistory, params: { reportId } });
};

/**
 * 获取整改历史
 * @param reportId 报告ID
 */
export const getBloodRectifyHistory = (reportId: string) => {
  return defHttp.get({ url: Api.rectifyHistory, params: { reportId } });
};
