-- ========================================
-- 不良事件上报系统 - 菜单权限配置
-- 版本: V1005
-- 描述: 创建不良事件管理系统的菜单和按钮权限
-- 作者: TC Agent
-- 创建时间: 2025-12-18
-- ========================================

-- ========================================
-- 一、清理已存在的菜单（可选，仅开发环境使用）
-- ========================================
-- DELETE FROM sys_permission WHERE id LIKE 'ae_%';

-- ========================================
-- 二、一级菜单：不良事件管理
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_root', NULL, '不良事件管理', '/adverse', 'layouts/RouteView', 0, NULL, 10.0,
  'ant-design:alert-outlined', 1, 0, 0, 0, '1', 0, 'admin', NOW()
);

-- ========================================
-- 三、二级菜单：我的上报
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_event', 'ae_root', '我的上报', '/adverse/event', 'adverse/event/index', 0, NULL, 1.0,
  'ant-design:form-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 我的上报按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_event_add', 'ae_event', '新增', NULL, NULL, 2, 'adverse:event:add', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_event_edit', 'ae_event', '编辑', NULL, NULL, 2, 'adverse:event:edit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_event_delete', 'ae_event', '删除', NULL, NULL, 2, 'adverse:event:delete', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_event_submit', 'ae_event', '提交', NULL, NULL, 2, 'adverse:event:submit', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 四、二级菜单：科室审核
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_audit', 'ae_root', '科室审核', '/adverse/audit', 'layouts/RouteView', 0, NULL, 2.0,
  'ant-design:audit-outlined', 1, 0, 0, 0, '1', 0, 'admin', NOW()
);

-- 待审核
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_audit_pending', 'ae_audit', '待审核', '/adverse/audit/pending', 'adverse/audit/PendingList', 0, NULL, 1.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 待审核按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_audit_approve', 'ae_audit_pending', '审核通过', NULL, NULL, 2, 'adverse:audit:approve', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_audit_reject', 'ae_audit_pending', '退回', NULL, NULL, 2, 'adverse:audit:reject', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- 已审核
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_audit_completed', 'ae_audit', '已审核', '/adverse/audit/completed', 'adverse/audit/CompletedList', 0, NULL, 2.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- ========================================
-- 五、二级菜单：职能处理
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_process', 'ae_root', '职能处理', '/adverse/process', 'layouts/RouteView', 0, NULL, 3.0,
  'ant-design:solution-outlined', 1, 0, 0, 0, '1', 0, 'admin', NOW()
);

-- 待处理
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_process_pending', 'ae_process', '待处理', '/adverse/process/pending', 'adverse/process/PendingList', 0, NULL, 1.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 待处理按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_process_handle', 'ae_process_pending', '处理', NULL, NULL, 2, 'adverse:process:handle', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_process_close', 'ae_process_pending', '直接结案', NULL, NULL, 2, 'adverse:process:close', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- 待确认
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_process_confirm', 'ae_process', '待确认', '/adverse/process/confirm', 'adverse/process/ConfirmList', 0, NULL, 2.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 待确认按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_confirm_pass', 'ae_process_confirm', '确认通过', NULL, NULL, 2, 'adverse:confirm:pass', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_confirm_reject', 'ae_process_confirm', '退回整改', NULL, NULL, 2, 'adverse:confirm:reject', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- 已结案
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_process_closed', 'ae_process', '已结案', '/adverse/process/closed', 'adverse/process/ClosedList', 0, NULL, 3.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- ========================================
-- 六、二级菜单：整改管理
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_rectify', 'ae_root', '整改管理', '/adverse/rectify', 'layouts/RouteView', 0, NULL, 4.0,
  'ant-design:tool-outlined', 1, 0, 0, 0, '1', 0, 'admin', NOW()
);

-- 待整改
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_rectify_pending', 'ae_rectify', '待整改', '/adverse/rectify/pending', 'adverse/rectify/PendingList', 0, NULL, 1.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 待整改按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_rectify_submit', 'ae_rectify_pending', '提交整改', NULL, NULL, 2, 'adverse:rectify:submit', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_rectify_draft', 'ae_rectify_pending', '保存草稿', NULL, NULL, 2, 'adverse:rectify:draft', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 七、二级菜单：综合查询
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_query', 'ae_root', '综合查询', '/adverse/query', 'adverse/query/index', 0, NULL, 5.0,
  'ant-design:search-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 综合查询按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_query_export', 'ae_query', '导出', NULL, NULL, 2, 'adverse:query:export', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 八、二级菜单：统计分析
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_stat', 'ae_root', '统计分析', '/adverse/stat', 'adverse/stat/Dashboard', 0, NULL, 6.0,
  'ant-design:bar-chart-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 统计分析按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_stat_export', 'ae_stat', '导出报表', NULL, NULL, 2, 'adverse:stat:export', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 九、二级菜单：系统配置
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_config', 'ae_root', '系统配置', '/adverse/config', 'layouts/RouteView', 0, NULL, 7.0,
  'ant-design:setting-outlined', 1, 0, 0, 0, '1', 0, 'admin', NOW()
);

-- 事件分类管理
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_category', 'ae_config', '事件分类管理', '/adverse/config/category', 'adverse/category/index', 0, NULL, 1.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 事件分类按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_category_add', 'ae_category', '新增', NULL, NULL, 2, 'adverse:category:add', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_category_edit', 'ae_category', '编辑', NULL, NULL, 2, 'adverse:category:edit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_category_delete', 'ae_category', '删除', NULL, NULL, 2, 'adverse:category:delete', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_category_status', 'ae_category', '启用/禁用', NULL, NULL, 2, 'adverse:category:status', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 十、为管理员角色添加菜单权限
-- ========================================
-- 获取 admin 角色 ID 并插入权限关联（假设 admin 角色 ID 为 f6817f48af4fb3af11b9e8bf
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_%';

-- ========================================
-- 菜单结构说明
-- ========================================
-- 不良事件管理 (ae_root)
-- ├── 我的上报 (ae_event) [新增/编辑/删除/提交]
-- ├── 科室审核 (ae_audit)
-- │   ├── 待审核 (ae_audit_pending) [审核通过/退回]
-- │   └── 已审核 (ae_audit_completed)
-- ├── 职能处理 (ae_process)
-- │   ├── 待处理 (ae_process_pending) [处理/直接结案]
-- │   ├── 待确认 (ae_process_confirm) [确认通过/退回整改]
-- │   └── 已结案 (ae_process_closed)
-- ├── 整改管理 (ae_rectify)
-- │   └── 待整改 (ae_rectify_pending) [提交整改/保存草稿]
-- ├── 综合查询 (ae_query) [导出]
-- ├── 统计分析 (ae_stat) [导出报表]
-- └── 系统配置 (ae_config)
--     └── 事件分类管理 (ae_category) [新增/编辑/删除/启用禁用]
