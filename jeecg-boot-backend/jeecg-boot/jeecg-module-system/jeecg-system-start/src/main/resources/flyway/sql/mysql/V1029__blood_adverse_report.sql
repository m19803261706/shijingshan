-- ============================================
-- 输血使用不良事件报告相关数据表
-- @author TC Agent
-- @since 2025-12-19
-- @see Issue #74, Epic #73
-- ============================================

-- ============================================
-- 1. 输血不良事件报告主表
-- ============================================
CREATE TABLE IF NOT EXISTS `blood_adverse_report` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `report_code` varchar(32) NOT NULL COMMENT '报告编号（自动生成）',

  -- A. 患者资料
  `patient_name` varchar(50) DEFAULT NULL COMMENT '患者姓名',
  `patient_gender` varchar(10) DEFAULT NULL COMMENT '性别(M/F)',
  `patient_age` int DEFAULT NULL COMMENT '年龄',
  `clinical_diagnosis` text COMMENT '临床诊断',
  `medical_record_no` varchar(50) DEFAULT NULL COMMENT '病案号',
  `involved_dept` varchar(100) DEFAULT NULL COMMENT '涉及科室',

  -- B. 不良事件情况
  `event_place` varchar(200) DEFAULT NULL COMMENT '事件发生场所（多选，逗号分隔）',
  `event_place_other` varchar(100) DEFAULT NULL COMMENT '其他场所说明',
  `event_description` text COMMENT '事件描述',

  -- C. 不良事件内容
  `event_name` varchar(50) DEFAULT NULL COMMENT '不良事件名称',
  `event_name_other` varchar(100) DEFAULT NULL COMMENT '其他事件名称说明',
  `transfusion_event` varchar(50) DEFAULT NULL COMMENT '输血事件类型',
  `transfusion_event_other` varchar(100) DEFAULT NULL COMMENT '其他输血事件说明',
  `involved_staff` varchar(200) DEFAULT NULL COMMENT '涉及人员',

  -- D. 不良事件等级
  `severity_level` varchar(10) DEFAULT NULL COMMENT '严重程度(A/B/C/D)',
  `event_level` varchar(10) DEFAULT NULL COMMENT '事件等级(I/II/III/IV)',

  -- E. 事件处理与分析
  `handling_measures` text COMMENT '事件处理措施',
  `possible_causes` text COMMENT '导致事件的可能原因',

  -- F. 改进措施
  `improvement_measures` text COMMENT '改进措施',

  -- G. 选填项目
  `reporter_type` varchar(100) DEFAULT NULL COMMENT '报告人类型（多选，逗号分隔）',
  `party_type` varchar(100) DEFAULT NULL COMMENT '当事人类别（多选，逗号分隔）',
  `professional_title` varchar(20) DEFAULT NULL COMMENT '职称',
  `reporter_signature` varchar(50) DEFAULT NULL COMMENT '报告人签名',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '科室名称',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT 'Email',
  `report_date` date DEFAULT NULL COMMENT '报告日期',

  -- 流程状态
  `status` varchar(20) DEFAULT 'draft' COMMENT '状态(draft/pending_audit/returned/pending_process/pending_rectify/rectifying/closed)',
  `close_type` varchar(20) DEFAULT NULL COMMENT '结案方式(direct/rectify)',
  `close_user_id` varchar(36) DEFAULT NULL COMMENT '结案人ID',
  `close_user_name` varchar(50) DEFAULT NULL COMMENT '结案人姓名',
  `close_time` datetime DEFAULT NULL COMMENT '结案时间',
  `close_comment` text COMMENT '结案意见',

  -- 审核信息
  `audit_user_id` varchar(36) DEFAULT NULL COMMENT '审核人ID',
  `audit_user_name` varchar(50) DEFAULT NULL COMMENT '审核人姓名',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_comment` text COMMENT '审核意见',

  -- JeecgBoot标准字段
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `del_flag` int DEFAULT 0 COMMENT '删除标志(0-正常,1-已删除)',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_report_code` (`report_code`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_patient_name` (`patient_name`),
  KEY `idx_event_name` (`event_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='输血不良事件报告表';


-- ============================================
-- 2. 输血不良事件流转记录表
-- ============================================
CREATE TABLE IF NOT EXISTS `blood_adverse_flow` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `report_id` varchar(36) NOT NULL COMMENT '报告ID',
  `action` varchar(20) NOT NULL COMMENT '操作类型(submit/audit_pass/audit_reject/close_direct/require_rectify/submit_rectify/confirm_approve/confirm_reject)',
  `from_status` varchar(20) DEFAULT NULL COMMENT '原状态',
  `to_status` varchar(20) DEFAULT NULL COMMENT '新状态',
  `operator_id` varchar(36) DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `comment` text COMMENT '操作意见',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',

  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`),
  KEY `idx_action` (`action`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='输血不良事件流转记录表';


-- ============================================
-- 3. 输血不良事件整改记录表
-- ============================================
CREATE TABLE IF NOT EXISTS `blood_adverse_rectify` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `report_id` varchar(36) NOT NULL COMMENT '报告ID',
  `round` int DEFAULT 1 COMMENT '整改轮次',

  -- 整改要求（输血科下发）
  `requirement` text COMMENT '整改要求',
  `deadline` date DEFAULT NULL COMMENT '整改期限',
  `require_user_id` varchar(36) DEFAULT NULL COMMENT '下发人ID',
  `require_user_name` varchar(50) DEFAULT NULL COMMENT '下发人姓名',
  `require_time` datetime DEFAULT NULL COMMENT '下发时间',

  -- 整改提交（临床提交）
  `measures` text COMMENT '整改措施',
  `completion` text COMMENT '完成情况',
  `submit_user_id` varchar(36) DEFAULT NULL COMMENT '提交人ID',
  `submit_user_name` varchar(50) DEFAULT NULL COMMENT '提交人姓名',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',

  -- 确认结果（输血科确认）
  `status` varchar(20) DEFAULT 'pending' COMMENT '状态(pending/submitted/approved/rejected)',
  `confirm_user_id` varchar(36) DEFAULT NULL COMMENT '确认人ID',
  `confirm_user_name` varchar(50) DEFAULT NULL COMMENT '确认人姓名',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `confirm_comment` text COMMENT '确认意见',

  -- 标准字段
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',

  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`),
  KEY `idx_round` (`round`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='输血不良事件整改记录表';
