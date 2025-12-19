/**
 * 临床科室整改提交 API
 * @description 临床科室人员查看和提交整改相关接口
 * @author TC Agent
 * @since 2025-12-19
 * @see https://github.com/m19803261706/shijingshan/issues/55
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 药品不良反应待整改列表
  drugList = '/adverse/clinic/rectify/drugList',
  // 药品不良反应整改详情
  drugDetail = '/adverse/clinic/rectify/drugDetail',
  // 提交药品不良反应整改
  drugSubmit = '/adverse/clinic/rectify/drugSubmit',
  // 药品不良反应整改历史
  drugHistory = '/adverse/clinic/rectify/drugHistory',
  // 当前整改记录
  drugCurrentRectify = '/adverse/clinic/rectify/drugCurrentRectify',
  // 流转历史
  drugFlowHistory = '/adverse/clinic/rectify/drugFlowHistory',
}

/**
 * 获取药品不良反应待整改列表（分页）
 * @description 查询当前用户所属科室的待整改药品不良反应报告
 * @param params 查询参数
 */
export const getDrugRectifyList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.drugList, params });
};

/**
 * 获取药品不良反应整改详情
 * @description 获取报告和当前整改要求的完整信息
 * @param reportId 报告ID
 */
export const getDrugRectifyDetail = (reportId: string) => {
  return defHttp.get({ url: Api.drugDetail, params: { reportId } });
};

/**
 * 提交药品不良反应整改
 * @description 临床人员填写整改措施并提交
 * @param params { rectifyId: string, measures: string, completion: string }
 */
export const submitDrugRectify = (params: {
  rectifyId: string;
  measures: string;
  completion: string;
}) => {
  return defHttp.post({ url: Api.drugSubmit, params }, { joinParamsToUrl: true });
};

/**
 * 获取药品不良反应整改历史
 * @description 查询指定报告的所有整改记录
 * @param reportId 报告ID
 */
export const getDrugRectifyHistory = (reportId: string) => {
  return defHttp.get({ url: Api.drugHistory, params: { reportId } });
};

/**
 * 获取当前整改记录
 * @description 查询指定报告的最新（待处理）整改记录
 * @param reportId 报告ID
 */
export const getDrugCurrentRectify = (reportId: string) => {
  return defHttp.get({ url: Api.drugCurrentRectify, params: { reportId } });
};

/**
 * 获取流转历史
 * @description 查询指定报告的所有流转记录
 * @param reportId 报告ID
 */
export const getDrugFlowHistory = (reportId: string) => {
  return defHttp.get({ url: Api.drugFlowHistory, params: { reportId } });
};
