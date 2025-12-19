-- ========================================
-- 医疗器械不良事件上报系统 - 流转记录表
-- 版本: V1025
-- 描述: 创建医疗器械不良事件流转记录表 device_adverse_flow
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- 参考: drug_adverse_flow 表结构（1:1 复刻）
-- GitHub Issue: #59
-- ========================================

-- 创建医疗器械不良事件流转记录表
CREATE TABLE IF NOT EXISTS `device_adverse_flow` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `report_id` varchar(36) NOT NULL COMMENT '关联报告ID（关联 device_adverse_report.id）',
  `action` varchar(20) NOT NULL COMMENT '操作类型：submit-提交, audit_pass-审核通过, audit_reject-审核退回, resubmit-重新提交, close_direct-直接结案, require_rectify-要求整改, submit_rectify-提交整改, confirm_approve-确认整改通过, confirm_reject-退回整改, close-结案',
  `from_status` varchar(20) DEFAULT NULL COMMENT '操作前状态',
  `to_status` varchar(20) NOT NULL COMMENT '操作后状态',
  `operator_id` varchar(36) NOT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) NOT NULL COMMENT '操作人姓名',
  `comment` text COMMENT '操作备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`) COMMENT '报告ID索引',
  KEY `idx_action` (`action`) COMMENT '操作类型索引',
  KEY `idx_operator_id` (`operator_id`) COMMENT '操作人索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引',
  CONSTRAINT `fk_device_flow_report` FOREIGN KEY (`report_id`) REFERENCES `device_adverse_report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='医疗器械不良事件流转记录表';

-- 添加表注释
ALTER TABLE `device_adverse_flow` COMMENT = '医疗器械不良事件流转记录表 - 记录报告在各状态间的流转历史，用于审计追踪';
