/**
 * 不良事件上报 API
 * @description 事件上报相关接口
 * @author TC Agent
 * @since 2025-12-18
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 事件上报
  list = '/adverse/event/list',
  myList = '/adverse/event/myList',
  save = '/adverse/event/save',
  submit = '/adverse/event/submit',
  edit = '/adverse/event/edit',
  delete = '/adverse/event/delete',
  deleteBatch = '/adverse/event/deleteBatch',
  queryById = '/adverse/event/queryById',
  resubmit = '/adverse/event/resubmit',
  // 分类
  categoryTree = '/adverse/category/tree',
}

/**
 * 获取我的上报列表（分页）
 * @param params 查询参数
 */
export const getMyEventList = (params) => {
  return defHttp.get({ url: Api.myList, params });
};

/**
 * 获取事件列表（分页）
 * @param params 查询参数
 */
export const getEventList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 根据 ID 查询事件详情
 * @param params { id: string }
 */
export const queryEventById = (params) => {
  return defHttp.get({ url: Api.queryById, params });
};

/**
 * 保存草稿
 * @param params 事件数据
 * @param isUpdate 是否更新
 */
export const saveEventDraft = (params, isUpdate: boolean) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};

/**
 * 提交事件
 * @param params 事件数据
 */
export const submitEvent = (params) => {
  return defHttp.post({ url: Api.submit, params });
};

/**
 * 重新提交（退回后）
 * @param params 事件数据
 */
export const resubmitEvent = (params) => {
  return defHttp.post({ url: Api.resubmit, params });
};

/**
 * 删除事件
 * @param params { id: string }
 */
export const deleteEvent = (params) => {
  return defHttp.delete({ url: Api.delete, params });
};

/**
 * 批量删除事件
 * @param params { ids: string }
 */
export const batchDeleteEvent = (params) => {
  return defHttp.delete({ url: Api.deleteBatch, params });
};

/**
 * 获取事件分类树
 */
export const getCategoryTree = () => {
  return defHttp.get({ url: Api.categoryTree });
};
