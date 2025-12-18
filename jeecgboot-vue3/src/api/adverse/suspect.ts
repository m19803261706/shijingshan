/**
 * 常用怀疑药品配置 API
 * <p>
 * 提供常用怀疑药品的增删改查接口
 * </p>
 *
 * @author TC Agent
 * @since 2025-12-19
 */
import { defHttp } from '@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  /** 分页查询列表 */
  list = '/adverse/drugCommonSuspect/list',
  /** 快速搜索（用于下拉选择） */
  search = '/adverse/drugCommonSuspect/search',
  /** 根据ID查询详情 */
  queryById = '/adverse/drugCommonSuspect/queryById',
  /** 新增 */
  add = '/adverse/drugCommonSuspect/add',
  /** 编辑 */
  edit = '/adverse/drugCommonSuspect/edit',
  /** 删除 */
  delete = '/adverse/drugCommonSuspect/delete',
  /** 批量删除 */
  deleteBatch = '/adverse/drugCommonSuspect/deleteBatch',
  /** 更新使用次数 */
  updateUseCount = '/adverse/drugCommonSuspect/updateUseCount',
  /** 导出Excel */
  exportXls = '/adverse/drugCommonSuspect/exportXls',
  /** 导入Excel */
  importExcel = '/adverse/drugCommonSuspect/importExcel',
}

/**
 * 分页查询常用怀疑药品列表
 *
 * @param params 查询参数
 * @returns 分页结果
 */
export const getSuspectList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 快速搜索常用怀疑药品（用于下拉选择）
 *
 * @param keyword 搜索关键词
 * @returns 匹配的药品列表
 */
export const searchSuspect = (keyword?: string) => {
  return defHttp.get({ url: Api.search, params: { keyword } });
};

/**
 * 根据ID查询详情
 *
 * @param id 主键ID
 * @returns 药品详情
 */
export const getSuspectById = (id: string) => {
  return defHttp.get({ url: Api.queryById, params: { id } });
};

/**
 * 新增常用怀疑药品
 *
 * @param params 药品信息
 * @returns 操作结果
 */
export const addSuspect = (params) => {
  return defHttp.post({ url: Api.add, params });
};

/**
 * 编辑常用怀疑药品
 *
 * @param params 药品信息
 * @returns 操作结果
 */
export const editSuspect = (params) => {
  return defHttp.put({ url: Api.edit, params });
};

/**
 * 删除常用怀疑药品
 *
 * @param id 主键ID
 * @returns 操作结果
 */
export const deleteSuspect = (id: string) => {
  return defHttp.delete({ url: `${Api.delete}?id=${id}` });
};

/**
 * 批量删除常用怀疑药品
 *
 * @param ids 主键ID列表（逗号分隔）
 * @returns 操作结果
 */
export const deleteSuspectBatch = (ids: string) => {
  return defHttp.delete({ url: `${Api.deleteBatch}?ids=${ids}` });
};

/**
 * 更新使用次数（选择药品时调用）
 *
 * @param id 主键ID
 * @returns 操作结果
 */
export const updateSuspectUseCount = (id: string) => {
  return defHttp.put({ url: `${Api.updateUseCount}?id=${id}` });
};

/**
 * 获取导出Excel的URL
 */
export const getExportUrl = Api.exportXls;

/**
 * 获取导入Excel的URL
 */
export const getImportUrl = Api.importExcel;
