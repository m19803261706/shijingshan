-- ========================================
-- 药品不良反应审核功能 - 数据库对象
-- 版本: V1017
-- 描述: 添加审核相关字段，创建流转记录表
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- Issue: #44
-- Epic: #43 临床科室审核功能开发
-- ========================================

-- ========== 1. 添加审核相关字段到 drug_adverse_report 表 ==========

-- 审核人ID
ALTER TABLE `drug_adverse_report`
ADD COLUMN `audit_user_id` varchar(36) DEFAULT NULL COMMENT '审核人ID' AFTER `status`;

-- 审核人姓名
ALTER TABLE `drug_adverse_report`
ADD COLUMN `audit_user_name` varchar(100) DEFAULT NULL COMMENT '审核人姓名' AFTER `audit_user_id`;

-- 审核时间
ALTER TABLE `drug_adverse_report`
ADD COLUMN `audit_time` datetime DEFAULT NULL COMMENT '审核时间' AFTER `audit_user_name`;

-- 审核意见
ALTER TABLE `drug_adverse_report`
ADD COLUMN `audit_comment` text COMMENT '审核意见' AFTER `audit_time`;

-- 添加审核人索引
ALTER TABLE `drug_adverse_report`
ADD KEY `idx_audit_user` (`audit_user_id`) COMMENT '审核人索引';

-- ========== 2. 创建药品不良反应流转记录表 ==========

CREATE TABLE IF NOT EXISTS `drug_adverse_flow` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `report_id` varchar(36) NOT NULL COMMENT '关联报告ID',
  `action` varchar(32) NOT NULL COMMENT '操作类型：submit-提交, audit_pass-审核通过, audit_reject-审核退回, resubmit-重新提交, process_start-开始处理, require_rectify-要求整改, submit_rectify-提交整改, confirm_rectify-确认整改, close-结案',
  `from_status` varchar(32) DEFAULT NULL COMMENT '操作前状态',
  `to_status` varchar(32) DEFAULT NULL COMMENT '操作后状态',
  `operator_id` varchar(36) DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(100) DEFAULT NULL COMMENT '操作人姓名',
  `comment` text COMMENT '操作备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`) COMMENT '报告ID索引',
  KEY `idx_action` (`action`) COMMENT '操作类型索引',
  KEY `idx_operator_id` (`operator_id`) COMMENT '操作人索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='药品不良反应流转记录表';

-- 添加表注释
ALTER TABLE `drug_adverse_flow` COMMENT = '药品不良反应流转记录表 - 记录报告在各状态间的流转历史';

-- ========== 3. 添加审核相关数据字典 ==========

-- 药品不良反应审核操作类型字典
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`, `tenant_id`)
SELECT UUID(), '药品不良反应流转操作', 'drug_adr_flow_action', '药品不良反应报告流转操作类型', 0, 'admin', NOW(), 0, NULL
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action');

-- 添加字典项
INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT UUID(), (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action'), '提交', 'submit', '提交报告', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action') AND `item_value` = 'submit');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT UUID(), (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action'), '审核通过', 'audit_pass', '审核通过', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action') AND `item_value` = 'audit_pass');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT UUID(), (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action'), '审核退回', 'audit_reject', '审核退回', 3, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action') AND `item_value` = 'audit_reject');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT UUID(), (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action'), '重新提交', 'resubmit', '重新提交', 4, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action') AND `item_value` = 'resubmit');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT UUID(), (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action'), '开始处理', 'process_start', '开始处理', 5, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action') AND `item_value` = 'process_start');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT UUID(), (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action'), '要求整改', 'require_rectify', '要求整改', 6, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action') AND `item_value` = 'require_rectify');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT UUID(), (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action'), '提交整改', 'submit_rectify', '提交整改', 7, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action') AND `item_value` = 'submit_rectify');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT UUID(), (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action'), '确认整改', 'confirm_rectify', '确认整改', 8, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action') AND `item_value` = 'confirm_rectify');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
SELECT UUID(), (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action'), '结案', 'close', '结案', 9, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = (SELECT `id` FROM `sys_dict` WHERE `dict_code` = 'drug_adr_flow_action') AND `item_value` = 'close');
