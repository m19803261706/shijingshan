/**
 * 药品不良反应科室处理 API
 * @description 药剂科处理药品不良反应报告相关接口
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/51
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 待处理列表
  pendingList = '/adverse/drug/process/pending',
  // 待确认列表
  confirmingList = '/adverse/drug/process/confirming',
  // 已结案列表
  closedList = '/adverse/drug/process/closed',
  // 直接结案
  close = '/adverse/drug/process/close',
  // 要求整改
  requireRectify = '/adverse/drug/process/requireRectify',
  // 确认整改通过
  confirmRectify = '/adverse/drug/process/confirmRectify',
  // 退回整改
  rejectRectify = '/adverse/drug/process/rejectRectify',
  // 获取报告详情
  detail = '/adverse/drug/process/detail',
  // 获取整改记录
  rectifyHistory = '/adverse/drug/process/rectifyHistory',
  // 获取流转历史
  flowHistory = '/adverse/drug/process/flowHistory',
}

/**
 * 获取待处理报告列表（分页）
 * @description 查询待处理状态的药品不良反应报告
 * @param params 查询参数
 */
export const getDrugPendingProcessList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.pendingList, params });
};

/**
 * 获取待确认报告列表（分页）
 * @description 查询待整改确认状态的药品不良反应报告
 * @param params 查询参数
 */
export const getDrugConfirmingList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.confirmingList, params });
};

/**
 * 获取已结案报告列表（分页）
 * @description 查询已结案状态的药品不良反应报告
 * @param params 查询参数
 */
export const getDrugClosedList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.closedList, params });
};

/**
 * 直接结案
 * @description 将报告直接结案，不需要整改流程
 * @param params { id: string, comment?: string }
 */
export const drugProcessClose = (params: { id: string; comment?: string }) => {
  return defHttp.post({ url: Api.close, params }, { joinParamsToUrl: true });
};

/**
 * 要求整改
 * @description 下发整改要求
 * @param params { id: string, requirement: string, deadline: string, comment?: string }
 */
export const drugProcessRequireRectify = (params: {
  id: string;
  requirement: string;
  deadline: string;
  comment?: string;
}) => {
  return defHttp.post({ url: Api.requireRectify, params }, { joinParamsToUrl: true });
};

/**
 * 确认整改通过
 * @description 确认整改已完成，报告结案
 * @param params { rectifyId: string, comment?: string }
 */
export const drugProcessConfirmRectify = (params: { rectifyId: string; comment?: string }) => {
  return defHttp.post({ url: Api.confirmRectify, params }, { joinParamsToUrl: true });
};

/**
 * 退回整改
 * @description 退回整改，要求重新整改
 * @param params { rectifyId: string, comment: string }
 */
export const drugProcessRejectRectify = (params: { rectifyId: string; comment: string }) => {
  return defHttp.post({ url: Api.rejectRectify, params }, { joinParamsToUrl: true });
};

/**
 * 获取报告处理详情
 * @description 获取报告完整信息（含子表）
 * @param id 报告ID
 */
export const getDrugProcessDetail = (id: string) => {
  return defHttp.get({ url: Api.detail, params: { id } });
};

/**
 * 获取整改记录列表
 * @description 查询指定报告的所有整改记录
 * @param reportId 报告ID
 */
export const getDrugRectifyHistory = (reportId: string) => {
  return defHttp.get({ url: Api.rectifyHistory, params: { reportId } });
};

/**
 * 获取报告流转历史
 * @description 查询指定报告的所有流转记录
 * @param reportId 报告ID
 */
export const getDrugProcessFlowHistory = (reportId: string) => {
  return defHttp.get({ url: Api.flowHistory, params: { reportId } });
};
