-- ============================================
-- 输血使用不良事件报告字典数据
-- @author TC Agent
-- @since 2025-12-19
-- @see Issue #75, Epic #73
-- ============================================

-- ============================================
-- 1. 事件发生场所字典 (blood_event_place)
-- ============================================
INSERT IGNORE INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`, `tenant_id`)
VALUES ('1869000001', '输血事件发生场所', 'blood_event_place', '输血不良事件发生场所', 0, 'admin', NOW(), 0, 0);

INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
VALUES
  ('1869000101', '1869000001', '门诊', 'outpatient', '门诊', 1, 1, 'admin', NOW()),
  ('1869000102', '1869000001', '急诊', 'emergency', '急诊', 2, 1, 'admin', NOW()),
  ('1869000103', '1869000001', '病区', 'ward', '病区', 3, 1, 'admin', NOW()),
  ('1869000104', '1869000001', '手术室', 'operating_room', '手术室', 4, 1, 'admin', NOW()),
  ('1869000105', '1869000001', '血透室', 'dialysis_room', '血透室', 5, 1, 'admin', NOW()),
  ('1869000106', '1869000001', 'ICU', 'icu', 'ICU', 6, 1, 'admin', NOW()),
  ('1869000107', '1869000001', '其他', 'other', '其他', 7, 1, 'admin', NOW());


-- ============================================
-- 2. 不良事件名称字典 (blood_event_name)
-- ============================================
INSERT IGNORE INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`, `tenant_id`)
VALUES ('1869000002', '输血不良事件名称', 'blood_event_name', '输血不良事件名称', 0, 'admin', NOW(), 0, 0);

INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
VALUES
  ('1869000201', '1869000002', '输血反应', 'transfusion_reaction', '输血反应', 1, 1, 'admin', NOW()),
  ('1869000202', '1869000002', '溶血反应', 'hemolysis', '溶血反应', 2, 1, 'admin', NOW()),
  ('1869000203', '1869000002', '过敏反应', 'allergy', '过敏反应', 3, 1, 'admin', NOW()),
  ('1869000204', '1869000002', '发热反应', 'fever', '发热反应', 4, 1, 'admin', NOW()),
  ('1869000205', '1869000002', '细菌污染', 'contamination', '细菌污染', 5, 1, 'admin', NOW()),
  ('1869000206', '1869000002', 'TRALI', 'trali', '输血相关急性肺损伤', 6, 1, 'admin', NOW()),
  ('1869000207', '1869000002', 'TA-GVHD', 'ta_gvhd', '输血相关移植物抗宿主病', 7, 1, 'admin', NOW()),
  ('1869000208', '1869000002', '其他', 'other', '其他', 8, 1, 'admin', NOW());


-- ============================================
-- 3. 输血事件类型字典 (blood_transfusion_event)
-- ============================================
INSERT IGNORE INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`, `tenant_id`)
VALUES ('1869000003', '输血事件类型', 'blood_transfusion_event', '输血事件类型', 0, 'admin', NOW(), 0, 0);

INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
VALUES
  ('1869000301', '1869000003', '输错血型', 'wrong_blood_type', '输错血型', 1, 1, 'admin', NOW()),
  ('1869000302', '1869000003', '输错患者', 'wrong_patient', '输错患者', 2, 1, 'admin', NOW()),
  ('1869000303', '1869000003', '输错血液成分', 'wrong_component', '输错血液成分', 3, 1, 'admin', NOW()),
  ('1869000304', '1869000003', '输注过期血液', 'expired_blood', '输注过期血液', 4, 1, 'admin', NOW()),
  ('1869000305', '1869000003', '其他', 'other', '其他', 5, 1, 'admin', NOW());


-- ============================================
-- 4. 严重程度字典 (blood_severity_level)
-- ============================================
INSERT IGNORE INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`, `tenant_id`)
VALUES ('1869000004', '输血事件严重程度', 'blood_severity_level', '输血不良事件严重程度', 0, 'admin', NOW(), 0, 0);

INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
VALUES
  ('1869000401', '1869000004', 'A级(轻微)', 'A', '轻微', 1, 1, 'admin', NOW()),
  ('1869000402', '1869000004', 'B级(一般)', 'B', '一般', 2, 1, 'admin', NOW()),
  ('1869000403', '1869000004', 'C级(严重)', 'C', '严重', 3, 1, 'admin', NOW()),
  ('1869000404', '1869000004', 'D级(灾难)', 'D', '灾难', 4, 1, 'admin', NOW());


-- ============================================
-- 5. 事件等级字典 (blood_event_level)
-- ============================================
INSERT IGNORE INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`, `tenant_id`)
VALUES ('1869000005', '输血事件等级', 'blood_event_level', '输血不良事件等级', 0, 'admin', NOW(), 0, 0);

INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
VALUES
  ('1869000501', '1869000005', 'I级', 'I', 'I级', 1, 1, 'admin', NOW()),
  ('1869000502', '1869000005', 'II级', 'II', 'II级', 2, 1, 'admin', NOW()),
  ('1869000503', '1869000005', 'III级', 'III', 'III级', 3, 1, 'admin', NOW()),
  ('1869000504', '1869000005', 'IV级', 'IV', 'IV级', 4, 1, 'admin', NOW());


-- ============================================
-- 6. 报告人/当事人类型字典 (blood_person_type)
-- ============================================
INSERT IGNORE INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`, `tenant_id`)
VALUES ('1869000006', '输血事件人员类型', 'blood_person_type', '报告人/当事人类型', 0, 'admin', NOW(), 0, 0);

INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
VALUES
  ('1869000601', '1869000006', '患者', 'patient', '患者', 1, 1, 'admin', NOW()),
  ('1869000602', '1869000006', '患者家属', 'family', '患者家属', 2, 1, 'admin', NOW()),
  ('1869000603', '1869000006', '护士', 'nurse', '护士', 3, 1, 'admin', NOW()),
  ('1869000604', '1869000006', '医生', 'doctor', '医生', 4, 1, 'admin', NOW()),
  ('1869000605', '1869000006', '其他职员', 'staff', '其他职员', 5, 1, 'admin', NOW()),
  ('1869000606', '1869000006', '其他', 'other', '其他', 6, 1, 'admin', NOW());


-- ============================================
-- 7. 职称字典 (blood_professional_title)
-- ============================================
INSERT IGNORE INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`, `tenant_id`)
VALUES ('1869000007', '输血事件报告人职称', 'blood_professional_title', '报告人职称', 0, 'admin', NOW(), 0, 0);

INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
VALUES
  ('1869000701', '1869000007', '医师', 'doctor', '医师', 1, 1, 'admin', NOW()),
  ('1869000702', '1869000007', '护师', 'nurse', '护师', 2, 1, 'admin', NOW()),
  ('1869000703', '1869000007', '技师', 'technician', '技师', 3, 1, 'admin', NOW()),
  ('1869000704', '1869000007', '其他', 'other', '其他', 4, 1, 'admin', NOW());


-- ============================================
-- 8. 输血不良事件报告状态字典 (blood_report_status)
-- ============================================
INSERT IGNORE INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `create_by`, `create_time`, `type`, `tenant_id`)
VALUES ('1869000008', '输血事件报告状态', 'blood_report_status', '输血不良事件报告状态', 0, 'admin', NOW(), 0, 0);

INSERT IGNORE INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `create_by`, `create_time`)
VALUES
  ('1869000801', '1869000008', '草稿', 'draft', '草稿', 1, 1, 'admin', NOW()),
  ('1869000802', '1869000008', '待审核', 'pending_audit', '待审核', 2, 1, 'admin', NOW()),
  ('1869000803', '1869000008', '已退回', 'returned', '已退回', 3, 1, 'admin', NOW()),
  ('1869000804', '1869000008', '待处理', 'pending_process', '待处理', 4, 1, 'admin', NOW()),
  ('1869000805', '1869000008', '待整改', 'pending_rectify', '待整改', 5, 1, 'admin', NOW()),
  ('1869000806', '1869000008', '整改中', 'rectifying', '整改中', 6, 1, 'admin', NOW()),
  ('1869000807', '1869000008', '已结案', 'closed', '已结案', 7, 1, 'admin', NOW());
