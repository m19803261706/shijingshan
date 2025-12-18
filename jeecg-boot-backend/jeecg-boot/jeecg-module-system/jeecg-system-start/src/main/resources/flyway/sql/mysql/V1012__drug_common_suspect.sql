-- ========================================
-- 药品不良反应上报系统 - 常用怀疑药品配置表
-- 版本: V1012
-- 描述: 创建常用怀疑药品配置表 drug_common_suspect
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- 用途: 存储常用的怀疑药品信息，支持快速选择和使用统计
-- Issue: #33
-- Epic: #32
-- ========================================

-- 创建常用怀疑药品配置表
CREATE TABLE IF NOT EXISTS `drug_common_suspect` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',

  -- ========== 药品基本信息 ==========
  `approval_no` varchar(100) DEFAULT NULL COMMENT '批准文号',
  `trade_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `generic_name` varchar(200) NOT NULL COMMENT '通用名称（含剂型）',
  `manufacturer` varchar(200) DEFAULT NULL COMMENT '生产厂家',
  `batch_no` varchar(100) DEFAULT NULL COMMENT '生产批号',

  -- ========== 用法用量 ==========
  `dose_per_time` varchar(50) DEFAULT NULL COMMENT '单次剂量',
  `dose_unit` varchar(20) DEFAULT NULL COMMENT '剂量单位：g-克, mg-毫克, ug-微克, ml-毫升, IU-国际单位',
  `route` varchar(50) DEFAULT NULL COMMENT '给药途径：oral-口服, iv-静脉注射, iv_drip-静脉滴注, im-肌肉注射, sc-皮下注射, topical-外用',
  `frequency` varchar(50) DEFAULT NULL COMMENT '用药频次（如：每日3次、每8小时一次等）',
  `indication` varchar(200) DEFAULT NULL COMMENT '用药原因',

  -- ========== 使用统计 ==========
  `use_count` int DEFAULT 0 COMMENT '使用次数（用于排序推荐，常用优先）',
  `last_used_time` datetime DEFAULT NULL COMMENT '最后使用时间',

  -- ========== 系统字段 ==========
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',

  PRIMARY KEY (`id`),
  KEY `idx_generic_name` (`generic_name`) COMMENT '通用名称索引（用于搜索）',
  KEY `idx_manufacturer` (`manufacturer`) COMMENT '生产厂家索引（用于搜索）',
  KEY `idx_use_count` (`use_count` DESC) COMMENT '使用次数索引（用于排序推荐）',
  KEY `idx_trade_name` (`trade_name`) COMMENT '商品名称索引（用于搜索）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='常用怀疑药品配置表';

-- 添加表注释
ALTER TABLE `drug_common_suspect` COMMENT = '常用怀疑药品配置表 - 存储常用的怀疑药品信息，支持快速选择和使用统计';
