-- ========================================
-- 医疗器械不良事件 - 菜单权限配置
-- 版本: V1027
-- 描述: 创建医疗器械不良事件上报的完整菜单和按钮权限
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- Issue: #72
-- ========================================

-- ========================================
-- 一、二级菜单：医疗器械不良事件（在不良事件管理下）
-- ========================================
-- 父菜单（is_leaf=0 表示有子菜单）
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_device', 'ae_root', '医疗器械不良事件', '/adverse/device', 'layouts/RouteView', 0, NULL, 1.6,
  'ant-design:tool-outlined', 1, 0, 0, 0, '1', 0, 'admin', NOW()
);

-- ========================================
-- 二、三级菜单：我的报告
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_device_report', 'ae_device', '我的报告', '/adverse/device/report', 'adverse/device/index', 0, NULL, 1.0,
  NULL, 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 我的报告按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_report_add', 'ae_device_report', '新增', NULL, NULL, 2, 'adverse:device:add', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_report_edit', 'ae_device_report', '编辑', NULL, NULL, 2, 'adverse:device:edit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_report_delete', 'ae_device_report', '删除', NULL, NULL, 2, 'adverse:device:delete', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_report_submit', 'ae_device_report', '提交', NULL, NULL, 2, 'adverse:device:submit', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_report_view', 'ae_device_report', '查看', NULL, NULL, 2, 'adverse:device:view', 5.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 三、隐藏菜单：报告表单页
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_device_form', 'ae_device', '报告表单', '/adverse/device/form', 'adverse/device/DeviceReportForm', 0, NULL, 2.0,
  NULL, 1, 1, 0, 1, '1', 0, 'admin', NOW()
);

-- ========================================
-- 四、临床科室审核菜单：医疗器械不良事件审核
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_clinic_audit_device', 'ae_clinic_audit', '医疗器械不良事件审核', '/adverse/clinic-audit/device', 'adverse/clinic-audit/device/index', 0, NULL, 2.0,
  'ant-design:tool-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 医疗器械审核按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_device_view', 'ae_clinic_audit_device', '查看', NULL, NULL, 2, 'adverse:clinicAudit:device:view', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_device_pass', 'ae_clinic_audit_device', '审核通过', NULL, NULL, 2, 'adverse:clinicAudit:device:pass', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_device_reject', 'ae_clinic_audit_device', '审核退回', NULL, NULL, 2, 'adverse:clinicAudit:device:reject', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 五、器械科处理菜单
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_device_process', 'ae_root', '器械科处理', '/adverse/device-process', 'adverse/device-process/index', 0, NULL, 2.7,
  'ant-design:tool-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 器械科处理按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_process_view', 'ae_device_process', '查看', NULL, NULL, 2, 'adverse:deviceProcess:view', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_process_close', 'ae_device_process', '直接结案', NULL, NULL, 2, 'adverse:deviceProcess:close', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_process_require', 'ae_device_process', '要求整改', NULL, NULL, 2, 'adverse:deviceProcess:require', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_process_confirm', 'ae_device_process', '确认整改', NULL, NULL, 2, 'adverse:deviceProcess:confirm', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_process_reject', 'ae_device_process', '退回整改', NULL, NULL, 2, 'adverse:deviceProcess:reject', 5.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 六、为管理员角色添加医疗器械相关权限
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_device%';

INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_clinic_audit_device%';

-- ========================================
-- 七、创建器械科角色
-- ========================================
INSERT IGNORE INTO sys_role (
  id, role_name, role_code, description, create_by, create_time, update_by, update_time
) VALUES (
  'ae_role_device', '器械科', 'ae_device', '器械科角色，负责医疗器械不良事件报告的处理和整改确认', 'admin', NOW(), NULL, NULL
);

-- ========================================
-- 八、为器械科角色添加权限
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_device', id FROM sys_permission WHERE id IN (
  'ae_root',                    -- 不良事件管理（父菜单）
  'ae_device_process',          -- 器械科处理（菜单）
  'ae_device_process_view',     -- 查看
  'ae_device_process_close',    -- 直接结案
  'ae_device_process_require',  -- 要求整改
  'ae_device_process_confirm',  -- 确认整改
  'ae_device_process_reject'    -- 退回整改
);

-- ========================================
-- 九、为临床科室角色添加医疗器械相关权限
-- ========================================
-- 临床科室需要器械上报权限和整改权限
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_clinic', id FROM sys_permission WHERE id IN (
  'ae_device',               -- 医疗器械不良事件（菜单）
  'ae_device_report',        -- 我的报告
  'ae_device_report_add',    -- 新增
  'ae_device_report_edit',   -- 编辑
  'ae_device_report_delete', -- 删除
  'ae_device_report_view',   -- 查看
  'ae_device_report_submit', -- 提交
  'ae_device_form'           -- 报告表单
) ON DUPLICATE KEY UPDATE id = id;

-- ========================================
-- 十、为审核人员角色添加医疗器械审核权限
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_auditor', id FROM sys_permission WHERE id IN (
  'ae_clinic_audit_device',
  'ae_clinic_audit_device_view',
  'ae_clinic_audit_device_pass',
  'ae_clinic_audit_device_reject'
) ON DUPLICATE KEY UPDATE id = id;

-- ========================================
-- 菜单结构说明
-- ========================================
-- 不良事件管理 (ae_root)
-- ├── 我的上报 (ae_event) - 通用事件上报
-- ├── 药品不良反应 (ae_drug)
-- │   ├── 我的上报 (ae_drug_report)
-- │   ├── 报告表单 (ae_drug_form) [隐藏]
-- │   └── 药品常用配置 (ae_drug_common_config)
-- ├── 医疗器械不良事件 (ae_device) <-- 新增
-- │   ├── 我的报告 (ae_device_report) [新增/编辑/删除/提交/查看]
-- │   └── 报告表单 (ae_device_form) [隐藏]
-- ├── 临床科室审核 (ae_clinic_audit)
-- │   ├── 药品不良反应审核 (ae_clinic_audit_drug)
-- │   └── 医疗器械不良事件审核 (ae_clinic_audit_device) <-- 新增
-- ├── 药品科室处理 (ae_drug_process)
-- ├── 临床科室整改 (ae_clinic_rectify)
-- ├── 器械科处理 (ae_device_process) <-- 新增
-- │   ├── 查看 (ae_device_process_view)
-- │   ├── 直接结案 (ae_device_process_close)
-- │   ├── 要求整改 (ae_device_process_require)
-- │   ├── 确认整改 (ae_device_process_confirm)
-- │   └── 退回整改 (ae_device_process_reject)
-- ├── 职能科室审核 (ae_audit)
-- ├── 事件处理 (ae_process)
-- ├── 整改追踪 (ae_rectify)
-- ├── 事件分类 (ae_category)
-- ├── 统计报表 (ae_stat)
-- └── 高级查询 (ae_query)
--
-- 角色权限说明
-- ========================================
-- ae_role_clinic (临床科室):
--   - 药品不良反应报告 (ae_drug_report + 按钮)
--   - 医疗器械不良事件报告 (ae_device_report + 按钮)
--   - 临床科室整改 (ae_clinic_rectify + 按钮)
--
-- ae_role_auditor (科室审核人员):
--   - 临床科室审核 (ae_clinic_audit)
--   - 药品不良反应审核 (ae_clinic_audit_drug + 按钮)
--   - 医疗器械不良事件审核 (ae_clinic_audit_device + 按钮)
--
-- ae_role_pharmacy (药剂科):
--   - 药品科室处理 (ae_drug_process + 按钮)
--
-- ae_role_device (器械科):
--   - 器械科处理 (ae_device_process + 按钮)
