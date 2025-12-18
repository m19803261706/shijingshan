-- ========================================
-- 不良事件上报系统 - 数据字典配置
-- 版本: V1005
-- 描述: 配置不良事件系统所需的数据字典
-- 作者: TC Agent
-- 创建时间: 2025-01-18
-- ========================================

-- ========================================
-- 1. 事件状态字典 (event_status)
-- ========================================

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `type`, `create_by`, `create_time`)
VALUES ('dict_event_status', '事件状态', 'event_status', '不良事件状态（草稿→待审核→待处理→待整改→已结案）', 0, 0, 'admin', NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `item_color`, `create_by`, `create_time`) VALUES
('dict_es_01', 'dict_event_status', '草稿', 'draft', '初始状态，可编辑修改', 1, 1, '#909399', 'admin', NOW()),
('dict_es_02', 'dict_event_status', '待审核', 'pending_audit', '已提交，等待科室审核', 2, 1, '#E6A23C', 'admin', NOW()),
('dict_es_03', 'dict_event_status', '已退回', 'returned', '审核不通过，退回修改', 3, 1, '#F56C6C', 'admin', NOW()),
('dict_es_04', 'dict_event_status', '待处理', 'pending_process', '审核通过，等待职能科室处理', 4, 1, '#409EFF', 'admin', NOW()),
('dict_es_05', 'dict_event_status', '待整改', 'pending_rectify', '职能科室要求整改', 5, 1, '#E6A23C', 'admin', NOW()),
('dict_es_06', 'dict_event_status', '整改中', 'rectifying', '整改措施编写中', 6, 1, '#409EFF', 'admin', NOW()),
('dict_es_07', 'dict_event_status', '待确认', 'pending_confirm', '整改已提交，等待确认', 7, 1, '#E6A23C', 'admin', NOW()),
('dict_es_08', 'dict_event_status', '已结案', 'closed', '流程结束，事件归档', 8, 1, '#67C23A', 'admin', NOW());

-- ========================================
-- 2. 事件级别字典 (event_level)
-- ========================================

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `type`, `create_by`, `create_time`)
VALUES ('dict_event_level', '事件级别', 'event_level', '不良事件级别（Ⅰ-Ⅳ级）', 0, 0, 'admin', NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `item_color`, `create_by`, `create_time`) VALUES
('dict_el_01', 'dict_event_level', 'Ⅰ级（警告事件）', 'I', '警告事件：非预期死亡或重大永久性功能丧失', 1, 1, '#F56C6C', 'admin', NOW()),
('dict_el_02', 'dict_event_level', 'Ⅱ级（不良后果事件）', 'II', '不良后果事件：对患者造成中等程度伤害', 2, 1, '#E6A23C', 'admin', NOW()),
('dict_el_03', 'dict_event_level', 'Ⅲ级（未造成后果事件）', 'III', '未造成后果事件：虽发生但未对患者造成伤害', 3, 1, '#409EFF', 'admin', NOW()),
('dict_el_04', 'dict_event_level', 'Ⅳ级（隐患事件）', 'IV', '隐患事件：有发生不良事件的潜在风险', 4, 1, '#909399', 'admin', NOW());

-- ========================================
-- 3. 流转操作类型字典 (flow_action)
-- ========================================

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `type`, `create_by`, `create_time`)
VALUES ('dict_flow_action', '流转操作类型', 'flow_action', '事件流转过程中的操作类型', 0, 0, 'admin', NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `item_color`, `create_by`, `create_time`) VALUES
('dict_fa_01', 'dict_flow_action', '提交', 'submit', '上报人提交事件', 1, 1, '#409EFF', 'admin', NOW()),
('dict_fa_02', 'dict_flow_action', '审核通过', 'approve', '科室审核通过', 2, 1, '#67C23A', 'admin', NOW()),
('dict_fa_03', 'dict_flow_action', '审核退回', 'reject', '科室审核退回', 3, 1, '#F56C6C', 'admin', NOW()),
('dict_fa_04', 'dict_flow_action', '处理', 'process', '职能科室处理', 4, 1, '#409EFF', 'admin', NOW()),
('dict_fa_05', 'dict_flow_action', '要求整改', 'require_rectify', '职能科室要求整改', 5, 1, '#E6A23C', 'admin', NOW()),
('dict_fa_06', 'dict_flow_action', '提交整改', 'submit_rectify', '科室提交整改', 6, 1, '#409EFF', 'admin', NOW()),
('dict_fa_07', 'dict_flow_action', '确认整改', 'confirm_rectify', '职能科室确认整改通过', 7, 1, '#67C23A', 'admin', NOW()),
('dict_fa_08', 'dict_flow_action', '退回整改', 'reject_rectify', '职能科室退回整改', 8, 1, '#F56C6C', 'admin', NOW()),
('dict_fa_09', 'dict_flow_action', '结案', 'close', '事件结案归档', 9, 1, '#67C23A', 'admin', NOW());

-- ========================================
-- 4. 整改状态字典 (rectify_status)
-- ========================================

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `type`, `create_by`, `create_time`)
VALUES ('dict_rectify_status', '整改状态', 'rectify_status', '整改记录的状态', 0, 0, 'admin', NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `item_color`, `create_by`, `create_time`) VALUES
('dict_rs_01', 'dict_rectify_status', '待整改', 'pending', '等待科室填写整改措施', 1, 1, '#E6A23C', 'admin', NOW()),
('dict_rs_02', 'dict_rectify_status', '已提交', 'submitted', '整改措施已提交，等待审核', 2, 1, '#409EFF', 'admin', NOW()),
('dict_rs_03', 'dict_rectify_status', '已通过', 'approved', '整改审核通过', 3, 1, '#67C23A', 'admin', NOW()),
('dict_rs_04', 'dict_rectify_status', '未通过', 'rejected', '整改审核未通过，需重新整改', 4, 1, '#F56C6C', 'admin', NOW());

-- ========================================
-- 5. 审核结果字典 (review_result)
-- ========================================

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `type`, `create_by`, `create_time`)
VALUES ('dict_review_result', '审核结果', 'review_result', '审核操作的结果', 0, 0, 'admin', NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `item_color`, `create_by`, `create_time`) VALUES
('dict_rr_01', 'dict_review_result', '通过', 'approved', '审核通过', 1, 1, '#67C23A', 'admin', NOW()),
('dict_rr_02', 'dict_review_result', '不通过', 'rejected', '审核不通过', 2, 1, '#F56C6C', 'admin', NOW());

-- ========================================
-- 6. 分类状态字典 (category_status)
-- ========================================

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `type`, `create_by`, `create_time`)
VALUES ('dict_category_status', '分类状态', 'category_status', '事件分类的启用状态', 0, 0, 'admin', NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `item_color`, `create_by`, `create_time`) VALUES
('dict_cs_01', 'dict_category_status', '启用', '1', '分类可用', 1, 1, '#67C23A', 'admin', NOW()),
('dict_cs_02', 'dict_category_status', '禁用', '0', '分类不可用', 2, 1, '#909399', 'admin', NOW());

-- ========================================
-- 7. 是否匿名字典 (yes_no)
-- 注：可能系统已有，这里添加备用
-- ========================================

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `type`, `create_by`, `create_time`)
SELECT 'dict_yes_no', '是否', 'yes_no', '是/否选项', 0, 0, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict` WHERE `dict_code` = 'yes_no');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `item_color`, `create_by`, `create_time`)
SELECT 'dict_yn_01', 'dict_yes_no', '是', '1', '', 1, 1, '#67C23A', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = 'dict_yes_no' AND `item_value` = '1');

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `item_color`, `create_by`, `create_time`)
SELECT 'dict_yn_02', 'dict_yes_no', '否', '0', '', 2, 1, '#909399', 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_dict_item` WHERE `dict_id` = 'dict_yes_no' AND `item_value` = '0');

-- ========================================
-- 8. 伤害程度字典 (harm_degree)
-- ========================================

INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `del_flag`, `type`, `create_by`, `create_time`)
VALUES ('dict_harm_degree', '伤害程度', 'harm_degree', '不良事件对患者造成的伤害程度', 0, 0, 'admin', NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_text`, `item_value`, `description`, `sort_order`, `status`, `item_color`, `create_by`, `create_time`) VALUES
('dict_hd_01', 'dict_harm_degree', '无伤害', 'none', '事件未对患者造成任何伤害', 1, 1, '#67C23A', 'admin', NOW()),
('dict_hd_02', 'dict_harm_degree', '轻度伤害', 'mild', '轻微伤害，无需或仅需简单处理', 2, 1, '#409EFF', 'admin', NOW()),
('dict_hd_03', 'dict_harm_degree', '中度伤害', 'moderate', '中等程度伤害，需要医疗干预', 3, 1, '#E6A23C', 'admin', NOW()),
('dict_hd_04', 'dict_harm_degree', '重度伤害', 'severe', '严重伤害，造成永久性功能障碍', 4, 1, '#F56C6C', 'admin', NOW()),
('dict_hd_05', 'dict_harm_degree', '死亡', 'death', '导致患者死亡', 5, 1, '#303133', 'admin', NOW());
