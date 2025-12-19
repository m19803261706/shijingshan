/**
 * 药品不良反应临床科室审核 API
 * @description 临床科室审核相关接口，用于科室审核药品不良反应报告
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/46
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 待审核列表
  pendingList = '/adverse/drug/audit/pending',
  // 已审核列表
  completedList = '/adverse/drug/audit/completed',
  // 审核通过
  pass = '/adverse/drug/audit/pass',
  // 审核退回
  reject = '/adverse/drug/audit/reject',
  // 获取审核详情
  detail = '/adverse/drug/audit/detail',
  // 获取流转历史
  flowHistory = '/adverse/drug/audit/flowHistory',
}

/**
 * 获取待审核报告列表（分页）
 * @description 查询当前用户所在科室的待审核药品不良反应报告
 * @param params 查询参数
 * @param params.pageNo 页码
 * @param params.pageSize 每页条数
 * @param params.reportNo 报告编号（模糊查询）
 * @param params.patientName 患者姓名（模糊查询）
 */
export const getDrugPendingAuditList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.pendingList, params });
};

/**
 * 获取已审核报告列表（分页）
 * @description 查询当前用户已审核的药品不良反应报告（已通过或已退回）
 * @param params 查询参数
 * @param params.pageNo 页码
 * @param params.pageSize 每页条数
 * @param params.reportNo 报告编号（模糊查询）
 * @param params.patientName 患者姓名（模糊查询）
 * @param params.status 状态筛选（pending_process-已通过，returned-已退回）
 */
export const getDrugCompletedAuditList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.completedList, params });
};

/**
 * 审核通过
 * @description 将报告状态从待审核改为待处理，流转至职能科室
 * @param params { id: string, comment?: string }
 * @param params.id 报告ID
 * @param params.comment 审核意见（选填）
 */
export const drugAuditPass = (params: { id: string; comment?: string }) => {
  return defHttp.post({ url: Api.pass, params });
};

/**
 * 审核退回
 * @description 将报告状态从待审核改为已退回，退回给上报人修改
 * @param params { id: string, comment: string }
 * @param params.id 报告ID
 * @param params.comment 退回原因（必填）
 */
export const drugAuditReject = (params: { id: string; comment: string }) => {
  return defHttp.post({ url: Api.reject, params });
};

/**
 * 获取报告审核详情
 * @description 获取报告完整信息（含子表），用于审核页面展示
 * @param id 报告ID
 */
export const getDrugAuditDetail = (id: string) => {
  return defHttp.get({ url: Api.detail, params: { id } });
};

/**
 * 获取报告流转历史
 * @description 查询指定报告的所有流转记录
 * @param reportId 报告ID
 */
export const getDrugFlowHistory = (reportId: string) => {
  return defHttp.get({ url: Api.flowHistory, params: { reportId } });
};
