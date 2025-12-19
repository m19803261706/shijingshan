-- ========================================
-- 临床科室整改 - 菜单权限配置
-- 版本: V1021
-- 描述: 创建临床科室整改提交的菜单和按钮权限
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- Issue: #56
-- ========================================

-- ========================================
-- 一、二级菜单：临床科室整改（在不良事件管理下）
-- ========================================
-- 放置在药品科室处理之后（sort_no = 2.6）
INSERT INTO sys_permission(
  id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
  is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
  'ae_clinic_rectify', 'ae_root', '临床科室整改', '/adverse/clinic-rectify', 'adverse/clinic-rectify/index', 0, NULL, 2.6,
  'ant-design:edit-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- ========================================
-- 二、按钮权限
-- ========================================
-- 查看权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_rectify_view', 'ae_clinic_rectify', '查看', NULL, NULL, 2, 'adverse:clinicRectify:view', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- 提交整改权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_clinic_rectify_submit', 'ae_clinic_rectify', '提交整改', NULL, NULL, 2, 'adverse:clinicRectify:submit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 三、为管理员角色添加临床科室整改权限
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_clinic_rectify%';

-- ========================================
-- 四、创建临床科室角色（如不存在）
-- ========================================
INSERT IGNORE INTO sys_role (
  id, role_name, role_code, description, create_by, create_time, update_by, update_time
) VALUES (
  'ae_role_clinic', '临床科室', 'ae_clinic', '临床科室角色，负责查看和提交整改措施', 'admin', NOW(), NULL, NULL
);

-- ========================================
-- 五、为临床科室角色添加相关权限
-- ========================================
-- 添加菜单和按钮权限
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_clinic', id FROM sys_permission WHERE id IN (
  'ae_root',                  -- 不良事件管理（父菜单）
  'ae_clinic_rectify',        -- 临床科室整改（菜单）
  'ae_clinic_rectify_view',   -- 查看
  'ae_clinic_rectify_submit'  -- 提交整改
);

-- 临床科室也需要药品不良反应报告权限（上报功能）
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_clinic', id FROM sys_permission WHERE id IN (
  'ae_drug',               -- 药品不良反应（菜单）
  'ae_drug_report',        -- 我的上报
  'ae_drug_report_add',    -- 新增
  'ae_drug_report_edit',   -- 编辑
  'ae_drug_report_delete', -- 删除
  'ae_drug_report_view',   -- 查看
  'ae_drug_form'           -- 报告表单
) ON DUPLICATE KEY UPDATE id = id;

-- ========================================
-- 菜单结构说明
-- ========================================
-- 不良事件管理 (ae_root)
-- ├── 不良事件上报 (ae_event)
-- ├── 药品不良反应 (ae_drug)
-- │   ├── 我的上报 (ae_drug_report)
-- │   ├── 报告表单 (ae_drug_form) [隐藏菜单]
-- │   ├── 怀疑药品配置 (ae_drug_suspect_config)
-- │   ├── 并用药品配置 (ae_drug_concomitant_config)
-- │   └── 药品常用配置 (ae_drug_common_config)
-- ├── 临床科室审核 (ae_clinic_audit)
-- │   └── 药品不良反应审核 (ae_clinic_audit_drug)
-- ├── 药品科室处理 (ae_drug_process)
-- │   ├── 查看 (ae_drug_process_view)
-- │   ├── 直接结案 (ae_drug_process_close)
-- │   ├── 要求整改 (ae_drug_process_require)
-- │   ├── 确认整改 (ae_drug_process_confirm)
-- │   └── 退回整改 (ae_drug_process_reject)
-- ├── 临床科室整改 (ae_clinic_rectify) <-- 新增
-- │   ├── 查看 (ae_clinic_rectify_view)
-- │   └── 提交整改 (ae_clinic_rectify_submit)
-- ├── 职能科室审核 (ae_audit)
-- ├── 事件处理 (ae_process)
-- ├── 整改追踪 (ae_rectify)
-- ├── 事件分类 (ae_category)
-- ├── 统计报表 (ae_stat)
-- └── 高级查询 (ae_query)
