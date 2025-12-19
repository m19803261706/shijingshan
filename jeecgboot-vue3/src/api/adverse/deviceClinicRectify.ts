/**
 * 医疗器械临床科室整改提交 API
 * @description 临床科室人员查看和提交医疗器械不良事件整改相关接口
 * @author TC Agent
 * @since 2025-12-19
 * @see Issue #71
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 医疗器械待整改列表
  list = '/adverse/device/clinicRectify/list',
  // 医疗器械整改详情
  detail = '/adverse/device/clinicRectify/detail',
  // 提交医疗器械整改
  submit = '/adverse/device/clinicRectify/submit',
  // 医疗器械整改历史
  history = '/adverse/device/clinicRectify/history',
  // 当前整改记录
  currentRectify = '/adverse/device/clinicRectify/currentRectify',
  // 流转历史
  flowHistory = '/adverse/device/clinicRectify/flowHistory',
}

/**
 * 获取医疗器械待整改列表（分页）
 * @description 查询当前用户所属科室的待整改医疗器械不良事件报告
 * @param params 查询参数
 */
export const getDeviceRectifyList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 获取医疗器械整改详情
 * @description 获取报告和当前整改要求的完整信息
 * @param reportId 报告ID
 */
export const getDeviceRectifyDetail = (reportId: string) => {
  return defHttp.get({ url: Api.detail, params: { reportId } });
};

/**
 * 提交医疗器械整改
 * @description 临床人员填写整改措施并提交
 * @param params { rectifyId: string, measures: string, completion: string }
 */
export const submitDeviceRectify = (params: {
  rectifyId: string;
  measures: string;
  completion: string;
}) => {
  return defHttp.post({ url: Api.submit, params }, { joinParamsToUrl: true });
};

/**
 * 获取医疗器械整改历史
 * @description 查询指定报告的所有整改记录
 * @param reportId 报告ID
 */
export const getDeviceRectifyHistoryList = (reportId: string) => {
  return defHttp.get({ url: Api.history, params: { reportId } });
};

/**
 * 获取当前整改记录
 * @description 查询指定报告的最新（待处理）整改记录
 * @param reportId 报告ID
 */
export const getDeviceCurrentRectify = (reportId: string) => {
  return defHttp.get({ url: Api.currentRectify, params: { reportId } });
};

/**
 * 获取流转历史
 * @description 查询指定报告的所有流转记录
 * @param reportId 报告ID
 */
export const getDeviceClinicFlowHistory = (reportId: string) => {
  return defHttp.get({ url: Api.flowHistory, params: { reportId } });
};
