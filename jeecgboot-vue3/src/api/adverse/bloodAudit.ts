/**
 * 输血使用不良事件审核 API
 * @description 临床科室审核输血使用不良事件报告相关接口
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/81
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 待审核列表
  pendingList = '/adverse/blood/audit/pending',
  // 已审核列表
  completedList = '/adverse/blood/audit/completed',
  // 审核通过
  pass = '/adverse/blood/audit/pass',
  // 审核退回
  reject = '/adverse/blood/audit/reject',
  // 获取审核详情
  detail = '/adverse/blood/audit/detail',
  // 获取流转历史
  flowHistory = '/adverse/blood/audit/flowHistory',
}

/**
 * 获取待审核报告列表（分页）
 * @description 查询当前用户所在科室的待审核输血使用不良事件报告
 * @param params 查询参数
 * @param params.pageNo 页码
 * @param params.pageSize 每页条数
 * @param params.reportNo 报告编号（模糊查询）
 * @param params.patientName 患者姓名（模糊查询）
 */
export const getBloodPendingAuditList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.pendingList, params });
};

/**
 * 获取已审核报告列表（分页）
 * @description 查询当前用户已审核的输血使用不良事件报告（已通过或已退回）
 * @param params 查询参数
 * @param params.pageNo 页码
 * @param params.pageSize 每页条数
 * @param params.reportNo 报告编号（模糊查询）
 * @param params.patientName 患者姓名（模糊查询）
 * @param params.status 状态筛选（pending_process-已通过，returned-已退回）
 */
export const getBloodCompletedAuditList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.completedList, params });
};

/**
 * 审核通过
 * @description 将报告状态从待审核改为待处理，流转至输血科
 * @param params { id: string, comment?: string }
 * @param params.id 报告ID
 * @param params.comment 审核意见（选填）
 */
export const bloodAuditPass = (params: { id: string; comment?: string }) => {
  return defHttp.post({ url: Api.pass, params }, { joinParamsToUrl: true });
};

/**
 * 审核退回
 * @description 将报告状态从待审核改为已退回，退回给上报人修改
 * @param params { id: string, comment: string }
 * @param params.id 报告ID
 * @param params.comment 退回原因（必填）
 */
export const bloodAuditReject = (params: { id: string; comment: string }) => {
  return defHttp.post({ url: Api.reject, params }, { joinParamsToUrl: true });
};

/**
 * 获取报告审核详情
 * @description 获取报告完整信息，用于审核页面展示
 * @param id 报告ID
 */
export const getBloodAuditDetail = (id: string) => {
  return defHttp.get({ url: Api.detail, params: { id } });
};

/**
 * 获取报告流转历史
 * @description 查询指定报告的所有流转记录
 * @param reportId 报告ID
 */
export const getBloodAuditFlowHistory = (reportId: string) => {
  return defHttp.get({ url: Api.flowHistory, params: { reportId } });
};
