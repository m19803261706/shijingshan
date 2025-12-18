/**
 * 事件分类管理 API
 * @description 事件分类 CRUD 接口
 * @author TC Agent
 * @since 2025-12-18
 */
import { defHttp } from '/@/utils/http/axios';

/**
 * API 路径枚举
 */
enum Api {
  // 分类树
  tree = '/adverse/category/tree',
  // 列表（扁平）
  list = '/adverse/category/list',
  // 保存
  save = '/adverse/category/add',
  // 编辑
  edit = '/adverse/category/edit',
  // 删除
  delete = '/adverse/category/delete',
  // 批量删除
  deleteBatch = '/adverse/category/deleteBatch',
  // 启用/禁用
  toggleStatus = '/adverse/category/toggleStatus',
}

/**
 * 分类数据接口
 */
export interface CategoryItem {
  /** ID */
  id: string;
  /** 分类名称 */
  name: string;
  /** 分类编码 */
  code: string;
  /** 父级 ID */
  parentId?: string;
  /** 排序 */
  sortOrder?: number;
  /** 状态 (0-禁用, 1-启用) */
  status?: number;
  /** 备注 */
  remark?: string;
  /** 子分类 */
  children?: CategoryItem[];
}

/**
 * 获取分类树
 */
export const getCategoryTree = () => {
  return defHttp.get<CategoryItem[]>({ url: Api.tree });
};

/**
 * 获取分类列表（扁平）
 * @param params 查询参数
 */
export const getCategoryList = (params?) => {
  return defHttp.get<CategoryItem[]>({ url: Api.list, params });
};

/**
 * 保存分类
 * @param params 分类数据
 */
export const saveCategory = (params: Partial<CategoryItem>) => {
  return defHttp.post({ url: Api.save, params });
};

/**
 * 编辑分类
 * @param params 分类数据
 */
export const editCategory = (params: Partial<CategoryItem>) => {
  return defHttp.put({ url: Api.edit, params });
};

/**
 * 删除分类
 * @param params { id: string }
 */
export const deleteCategory = (params: { id: string }) => {
  return defHttp.delete({ url: Api.delete, params });
};

/**
 * 批量删除分类
 * @param params { ids: string }
 */
export const batchDeleteCategory = (params: { ids: string }) => {
  return defHttp.delete({ url: Api.deleteBatch, params });
};

/**
 * 切换分类状态
 * @param params { id: string, status: number }
 */
export const toggleCategoryStatus = (params: { id: string; status: number }) => {
  return defHttp.put({ url: Api.toggleStatus, params });
};
