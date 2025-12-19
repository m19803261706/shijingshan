-- ========================================
-- 临床科室审核 - 菜单权限配置
-- 版本: V1018
-- 描述: 创建临床科室审核的菜单和按钮权限
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- Issue: #47
-- ========================================

-- ========================================
-- 一、二级菜单：临床科室审核（在不良事件管理下）
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_clinic_audit', 'ae_root', '临床科室审核', '/adverse/clinic-audit', 'layouts/RouteView', 0, NULL, 1.8,
  'ant-design:audit-outlined', 1, 0, 0, 0, '1', 0, 'admin', NOW()
);

-- ========================================
-- 二、三级菜单：药品不良反应审核
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_clinic_audit_drug', 'ae_clinic_audit', '药品不良反应审核', '/adverse/clinic-audit/drug', 'adverse/clinic-audit/drug/index', 0, NULL, 1.0,
  'ant-design:medicine-box-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 药品不良反应审核按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_drug_view', 'ae_clinic_audit_drug', '查看', NULL, NULL, 2, 'adverse:clinicAudit:drug:view', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_drug_pass', 'ae_clinic_audit_drug', '审核通过', NULL, NULL, 2, 'adverse:clinicAudit:drug:pass', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_drug_reject', 'ae_clinic_audit_drug', '审核退回', NULL, NULL, 2, 'adverse:clinicAudit:drug:reject', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 三、三级菜单：不良事件审核（预留，后续开发）
-- ========================================
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_clinic_audit_event', 'ae_clinic_audit', '不良事件审核', '/adverse/clinic-audit/event', 'adverse/clinic-audit/event/index', 0, NULL, 2.0,
  'ant-design:exception-outlined', 1, 1, 0, 1, '1', 0, 'admin', NOW()
);
-- 注：不良事件审核暂时隐藏（hidden=1），等待后续开发

-- 不良事件审核按钮权限（预留）
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_event_view', 'ae_clinic_audit_event', '查看', NULL, NULL, 2, 'adverse:clinicAudit:event:view', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_event_pass', 'ae_clinic_audit_event', '审核通过', NULL, NULL, 2, 'adverse:clinicAudit:event:pass', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_audit_event_reject', 'ae_clinic_audit_event', '审核退回', NULL, NULL, 2, 'adverse:clinicAudit:event:reject', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 四、更新原有科室审核菜单名称（区分为职能科室审核）
-- ========================================
UPDATE sys_permission SET name = '职能科室审核' WHERE id = 'ae_audit';

-- ========================================
-- 五、为管理员角色添加临床科室审核权限
-- ========================================
-- 添加临床科室审核相关权限到 admin 角色
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_clinic_audit%';

-- ========================================
-- 六、为科室审核人员角色添加临床科室审核权限
-- ========================================
-- 添加临床科室审核菜单权限到审核人员角色
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_auditor', id FROM sys_permission WHERE id IN (
  'ae_clinic_audit',
  'ae_clinic_audit_drug',
  'ae_clinic_audit_drug_view',
  'ae_clinic_audit_drug_pass',
  'ae_clinic_audit_drug_reject'
);

-- ========================================
-- 菜单结构说明
-- ========================================
-- 不良事件管理 (ae_root)
-- ├── 我的上报 (ae_event) - 通用事件上报
-- ├── 药品不良反应 (ae_drug)
-- │   ├── 我的上报 (ae_drug_report)
-- │   ├── 报告表单 (ae_drug_form) [隐藏菜单]
-- │   ├── 怀疑药品配置 (ae_drug_suspect_config)
-- │   └── 并用药品配置 (ae_drug_concomitant_config)
-- ├── 临床科室审核 (ae_clinic_audit) <-- 新增
-- │   ├── 药品不良反应审核 (ae_clinic_audit_drug) [查看/审核通过/审核退回]
-- │   └── 不良事件审核 (ae_clinic_audit_event) [隐藏，预留]
-- ├── 职能科室审核 (ae_audit) <-- 原科室审核，改名
-- │   ├── 待审核 (ae_audit_pending)
-- │   └── 已审核 (ae_audit_completed)
-- ├── 职能处理 (ae_process)
-- ├── 整改管理 (ae_rectify)
-- ├── 综合查询 (ae_query)
-- ├── 统计分析 (ae_stat)
-- └── 系统配置 (ae_config)
