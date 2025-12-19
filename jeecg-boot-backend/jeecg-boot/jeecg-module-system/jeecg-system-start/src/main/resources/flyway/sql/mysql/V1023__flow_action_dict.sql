-- ========================================
-- 补充流转操作字典项
-- 版本: V1023
-- 描述: 补充 drug_adr_flow_action 字典中缺失的操作类型
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- ========================================

-- ========================================
-- 一、补充流转操作类型字典项
-- ========================================

-- 直接结案
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'drug_adr_flow_action'), '直接结案', 'close_direct', '无需整改，直接结案', 10, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'drug_adr_flow_action') AND item_value = 'close_direct');

-- 确认整改通过
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'drug_adr_flow_action'), '确认整改通过', 'confirm_approve', '确认整改通过，结案', 11, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'drug_adr_flow_action') AND item_value = 'confirm_approve');

-- 退回整改
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), (SELECT id FROM sys_dict WHERE dict_code = 'drug_adr_flow_action'), '退回整改', 'confirm_reject', '整改不到位，退回重新整改', 12, 1, 'admin', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'drug_adr_flow_action') AND item_value = 'confirm_reject');

-- ========================================
-- 字典项汇总（drug_adr_flow_action）
-- ========================================
-- 1. submit - 提交
-- 2. audit_pass - 审核通过
-- 3. audit_reject - 审核退回
-- 4. resubmit - 重新提交
-- 5. process_start - 开始处理
-- 6. require_rectify - 要求整改
-- 7. submit_rectify - 提交整改
-- 8. confirm_rectify - 确认整改（旧）
-- 9. close - 结案
-- 10. close_direct - 直接结案（新增）
-- 11. confirm_approve - 确认整改通过（新增）
-- 12. confirm_reject - 退回整改（新增）
