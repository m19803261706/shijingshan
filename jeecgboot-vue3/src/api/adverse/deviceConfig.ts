/**
 * 常用医疗器械配置 API
 * @description 常用医疗器械配置的增删改查、搜索、导入导出
 * @author TC Agent
 * @since 2025-12-19
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 分页列表
  list = '/adverse/deviceConfig/list',
  // 搜索（用于下拉选择）
  search = '/adverse/deviceConfig/search',
  // 新增
  add = '/adverse/deviceConfig/add',
  // 编辑
  edit = '/adverse/deviceConfig/edit',
  // 删除
  delete = '/adverse/deviceConfig/delete',
  // 批量删除
  deleteBatch = '/adverse/deviceConfig/deleteBatch',
  // 更新使用次数
  updateUseCount = '/adverse/deviceConfig/updateUseCount',
  // 保存（如果不存在）
  saveIfNotExists = '/adverse/deviceConfig/saveIfNotExists',
  // 导出
  exportXls = '/adverse/deviceConfig/exportXls',
  // 导入
  importExcel = '/adverse/deviceConfig/importExcel',
}

/**
 * 获取常用器械配置列表（分页）
 * @param params 查询参数
 */
export const getDeviceConfigList = (params: Record<string, any>) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 搜索常用器械（用于下拉选择）
 * @param keyword 搜索关键词
 */
export const searchDeviceConfig = (keyword?: string) => {
  return defHttp.get({ url: Api.search, params: { keyword } });
};

/**
 * 新增常用器械配置
 * @param params 配置信息
 */
export const addDeviceConfig = (params: Record<string, any>) => {
  return defHttp.post({ url: Api.add, params });
};

/**
 * 编辑常用器械配置
 * @param params 配置信息
 */
export const editDeviceConfig = (params: Record<string, any>) => {
  return defHttp.post({ url: Api.edit, params });
};

/**
 * 删除常用器械配置
 * @param id 配置ID
 */
export const deleteDeviceConfig = (id: string) => {
  return defHttp.delete({ url: Api.delete, params: { id } }, { joinParamsToUrl: true });
};

/**
 * 批量删除常用器械配置
 * @param ids ID列表，逗号分隔
 */
export const deleteDeviceConfigBatch = (ids: string) => {
  return defHttp.delete({ url: Api.deleteBatch, params: { ids } }, { joinParamsToUrl: true });
};

/**
 * 更新使用次数
 * @param id 配置ID
 */
export const updateDeviceConfigUseCount = (id: string) => {
  return defHttp.post({ url: Api.updateUseCount, params: { id } }, { joinParamsToUrl: true });
};

/**
 * 保存器械配置（如果不存在）
 * @param params 配置信息
 */
export const saveDeviceConfigIfNotExists = (params: Record<string, any>) => {
  return defHttp.post({ url: Api.saveIfNotExists, params });
};

/**
 * 导出URL
 */
export const getExportUrl = Api.exportXls;

/**
 * 导入URL
 */
export const getImportUrl = Api.importExcel;
