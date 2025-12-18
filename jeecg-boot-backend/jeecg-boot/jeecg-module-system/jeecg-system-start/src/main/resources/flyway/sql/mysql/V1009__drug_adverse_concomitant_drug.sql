-- ========================================
-- 药品不良反应上报系统 - 并用药品子表
-- 版本: V1009
-- 描述: 创建并用药品子表 drug_adverse_concomitant_drug
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- 参考: 国家药品不良反应/事件报告表 - 并用药品栏
-- ========================================

-- 创建并用药品子表（一对多关系，一个报告可有多个并用药品）
CREATE TABLE IF NOT EXISTS `drug_adverse_concomitant_drug` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `report_id` varchar(36) NOT NULL COMMENT '关联报告ID（关联 drug_adverse_report.id）',
  `sort_order` int DEFAULT 0 COMMENT '排序号',

  -- ========== 药品基本信息 ==========
  `approval_no` varchar(100) DEFAULT NULL COMMENT '批准文号',
  `trade_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `generic_name` varchar(200) NOT NULL COMMENT '通用名称（含剂型）',
  `manufacturer` varchar(200) DEFAULT NULL COMMENT '生产厂家',
  `batch_no` varchar(100) DEFAULT NULL COMMENT '生产批号',

  -- ========== 用法用量 ==========
  `dose_per_time` varchar(50) DEFAULT NULL COMMENT '单次剂量（数值部分）',
  `dose_unit` varchar(20) DEFAULT NULL COMMENT '剂量单位：g-克, mg-毫克, ug-微克, ml-毫升, IU-国际单位, other-其他',
  `route` varchar(50) DEFAULT NULL COMMENT '给药途径：oral-口服, iv-静脉注射, iv_drip-静脉滴注, im-肌肉注射, sc-皮下注射, topical-外用, other-其他',
  `frequency` varchar(50) DEFAULT NULL COMMENT '日次数（如：每日3次、每8小时一次等）',

  -- ========== 用药时间 ==========
  `start_date` date DEFAULT NULL COMMENT '用药开始时间',
  `end_date` date DEFAULT NULL COMMENT '用药截止时间',

  -- ========== 用药原因 ==========
  `indication` varchar(200) DEFAULT NULL COMMENT '用药原因',

  -- ========== 系统字段 ==========
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',

  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`) COMMENT '报告ID索引',
  KEY `idx_generic_name` (`generic_name`) COMMENT '通用名称索引',
  KEY `idx_manufacturer` (`manufacturer`) COMMENT '生产厂家索引',
  CONSTRAINT `fk_concomitant_drug_report` FOREIGN KEY (`report_id`) REFERENCES `drug_adverse_report` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='药品不良反应报告-并用药品子表';

-- 添加表注释
ALTER TABLE `drug_adverse_concomitant_drug` COMMENT = '药品不良反应报告-并用药品子表 - 存储报告中涉及的并用药品信息，用于评估药物相互作用';
