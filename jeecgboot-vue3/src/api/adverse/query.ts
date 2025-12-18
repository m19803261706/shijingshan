/**
 * 事件查询 API
 * @description 综合查询相关接口
 * @author TC Agent
 * @since 2025-12-18
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 综合查询
  list = '/adverse/query/list',
  // 导出 Excel
  exportExcel = '/adverse/query/exportXls',
}

/**
 * 综合查询事件列表（分页）
 * @param params 查询参数
 */
export const queryEventList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 导出事件列表 Excel
 * @param params 查询参数
 */
export const exportEventExcel = (params) => {
  return defHttp.get(
    { url: Api.exportExcel, params, responseType: 'blob' },
    { isTransformResponse: false }
  );
};
