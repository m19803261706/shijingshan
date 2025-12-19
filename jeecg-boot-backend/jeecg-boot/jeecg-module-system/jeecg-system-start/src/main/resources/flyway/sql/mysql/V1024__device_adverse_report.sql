-- ========================================
-- 医疗器械不良事件上报系统 - 主表
-- 版本: V1024
-- 描述: 创建医疗器械不良事件报告主表 device_adverse_report
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- 参考: 国家食品药品监督管理局《可疑医疗器械不良事件报告表》
-- GitHub Issue: #58
-- ========================================

-- 创建医疗器械不良事件报告主表
CREATE TABLE IF NOT EXISTS `device_adverse_report` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `report_code` varchar(50) NOT NULL COMMENT '报告编号（自动生成：MDE+年月日+4位序号）',
  `report_date` date DEFAULT NULL COMMENT '报告日期',

  -- ========== A. 患者资料 ==========
  `patient_name` varchar(50) DEFAULT NULL COMMENT '1. 患者姓名',
  `patient_age` int DEFAULT NULL COMMENT '2. 年龄',
  `patient_gender` varchar(10) DEFAULT NULL COMMENT '3. 性别：M-男, F-女',
  `patient_disease` text COMMENT '4. 预期治疗疾病或作用',

  -- ========== B. 不良事件情况 ==========
  `event_manifestation` text COMMENT '5. 事件主要表现',
  `event_occur_date` date DEFAULT NULL COMMENT '6. 事件发生日期',
  `event_discover_date` date DEFAULT NULL COMMENT '7. 发现或者知悉日期',
  `device_use_place` varchar(20) DEFAULT NULL COMMENT '8. 医疗器械实际使用场所：hospital-医疗机构, home-家庭, other-其它',
  `device_use_place_other` varchar(200) DEFAULT NULL COMMENT '使用场所-其它说明',

  -- ========== 9. 事件后果（多选） ==========
  `result_death` tinyint(1) DEFAULT 0 COMMENT '死亡（0-否 1-是）',
  `result_death_time` datetime DEFAULT NULL COMMENT '死亡时间',
  `result_life_risk` tinyint(1) DEFAULT 0 COMMENT '危及生命（0-否 1-是）',
  `result_hospitalization` tinyint(1) DEFAULT 0 COMMENT '导致或延长住院（0-否 1-是）',
  `result_permanent_damage` tinyint(1) DEFAULT 0 COMMENT '机体功能结构永久性损伤（0-否 1-是）',
  `result_intervention_needed` tinyint(1) DEFAULT 0 COMMENT '可能导致机体功能机构永久性损伤（0-否 1-是）',
  `result_surgical_avoid` tinyint(1) DEFAULT 0 COMMENT '需要内、外科治疗避免上述永久损伤（0-否 1-是）',
  `result_other` tinyint(1) DEFAULT 0 COMMENT '其它后果（0-否 1-是）',
  `result_other_desc` text COMMENT '其它后果描述',

  -- ========== 10. 事件陈述 ==========
  `event_statement` text COMMENT '事件陈述（至少包括器械使用时间、使用目的、不良事件情况、对受害者影响、采取的治疗措施、器械联合使用情况）',

  -- ========== C. 医疗器械情况 ==========
  `product_name` varchar(200) DEFAULT NULL COMMENT '11. 产品名称',
  `trade_name` varchar(200) DEFAULT NULL COMMENT '12. 商品名称',
  `registration_no` varchar(100) DEFAULT NULL COMMENT '13. 注册证号',
  `manufacturer_name` varchar(200) DEFAULT NULL COMMENT '14. 生产企业名称',
  `manufacturer_address` varchar(500) DEFAULT NULL COMMENT '生产企业地址',
  `manufacturer_contact` varchar(100) DEFAULT NULL COMMENT '企业联系方式',
  `model_spec` varchar(200) DEFAULT NULL COMMENT '15. 型号规格',
  `product_code` varchar(100) DEFAULT NULL COMMENT '产品编号',
  `product_batch` varchar(100) DEFAULT NULL COMMENT '产品批号',

  -- ========== 16. 操作人 ==========
  `operator_type` varchar(20) DEFAULT NULL COMMENT '操作人类型：professional-专业人员, non_professional-非专业人员, patient-患者, other-其它',
  `operator_type_other` varchar(100) DEFAULT NULL COMMENT '其它操作人说明',

  -- ========== 产品有效期相关 ==========
  `valid_period_to` date DEFAULT NULL COMMENT '17. 有效期至',
  `production_date` date DEFAULT NULL COMMENT '18. 生产日期',
  `deactivate_date` date DEFAULT NULL COMMENT '19. 停用日期',
  `implant_date` date DEFAULT NULL COMMENT '20. 植入日期（若植入）',

  -- ========== 21. 事件发生初步原因分析 ==========
  `cause_analysis` text COMMENT '事件发生初步原因分析',

  -- ========== 22. 事件初步处理情况 ==========
  `initial_handling` text COMMENT '事件初步处理情况',

  -- ========== 23. 事件报告状态（多选） ==========
  `report_status_use_unit` tinyint(1) DEFAULT 0 COMMENT '已通知使用单位（0-否 1-是）',
  `report_status_business` tinyint(1) DEFAULT 0 COMMENT '已通知经营企业（0-否 1-是）',
  `report_status_manufacturer` tinyint(1) DEFAULT 0 COMMENT '已通知生产企业（0-否 1-是）',
  `report_status_fda` tinyint(1) DEFAULT 0 COMMENT '已通知药监部门（0-否 1-是）',

  -- ========== D. 不良事件评价 ==========
  `eval_time_sequence` varchar(20) DEFAULT NULL COMMENT '*1. 时间先后顺序是否合理：yes-是, no-否',
  `eval_harm_type` varchar(20) DEFAULT NULL COMMENT '*2. 伤害事件是否属于器械可能导致的伤害类型：yes-是, no-否, unknown-无法确定',
  `eval_exclude_other` varchar(20) DEFAULT NULL COMMENT '*3. 伤害事件是否可以用合并用药和/或械的作用、患者病情或其他非医疗器械因素来解释：yes-是, no-否, unknown-无法确定',

  -- ========== 报告人信息 ==========
  `reporter_type` varchar(20) DEFAULT NULL COMMENT '报告人类型：doctor-医师, technician-技师, nurse-护士, other-其他',
  `reporter_signature` varchar(50) DEFAULT NULL COMMENT '报告人签名',

  -- ========== 系统流转字段 ==========
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态：draft-草稿, pending_audit-待审核, returned-已退回, pending_process-待处理, pending_rectify-待整改, rectifying-整改中, closed-已结案',
  `department_id` varchar(36) DEFAULT NULL COMMENT '上报科室ID（关联 sys_depart 表）',

  -- 审核相关
  `audit_user_id` varchar(36) DEFAULT NULL COMMENT '审核人ID',
  `audit_user_name` varchar(50) DEFAULT NULL COMMENT '审核人姓名',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_comment` text COMMENT '审核意见',

  -- 处理相关
  `process_user_id` varchar(36) DEFAULT NULL COMMENT '处理人ID',
  `process_user_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `process_time` datetime DEFAULT NULL COMMENT '处理时间',
  `process_comment` text COMMENT '处理意见',

  -- 结案相关
  `close_type` varchar(20) DEFAULT NULL COMMENT '结案方式：direct-直接结案, rectify-整改结案',
  `close_user_id` varchar(36) DEFAULT NULL COMMENT '结案人ID',
  `close_user_name` varchar(50) DEFAULT NULL COMMENT '结案人姓名',
  `close_time` datetime DEFAULT NULL COMMENT '结案时间',
  `close_comment` text COMMENT '结案意见',

  -- ========== JeecgBoot 标准系统字段 ==========
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
  KEY `idx_patient_name` (`patient_name`) COMMENT '患者姓名索引',
  KEY `idx_event_occur_date` (`event_occur_date`) COMMENT '事件发生日期索引',
  KEY `idx_product_name` (`product_name`) COMMENT '产品名称索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引',
  KEY `idx_department` (`department_id`) COMMENT '科室索引',
  KEY `idx_create_by` (`create_by`) COMMENT '创建人索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='医疗器械不良事件报告主表';

-- 添加表注释
ALTER TABLE `device_adverse_report` COMMENT = '医疗器械不良事件报告主表 - 按照国家食品药品监督管理局《可疑医疗器械不良事件报告表》标准设计';
