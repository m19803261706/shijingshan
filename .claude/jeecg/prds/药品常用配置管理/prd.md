# 药品常用配置管理 PRD

## 1. 概述

### 1.1 背景

在药品不良反应上报过程中，用户需要频繁录入怀疑药品和并用药品信息。目前每次录入都需要手动填写完整的药品信息，效率较低。

### 1.2 目标

- 建立常用药品配置库，支持快速搜索和下拉选择
- 在上报表单中实现药品信息的一键填充
- 支持自定义添加新药品，自动保存至配置库供后续复用

### 1.3 范围

本功能为「药品不良反应」模块的子功能，新增两个子菜单：
1. **怀疑药品配置** - 管理常用怀疑药品库
2. **并用药品配置** - 管理常用并用药品库

---

## 2. 功能需求

### 2.1 怀疑药品配置页面

#### 2.1.1 功能描述

提供常用怀疑药品的 CRUD 管理功能，用户可以预先配置常用药品信息。

#### 2.1.2 页面布局

- **顶部搜索区**：通用名称、商品名称、生产厂家、批准文号
- **操作按钮区**：新增、批量删除、导入、导出
- **数据列表区**：表格展示药品信息
- **分页组件**：标准分页

#### 2.1.3 字段定义

| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| generic_name | varchar(200) | 是 | 通用名称（含剂型） |
| trade_name | varchar(200) | 否 | 商品名称 |
| manufacturer | varchar(200) | 否 | 生产厂家 |
| approval_no | varchar(100) | 否 | 批准文号 |
| batch_no | varchar(100) | 否 | 生产批号 |
| dose_per_time | varchar(50) | 否 | 单次剂量 |
| dose_unit | varchar(20) | 否 | 剂量单位 |
| route | varchar(50) | 否 | 给药途径 |
| frequency | varchar(50) | 否 | 用药频次 |
| indication | varchar(200) | 否 | 用药原因 |

#### 2.1.4 操作功能

- **新增**：弹窗表单，填写药品信息后保存
- **编辑**：弹窗表单，修改已有药品信息
- **删除**：单条/批量删除，需确认
- **导入**：支持 Excel 批量导入
- **导出**：支持 Excel 导出

---

### 2.2 并用药品配置页面

#### 2.2.1 功能描述

提供常用并用药品的 CRUD 管理功能，结构与怀疑药品类似。

#### 2.2.2 字段定义

与怀疑药品配置字段相同。

---

### 2.3 上报表单集成

#### 2.3.1 怀疑药品子表优化

**当前行为**：手动填写所有字段

**优化后**：
1. 新增「快速选择」按钮，打开药品选择弹窗
2. 弹窗支持搜索、分页、选择
3. 选择后自动填充对应字段
4. 用户可在填充基础上修改
5. 保留原有手动填写功能

#### 2.3.2 并用药品子表优化

与怀疑药品子表优化方案相同。

#### 2.3.3 自动保存机制

当用户在子表中手动录入新药品信息时：
1. 保存上报表单时，检测是否为新药品
2. 判断逻辑：`通用名称 + 生产厂家 + 批准文号` 的组合不存在于配置库
3. 自动将新药品保存至对应配置库
4. 提示用户「药品信息已保存至常用配置」

---

## 3. 数据库设计

### 3.1 怀疑药品配置表

```sql
-- 表名: drug_common_suspect
-- 描述: 常用怀疑药品配置表

CREATE TABLE IF NOT EXISTS `drug_common_suspect` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',

  -- 药品基本信息（与 drug_adverse_suspect_drug 字段对齐）
  `approval_no` varchar(100) DEFAULT NULL COMMENT '批准文号',
  `trade_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `generic_name` varchar(200) NOT NULL COMMENT '通用名称（含剂型）',
  `manufacturer` varchar(200) DEFAULT NULL COMMENT '生产厂家',
  `batch_no` varchar(100) DEFAULT NULL COMMENT '生产批号',

  -- 用法用量
  `dose_per_time` varchar(50) DEFAULT NULL COMMENT '单次剂量',
  `dose_unit` varchar(20) DEFAULT NULL COMMENT '剂量单位',
  `route` varchar(50) DEFAULT NULL COMMENT '给药途径',
  `frequency` varchar(50) DEFAULT NULL COMMENT '用药频次',
  `indication` varchar(200) DEFAULT NULL COMMENT '用药原因',

  -- 使用统计
  `use_count` int DEFAULT 0 COMMENT '使用次数（用于排序推荐）',
  `last_used_time` datetime DEFAULT NULL COMMENT '最后使用时间',

  -- 系统字段
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',

  PRIMARY KEY (`id`),
  KEY `idx_generic_name` (`generic_name`),
  KEY `idx_manufacturer` (`manufacturer`),
  KEY `idx_use_count` (`use_count` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='常用怀疑药品配置表';
```

### 3.2 并用药品配置表

```sql
-- 表名: drug_common_concomitant
-- 描述: 常用并用药品配置表

CREATE TABLE IF NOT EXISTS `drug_common_concomitant` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',

  -- 药品基本信息（与 drug_adverse_concomitant_drug 字段对齐）
  `approval_no` varchar(100) DEFAULT NULL COMMENT '批准文号',
  `trade_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `generic_name` varchar(200) NOT NULL COMMENT '通用名称（含剂型）',
  `manufacturer` varchar(200) DEFAULT NULL COMMENT '生产厂家',
  `batch_no` varchar(100) DEFAULT NULL COMMENT '生产批号',

  -- 用法用量
  `dose_per_time` varchar(50) DEFAULT NULL COMMENT '单次剂量',
  `dose_unit` varchar(20) DEFAULT NULL COMMENT '剂量单位',
  `route` varchar(50) DEFAULT NULL COMMENT '给药途径',
  `frequency` varchar(50) DEFAULT NULL COMMENT '用药频次',
  `indication` varchar(200) DEFAULT NULL COMMENT '用药原因',

  -- 使用统计
  `use_count` int DEFAULT 0 COMMENT '使用次数（用于排序推荐）',
  `last_used_time` datetime DEFAULT NULL COMMENT '最后使用时间',

  -- 系统字段
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',

  PRIMARY KEY (`id`),
  KEY `idx_generic_name` (`generic_name`),
  KEY `idx_manufacturer` (`manufacturer`),
  KEY `idx_use_count` (`use_count` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='常用并用药品配置表';
```

---

## 4. API 设计

### 4.1 怀疑药品配置 API

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 列表查询 | GET | `/adverse/drugCommonSuspect/list` | 分页查询 |
| 详情 | GET | `/adverse/drugCommonSuspect/queryById` | 根据ID查询 |
| 新增 | POST | `/adverse/drugCommonSuspect/add` | 新增配置 |
| 编辑 | PUT | `/adverse/drugCommonSuspect/edit` | 编辑配置 |
| 删除 | DELETE | `/adverse/drugCommonSuspect/delete` | 删除配置 |
| 批量删除 | DELETE | `/adverse/drugCommonSuspect/deleteBatch` | 批量删除 |
| 导入 | POST | `/adverse/drugCommonSuspect/importExcel` | Excel导入 |
| 导出 | GET | `/adverse/drugCommonSuspect/exportXls` | Excel导出 |
| 搜索 | GET | `/adverse/drugCommonSuspect/search` | 模糊搜索（用于快速选择） |
| 更新使用次数 | PUT | `/adverse/drugCommonSuspect/updateUseCount` | 选择后更新统计 |

### 4.2 并用药品配置 API

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 列表查询 | GET | `/adverse/drugCommonConcomitant/list` | 分页查询 |
| 详情 | GET | `/adverse/drugCommonConcomitant/queryById` | 根据ID查询 |
| 新增 | POST | `/adverse/drugCommonConcomitant/add` | 新增配置 |
| 编辑 | PUT | `/adverse/drugCommonConcomitant/edit` | 编辑配置 |
| 删除 | DELETE | `/adverse/drugCommonConcomitant/delete` | 删除配置 |
| 批量删除 | DELETE | `/adverse/drugCommonConcomitant/deleteBatch` | 批量删除 |
| 导入 | POST | `/adverse/drugCommonConcomitant/importExcel` | Excel导入 |
| 导出 | GET | `/adverse/drugCommonConcomitant/exportXls` | Excel导出 |
| 搜索 | GET | `/adverse/drugCommonConcomitant/search` | 模糊搜索（用于快速选择） |
| 更新使用次数 | PUT | `/adverse/drugCommonConcomitant/updateUseCount` | 选择后更新统计 |

### 4.3 自动保存 API

在原有上报保存接口中扩展：

```java
// DrugAdverseReportController.add() 和 edit() 方法中
// 调用 DrugCommonSuspectService.saveIfNotExists(suspectDrug)
// 调用 DrugCommonConcomitantService.saveIfNotExists(concomitantDrug)
```

---

## 5. 前端页面设计

### 5.1 菜单结构

```
药品不良反应
├── 不良反应上报（已有）
├── 怀疑药品配置（新增）
└── 并用药品配置（新增）
```

### 5.2 页面文件结构

```
src/views/adverse/drug/
├── index.vue                    # 列表页（已有）
├── DrugReportForm.vue           # 表单页（已有，需修改）
├── drug.data.ts                 # 数据配置（已有，需修改）
├── drug.api.ts                  # API（已有，需修改）
├── suspect/                     # 怀疑药品配置（新增）
│   ├── index.vue                # 列表页
│   ├── SuspectModal.vue         # 编辑弹窗
│   └── suspect.data.ts          # 数据配置
├── concomitant/                 # 并用药品配置（新增）
│   ├── index.vue                # 列表页
│   ├── ConcomitantModal.vue     # 编辑弹窗
│   └── concomitant.data.ts      # 数据配置
└── components/                  # 公共组件（新增）
    └── DrugSelectModal.vue      # 药品选择弹窗（复用）
```

### 5.3 药品选择弹窗设计

**DrugSelectModal.vue** 组件功能：
- Props: `type: 'suspect' | 'concomitant'`（区分调用哪个API）
- 顶部搜索框：支持通用名称、商品名称、厂家模糊搜索
- 表格展示：通用名称、商品名称、生产厂家、用药途径
- 按使用次数降序排列（常用优先）
- 点击行选择，返回完整药品信息

---

## 6. 实现任务拆解

### Phase 1: 数据库设计
- [ ] Issue #32: 创建常用怀疑药品配置表 drug_common_suspect
- [ ] Issue #33: 创建常用并用药品配置表 drug_common_concomitant

### Phase 2: 后端实体与CRUD
- [ ] Issue #34: 创建 DrugCommonSuspect 实体类和 CRUD
- [ ] Issue #35: 创建 DrugCommonConcomitant 实体类和 CRUD
- [ ] Issue #36: 实现自动保存逻辑（saveIfNotExists）

### Phase 3: 前端页面
- [ ] Issue #37: 创建怀疑药品配置列表页
- [ ] Issue #38: 创建并用药品配置列表页
- [ ] Issue #39: 创建药品选择弹窗组件
- [ ] Issue #40: 集成选择弹窗到上报表单子表

### Phase 4: 菜单权限
- [ ] Issue #41: 创建菜单 SQL 和路由配置

---

## 7. 验收标准

### 7.1 基础功能
- [ ] 怀疑药品配置页面 CRUD 正常
- [ ] 并用药品配置页面 CRUD 正常
- [ ] Excel 导入导出正常

### 7.2 快速选择功能
- [ ] 上报表单怀疑药品子表可快速选择
- [ ] 上报表单并用药品子表可快速选择
- [ ] 选择后正确填充所有字段
- [ ] 按使用次数排序，常用优先

### 7.3 自动保存功能
- [ ] 新增药品自动保存至配置库
- [ ] 重复药品不重复保存
- [ ] 使用次数正确累加

---

## 8. 附录

### 8.1 相关文档
- 药品不良反应上报 PRD: `.claude/jeecg/prds/药品不良反应上报/prd.md`
- 怀疑药品表结构: `V1008__drug_adverse_suspect_drug.sql`
- 并用药品表结构: `V1009__drug_adverse_concomitant_drug.sql`

### 8.2 技术栈
- 后端: JeecgBoot 3.9.0 + Spring Boot 3.5.5
- 前端: Vue3 + Ant Design Vue + Vite
- 数据库: MySQL 8.0
