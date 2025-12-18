/**
 * 统计分析 API
 * @description 统计分析相关接口
 * @author TC Agent
 * @since 2025-12-18
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 概览数据
  overview = '/adverse/stat/overview',
  // 科室分布
  deptDistribution = '/adverse/stat/deptDistribution',
  // 分类分布
  categoryDistribution = '/adverse/stat/categoryDistribution',
  // 月度趋势
  monthlyTrend = '/adverse/stat/monthlyTrend',
  // 级别分布
  levelDistribution = '/adverse/stat/levelDistribution',
  // 导出报表
  exportReport = '/adverse/stat/exportReport',
}

/**
 * 统计概览数据
 */
export interface StatOverview {
  /** 事件总数 */
  total: number;
  /** 本月新增 */
  monthlyNew: number;
  /** 待处理数 */
  pendingProcess: number;
  /** 待整改数 */
  pendingRectify: number;
  /** 同比增长率 */
  growthRate?: number;
}

/**
 * 分布数据项
 */
export interface DistributionItem {
  /** 名称 */
  name: string;
  /** 数量 */
  value: number;
  /** 占比 */
  percent?: number;
}

/**
 * 趋势数据项
 */
export interface TrendItem {
  /** 月份 */
  month: string;
  /** 数量 */
  count: number;
}

/**
 * 获取统计概览数据
 * @param params { year?: number, month?: number } 时间范围
 */
export const getStatOverview = (params?) => {
  return defHttp.get<StatOverview>({ url: Api.overview, params });
};

/**
 * 获取科室分布数据
 * @param params { startDate?: string, endDate?: string } 时间范围
 */
export const getDeptDistribution = (params?) => {
  return defHttp.get<DistributionItem[]>({ url: Api.deptDistribution, params });
};

/**
 * 获取分类分布数据
 * @param params { startDate?: string, endDate?: string } 时间范围
 */
export const getCategoryDistribution = (params?) => {
  return defHttp.get<DistributionItem[]>({ url: Api.categoryDistribution, params });
};

/**
 * 获取月度趋势数据
 * @param params { year?: number } 年份
 */
export const getMonthlyTrend = (params?) => {
  return defHttp.get<TrendItem[]>({ url: Api.monthlyTrend, params });
};

/**
 * 获取级别分布数据
 * @param params { startDate?: string, endDate?: string } 时间范围
 */
export const getLevelDistribution = (params?) => {
  return defHttp.get<DistributionItem[]>({ url: Api.levelDistribution, params });
};

/**
 * 导出统计报表
 * @param params { startDate: string, endDate: string, type: string } 导出参数
 */
export const exportStatReport = (params) => {
  return defHttp.get(
    { url: Api.exportReport, params, responseType: 'blob' },
    { isTransformResponse: false }
  );
};
