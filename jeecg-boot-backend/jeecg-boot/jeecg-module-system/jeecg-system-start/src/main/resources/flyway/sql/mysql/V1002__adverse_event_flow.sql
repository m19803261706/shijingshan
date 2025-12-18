-- ========================================
-- 不良事件上报系统 - 事件流转记录表
-- 版本: V1002
-- 描述: 创建事件流转记录表 adverse_event_flow
-- 作者: TC Agent
-- 创建时间: 2025-01-18
-- ========================================

-- 创建事件流转记录表
CREATE TABLE IF NOT EXISTS `adverse_event_flow` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `event_id` varchar(36) NOT NULL COMMENT '关联事件ID（关联 adverse_event 表）',
  `from_status` varchar(20) DEFAULT NULL COMMENT '原状态（流转前的状态）',
  `to_status` varchar(20) NOT NULL COMMENT '目标状态（流转后的状态）',
  `action` varchar(50) NOT NULL COMMENT '操作类型（submit:提交 approve:审核通过 reject:退回 process:处理 require_rectify:要求整改 submit_rectify:提交整改 confirm_rectify:确认整改 close:结案）',
  `operator_id` varchar(36) NOT NULL COMMENT '操作人ID（关联 sys_user 表）',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名（冗余字段）',
  `operator_dept_id` varchar(36) DEFAULT NULL COMMENT '操作人部门ID（关联 sys_depart 表）',
  `operator_dept_name` varchar(100) DEFAULT NULL COMMENT '操作人部门名称（冗余字段）',
  `comment` text COMMENT '审批意见/操作备注',
  `attachments` text COMMENT '附件信息（JSON格式，如退回说明附件）',
  `next_handler_id` varchar(36) DEFAULT NULL COMMENT '下一处理人ID',
  `next_handler_dept` varchar(36) DEFAULT NULL COMMENT '下一处理部门ID',
  `deadline` datetime DEFAULT NULL COMMENT '处理期限（如整改期限）',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_event_id` (`event_id`) COMMENT '事件ID索引',
  KEY `idx_operator` (`operator_id`) COMMENT '操作人索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引',
  KEY `idx_action` (`action`) COMMENT '操作类型索引',
  CONSTRAINT `fk_flow_event` FOREIGN KEY (`event_id`) REFERENCES `adverse_event` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='事件流转记录表 - 记录不良事件的审批流转历史';

-- 添加表注释
ALTER TABLE `adverse_event_flow` COMMENT = '事件流转记录表 - 记录不良事件从创建到结案的每一步操作，支持完整的流程追溯';
