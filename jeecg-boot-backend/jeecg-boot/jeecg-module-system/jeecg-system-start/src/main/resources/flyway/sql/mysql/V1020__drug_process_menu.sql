-- ========================================
-- 药品科室处理 - 菜单权限配置
-- 版本: V1020
-- 描述: 创建药品科室处理（药剂科）的菜单和按钮权限
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- Issue: #52
-- ========================================

-- ========================================
-- 一、二级菜单：药品科室处理（在不良事件管理下）
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_drug_process', 'ae_root', '药品科室处理', '/adverse/drug-process', 'adverse/drug-process/index', 0, NULL, 2.5,
  'ant-design:experiment-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- ========================================
-- 二、按钮权限
-- ========================================
-- 查看权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_process_view', 'ae_drug_process', '查看', NULL, NULL, 2, 'adverse:drugProcess:view', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- 直接结案权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_process_close', 'ae_drug_process', '直接结案', NULL, NULL, 2, 'adverse:drugProcess:close', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- 要求整改权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_process_require', 'ae_drug_process', '要求整改', NULL, NULL, 2, 'adverse:drugProcess:require', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- 确认整改权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_process_confirm', 'ae_drug_process', '确认整改', NULL, NULL, 2, 'adverse:drugProcess:confirm', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- 退回整改权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_drug_process_reject', 'ae_drug_process', '退回整改', NULL, NULL, 2, 'adverse:drugProcess:reject', 5.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 三、为管理员角色添加药品科室处理权限
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_drug_process%';

-- ========================================
-- 四、创建药剂科角色（如不存在）
-- ========================================
INSERT IGNORE INTO sys_role (
  id, role_name, role_code, description, create_by, create_time, update_by, update_time
) VALUES (
  'ae_role_pharmacy', '药剂科', 'ae_pharmacy', '药剂科角色，负责药品不良反应报告的处理和整改确认', 'admin', NOW(), NULL, NULL
);

-- ========================================
-- 五、为药剂科角色添加药品科室处理权限
-- ========================================
-- 添加菜单和按钮权限
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_pharmacy', id FROM sys_permission WHERE id IN (
  'ae_root',           -- 不良事件管理（父菜单）
  'ae_drug_process',   -- 药品科室处理（菜单）
  'ae_drug_process_view',    -- 查看
  'ae_drug_process_close',   -- 直接结案
  'ae_drug_process_require', -- 要求整改
  'ae_drug_process_confirm', -- 确认整改
  'ae_drug_process_reject'   -- 退回整改
);

-- ========================================
-- 菜单结构说明
-- ========================================
-- 不良事件管理 (ae_root)
-- ├── 不良事件上报 (ae_event)
-- ├── 药品不良反应 (ae_drug)
-- │   ├── 我的上报 (ae_drug_report)
-- │   ├── 报告表单 (ae_drug_form) [隐藏菜单]
-- │   ├── 怀疑药品配置 (ae_drug_suspect_config)
-- │   └── 并用药品配置 (ae_drug_concomitant_config)
-- ├── 临床科室审核 (ae_clinic_audit)
-- │   ├── 药品不良反应审核 (ae_clinic_audit_drug)
-- │   └── 不良事件审核 (ae_clinic_audit_event) [隐藏，预留]
-- ├── 药品科室处理 (ae_drug_process) <-- 新增
-- │   ├── 查看 (ae_drug_process_view)
-- │   ├── 直接结案 (ae_drug_process_close)
-- │   ├── 要求整改 (ae_drug_process_require)
-- │   ├── 确认整改 (ae_drug_process_confirm)
-- │   └── 退回整改 (ae_drug_process_reject)
-- ├── 职能科室审核 (ae_audit)
-- ├── 事件处理 (ae_process)
-- ├── 整改追踪 (ae_rectify)
-- ├── 事件分类 (ae_category)
-- ├── 统计报表 (ae_stat)
-- └── 高级查询 (ae_query)
