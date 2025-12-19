/**
 * 输血使用不良事件临床整改 API
 * @description 临床人员查看和提交整改相关接口
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/81
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 待整改列表
  pendingList = '/adverse/blood/rectify/pending',
  // 整改中列表（已提交，待确认）
  submittedList = '/adverse/blood/rectify/submitted',
  // 已结案列表
  closedList = '/adverse/blood/rectify/closed',
  // 提交整改
  submit = '/adverse/blood/rectify/submit',
  // 获取报告详情
  detail = '/adverse/blood/rectify/detail',
  // 获取当前整改记录
  currentRectify = '/adverse/blood/rectify/currentRectify',
  // 获取整改历史
  rectifyHistory = '/adverse/blood/rectify/rectifyHistory',
  // 获取流转历史
  flowHistory = '/adverse/blood/rectify/flowHistory',
}

/**
 * 获取待整改报告列表（分页）
 * @description 查询当前用户所在科室的待整改报告
 * @param params 查询参数
 * @param params.pageNo 页码
 * @param params.pageSize 每页条数
 * @param params.reportNo 报告编号（模糊查询）
 * @param params.patientName 患者姓名（模糊查询）
 */
export const getBloodPendingRectifyList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.pendingList, params });
};

/**
 * 获取整改中报告列表（分页）
 * @description 查询当前用户所在科室的整改中报告（已提交，待输血科确认）
 * @param params 查询参数
 */
export const getBloodSubmittedRectifyList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.submittedList, params });
};

/**
 * 获取已结案报告列表（分页）
 * @description 查询当前用户所在科室的已结案报告
 * @param params 查询参数
 */
export const getBloodClosedRectifyList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.closedList, params });
};

/**
 * 提交整改
 * @description 临床人员提交整改措施和完成情况
 * @param params { rectifyId: string, measures: string, completion: string }
 * @param params.rectifyId 整改记录ID
 * @param params.measures 整改措施
 * @param params.completion 完成情况
 */
export const submitBloodRectify = (params: {
  rectifyId: string;
  measures: string;
  completion: string;
}) => {
  return defHttp.post({ url: Api.submit, params }, { joinParamsToUrl: true });
};

/**
 * 获取报告整改详情
 * @description 获取报告完整信息，用于整改页面展示
 * @param id 报告ID
 */
export const getBloodRectifyDetail = (id: string) => {
  return defHttp.get({ url: Api.detail, params: { id } });
};

/**
 * 获取当前待提交的整改记录
 * @description 查询指定报告的当前待整改记录
 * @param reportId 报告ID
 */
export const getBloodCurrentRectify = (reportId: string) => {
  return defHttp.get({ url: Api.currentRectify, params: { reportId } });
};

/**
 * 获取整改记录列表
 * @description 查询指定报告的所有整改记录
 * @param reportId 报告ID
 */
export const getBloodRectifyRecordHistory = (reportId: string) => {
  return defHttp.get({ url: Api.rectifyHistory, params: { reportId } });
};

/**
 * 获取报告流转历史
 * @description 查询指定报告的所有流转记录
 * @param reportId 报告ID
 */
export const getBloodRectifyFlowHistory = (reportId: string) => {
  return defHttp.get({ url: Api.flowHistory, params: { reportId } });
};
