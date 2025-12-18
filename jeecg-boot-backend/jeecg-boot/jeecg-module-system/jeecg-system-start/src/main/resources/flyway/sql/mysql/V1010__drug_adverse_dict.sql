-- ========================================
-- 药品不良反应上报系统 - 数据字典配置
-- 版本: V1010
-- 描述: 配置药品不良反应上报所需的数据字典
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- 参考: 国家药品不良反应/事件报告表
-- ========================================

-- ========================================
-- 1. drug_adr_report_type - 报告类型
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_report_type', '药品ADR报告类型', 'drug_adr_report_type', '药品不良反应报告类型：首次报告、跟踪报告', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_report_type_1', 'drug_adr_report_type', '首次报告', 'first', '首次上报的不良反应报告', 1, 1, 'admin', NOW()),
  ('drug_adr_report_type_2', 'drug_adr_report_type', '跟踪报告', 'follow_up', '对已报告事件的跟踪补充报告', 2, 1, 'admin', NOW());

-- ========================================
-- 2. drug_adr_severity - 严重程度
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_severity', '药品ADR严重程度', 'drug_adr_severity', '药品不良反应严重程度分类', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_severity_1', 'drug_adr_severity', '新的', 'new', '新发现的不良反应', 1, 1, 'admin', NOW()),
  ('drug_adr_severity_2', 'drug_adr_severity', '严重', 'serious', '导致死亡、危及生命、致残等严重后果', 2, 1, 'admin', NOW()),
  ('drug_adr_severity_3', 'drug_adr_severity', '一般', 'general', '一般程度的不良反应', 3, 1, 'admin', NOW());

-- ========================================
-- 3. drug_adr_unit_category - 报告单位类别
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_unit_category', '药品ADR报告单位类别', 'drug_adr_unit_category', '药品不良反应报告单位类别', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_unit_category_1', 'drug_adr_unit_category', '医疗机构', 'hospital', '医院、诊所等医疗机构', 1, 1, 'admin', NOW()),
  ('drug_adr_unit_category_2', 'drug_adr_unit_category', '经营企业', 'business', '药品经营企业', 2, 1, 'admin', NOW()),
  ('drug_adr_unit_category_3', 'drug_adr_unit_category', '生产企业', 'manufacture', '药品生产企业', 3, 1, 'admin', NOW()),
  ('drug_adr_unit_category_4', 'drug_adr_unit_category', '个人', 'personal', '个人上报', 4, 1, 'admin', NOW()),
  ('drug_adr_unit_category_5', 'drug_adr_unit_category', '其他', 'other', '其他来源', 5, 1, 'admin', NOW());

-- ========================================
-- 4. drug_adr_history - 既往/家族病史选项
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_history', '药品ADR病史选项', 'drug_adr_history', '既往/家族药品不良反应史选项', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_history_1', 'drug_adr_history', '有', 'yes', '有相关病史', 1, 1, 'admin', NOW()),
  ('drug_adr_history_2', 'drug_adr_history', '无', 'no', '无相关病史', 2, 1, 'admin', NOW()),
  ('drug_adr_history_3', 'drug_adr_history', '不详', 'unknown', '病史不详', 3, 1, 'admin', NOW());

-- ========================================
-- 5. drug_adr_route - 给药途径
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_route', '药品ADR给药途径', 'drug_adr_route', '药品给药途径', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_route_1', 'drug_adr_route', '口服', 'oral', '经口服用', 1, 1, 'admin', NOW()),
  ('drug_adr_route_2', 'drug_adr_route', '静脉注射', 'iv', '静脉推注', 2, 1, 'admin', NOW()),
  ('drug_adr_route_3', 'drug_adr_route', '静脉滴注', 'iv_drip', '静脉输液', 3, 1, 'admin', NOW()),
  ('drug_adr_route_4', 'drug_adr_route', '肌肉注射', 'im', '肌肉注射', 4, 1, 'admin', NOW()),
  ('drug_adr_route_5', 'drug_adr_route', '皮下注射', 'sc', '皮下注射', 5, 1, 'admin', NOW()),
  ('drug_adr_route_6', 'drug_adr_route', '外用', 'topical', '外用涂抹', 6, 1, 'admin', NOW()),
  ('drug_adr_route_7', 'drug_adr_route', '吸入', 'inhalation', '吸入给药', 7, 1, 'admin', NOW()),
  ('drug_adr_route_8', 'drug_adr_route', '舌下含服', 'sublingual', '舌下含服', 8, 1, 'admin', NOW()),
  ('drug_adr_route_9', 'drug_adr_route', '其他', 'other', '其他给药途径', 9, 1, 'admin', NOW());

-- ========================================
-- 6. drug_adr_dose_unit - 剂量单位
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_dose_unit', '药品ADR剂量单位', 'drug_adr_dose_unit', '药品剂量单位', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_dose_unit_1', 'drug_adr_dose_unit', 'g (克)', 'g', '克', 1, 1, 'admin', NOW()),
  ('drug_adr_dose_unit_2', 'drug_adr_dose_unit', 'mg (毫克)', 'mg', '毫克', 2, 1, 'admin', NOW()),
  ('drug_adr_dose_unit_3', 'drug_adr_dose_unit', 'μg (微克)', 'ug', '微克', 3, 1, 'admin', NOW()),
  ('drug_adr_dose_unit_4', 'drug_adr_dose_unit', 'ml (毫升)', 'ml', '毫升', 4, 1, 'admin', NOW()),
  ('drug_adr_dose_unit_5', 'drug_adr_dose_unit', 'IU (国际单位)', 'IU', '国际单位', 5, 1, 'admin', NOW()),
  ('drug_adr_dose_unit_6', 'drug_adr_dose_unit', '片', 'tablet', '片剂', 6, 1, 'admin', NOW()),
  ('drug_adr_dose_unit_7', 'drug_adr_dose_unit', '粒', 'capsule', '胶囊', 7, 1, 'admin', NOW()),
  ('drug_adr_dose_unit_8', 'drug_adr_dose_unit', '支', 'ampoule', '针剂支', 8, 1, 'admin', NOW()),
  ('drug_adr_dose_unit_9', 'drug_adr_dose_unit', '其他', 'other', '其他单位', 9, 1, 'admin', NOW());

-- ========================================
-- 7. drug_adr_result - 不良反应结果
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_result', '药品ADR反应结果', 'drug_adr_result', '药品不良反应/事件结果', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_result_1', 'drug_adr_result', '痊愈', 'cured', '完全康复', 1, 1, 'admin', NOW()),
  ('drug_adr_result_2', 'drug_adr_result', '好转', 'improved', '症状好转', 2, 1, 'admin', NOW()),
  ('drug_adr_result_3', 'drug_adr_result', '未好转', 'not_improved', '症状未改善', 3, 1, 'admin', NOW()),
  ('drug_adr_result_4', 'drug_adr_result', '不详', 'unknown', '结果不详', 4, 1, 'admin', NOW()),
  ('drug_adr_result_5', 'drug_adr_result', '有后遗症', 'sequela', '留有后遗症', 5, 1, 'admin', NOW()),
  ('drug_adr_result_6', 'drug_adr_result', '死亡', 'death', '患者死亡', 6, 1, 'admin', NOW());

-- ========================================
-- 8. drug_adr_stop_reaction - 停药后反应
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_stop_reaction', '药品ADR停药后反应', 'drug_adr_stop_reaction', '停药或减量后反应是否消失或减轻', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_stop_reaction_1', 'drug_adr_stop_reaction', '是', 'yes', '反应消失或减轻', 1, 1, 'admin', NOW()),
  ('drug_adr_stop_reaction_2', 'drug_adr_stop_reaction', '否', 'no', '反应未消失', 2, 1, 'admin', NOW()),
  ('drug_adr_stop_reaction_3', 'drug_adr_stop_reaction', '不明', 'unknown', '情况不明', 3, 1, 'admin', NOW()),
  ('drug_adr_stop_reaction_4', 'drug_adr_stop_reaction', '未停药或未减量', 'not_stop', '继续用药未停', 4, 1, 'admin', NOW());

-- ========================================
-- 9. drug_adr_rechallenge - 再次使用后反应
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_rechallenge', '药品ADR再激发反应', 'drug_adr_rechallenge', '再次使用可疑药品后是否再次出现同样反应', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_rechallenge_1', 'drug_adr_rechallenge', '是', 'yes', '再次出现同样反应', 1, 1, 'admin', NOW()),
  ('drug_adr_rechallenge_2', 'drug_adr_rechallenge', '否', 'no', '未再次出现', 2, 1, 'admin', NOW()),
  ('drug_adr_rechallenge_3', 'drug_adr_rechallenge', '不明', 'unknown', '情况不明', 3, 1, 'admin', NOW()),
  ('drug_adr_rechallenge_4', 'drug_adr_rechallenge', '未再使用', 'not_use', '未再使用该药', 4, 1, 'admin', NOW());

-- ========================================
-- 10. drug_adr_disease_impact - 对原患疾病的影响
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_disease_impact', '药品ADR对原疾病影响', 'drug_adr_disease_impact', '不良反应对原患疾病的影响', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_disease_impact_1', 'drug_adr_disease_impact', '不明显', 'none', '对原疾病影响不明显', 1, 1, 'admin', NOW()),
  ('drug_adr_disease_impact_2', 'drug_adr_disease_impact', '病程延长', 'prolonged', '导致病程延长', 2, 1, 'admin', NOW()),
  ('drug_adr_disease_impact_3', 'drug_adr_disease_impact', '病情加重', 'aggravated', '导致病情加重', 3, 1, 'admin', NOW()),
  ('drug_adr_disease_impact_4', 'drug_adr_disease_impact', '导致后遗症', 'sequela', '导致后遗症', 4, 1, 'admin', NOW()),
  ('drug_adr_disease_impact_5', 'drug_adr_disease_impact', '导致死亡', 'death', '导致患者死亡', 5, 1, 'admin', NOW());

-- ========================================
-- 11. drug_adr_evaluation - 关联性评价
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_evaluation', '药品ADR关联性评价', 'drug_adr_evaluation', '药品不良反应关联性评价', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_evaluation_1', 'drug_adr_evaluation', '肯定', 'definite', '肯定相关', 1, 1, 'admin', NOW()),
  ('drug_adr_evaluation_2', 'drug_adr_evaluation', '很可能', 'probable', '很可能相关', 2, 1, 'admin', NOW()),
  ('drug_adr_evaluation_3', 'drug_adr_evaluation', '可能', 'possible', '可能相关', 3, 1, 'admin', NOW()),
  ('drug_adr_evaluation_4', 'drug_adr_evaluation', '可能无关', 'unlikely', '可能无关', 4, 1, 'admin', NOW()),
  ('drug_adr_evaluation_5', 'drug_adr_evaluation', '待评价', 'pending', '待进一步评价', 5, 1, 'admin', NOW()),
  ('drug_adr_evaluation_6', 'drug_adr_evaluation', '无法评价', 'unable', '资料不足无法评价', 6, 1, 'admin', NOW());

-- ========================================
-- 12. drug_adr_profession - 报告人职业
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_profession', '药品ADR报告人职业', 'drug_adr_profession', '药品不良反应报告人职业', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_profession_1', 'drug_adr_profession', '医生', 'doctor', '执业医师', 1, 1, 'admin', NOW()),
  ('drug_adr_profession_2', 'drug_adr_profession', '药师', 'pharmacist', '执业药师', 2, 1, 'admin', NOW()),
  ('drug_adr_profession_3', 'drug_adr_profession', '护士', 'nurse', '执业护士', 3, 1, 'admin', NOW()),
  ('drug_adr_profession_4', 'drug_adr_profession', '其他', 'other', '其他人员', 4, 1, 'admin', NOW());

-- ========================================
-- 13. drug_adr_info_source - 信息来源
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_info_source', '药品ADR信息来源', 'drug_adr_info_source', '生产企业填写的信息来源', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_info_source_1', 'drug_adr_info_source', '医疗机构', 'hospital', '来源于医疗机构', 1, 1, 'admin', NOW()),
  ('drug_adr_info_source_2', 'drug_adr_info_source', '经营企业', 'business', '来源于经营企业', 2, 1, 'admin', NOW()),
  ('drug_adr_info_source_3', 'drug_adr_info_source', '个人', 'personal', '来源于个人', 3, 1, 'admin', NOW()),
  ('drug_adr_info_source_4', 'drug_adr_info_source', '文献报道', 'literature', '来源于文献报道', 4, 1, 'admin', NOW()),
  ('drug_adr_info_source_5', 'drug_adr_info_source', '上市后研究', 'study', '来源于上市后研究', 5, 1, 'admin', NOW()),
  ('drug_adr_info_source_6', 'drug_adr_info_source', '其他', 'other', '其他来源', 6, 1, 'admin', NOW());

-- ========================================
-- 14. drug_adr_status - 报告状态
-- ========================================
INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES ('drug_adr_status', '药品ADR报告状态', 'drug_adr_status', '药品不良反应报告状态', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
VALUES
  ('drug_adr_status_1', 'drug_adr_status', '草稿', 'draft', '草稿状态，可编辑', 1, 1, 'admin', NOW()),
  ('drug_adr_status_2', 'drug_adr_status', '待审核', 'pending_audit', '已提交，待科室审核', 2, 1, 'admin', NOW()),
  ('drug_adr_status_3', 'drug_adr_status', '已退回', 'returned', '审核退回，需修改', 3, 1, 'admin', NOW()),
  ('drug_adr_status_4', 'drug_adr_status', '待处理', 'pending_process', '审核通过，待职能处理', 4, 1, 'admin', NOW()),
  ('drug_adr_status_5', 'drug_adr_status', '已结案', 'closed', '处理完成，已结案', 5, 1, 'admin', NOW());
