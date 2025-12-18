-- ========================================
-- 药品不良反应上报 - 菜单权限配置
-- 版本: V1007
-- 描述: 创建药品不良反应上报的菜单和按钮权限
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- ========================================

-- ========================================
-- 一、二级菜单：药品不良反应（在不良事件管理下）
-- ========================================
-- 父菜单（is_leaf=0 表示有子菜单）
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_drug', 'ae_root', '药品不良反应', '/adverse/drug', 'layouts/RouteView', 0, NULL, 1.5,
  'ant-design:medicine-box-outlined', 1, 0, 0, 0, '1', 0, 'admin', NOW()
);

-- ========================================
-- 二、三级菜单：我的上报
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_drug_report', 'ae_drug', '我的上报', '/adverse/drug/report', 'adverse/drug/index', 0, NULL, 1.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 我的上报按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_report_add', 'ae_drug_report', '新增', NULL, NULL, 2, 'adverse:drug:add', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_report_edit', 'ae_drug_report', '编辑', NULL, NULL, 2, 'adverse:drug:edit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_report_delete', 'ae_drug_report', '删除', NULL, NULL, 2, 'adverse:drug:delete', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_report_submit', 'ae_drug_report', '提交', NULL, NULL, 2, 'adverse:drug:submit', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_report_view', 'ae_drug_report', '查看', NULL, NULL, 2, 'adverse:drug:view', 5.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 三、隐藏菜单：报告表单页（不在菜单显示，但需要路由）
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_drug_form', 'ae_drug', '报告表单', '/adverse/drug/form', 'adverse/drug/DrugReportForm', 0, NULL, 2.0,
  NULL, 1, 1, 0, 1, '1', 0, 'admin', NOW()
);

-- ========================================
-- 四、为管理员角色添加菜单权限
-- ========================================
-- 添加药品不良反应相关权限到 admin 角色
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_drug%';

-- ========================================
-- 菜单结构说明
-- ========================================
-- 不良事件管理 (ae_root)
-- ├── 我的上报 (ae_event) - 通用事件上报
-- ├── 药品不良反应 (ae_drug) <-- 新增
-- │   ├── 我的上报 (ae_drug_report) [新增/编辑/删除/提交/查看]
-- │   └── 报告表单 (ae_drug_form) [隐藏菜单]
-- ├── 科室审核 (ae_audit)
-- │   ├── 待审核 (ae_audit_pending)
-- │   └── 已审核 (ae_audit_completed)
-- ├── 职能处理 (ae_process)
-- │   ├── 待处理 (ae_process_pending)
-- │   ├── 待确认 (ae_process_confirm)
-- │   └── 已结案 (ae_process_closed)
-- ├── 整改管理 (ae_rectify)
-- │   └── 待整改 (ae_rectify_pending)
-- ├── 综合查询 (ae_query)
-- ├── 统计分析 (ae_stat)
-- └── 系统配置 (ae_config)
--     └── 事件分类管理 (ae_category)
