-- ========================================
-- 医疗器械不良事件上报系统 - 整改表 + 数据字典
-- 版本: V1026
-- 描述: 创建医疗器械不良事件整改表 device_adverse_rectify 及相关数据字典
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- 参考: drug_adverse_rectify 表结构（1:1 复刻）
-- GitHub Issue: #60
-- ========================================

-- ========================================
-- 一、创建医疗器械不良事件整改表
-- ========================================

CREATE TABLE IF NOT EXISTS `device_adverse_rectify` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `report_id` varchar(36) NOT NULL COMMENT '关联报告ID（关联 device_adverse_report.id）',
  `rectify_round` int NOT NULL DEFAULT 1 COMMENT '整改轮次（第几次整改）',
  `prev_rectify_id` varchar(36) DEFAULT NULL COMMENT '上一轮整改记录ID（退回后创建新记录时关联）',

  -- ========== 整改要求（器械科填写） ==========
  `requirement` text COMMENT '整改要求',
  `deadline` date DEFAULT NULL COMMENT '整改期限',
  `require_user_id` varchar(36) DEFAULT NULL COMMENT '下发人ID',
  `require_user_name` varchar(50) DEFAULT NULL COMMENT '下发人姓名',
  `require_time` datetime DEFAULT NULL COMMENT '下发时间',
  `require_comment` text COMMENT '下发备注',

  -- ========== 整改措施（临床人员填写） ==========
  `measures` text COMMENT '整改措施',
  `completion` text COMMENT '完成情况',
  `submit_user_id` varchar(36) DEFAULT NULL COMMENT '提交人ID',
  `submit_user_name` varchar(50) DEFAULT NULL COMMENT '提交人姓名',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',

  -- ========== 确认结果（器械科填写） ==========
  `confirm_result` varchar(20) DEFAULT NULL COMMENT '确认结果：approved-通过, rejected-退回',
  `confirm_user_id` varchar(36) DEFAULT NULL COMMENT '确认人ID',
  `confirm_user_name` varchar(50) DEFAULT NULL COMMENT '确认人姓名',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `confirm_comment` text COMMENT '确认意见',

  -- ========== 状态 ==========
  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待整改, submitted-已提交, approved-已通过, rejected-已退回',

  -- ========== JeecgBoot 标准系统字段 ==========
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标记（0-正常 1-已删除）',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID（多租户支持）',

  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`) COMMENT '报告ID索引',
  KEY `idx_rectify_round` (`rectify_round`) COMMENT '整改轮次索引',
  KEY `idx_status` (`status`) COMMENT '状态索引',
  KEY `idx_require_user` (`require_user_id`) COMMENT '下发人索引',
  KEY `idx_submit_user` (`submit_user_id`) COMMENT '提交人索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引',
  CONSTRAINT `fk_device_rectify_report` FOREIGN KEY (`report_id`) REFERENCES `device_adverse_report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_device_rectify_prev` FOREIGN KEY (`prev_rectify_id`) REFERENCES `device_adverse_rectify` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='医疗器械不良事件整改表';

-- 添加表注释
ALTER TABLE `device_adverse_rectify` COMMENT = '医疗器械不良事件整改表 - 记录整改要求、整改措施和确认结果，支持多轮整改';

-- ========================================
-- 二、创建数据字典
-- ========================================

-- 1. 医疗器械不良事件状态
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
SELECT UUID(), '医疗器械不良事件状态', 'device_adr_status', '医疗器械不良事件报告状态', 0, 'admin', NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE dict_code = 'device_adr_status');

-- 状态字典项
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status'), '草稿', 'draft', '临床人员填写中', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status') AND item_value = 'draft');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status'), '待审核', 'pending_audit', '已提交，等待科室审核', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status') AND item_value = 'pending_audit');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status'), '已退回', 'returned', '科室审核退回', 3, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status') AND item_value = 'returned');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status'), '待处理', 'pending_process', '审核通过，等待器械科处理', 4, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status') AND item_value = 'pending_process');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status'), '待整改', 'pending_rectify', '器械科下发整改要求', 5, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status') AND item_value = 'pending_rectify');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status'), '整改中', 'rectifying', '临床人员填写整改中', 6, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status') AND item_value = 'rectifying');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status'), '已结案', 'closed', '处理完成', 7, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_status') AND item_value = 'closed');

-- 2. 器械使用场所
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
SELECT UUID(), '器械使用场所', 'device_adr_use_place', '医疗器械实际使用场所', 0, 'admin', NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE dict_code = 'device_adr_use_place');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_use_place'), '医疗机构', 'hospital', '医疗机构', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_use_place') AND item_value = 'hospital');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_use_place'), '家庭', 'home', '家庭', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_use_place') AND item_value = 'home');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_use_place'), '其它', 'other', '其它场所', 3, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_use_place') AND item_value = 'other');

-- 3. 操作人类型
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
SELECT UUID(), '操作人类型', 'device_adr_operator_type', '医疗器械操作人类型', 0, 'admin', NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE dict_code = 'device_adr_operator_type');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_operator_type'), '专业人员', 'professional', '医护专业人员', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_operator_type') AND item_value = 'professional');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_operator_type'), '非专业人员', 'non_professional', '非医护专业人员', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_operator_type') AND item_value = 'non_professional');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_operator_type'), '患者', 'patient', '患者本人', 3, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_operator_type') AND item_value = 'patient');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_operator_type'), '其它', 'other', '其它操作人', 4, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_operator_type') AND item_value = 'other');

-- 4. 报告人类型
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
SELECT UUID(), '器械报告人类型', 'device_adr_reporter_type', '医疗器械不良事件报告人类型', 0, 'admin', NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE dict_code = 'device_adr_reporter_type');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_reporter_type'), '医师', 'doctor', '医师', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_reporter_type') AND item_value = 'doctor');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_reporter_type'), '技师', 'technician', '技师', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_reporter_type') AND item_value = 'technician');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_reporter_type'), '护士', 'nurse', '护士', 3, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_reporter_type') AND item_value = 'nurse');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_reporter_type'), '其他', 'other', '其他人员', 4, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_reporter_type') AND item_value = 'other');

-- 5. 评价结果
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
SELECT UUID(), '器械不良事件评价结果', 'device_adr_eval_result', '医疗器械不良事件评价结果', 0, 'admin', NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE dict_code = 'device_adr_eval_result');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_eval_result'), '是', 'yes', '是', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_eval_result') AND item_value = 'yes');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_eval_result'), '否', 'no', '否', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_eval_result') AND item_value = 'no');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_eval_result'), '无法确定', 'unknown', '无法确定', 3, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_eval_result') AND item_value = 'unknown');

-- 6. 流转操作类型
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
SELECT UUID(), '器械不良事件流转操作', 'device_adr_flow_action', '医疗器械不良事件流转操作类型', 0, 'admin', NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE dict_code = 'device_adr_flow_action');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '提交', 'submit', '提交报告', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'submit');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '审核通过', 'audit_pass', '审核通过', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'audit_pass');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '审核退回', 'audit_reject', '审核退回', 3, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'audit_reject');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '重新提交', 'resubmit', '重新提交', 4, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'resubmit');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '直接结案', 'close_direct', '无需整改，直接结案', 5, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'close_direct');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '要求整改', 'require_rectify', '要求整改', 6, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'require_rectify');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '提交整改', 'submit_rectify', '提交整改', 7, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'submit_rectify');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '确认整改通过', 'confirm_approve', '确认整改通过，结案', 8, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'confirm_approve');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '退回整改', 'confirm_reject', '整改不到位，退回重新整改', 9, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'confirm_reject');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action'), '结案', 'close', '结案', 10, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_flow_action') AND item_value = 'close');

-- 7. 整改状态
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
SELECT UUID(), '器械整改状态', 'device_adr_rectify_status', '医疗器械不良事件整改状态', 0, 'admin', NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE dict_code = 'device_adr_rectify_status');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_rectify_status'), '待整改', 'pending', '待整改', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_rectify_status') AND item_value = 'pending');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_rectify_status'), '已提交', 'submitted', '已提交', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_rectify_status') AND item_value = 'submitted');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_rectify_status'), '已通过', 'approved', '已通过', 3, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_rectify_status') AND item_value = 'approved');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_rectify_status'), '已退回', 'rejected', '已退回', 4, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_rectify_status') AND item_value = 'rejected');

-- 8. 确认结果
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
SELECT UUID(), '器械整改确认结果', 'device_adr_confirm_result', '医疗器械不良事件整改确认结果', 0, 'admin', NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE dict_code = 'device_adr_confirm_result');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_confirm_result'), '通过', 'approved', '通过', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_confirm_result') AND item_value = 'approved');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_confirm_result'), '退回', 'rejected', '退回', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_confirm_result') AND item_value = 'rejected');

-- 9. 结案方式
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
SELECT UUID(), '器械不良事件结案方式', 'device_adr_close_type', '医疗器械不良事件结案方式', 0, 'admin', NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict WHERE dict_code = 'device_adr_close_type');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_close_type'), '直接结案', 'direct', '无需整改，直接结案', 1, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_close_type') AND item_value = 'direct');

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_close_type'), '整改结案', 'rectify', '整改通过后结案', 2, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'device_adr_close_type') AND item_value = 'rectify');

-- ========================================
-- 字典汇总
-- ========================================
-- 1. device_adr_status (7项): draft, pending_audit, returned, pending_process, pending_rectify, rectifying, closed
-- 2. device_adr_use_place (3项): hospital, home, other
-- 3. device_adr_operator_type (4项): professional, non_professional, patient, other
-- 4. device_adr_reporter_type (4项): doctor, technician, nurse, other
-- 5. device_adr_eval_result (3项): yes, no, unknown
-- 6. device_adr_flow_action (10项): submit, audit_pass, audit_reject, resubmit, close_direct, require_rectify, submit_rectify, confirm_approve, confirm_reject, close
-- 7. device_adr_rectify_status (4项): pending, submitted, approved, rejected
-- 8. device_adr_confirm_result (2项): approved, rejected
-- 9. device_adr_close_type (2项): direct, rectify
