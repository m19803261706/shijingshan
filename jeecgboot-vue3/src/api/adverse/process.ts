/**
 * 职能处理 API
 * @description 职能科室处理相关接口
 * @author TC Agent
 * @since 2025-12-18
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 待处理列表
  pendingList = '/adverse/process/pending',
  // 待确认列表（整改确认）
  confirmList = '/adverse/process/confirm',
  // 已结案列表
  closedList = '/adverse/process/closed',
  // 要求整改
  requireRectify = '/adverse/process/requireRectify',
  // 直接结案
  closeEvent = '/adverse/process/close',
  // 确认整改
  confirmRectify = '/adverse/process/confirmRectify',
  // 退回整改
  rejectRectify = '/adverse/process/rejectRectify',
  // 获取流转记录
  flowRecords = '/adverse/event/flowRecords',
}

/**
 * 获取待处理列表（分页）
 * @param params 查询参数
 */
export const getPendingProcessList = (params) => {
  return defHttp.get({ url: Api.pendingList, params });
};

/**
 * 获取待确认列表（分页）
 * @param params 查询参数
 */
export const getConfirmList = (params) => {
  return defHttp.get({ url: Api.confirmList, params });
};

/**
 * 获取已结案列表（分页）
 * @param params 查询参数
 */
export const getClosedList = (params) => {
  return defHttp.get({ url: Api.closedList, params });
};

/**
 * 要求整改
 * @param params { id: string, comment: string, deadline?: string }
 */
export const requireRectify = (params) => {
  return defHttp.post({ url: Api.requireRectify, params });
};

/**
 * 直接结案
 * @param params { id: string, comment?: string }
 */
export const closeEvent = (params) => {
  return defHttp.post({ url: Api.closeEvent, params });
};

/**
 * 确认整改
 * @param params { id: string, comment?: string }
 */
export const confirmRectify = (params) => {
  return defHttp.post({ url: Api.confirmRectify, params });
};

/**
 * 退回整改
 * @param params { id: string, comment: string }
 */
export const rejectRectify = (params) => {
  return defHttp.post({ url: Api.rejectRectify, params });
};

/**
 * 获取流转记录
 * @param params { eventId: string }
 */
export const getFlowRecords = (params) => {
  return defHttp.get({ url: Api.flowRecords, params });
};
