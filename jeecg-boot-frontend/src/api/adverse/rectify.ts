/**
 * 整改管理 API
 * @description 整改相关接口
 * @author TC Agent
 * @since 2025-12-18
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 待整改列表
  pendingList = '/adverse/rectify/pending',
  // 整改详情
  detail = '/adverse/rectify/detail',
  // 整改历史
  history = '/adverse/rectify/history',
  // 保存草稿
  saveDraft = '/adverse/rectify/saveDraft',
  // 提交整改
  submit = '/adverse/rectify/submit',
}

/**
 * 获取待整改列表（分页）
 * @param params 查询参数
 */
export const getPendingRectifyList = (params) => {
  return defHttp.get({ url: Api.pendingList, params });
};

/**
 * 获取整改详情
 * @param params { id: string } 整改记录ID
 */
export const getRectifyDetail = (params) => {
  return defHttp.get({ url: Api.detail, params });
};

/**
 * 获取整改历史
 * @param params { eventId: string } 事件ID
 */
export const getRectifyHistory = (params) => {
  return defHttp.get({ url: Api.history, params });
};

/**
 * 保存整改草稿
 * @param params 整改数据
 */
export const saveRectifyDraft = (params) => {
  return defHttp.post({ url: Api.saveDraft, params });
};

/**
 * 提交整改
 * @param params 整改数据
 */
export const submitRectify = (params) => {
  return defHttp.post({ url: Api.submit, params });
};
