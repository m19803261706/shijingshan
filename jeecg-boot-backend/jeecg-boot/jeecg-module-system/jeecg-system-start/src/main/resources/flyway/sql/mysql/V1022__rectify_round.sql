-- ========================================
-- 整改轮次字段
-- 版本: V1022
-- 描述: 添加 rectify_round 字段支持多轮整改，保留上一轮退回原因
-- 作者: TC Agent
-- 创建时间: 2025-12-19
-- ========================================

-- ========================================
-- 一、添加整改轮次字段
-- ========================================
ALTER TABLE drug_adverse_rectify ADD COLUMN rectify_round INT NOT NULL DEFAULT 1 COMMENT '整改轮次（第几次整改）' AFTER report_id;

-- ========================================
-- 二、添加上一轮整改ID字段（用于关联退回原因）
-- ========================================
ALTER TABLE drug_adverse_rectify ADD COLUMN prev_rectify_id VARCHAR(32) COMMENT '上一轮整改记录ID（退回后创建新记录时关联）' AFTER rectify_round;

-- ========================================
-- 三、添加索引
-- ========================================
ALTER TABLE drug_adverse_rectify ADD INDEX idx_rectify_round (report_id, rectify_round);
