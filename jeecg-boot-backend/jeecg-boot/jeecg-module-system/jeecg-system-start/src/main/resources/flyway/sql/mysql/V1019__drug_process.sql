-- ========================================
-- 药品科室处理 - 数据库对象
-- 版本: V1019
-- 描述: 扩展 drug_adverse_report 表，创建整改表，添加字典
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- Issue: #49
-- ========================================

-- ========================================
-- 一、扩展 drug_adverse_report 表字段
-- ========================================

-- 处理相关字段
ALTER TABLE drug_adverse_report ADD COLUMN process_user_id VARCHAR(32) COMMENT '处理人ID';
ALTER TABLE drug_adverse_report ADD COLUMN process_user_name VARCHAR(100) COMMENT '处理人姓名';
ALTER TABLE drug_adverse_report ADD COLUMN process_time DATETIME COMMENT '处理时间';
ALTER TABLE drug_adverse_report ADD COLUMN process_comment TEXT COMMENT '处理意见';

-- 结案相关字段
ALTER TABLE drug_adverse_report ADD COLUMN close_type VARCHAR(32) COMMENT '结案方式：direct-直接结案 / rectify-整改结案';
ALTER TABLE drug_adverse_report ADD COLUMN close_user_id VARCHAR(32) COMMENT '结案人ID';
ALTER TABLE drug_adverse_report ADD COLUMN close_user_name VARCHAR(100) COMMENT '结案人姓名';
ALTER TABLE drug_adverse_report ADD COLUMN close_time DATETIME COMMENT '结案时间';
ALTER TABLE drug_adverse_report ADD COLUMN close_comment TEXT COMMENT '结案意见';

-- ========================================
-- 二、创建药品不良反应整改表
-- ========================================

CREATE TABLE IF NOT EXISTS drug_adverse_rectify (
  id VARCHAR(32) NOT NULL COMMENT '主键',
  report_id VARCHAR(32) NOT NULL COMMENT '关联报告ID',

  -- 整改要求（药剂科填写）
  requirement TEXT NOT NULL COMMENT '整改要求',
  deadline DATE NOT NULL COMMENT '整改期限',
  require_user_id VARCHAR(32) COMMENT '下发人ID',
  require_user_name VARCHAR(100) COMMENT '下发人姓名',
  require_time DATETIME COMMENT '下发时间',
  require_comment TEXT COMMENT '下发备注',

  -- 整改措施（临床人员填写）
  measures TEXT COMMENT '整改措施',
  completion TEXT COMMENT '完成情况',
  submit_user_id VARCHAR(32) COMMENT '提交人ID',
  submit_user_name VARCHAR(100) COMMENT '提交人姓名',
  submit_time DATETIME COMMENT '提交时间',

  -- 确认结果（药剂科填写）
  confirm_result VARCHAR(32) COMMENT '确认结果：approved-通过 / rejected-退回',
  confirm_user_id VARCHAR(32) COMMENT '确认人ID',
  confirm_user_name VARCHAR(100) COMMENT '确认人姓名',
  confirm_time DATETIME COMMENT '确认时间',
  confirm_comment TEXT COMMENT '确认意见',

  -- 状态
  status VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT '状态：pending-待整改 / submitted-已提交 / approved-已通过 / rejected-已退回',

  -- 系统字段
  create_by VARCHAR(32) COMMENT '创建人',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by VARCHAR(32) COMMENT '更新人',
  update_time DATETIME COMMENT '更新时间',
  del_flag INT DEFAULT 0 COMMENT '删除标记',
  tenant_id INT COMMENT '租户ID',

  PRIMARY KEY (id),
  INDEX idx_report_id (report_id),
  INDEX idx_status (status),
  INDEX idx_deadline (deadline)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品不良反应整改表';

-- ========================================
-- 三、扩展状态字典项
-- ========================================

-- 在 drug_adr_status 字典中添加新状态
INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '待整改', 'pending_rectify', '药剂科已下发整改要求', 5, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_status';

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '整改中', 'rectifying', '临床人员已提交整改', 6, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_status';

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '已结案', 'closed', '处理完成', 7, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_status';

-- ========================================
-- 四、创建结案方式字典
-- ========================================

INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES (UUID(), '结案方式', 'drug_adr_close_type', '药品不良反应结案方式', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '直接结案', 'direct', '无需整改，直接结案', 1, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_close_type';

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '整改结案', 'rectify', '整改通过后结案', 2, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_close_type';

-- ========================================
-- 五、创建整改状态字典
-- ========================================

INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES (UUID(), '整改状态', 'drug_adr_rectify_status', '药品不良反应整改状态', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '待整改', 'pending', '等待临床人员填写整改措施', 1, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_rectify_status';

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '已提交', 'submitted', '临床人员已提交整改', 2, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_rectify_status';

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '已通过', 'approved', '药剂科确认通过', 3, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_rectify_status';

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '已退回', 'rejected', '药剂科退回，需再次整改', 4, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_rectify_status';

-- ========================================
-- 六、创建确认结果字典
-- ========================================

INSERT INTO sys_dict (id, dict_name, dict_code, description, del_flag, create_by, create_time, type)
VALUES (UUID(), '确认结果', 'drug_adr_confirm_result', '药品不良反应整改确认结果', 0, 'admin', NOW(), 0);

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '通过', 'approved', '整改通过，结案', 1, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_confirm_result';

INSERT INTO sys_dict_item (id, dict_id, item_text, item_value, description, sort_order, status, create_by, create_time)
SELECT UUID(), id, '退回', 'rejected', '整改不到位，需再次整改', 2, 1, 'admin', NOW()
FROM sys_dict WHERE dict_code = 'drug_adr_confirm_result';
