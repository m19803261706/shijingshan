-- ========================================
-- 不良事件上报系统 - 角色权限配置
-- 版本: V1006
-- 描述: 创建角色并分配菜单权限
-- 作者: TC Agent
-- 创建时间: 2025-12-18
-- ========================================

-- ========================================
-- 一、创建角色
-- ========================================

-- 临床上报人员：可上报事件、填写整改
INSERT INTO sys_role (
  id, role_name, role_code, description, create_by, create_time, update_by, update_time
) VALUES (
  'ae_role_reporter', '临床上报人员', 'ae_reporter',
  '不良事件上报系统-临床上报人员，可上报事件、填写整改',
  'admin', NOW(), NULL, NULL
);

-- 科室审核人员：可审核本科室事件
INSERT INTO sys_role (
  id, role_name, role_code, description, create_by, create_time, update_by, update_time
) VALUES (
  'ae_role_auditor', '科室审核人员', 'ae_auditor',
  '不良事件上报系统-科室审核人员，可审核本科室事件',
  'admin', NOW(), NULL, NULL
);

-- 职能科室人员：可处理、要求整改、结案
INSERT INTO sys_role (
  id, role_name, role_code, description, create_by, create_time, update_by, update_time
) VALUES (
  'ae_role_processor', '职能科室人员', 'ae_processor',
  '不良事件上报系统-职能科室人员，可处理事件、要求整改、确认结案',
  'admin', NOW(), NULL, NULL
);

-- 不良事件管理员：全部权限
INSERT INTO sys_role (
  id, role_name, role_code, description, create_by, create_time, update_by, update_time
) VALUES (
  'ae_role_admin', '不良事件管理员', 'ae_admin',
  '不良事件上报系统-系统管理员，拥有全部权限',
  'admin', NOW(), NULL, NULL
);

-- ========================================
-- 二、临床上报人员权限分配
-- 权限：事件上报、整改管理
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_reporter', id FROM sys_permission WHERE id IN (
  'ae_root',           -- 一级菜单
  'ae_event',          -- 我的上报
  'ae_event_add',      -- 新增
  'ae_event_edit',     -- 编辑
  'ae_event_delete',   -- 删除
  'ae_event_submit',   -- 提交
  'ae_rectify',        -- 整改管理
  'ae_rectify_pending',-- 待整改
  'ae_rectify_submit', -- 提交整改
  'ae_rectify_draft'   -- 保存草稿
);

-- ========================================
-- 三、科室审核人员权限分配
-- 权限：事件上报、科室审核、整改管理、统计分析（本科室）
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_auditor', id FROM sys_permission WHERE id IN (
  'ae_root',             -- 一级菜单
  'ae_event',            -- 我的上报
  'ae_event_add',        -- 新增
  'ae_event_edit',       -- 编辑
  'ae_event_delete',     -- 删除
  'ae_event_submit',     -- 提交
  'ae_audit',            -- 科室审核
  'ae_audit_pending',    -- 待审核
  'ae_audit_approve',    -- 审核通过
  'ae_audit_reject',     -- 退回
  'ae_audit_completed',  -- 已审核
  'ae_rectify',          -- 整改管理
  'ae_rectify_pending',  -- 待整改
  'ae_rectify_submit',   -- 提交整改
  'ae_rectify_draft',    -- 保存草稿
  'ae_query',            -- 综合查询（本科室）
  'ae_stat'              -- 统计分析（本科室）
);

-- ========================================
-- 四、职能科室人员权限分配
-- 权限：职能处理、综合查询、统计分析
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_processor', id FROM sys_permission WHERE id IN (
  'ae_root',              -- 一级菜单
  'ae_process',           -- 职能处理
  'ae_process_pending',   -- 待处理
  'ae_process_handle',    -- 处理
  'ae_process_close',     -- 直接结案
  'ae_process_confirm',   -- 待确认
  'ae_confirm_pass',      -- 确认通过
  'ae_confirm_reject',    -- 退回整改
  'ae_process_closed',    -- 已结案
  'ae_query',             -- 综合查询
  'ae_query_export',      -- 导出
  'ae_stat',              -- 统计分析
  'ae_stat_export'        -- 导出报表
);

-- ========================================
-- 五、不良事件管理员权限分配
-- 权限：全部权限
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_admin', id FROM sys_permission WHERE id LIKE 'ae_%';

-- ========================================
-- 六、数据权限规则配置
-- 使用 JeecgBoot 数据权限功能
-- ========================================

-- 临床上报人员：仅看本人数据
INSERT INTO sys_permission_data_rule (
  id, permission_id, rule_name, rule_column, rule_conditions, rule_value,
  status, create_by, create_time
) VALUES (
  'ae_dr_reporter_event', 'ae_event', '上报人员-仅看本人数据',
  'create_by', '=', '#{sys_user_code}',
  1, 'admin', NOW()
);

INSERT INTO sys_permission_data_rule (
  id, permission_id, rule_name, rule_column, rule_conditions, rule_value,
  status, create_by, create_time
) VALUES (
  'ae_dr_reporter_rectify', 'ae_rectify_pending', '上报人员-仅看本人待整改',
  'create_by', '=', '#{sys_user_code}',
  1, 'admin', NOW()
);

-- 科室审核人员：仅看本科室数据
INSERT INTO sys_permission_data_rule (
  id, permission_id, rule_name, rule_column, rule_conditions, rule_value,
  status, create_by, create_time
) VALUES (
  'ae_dr_auditor_pending', 'ae_audit_pending', '审核人员-仅看本科室待审核',
  'department_id', '=', '#{sys_org_code}',
  1, 'admin', NOW()
);

INSERT INTO sys_permission_data_rule (
  id, permission_id, rule_name, rule_column, rule_conditions, rule_value,
  status, create_by, create_time
) VALUES (
  'ae_dr_auditor_completed', 'ae_audit_completed', '审核人员-仅看本科室已审核',
  'department_id', '=', '#{sys_org_code}',
  1, 'admin', NOW()
);

INSERT INTO sys_permission_data_rule (
  id, permission_id, rule_name, rule_column, rule_conditions, rule_value,
  status, create_by, create_time
) VALUES (
  'ae_dr_auditor_query', 'ae_query', '审核人员-综合查询仅看本科室',
  'department_id', '=', '#{sys_org_code}',
  1, 'admin', NOW()
);

INSERT INTO sys_permission_data_rule (
  id, permission_id, rule_name, rule_column, rule_conditions, rule_value,
  status, create_by, create_time
) VALUES (
  'ae_dr_auditor_stat', 'ae_stat', '审核人员-统计分析仅看本科室',
  'department_id', '=', '#{sys_org_code}',
  1, 'admin', NOW()
);

-- 职能科室人员：查看全部数据（处理环节无限制）
-- 注：职能处理人员需要查看所有科室提交的事件，不设数据权限限制

-- ========================================
-- 七、角色-数据权限规则关联
-- ========================================

-- 临床上报人员关联数据权限规则
INSERT INTO sys_role_permission_data_rule (id, role_id, permission_data_rule_id)
VALUES (UUID(), 'ae_role_reporter', 'ae_dr_reporter_event');

INSERT INTO sys_role_permission_data_rule (id, role_id, permission_data_rule_id)
VALUES (UUID(), 'ae_role_reporter', 'ae_dr_reporter_rectify');

-- 科室审核人员关联数据权限规则
INSERT INTO sys_role_permission_data_rule (id, role_id, permission_data_rule_id)
VALUES (UUID(), 'ae_role_auditor', 'ae_dr_auditor_pending');

INSERT INTO sys_role_permission_data_rule (id, role_id, permission_data_rule_id)
VALUES (UUID(), 'ae_role_auditor', 'ae_dr_auditor_completed');

INSERT INTO sys_role_permission_data_rule (id, role_id, permission_data_rule_id)
VALUES (UUID(), 'ae_role_auditor', 'ae_dr_auditor_query');

INSERT INTO sys_role_permission_data_rule (id, role_id, permission_data_rule_id)
VALUES (UUID(), 'ae_role_auditor', 'ae_dr_auditor_stat');

-- ========================================
-- 角色权限矩阵说明
-- ========================================
-- | 功能       | reporter | auditor | processor | admin |
-- |------------|:--------:|:-------:|:---------:|:-----:|
-- | 事件上报   |    ✓     |    ✓    |     -     |   ✓   |
-- | 科室审核   |    -     |    ✓    |     -     |   ✓   |
-- | 职能处理   |    -     |    -    |     ✓     |   ✓   |
-- | 填写整改   |    ✓     |    ✓    |     -     |   ✓   |
-- | 综合查询   |  本人    |  本科室 |   本科室  |  全部 |
-- | 统计分析   |    -     |  本科室 |   本科室  |  全部 |
-- | 系统配置   |    -     |    -    |     -     |   ✓   |
