-- ========================================
-- 输血使用不良事件 - 菜单权限配置
-- 版本: V1031
-- 描述: 创建输血不良事件上报的完整菜单和按钮权限
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- Issue: #76, Epic #73
-- ========================================

-- ========================================
-- 一、二级菜单：输血不良事件（在不良事件管理下）
-- ========================================
-- 父菜单（is_leaf=0 表示有子菜单）
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_blood', 'ae_root', '输血不良事件', '/adverse/blood', 'layouts/RouteView', 0, NULL, 1.7,
  'ant-design:heart-outlined', 1, 0, 0, 0, '1', 0, 'admin', NOW()
);

-- ========================================
-- 二、三级菜单：我的报告
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_blood_report', 'ae_blood', '我的报告', '/adverse/blood/report', 'adverse/blood/index', 0, NULL, 1.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 我的报告按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_blood_report_add', 'ae_blood_report', '新增', NULL, NULL, 2, 'adverse:blood:add', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_blood_report_edit', 'ae_blood_report', '编辑', NULL, NULL, 2, 'adverse:blood:edit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_blood_report_delete', 'ae_blood_report', '删除', NULL, NULL, 2, 'adverse:blood:delete', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_blood_report_submit', 'ae_blood_report', '提交', NULL, NULL, 2, 'adverse:blood:submit', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_blood_report_view', 'ae_blood_report', '查看', NULL, NULL, 2, 'adverse:blood:view', 5.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 三、隐藏菜单：报告表单页
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_blood_form', 'ae_blood', '报告表单', '/adverse/blood/form', 'adverse/blood/BloodReportForm', 0, NULL, 2.0,
  NULL, 1, 1, 0, 1, '1', 0, 'admin', NOW()
);

-- ========================================
-- 四、临床科室审核菜单：输血不良事件审核
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_clinic_audit_blood', 'ae_clinic_audit', '输血不良事件审核', '/adverse/clinic-audit/blood', 'adverse/clinic-audit/blood/index', 0, NULL, 3.0,
  'ant-design:heart-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 输血科审核按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_blood_view', 'ae_clinic_audit_blood', '查看', NULL, NULL, 2, 'adverse:clinicAudit:blood:view', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_blood_pass', 'ae_clinic_audit_blood', '审核通过', NULL, NULL, 2, 'adverse:clinicAudit:blood:pass', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_blood_reject', 'ae_clinic_audit_blood', '审核退回', NULL, NULL, 2, 'adverse:clinicAudit:blood:reject', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 五、输血科处理菜单
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_blood_process', 'ae_root', '输血科处理', '/adverse/blood-process', 'adverse/blood-process/index', 0, NULL, 2.8,
  'ant-design:heart-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 输血科处理按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_blood_process_view', 'ae_blood_process', '查看', NULL, NULL, 2, 'adverse:bloodProcess:view', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_blood_process_close', 'ae_blood_process', '直接结案', NULL, NULL, 2, 'adverse:bloodProcess:close', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_blood_process_require', 'ae_blood_process', '要求整改', NULL, NULL, 2, 'adverse:bloodProcess:require', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_blood_process_confirm', 'ae_blood_process', '确认整改', NULL, NULL, 2, 'adverse:bloodProcess:confirm', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 六、临床整改菜单（输血部分）
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_clinic_rectify_blood', 'ae_clinic_rectify', '输血不良事件整改', '/adverse/clinic-rectify/blood', 'adverse/clinic-rectify/blood/index', 0, NULL, 3.0,
  'ant-design:heart-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 临床整改按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_rectify_blood_view', 'ae_clinic_rectify_blood', '查看', NULL, NULL, 2, 'adverse:clinicRectify:blood:view', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_rectify_blood_submit', 'ae_clinic_rectify_blood', '提交整改', NULL, NULL, 2, 'adverse:clinicRectify:blood:submit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 七、角色权限分配
-- ========================================

-- 管理员角色(admin)拥有所有权限 - 插入角色权限关联
INSERT IGNORE INTO sys_role_permission(id, role_id, permission_id)
SELECT CONCAT('srp_blood_', p.id), 'f6817f48af4fb3af11b9e8bf182f618b', p.id
FROM sys_permission p
WHERE p.id LIKE 'ae_blood%' OR p.id LIKE 'ae_clinic_%_blood%';

-- 临床人员角色 - 可以上报和整改
INSERT IGNORE INTO sys_role_permission(id, role_id, permission_id)
SELECT CONCAT('srp_clinic_blood_', p.id), 'ae_role_clinic', p.id
FROM sys_permission p
WHERE p.id IN (
  'ae_blood',
  'ae_blood_report', 'ae_blood_report_add', 'ae_blood_report_edit', 'ae_blood_report_delete', 'ae_blood_report_submit', 'ae_blood_report_view',
  'ae_blood_form',
  'ae_clinic_rectify_blood', 'ae_clinic_rectify_blood_view', 'ae_clinic_rectify_blood_submit'
);

-- 输血科角色 - 可以审核和处理
INSERT IGNORE INTO sys_role_permission(id, role_id, permission_id)
SELECT CONCAT('srp_blood_dept_', p.id), 'ae_role_blood_dept', p.id
FROM sys_permission p
WHERE p.id IN (
  'ae_clinic_audit_blood', 'ae_clinic_audit_blood_view', 'ae_clinic_audit_blood_pass', 'ae_clinic_audit_blood_reject',
  'ae_blood_process', 'ae_blood_process_view', 'ae_blood_process_close', 'ae_blood_process_require', 'ae_blood_process_confirm'
);

-- ========================================
-- 八、创建输血科角色（如果不存在）
-- ========================================
INSERT IGNORE INTO sys_role(id, role_name, role_code, description, create_by, create_time)
VALUES ('ae_role_blood_dept', '输血科', 'blood_dept', '输血科角色，负责输血不良事件的审核和处理', 'admin', NOW());
