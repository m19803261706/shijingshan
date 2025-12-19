/**
 * 输血科处理 API
 * @description 输血科处理输血使用不良事件报告相关接口
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/81
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 待处理列表
  pendingList = '/adverse/blood/process/pending',
  // 待确认列表（整改中）
  confirmingList = '/adverse/blood/process/confirming',
  // 已结案列表
  closedList = '/adverse/blood/process/closed',
  // 直接结案
  close = '/adverse/blood/process/close',
  // 要求整改
  requireRectify = '/adverse/blood/process/requireRectify',
  // 确认整改通过
  confirmRectify = '/adverse/blood/process/confirmRectify',
  // 退回整改
  rejectRectify = '/adverse/blood/process/rejectRectify',
  // 获取报告详情
  detail = '/adverse/blood/process/detail',
  // 获取整改记录
  rectifyHistory = '/adverse/blood/process/rectifyHistory',
  // 获取流转历史
  flowHistory = '/adverse/blood/process/flowHistory',
}

/**
 * 获取待处理报告列表（分页）
 * @description 查询待处理状态的输血使用不良事件报告（status = pending_process）
 * @param params 查询参数
 * @param params.pageNo 页码
 * @param params.pageSize 每页条数
 * @param params.reportNo 报告编号（模糊查询）
 * @param params.patientName 患者姓名（模糊查询）
 */
export const getBloodPendingProcessList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.pendingList, params });
};

/**
 * 获取待确认报告列表（分页）
 * @description 查询待整改确认状态的报告（status = pending_rectify 或 rectifying）
 * @param params 查询参数
 */
export const getBloodConfirmingList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.confirmingList, params });
};

/**
 * 获取已结案报告列表（分页）
 * @description 查询已结案状态的输血使用不良事件报告（status = closed）
 * @param params 查询参数
 * @param params.closeType 结案方式筛选（direct-直接结案，rectify-整改结案）
 */
export const getBloodClosedList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.closedList, params });
};

/**
 * 直接结案
 * @description 将报告直接结案，不需要整改流程
 * @param params { id: string, comment?: string }
 * @param params.id 报告ID
 * @param params.comment 结案意见（选填）
 */
export const bloodProcessClose = (params: { id: string; comment?: string }) => {
  return defHttp.post({ url: Api.close, params }, { joinParamsToUrl: true });
};

/**
 * 要求整改
 * @description 下发整改要求，创建整改记录
 * @param params { id: string, requirement: string, deadline: string }
 * @param params.id 报告ID
 * @param params.requirement 整改要求
 * @param params.deadline 整改期限（格式：yyyy-MM-dd）
 */
export const bloodProcessRequireRectify = (params: {
  id: string;
  requirement: string;
  deadline: string;
}) => {
  return defHttp.post({ url: Api.requireRectify, params }, { joinParamsToUrl: true });
};

/**
 * 确认整改通过
 * @description 确认整改已完成，报告结案
 * @param params { id: string, comment?: string }
 * @param params.id 报告ID
 * @param params.comment 确认意见（选填）
 */
export const bloodProcessConfirmRectify = (params: { id: string; comment?: string }) => {
  return defHttp.post({ url: Api.confirmRectify, params }, { joinParamsToUrl: true });
};

/**
 * 退回整改
 * @description 退回整改，要求重新整改
 * @param params { id: string, comment: string }
 * @param params.id 报告ID
 * @param params.comment 退回原因（必填）
 */
export const bloodProcessRejectRectify = (params: { id: string; comment: string }) => {
  return defHttp.post({ url: Api.rejectRectify, params }, { joinParamsToUrl: true });
};

/**
 * 获取报告处理详情
 * @description 获取报告完整信息，用于处理页面展示
 * @param id 报告ID
 */
export const getBloodProcessDetail = (id: string) => {
  return defHttp.get({ url: Api.detail, params: { id } });
};

/**
 * 获取整改记录列表
 * @description 查询指定报告的所有整改记录
 * @param reportId 报告ID
 */
export const getBloodProcessRectifyHistory = (reportId: string) => {
  return defHttp.get({ url: Api.rectifyHistory, params: { reportId } });
};

/**
 * 获取报告流转历史
 * @description 查询指定报告的所有流转记录
 * @param reportId 报告ID
 */
export const getBloodProcessFlowHistory = (reportId: string) => {
  return defHttp.get({ url: Api.flowHistory, params: { reportId } });
};
