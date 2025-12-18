-- ========================================
-- 不良事件上报系统 - 事件分类表
-- 版本: V1004
-- 描述: 创建事件分类表 event_category，支持多级分类
-- 作者: TC Agent
-- 创建时间: 2025-01-18
-- ========================================

-- 创建事件分类表
CREATE TABLE IF NOT EXISTS `event_category` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `parent_id` varchar(36) DEFAULT NULL COMMENT '父级ID（NULL表示顶级分类）',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `code` varchar(50) DEFAULT NULL COMMENT '分类编码（便于程序识别）',
  `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
  `level` int NOT NULL DEFAULT 1 COMMENT '层级（1-一级分类 2-二级分类 3-三级分类）',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序号（同级内排序）',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（1-启用 0-禁用）',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`) COMMENT '父级ID索引',
  KEY `idx_code` (`code`) COMMENT '分类编码索引',
  KEY `idx_status` (`status`) COMMENT '状态索引',
  KEY `idx_sort` (`level`, `sort_order`) COMMENT '排序索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='事件分类表 - 支持多级分类，用于不良事件的分类管理';

-- 添加表注释
ALTER TABLE `event_category` COMMENT = '事件分类表 - 支持多级分类管理，医疗不良事件按类型分类';

-- ========================================
-- 初始化一级分类数据
-- ========================================

INSERT INTO `event_category` (`id`, `parent_id`, `name`, `code`, `description`, `level`, `sort_order`, `status`, `create_by`, `create_time`) VALUES
('cat_drug', NULL, '药物不良事件', 'DRUG', '与药物使用相关的不良事件，包括用药错误、药物不良反应等', 1, 1, 1, 'admin', NOW()),
('cat_nursing', NULL, '护理不良事件', 'NURSING', '护理操作过程中发生的不良事件，包括跌倒、压疮、管路滑脱等', 1, 2, 1, 'admin', NOW()),
('cat_medical', NULL, '医疗不良事件', 'MEDICAL', '诊疗过程中发生的不良事件，包括手术并发症、诊断延误等', 1, 3, 1, 'admin', NOW()),
('cat_infection', NULL, '感染不良事件', 'INFECTION', '医院感染相关不良事件，包括院内感染、手术部位感染等', 1, 4, 1, 'admin', NOW()),
('cat_blood', NULL, '输血不良事件', 'BLOOD', '输血相关不良事件，包括输血反应、血型不符等', 1, 5, 1, 'admin', NOW()),
('cat_equipment', NULL, '设备器械不良事件', 'EQUIPMENT', '医疗设备器械相关不良事件，包括设备故障、器械损伤等', 1, 6, 1, 'admin', NOW()),
('cat_other', NULL, '其他不良事件', 'OTHER', '无法归入以上分类的其他不良事件', 1, 99, 1, 'admin', NOW());

-- ========================================
-- 初始化二级分类数据
-- ========================================

-- 药物不良事件 - 二级分类
INSERT INTO `event_category` (`id`, `parent_id`, `name`, `code`, `description`, `level`, `sort_order`, `status`, `create_by`, `create_time`) VALUES
('cat_drug_01', 'cat_drug', '用药错误', 'DRUG_ERROR', '处方、调剂、给药环节的错误', 2, 1, 1, 'admin', NOW()),
('cat_drug_02', 'cat_drug', '药物不良反应', 'DRUG_ADR', '药物引起的不良反应', 2, 2, 1, 'admin', NOW()),
('cat_drug_03', 'cat_drug', '药物相互作用', 'DRUG_INTERACTION', '多种药物之间的不良相互作用', 2, 3, 1, 'admin', NOW());

-- 护理不良事件 - 二级分类
INSERT INTO `event_category` (`id`, `parent_id`, `name`, `code`, `description`, `level`, `sort_order`, `status`, `create_by`, `create_time`) VALUES
('cat_nursing_01', 'cat_nursing', '跌倒/坠床', 'NURSING_FALL', '患者跌倒或坠床事件', 2, 1, 1, 'admin', NOW()),
('cat_nursing_02', 'cat_nursing', '压力性损伤', 'NURSING_PRESSURE', '压疮、压力性溃疡', 2, 2, 1, 'admin', NOW()),
('cat_nursing_03', 'cat_nursing', '管路滑脱', 'NURSING_TUBE', '各类导管意外滑脱', 2, 3, 1, 'admin', NOW()),
('cat_nursing_04', 'cat_nursing', '意外伤害', 'NURSING_INJURY', '烫伤、划伤等意外伤害', 2, 4, 1, 'admin', NOW());

-- 医疗不良事件 - 二级分类
INSERT INTO `event_category` (`id`, `parent_id`, `name`, `code`, `description`, `level`, `sort_order`, `status`, `create_by`, `create_time`) VALUES
('cat_medical_01', 'cat_medical', '手术并发症', 'MEDICAL_SURGERY', '手术过程中或术后的并发症', 2, 1, 1, 'admin', NOW()),
('cat_medical_02', 'cat_medical', '诊断延误', 'MEDICAL_DELAY', '诊断延误导致的问题', 2, 2, 1, 'admin', NOW()),
('cat_medical_03', 'cat_medical', '漏诊/误诊', 'MEDICAL_MISDIAG', '漏诊或误诊事件', 2, 3, 1, 'admin', NOW());

-- 感染不良事件 - 二级分类
INSERT INTO `event_category` (`id`, `parent_id`, `name`, `code`, `description`, `level`, `sort_order`, `status`, `create_by`, `create_time`) VALUES
('cat_infection_01', 'cat_infection', '院内感染', 'INFECTION_NOSOCOMIAL', '医院获得性感染', 2, 1, 1, 'admin', NOW()),
('cat_infection_02', 'cat_infection', '手术部位感染', 'INFECTION_SSI', '手术切口或深部感染', 2, 2, 1, 'admin', NOW()),
('cat_infection_03', 'cat_infection', '导管相关感染', 'INFECTION_CATHETER', '中心静脉导管、尿管等相关感染', 2, 3, 1, 'admin', NOW());

-- 输血不良事件 - 二级分类
INSERT INTO `event_category` (`id`, `parent_id`, `name`, `code`, `description`, `level`, `sort_order`, `status`, `create_by`, `create_time`) VALUES
('cat_blood_01', 'cat_blood', '输血反应', 'BLOOD_REACTION', '输血过程中的不良反应', 2, 1, 1, 'admin', NOW()),
('cat_blood_02', 'cat_blood', '血型错误', 'BLOOD_MISMATCH', '血型不符或交叉配血错误', 2, 2, 1, 'admin', NOW());

-- 设备器械不良事件 - 二级分类
INSERT INTO `event_category` (`id`, `parent_id`, `name`, `code`, `description`, `level`, `sort_order`, `status`, `create_by`, `create_time`) VALUES
('cat_equipment_01', 'cat_equipment', '设备故障', 'EQUIPMENT_FAILURE', '医疗设备故障导致的问题', 2, 1, 1, 'admin', NOW()),
('cat_equipment_02', 'cat_equipment', '器械损伤', 'EQUIPMENT_INJURY', '医疗器械使用不当造成的损伤', 2, 2, 1, 'admin', NOW());
