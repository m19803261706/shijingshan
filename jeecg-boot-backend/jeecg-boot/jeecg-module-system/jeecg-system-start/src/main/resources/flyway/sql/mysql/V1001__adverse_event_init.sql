-- ========================================
-- 不良事件上报系统 - 初始化脚本
-- 版本: V1001
-- 描述: 创建不良事件主表 adverse_event
-- 作者: TC Agent
-- 创建时间: 2025-01-18
-- ========================================

-- 创建不良事件主表
CREATE TABLE IF NOT EXISTS `adverse_event` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `event_code` varchar(50) NOT NULL COMMENT '事件编号（自动生成，格式：AE+年月日+4位序号）',
  `title` varchar(200) NOT NULL COMMENT '事件标题',
  `category_id` varchar(36) DEFAULT NULL COMMENT '事件分类ID（关联 event_category 表）',
  `level` varchar(10) DEFAULT NULL COMMENT '事件级别（I级/II级/III级/IV级，对应字典 event_level）',
  `occur_time` datetime DEFAULT NULL COMMENT '事件发生时间',
  `occur_place` varchar(200) DEFAULT NULL COMMENT '事件发生地点',
  `department_id` varchar(36) DEFAULT NULL COMMENT '上报科室ID（关联 sys_depart 表）',
  `reporter_id` varchar(36) DEFAULT NULL COMMENT '上报人ID（关联 sys_user 表）',
  `reporter_name` varchar(50) DEFAULT NULL COMMENT '上报人姓名（冗余字段）',
  `patient_name` varchar(50) DEFAULT NULL COMMENT '患者姓名',
  `patient_gender` varchar(10) DEFAULT NULL COMMENT '患者性别（1:男 2:女）',
  `patient_age` int DEFAULT NULL COMMENT '患者年龄',
  `patient_bed_no` varchar(20) DEFAULT NULL COMMENT '患者床号',
  `patient_admission_no` varchar(50) DEFAULT NULL COMMENT '患者住院号',
  `description` text COMMENT '事件经过详细描述',
  `cause_analysis` text COMMENT '初步原因分析',
  `immediate_measures` text COMMENT '已采取的紧急措施',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '事件状态（draft:草稿 pending_review:待审核 returned:已退回 pending_process:待处理 pending_rectify:待整改 rectifying:整改中 closed:已结案）',
  `attachments` text COMMENT '附件信息（JSON格式存储）',
  `current_handler_id` varchar(36) DEFAULT NULL COMMENT '当前处理人ID',
  `current_handler_dept` varchar(36) DEFAULT NULL COMMENT '当前处理科室ID',
  `submit_time` datetime DEFAULT NULL COMMENT '首次提交时间',
  `close_time` datetime DEFAULT NULL COMMENT '结案时间',
  `is_anonymous` tinyint(1) DEFAULT 0 COMMENT '是否匿名上报（0:否 1:是）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门编码',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID（多租户支持）',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标记（0:正常 1:已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_event_code` (`event_code`) COMMENT '事件编号唯一索引',
  KEY `idx_status` (`status`) COMMENT '状态索引（用于列表筛选）',
  KEY `idx_department` (`department_id`) COMMENT '科室索引',
  KEY `idx_reporter` (`reporter_id`) COMMENT '上报人索引',
  KEY `idx_category` (`category_id`) COMMENT '分类索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引（用于时间范围查询）',
  KEY `idx_occur_time` (`occur_time`) COMMENT '发生时间索引',
  KEY `idx_current_handler` (`current_handler_id`) COMMENT '当前处理人索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='不良事件主表';

-- 添加表注释
ALTER TABLE `adverse_event` COMMENT = '不良事件主表 - 存储不良事件的基本信息、患者信息和处理状态';
