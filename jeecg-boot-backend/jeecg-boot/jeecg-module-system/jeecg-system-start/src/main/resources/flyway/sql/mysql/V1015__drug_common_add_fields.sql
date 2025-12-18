-- ========================================
-- 药品常用配置 - 添加缺失字段
-- 版本: V1015
-- 描述: 为常用怀疑药品和常用并用药品表添加剂型、规格、用法用量字段
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- ========================================

-- ========================================
-- 一、为 drug_common_suspect 添加字段
-- ========================================

-- 添加剂型字段
ALTER TABLE `drug_common_suspect`
ADD COLUMN `dosage_form` varchar(50) DEFAULT NULL COMMENT '剂型（如：片剂、胶囊剂、注射剂）' AFTER `route`;

-- 添加规格字段
ALTER TABLE `drug_common_suspect`
ADD COLUMN `specification` varchar(100) DEFAULT NULL COMMENT '规格（如：0.5g*24粒）' AFTER `dosage_form`;

-- 添加用法用量字段
ALTER TABLE `drug_common_suspect`
ADD COLUMN `dosage` varchar(200) DEFAULT NULL COMMENT '用法用量（如：一次0.5g，一日3次）' AFTER `specification`;

-- ========================================
-- 二、为 drug_common_concomitant 添加字段
-- ========================================

-- 添加剂型字段
ALTER TABLE `drug_common_concomitant`
ADD COLUMN `dosage_form` varchar(50) DEFAULT NULL COMMENT '剂型（如：片剂、胶囊剂、注射剂）' AFTER `route`;

-- 添加规格字段
ALTER TABLE `drug_common_concomitant`
ADD COLUMN `specification` varchar(100) DEFAULT NULL COMMENT '规格（如：0.5g*24粒）' AFTER `dosage_form`;

-- 添加用法用量字段
ALTER TABLE `drug_common_concomitant`
ADD COLUMN `dosage` varchar(200) DEFAULT NULL COMMENT '用法用量（如：一次0.5g，一日3次）' AFTER `specification`;

-- ========================================
-- 字段说明
-- ========================================
-- dosage_form: 剂型，用于描述药品的剂型形式
-- specification: 规格，用于描述药品的规格信息
-- dosage: 用法用量，用于描述药品的使用方法和剂量
