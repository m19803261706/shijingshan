-- ========================================
-- 药品不良反应上报系统 - 添加事件过程描述结构化字段
-- 版本: V1011
-- 描述: 为 drug_adverse_report 表添加1:1复刻国标表单的结构化字段
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- 参考: 国家药品不良反应/事件报告表 - 事件过程描述部分
-- ========================================

-- 添加事件过程描述结构化字段
ALTER TABLE `drug_adverse_report`
  -- ========== 第一行：患者因____病于____年__月__日__时 ==========
  ADD COLUMN `patient_reason` varchar(200) DEFAULT NULL COMMENT '患者病因/原因' AFTER `reaction_description`,
  ADD COLUMN `onset_year` int DEFAULT NULL COMMENT '发病年份' AFTER `patient_reason`,
  ADD COLUMN `onset_month` int DEFAULT NULL COMMENT '发病月份' AFTER `onset_year`,
  ADD COLUMN `onset_day` int DEFAULT NULL COMMENT '发病日期' AFTER `onset_month`,
  ADD COLUMN `onset_hour` int DEFAULT NULL COMMENT '发病小时' AFTER `onset_day`,
  ADD COLUMN `route_type` varchar(100) DEFAULT NULL COMMENT '给药途径（多选JSON）：iv_drip-静滴, im-肌注, oral-口服, topical-外用' AFTER `onset_hour`,

  -- ========== 第二行：____药____（g□、mg□...）+____溶媒____ml ==========
  ADD COLUMN `drug_name_desc` varchar(200) DEFAULT NULL COMMENT '药物名称（事件描述用）' AFTER `route_type`,
  ADD COLUMN `dose_amount` decimal(10,2) DEFAULT NULL COMMENT '剂量数值' AFTER `drug_name_desc`,
  ADD COLUMN `dose_type` varchar(100) DEFAULT NULL COMMENT '剂量单位（多选JSON）：g, mg, ug, ml, IU' AFTER `dose_amount`,
  ADD COLUMN `solvent_name` varchar(100) DEFAULT NULL COMMENT '溶媒名称' AFTER `dose_type`,
  ADD COLUMN `solvent_volume` decimal(10,2) DEFAULT NULL COMMENT '溶媒量(ml)' AFTER `solvent_name`,

  -- ========== 第三行：约____（天、小时、分钟）输入____ml后，出现____等症状 ==========
  ADD COLUMN `infusion_duration` int DEFAULT NULL COMMENT '输液时长数值' AFTER `solvent_volume`,
  ADD COLUMN `infusion_time_unit` varchar(100) DEFAULT NULL COMMENT '输液时长单位（多选JSON）：day-天, hour-小时, minute-分钟' AFTER `infusion_duration`,
  ADD COLUMN `infused_volume` decimal(10,2) DEFAULT NULL COMMENT '已输入量(ml)' AFTER `infusion_time_unit`,
  ADD COLUMN `symptoms` varchar(500) DEFAULT NULL COMMENT '出现的症状' AFTER `infused_volume`,

  -- ========== 第四行：经____等治疗后 ==========
  ADD COLUMN `treatment_methods` varchar(500) DEFAULT NULL COMMENT '治疗方法' AFTER `symptoms`,

  -- ========== 第五行：约____（天、小时、分钟）后，症状（...） ==========
  ADD COLUMN `recovery_duration` int DEFAULT NULL COMMENT '恢复时长数值' AFTER `treatment_methods`,
  ADD COLUMN `recovery_time_unit` varchar(100) DEFAULT NULL COMMENT '恢复时长单位（多选JSON）：day-天, hour-小时, minute-分钟' AFTER `recovery_duration`,
  ADD COLUMN `symptom_outcome` varchar(100) DEFAULT NULL COMMENT '症状转归（多选JSON）：improved-逐渐有所好转, no_change-无明显好转, worsened-进一步加重' AFTER `recovery_time_unit`,

  -- ========== 第六行：此后（未再继续使用该药□、继续使用该药□） ==========
  ADD COLUMN `subsequent_usage` varchar(100) DEFAULT NULL COMMENT '后续用药（多选JSON）：discontinued-未再继续使用该药, continued-继续使用该药' AFTER `symptom_outcome`;

-- 添加字段索引（用于查询优化）
ALTER TABLE `drug_adverse_report`
  ADD INDEX `idx_symptoms` (`symptoms`(100)) COMMENT '症状索引';
