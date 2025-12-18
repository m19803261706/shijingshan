-- ========================================
-- 药品不良反应上报系统 - 主表
-- 版本: V1007
-- 描述: 创建药品不良反应报告主表 drug_adverse_report
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- 参考: 国家药品不良反应/事件报告表
-- ========================================

-- 创建药品不良反应报告主表
CREATE TABLE IF NOT EXISTS `drug_adverse_report` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `report_code` varchar(50) NOT NULL COMMENT '报告编号（自动生成：ADR+年月日+4位序号）',

  -- ========== 报告基本信息 ==========
  `report_type` varchar(20) NOT NULL DEFAULT 'first' COMMENT '报告类型：first-首次报告, follow_up-跟踪报告',
  `severity_type` varchar(20) NOT NULL DEFAULT 'general' COMMENT '严重程度：new-新的, serious-严重, general-一般',
  `unit_category` varchar(20) NOT NULL DEFAULT 'hospital' COMMENT '报告单位类别：hospital-医疗机构, business-经营企业, manufacture-生产企业, personal-个人, other-其他',

  -- ========== 患者基本信息 ==========
  `patient_name` varchar(50) DEFAULT NULL COMMENT '患者姓名',
  `patient_gender` varchar(10) DEFAULT NULL COMMENT '患者性别：M-男, F-女',
  `patient_birth_date` date DEFAULT NULL COMMENT '出生日期',
  `patient_nationality` varchar(20) DEFAULT NULL COMMENT '民族',
  `patient_weight` decimal(5,2) DEFAULT NULL COMMENT '体重(kg)',
  `patient_phone` varchar(20) DEFAULT NULL COMMENT '联系方式',
  `original_disease` text COMMENT '原患疾病',
  `hospital_name` varchar(100) DEFAULT NULL COMMENT '医院名称',
  `medical_record_no` varchar(50) DEFAULT NULL COMMENT '病历号/门诊号',

  -- ========== 既往/家族不良反应史 ==========
  `history_adr` varchar(20) DEFAULT NULL COMMENT '既往药品不良反应：yes-有, no-无, unknown-不详',
  `history_adr_detail` text COMMENT '既往不良反应详情',
  `family_adr` varchar(20) DEFAULT NULL COMMENT '家族药品不良反应：yes-有, no-无, unknown-不详',
  `family_adr_detail` text COMMENT '家族不良反应详情',

  -- ========== 相关重要信息（病史）==========
  `has_smoking` tinyint(1) DEFAULT 0 COMMENT '吸烟史（0-否 1-是）',
  `has_drinking` tinyint(1) DEFAULT 0 COMMENT '饮酒史（0-否 1-是）',
  `is_pregnant` tinyint(1) DEFAULT 0 COMMENT '妊娠期（0-否 1-是）',
  `has_liver_disease` tinyint(1) DEFAULT 0 COMMENT '肝病史（0-否 1-是）',
  `has_kidney_disease` tinyint(1) DEFAULT 0 COMMENT '肾病史（0-否 1-是）',
  `has_allergy` tinyint(1) DEFAULT 0 COMMENT '过敏史（0-否 1-是）',
  `other_history` text COMMENT '其他病史',

  -- ========== 不良反应/事件信息 ==========
  `reaction_name` varchar(200) DEFAULT NULL COMMENT '不良反应/事件名称',
  `reaction_time` datetime DEFAULT NULL COMMENT '不良反应/事件发生时间',
  `reaction_description` text COMMENT '不良反应/事件过程描述（包括症状、体征、临床检验等）及处理情况',

  -- ========== 不良反应/事件结果 ==========
  `reaction_result` varchar(20) DEFAULT NULL COMMENT '结果：cured-痊愈, improved-好转, not_improved-未好转, unknown-不详, sequela-有后遗症, death-死亡',
  `sequela_description` text COMMENT '后遗症表现',
  `death_time` datetime DEFAULT NULL COMMENT '死亡时间',
  `death_cause` text COMMENT '直接死因',

  -- ========== 停药或减量后情况 ==========
  `stop_drug_reaction` varchar(20) DEFAULT NULL COMMENT '停药或减量后反应是否消失或减轻：yes-是, no-否, unknown-不明, not_stop-未停药或未减量',
  `rechallenge_reaction` varchar(20) DEFAULT NULL COMMENT '再次使用后是否再次出现：yes-是, no-否, unknown-不明, not_use-未再使用',

  -- ========== 对原患疾病的影响 ==========
  `disease_impact` varchar(20) DEFAULT NULL COMMENT '影响：none-不明显, prolonged-病程延长, aggravated-病情加重, sequela-导致后遗症, death-导致死亡',

  -- ========== 关联性评价 ==========
  `reporter_evaluation` varchar(20) DEFAULT NULL COMMENT '报告人评价：definite-肯定, probable-很可能, possible-可能, unlikely-可能无关, pending-待评价, unable-无法评价',
  `unit_evaluation` varchar(20) DEFAULT NULL COMMENT '报告单位评价：同上',
  `unit_evaluator_name` varchar(50) DEFAULT NULL COMMENT '评价人签名',

  -- ========== 报告人信息 ==========
  `reporter_phone` varchar(20) DEFAULT NULL COMMENT '报告人联系电话',
  `reporter_profession` varchar(20) DEFAULT NULL COMMENT '职业：doctor-医生, pharmacist-药师, nurse-护士, other-其他',
  `reporter_email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `reporter_signature` varchar(50) DEFAULT NULL COMMENT '报告人签名',

  -- ========== 报告单位信息 ==========
  `unit_name` varchar(200) DEFAULT NULL COMMENT '报告单位名称',
  `unit_contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `unit_phone` varchar(20) DEFAULT NULL COMMENT '单位电话',
  `report_date` date DEFAULT NULL COMMENT '报告日期',

  -- ========== 生产企业填写信息来源 ==========
  `info_source` varchar(20) DEFAULT NULL COMMENT '信息来源：hospital-医疗机构, business-经营企业, personal-个人, literature-文献报道, study-上市后研究, other-其他',
  `remark` text COMMENT '备注',

  -- ========== 系统字段 ==========
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态：draft-草稿, pending_audit-待审核, returned-已退回, pending_process-待处理, closed-已结案',
  `department_id` varchar(36) DEFAULT NULL COMMENT '上报科室ID（关联 sys_depart 表）',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人登录名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人登录名',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门编码',
  `tenant_id` int DEFAULT NULL COMMENT '租户ID（多租户支持）',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除标记（0-正常 1-已删除）',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_report_code` (`report_code`) COMMENT '报告编号唯一索引',
  KEY `idx_status` (`status`) COMMENT '状态索引',
  KEY `idx_report_type` (`report_type`) COMMENT '报告类型索引',
  KEY `idx_severity_type` (`severity_type`) COMMENT '严重程度索引',
  KEY `idx_patient_name` (`patient_name`) COMMENT '患者姓名索引',
  KEY `idx_reaction_time` (`reaction_time`) COMMENT '不良反应发生时间索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引',
  KEY `idx_department` (`department_id`) COMMENT '科室索引',
  KEY `idx_create_by` (`create_by`) COMMENT '创建人索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='药品不良反应报告主表';

-- 添加表注释
ALTER TABLE `drug_adverse_report` COMMENT = '药品不良反应报告主表 - 按照国家药品不良反应/事件报告表标准设计，存储完整的报告信息';
