-- ========================================
-- 常用医疗器械配置表
-- 版本: V1028
-- 描述: 创建常用医疗器械配置表，支持快速选择和使用统计
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- ========================================

-- ========================================
-- 一、创建常用医疗器械配置表
-- ========================================
CREATE TABLE IF NOT EXISTS `device_common_config` (
    `id` varchar(36) NOT NULL COMMENT '主键ID',

    -- 器械基本信息
    `product_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
    `trade_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
    `registration_no` varchar(100) DEFAULT NULL COMMENT '注册证号',
    `manufacturer_name` varchar(200) DEFAULT NULL COMMENT '生产企业名称',
    `manufacturer_address` varchar(500) DEFAULT NULL COMMENT '生产企业地址',
    `manufacturer_contact` varchar(100) DEFAULT NULL COMMENT '企业联系方式',
    `model_spec` varchar(200) DEFAULT NULL COMMENT '型号规格',
    `product_code` varchar(100) DEFAULT NULL COMMENT '产品编号',
    `product_batch` varchar(100) DEFAULT NULL COMMENT '产品批号',

    -- 使用统计
    `use_count` int(11) DEFAULT 0 COMMENT '使用次数（用于排序推荐）',
    `last_used_time` datetime DEFAULT NULL COMMENT '最后使用时间',

    -- 系统字段
    `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `del_flag` int(1) DEFAULT 0 COMMENT '删除标记（0-正常 1-已删除）',
    `tenant_id` int(10) DEFAULT NULL COMMENT '租户ID',

    PRIMARY KEY (`id`),
    KEY `idx_product_name` (`product_name`),
    KEY `idx_registration_no` (`registration_no`),
    KEY `idx_manufacturer` (`manufacturer_name`),
    KEY `idx_use_count` (`use_count` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='常用医疗器械配置表';

-- ========================================
-- 二、添加菜单（在医疗器械不良事件菜单下）
-- ========================================
INSERT INTO sys_permission(
    id, parent_id, name, url, component, menu_type, perms, sort_no, icon,
    is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time
) VALUES (
    'ae_device_config', 'ae_device', '常用器械配置', '/adverse/device/config',
    'adverse/device/config/index', 0, NULL, 3.0,
    'ant-design:setting-outlined', 1, 1, 0, 0, '1', 0, 'admin', NOW()
);

-- 按钮权限
INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_config_add', 'ae_device_config', '新增', NULL, NULL, 2, 'adverse:deviceConfig:add', 1.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_config_edit', 'ae_device_config', '编辑', NULL, NULL, 2, 'adverse:deviceConfig:edit', 2.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_config_delete', 'ae_device_config', '删除', NULL, NULL, 2, 'adverse:deviceConfig:delete', 3.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_config_view', 'ae_device_config', '查看', NULL, NULL, 2, 'adverse:deviceConfig:view', 4.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_config_export', 'ae_device_config', '导出', NULL, NULL, 2, 'adverse:deviceConfig:export', 5.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

INSERT INTO sys_permission(id, parent_id, name, url, component, menu_type, perms, sort_no, icon, is_route, is_leaf, keep_alive, hidden, status, del_flag, create_by, create_time)
VALUES ('ae_device_config_import', 'ae_device_config', '导入', NULL, NULL, 2, 'adverse:deviceConfig:import', 6.0, NULL, 0, 1, 0, 0, '1', 0, 'admin', NOW());

-- ========================================
-- 三、为管理员角色添加权限
-- ========================================
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'f6817f48af4fb3af11b9e8bf', id FROM sys_permission WHERE id LIKE 'ae_device_config%';

-- ========================================
-- 四、为临床科室角色添加查看和使用权限
-- ========================================
INSERT IGNORE INTO sys_role_permission (id, role_id, permission_id)
SELECT UUID(), 'ae_role_clinic', sp.id FROM sys_permission sp WHERE sp.id IN (
    'ae_device_config',
    'ae_device_config_view',
    'ae_device_config_add'
);

-- ========================================
-- 菜单结构说明
-- ========================================
-- 不良事件管理 (ae_root)
-- └── 医疗器械不良事件 (ae_device)
--     ├── 我的报告 (ae_device_report)
--     ├── 报告表单 (ae_device_form) [隐藏]
--     └── 常用器械配置 (ae_device_config) <-- 新增
--         ├── 新增 (ae_device_config_add)
--         ├── 编辑 (ae_device_config_edit)
--         ├── 删除 (ae_device_config_delete)
--         ├── 查看 (ae_device_config_view)
--         ├── 导出 (ae_device_config_export)
--         └── 导入 (ae_device_config_import)
