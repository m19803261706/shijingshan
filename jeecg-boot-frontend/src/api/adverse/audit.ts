/**
 * 科室审核 API
 * @description 科室审核相关接口
 * @author TC Agent
 * @since 2025-12-18
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 待审核列表
  pendingList = '/adverse/audit/pending',
  // 已审核列表
  completedList = '/adverse/audit/completed',
  // 审核通过
  approve = '/adverse/audit/approve',
  // 审核退回
  reject = '/adverse/audit/reject',
}

/**
 * 获取待审核列表（分页）
 * @param params 查询参数
 */
export const getPendingAuditList = (params) => {
  return defHttp.get({ url: Api.pendingList, params });
};

/**
 * 获取已审核列表（分页）
 * @param params 查询参数
 */
export const getCompletedAuditList = (params) => {
  return defHttp.get({ url: Api.completedList, params });
};

/**
 * 审核通过
 * @param params { id: string, comment?: string }
 */
export const approveEvent = (params) => {
  return defHttp.post({ url: Api.approve, params });
};

/**
 * 审核退回
 * @param params { id: string, comment: string }
 */
export const rejectEvent = (params) => {
  return defHttp.post({ url: Api.reject, params });
};
