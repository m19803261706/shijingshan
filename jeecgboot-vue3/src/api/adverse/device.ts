/**
 * 医疗器械不良事件报告 API
 * @description 医疗器械不良事件报告相关接口
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #68
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 报告列表
  list = '/adverse/device/report/list',
  myList = '/adverse/device/report/myList',
  // 报告详情
  queryById = '/adverse/device/report/queryById',
  // 报告操作
  add = '/adverse/device/report/add',
  edit = '/adverse/device/report/edit',
  delete = '/adverse/device/report/delete',
  deleteBatch = '/adverse/device/report/deleteBatch',
  submit = '/adverse/device/report/submit',
  saveDraft = '/adverse/device/report/saveDraft',
  canEdit = '/adverse/device/report/canEdit',
}

/**
 * 获取报告列表（分页）
 * @param params 查询参数
 */
export const getDeviceReportList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 获取我的报告列表（分页）
 * @param params 查询参数
 */
export const getMyDeviceReportList = (params) => {
  return defHttp.get({ url: Api.myList, params });
};

/**
 * 根据 ID 查询报告详情
 * @param params { id: string }
 */
export const queryDeviceReportById = (params) => {
  return defHttp.get({ url: Api.queryById, params });
};

/**
 * 新增报告
 * @param data 报告数据（请求体）
 */
export const addDeviceReport = (data) => {
  return defHttp.post({ url: Api.add, data });
};

/**
 * 编辑报告
 * @param data 报告数据（请求体）
 */
export const editDeviceReport = (data) => {
  return defHttp.post({ url: Api.edit, data });
};

/**
 * 保存草稿
 * @param data 报告数据（请求体）
 */
export const saveDeviceReportDraft = (data) => {
  return defHttp.post({ url: Api.saveDraft, data });
};

/**
 * 提交报告
 * @param id 报告ID
 */
export const submitDeviceReport = (id: string) => {
  return defHttp.post({ url: `${Api.submit}?id=${id}` });
};

/**
 * 删除报告
 * @param id 报告ID
 */
export const deleteDeviceReport = (id: string) => {
  return defHttp.delete({ url: `${Api.delete}?id=${id}` });
};

/**
 * 批量删除报告
 * @param ids 报告ID列表，逗号分隔
 */
export const batchDeleteDeviceReport = (ids: string) => {
  return defHttp.delete({ url: `${Api.deleteBatch}?ids=${ids}` });
};

/**
 * 校验报告是否可编辑
 * @param params { id: string }
 */
export const canEditDeviceReport = (params) => {
  return defHttp.get({ url: Api.canEdit, params });
};
