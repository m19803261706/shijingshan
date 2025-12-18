-- ========================================
-- 药品常用配置 - 菜单权限配置
-- 版本: V1014
-- 描述: 创建常用怀疑药品和常用并用药品的菜单和按钮权限
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- ========================================

-- ========================================
-- 一、三级菜单：怀疑药品配置（在药品不良反应下）
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_drug_suspect_config', 'ae_drug', '怀疑药品配置', '/adverse/drug/suspect', 'adverse/drug/suspect/index', 0, NULL, 3.0,
  'ant-design:database-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 怀疑药品配置按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_suspect_config_add', 'ae_drug_suspect_config', '新增', NULL, NULL, 2, 'adverse:drugCommonSuspect:add', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_suspect_config_edit', 'ae_drug_suspect_config', '编辑', NULL, NULL, 2, 'adverse:drugCommonSuspect:edit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_suspect_config_delete', 'ae_drug_suspect_config', '删除', NULL, NULL, 2, 'adverse:drugCommonSuspect:delete', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_suspect_config_import', 'ae_drug_suspect_config', '导入', NULL, NULL, 2, 'adverse:drugCommonSuspect:importExcel', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_suspect_config_export', 'ae_drug_suspect_config', '导出', NULL, NULL, 2, 'adverse:drugCommonSuspect:exportXls', 5.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 二、三级菜单：并用药品配置（在药品不良反应下）
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_drug_concomitant_config', 'ae_drug', '并用药品配置', '/adverse/drug/concomitant', 'adverse/drug/concomitant/index', 0, NULL, 4.0,
  'ant-design:cluster-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 并用药品配置按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_concomitant_config_add', 'ae_drug_concomitant_config', '新增', NULL, NULL, 2, 'adverse:drugCommonConcomitant:add', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_concomitant_config_edit', 'ae_drug_concomitant_config', '编辑', NULL, NULL, 2, 'adverse:drugCommonConcomitant:edit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_concomitant_config_delete', 'ae_drug_concomitant_config', '删除', NULL, NULL, 2, 'adverse:drugCommonConcomitant:delete', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_concomitant_config_import', 'ae_drug_concomitant_config', '导入', NULL, NULL, 2, 'adverse:drugCommonConcomitant:importExcel', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_concomitant_config_export', 'ae_drug_concomitant_config', '导出', NULL, NULL, 2, 'adverse:drugCommonConcomitant:exportXls', 5.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 三、为管理员角色添加菜单权限
-- ========================================
-- 添加怀疑药品配置相关权限到 admin 角色
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_drug_suspect_config%';

-- 添加并用药品配置相关权限到 admin 角色
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_drug_concomitant_config%';

-- ========================================
-- 菜单结构说明
-- ========================================
-- 不良事件管理 (ae_root)
-- ├── 我的上报 (ae_event) - 通用事件上报
-- ├── 药品不良反应 (ae_drug)
-- │   ├── 我的上报 (ae_drug_report) [新增/编辑/删除/提交/查看]
-- │   ├── 报告表单 (ae_drug_form) [隐藏菜单]
-- │   ├── 怀疑药品配置 (ae_drug_suspect_config) <-- 新增
-- │   │   └── [新增/编辑/删除/导入/导出]
-- │   └── 并用药品配置 (ae_drug_concomitant_config) <-- 新增
-- │       └── [新增/编辑/删除/导入/导出]
-- ├── 科室审核 (ae_audit)
-- ├── 职能处理 (ae_process)
-- ├── 整改管理 (ae_rectify)
-- ├── 综合查询 (ae_query)
-- ├── 统计分析 (ae_stat)
-- └── 系统配置 (ae_config)
