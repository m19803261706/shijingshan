-- ========================================
-- 不良事件上报系统 - 整改记录表
-- 版本: V1003
-- 描述: 创建整改记录表 adverse_event_rectify
-- 作者: TC Agent
-- 创建时间: 2025-01-18
-- ========================================

-- 创建整改记录表
CREATE TABLE IF NOT EXISTS `adverse_event_rectify` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `event_id` varchar(36) NOT NULL COMMENT '关联事件ID（关联 adverse_event 表）',
  `rectify_no` int NOT NULL DEFAULT 1 COMMENT '整改轮次（第几次整改）',
  `requirement` text COMMENT '整改要求（职能部门提出的整改要求）',
  `requirement_by` varchar(36) DEFAULT NULL COMMENT '要求提出人ID',
  `requirement_time` datetime DEFAULT NULL COMMENT '要求提出时间',
  `deadline` date DEFAULT NULL COMMENT '整改期限',
  `measures` text COMMENT '整改措施（科室填写的整改措施）',
  `result` text COMMENT '整改结果（整改后的效果描述）',
  `attachments` text COMMENT '整改附件（JSON格式，整改前后对比照片等）',
  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '整改状态（pending:待整改 submitted:已提交 approved:已通过 rejected:未通过）',
  `submit_time` datetime DEFAULT NULL COMMENT '整改提交时间',
  `reviewer_id` varchar(36) DEFAULT NULL COMMENT '审核人ID（职能部门审核人）',
  `reviewer_name` varchar(50) DEFAULT NULL COMMENT '审核人姓名',
  `review_comment` text COMMENT '审核意见',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `review_result` varchar(20) DEFAULT NULL COMMENT '审核结果（approved:通过 rejected:不通过）',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_event_id` (`event_id`) COMMENT '事件ID索引',
  KEY `idx_status` (`status`) COMMENT '整改状态索引',
  KEY `idx_deadline` (`deadline`) COMMENT '整改期限索引',
  KEY `idx_reviewer` (`reviewer_id`) COMMENT '审核人索引',
  UNIQUE KEY `uk_event_rectify_no` (`event_id`, `rectify_no`) COMMENT '事件-轮次唯一索引',
  CONSTRAINT `fk_rectify_event` FOREIGN KEY (`event_id`) REFERENCES `adverse_event` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='整改记录表 - 记录不良事件的整改要求、措施和审核结果';

-- 添加表注释
ALTER TABLE `adverse_event_rectify` COMMENT = '整改记录表 - 支持多轮整改，记录每轮整改的要求、措施、结果和审核情况';
