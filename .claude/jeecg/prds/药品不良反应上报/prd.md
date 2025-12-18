# 药品不良反应上报 PRD

## 1. 功能概述

### 1.1 背景
根据国家药品监督管理局《药品不良反应报告和监测管理办法》的要求，医疗机构需要对药品不良反应/事件进行标准化上报。本功能完全按照国家标准的"药品不良反应/事件报告表"1:1复刻实现。

### 1.2 目标
- 实现标准化的药品不良反应/事件上报表单
- 支持首次报告和跟踪报告
- 支持多行怀疑药品和并用药品录入
- 完整记录不良反应过程和结果
- 支持关联性评价和报告人/单位信息

## 2. 数据表设计

### 2.1 主表：drug_adverse_report（药品不良反应报告主表）

| 字段名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| id | varchar(36) | Y | 主键ID |
| report_code | varchar(50) | Y | 报告编号（自动生成：ADR+年月日+4位序号）|
| report_type | varchar(20) | Y | 报告类型：first-首次报告, follow_up-跟踪报告 |
| severity_type | varchar(20) | Y | 严重程度：new-新的, serious-严重, general-一般 |
| unit_category | varchar(20) | Y | 报告单位类别：hospital-医疗机构, business-经营企业, manufacture-生产企业, personal-个人, other-其他 |
| **患者基本信息** |
| patient_name | varchar(50) | N | 患者姓名 |
| patient_gender | varchar(10) | N | 患者性别：M-男, F-女 |
| patient_birth_date | date | N | 出生日期 |
| patient_nationality | varchar(20) | N | 民族 |
| patient_weight | decimal(5,2) | N | 体重(kg) |
| patient_phone | varchar(20) | N | 联系方式 |
| original_disease | text | N | 原患疾病 |
| hospital_name | varchar(100) | N | 医院名称 |
| medical_record_no | varchar(50) | N | 病历号/门诊号 |
| history_adr | varchar(20) | N | 既往药品不良反应：yes-有, no-无, unknown-不详 |
| history_adr_detail | text | N | 既往不良反应详情 |
| family_adr | varchar(20) | N | 家族药品不良反应：yes-有, no-无, unknown-不详 |
| family_adr_detail | text | N | 家族不良反应详情 |
| **相关重要信息** |
| has_smoking | tinyint(1) | N | 吸烟史 |
| has_drinking | tinyint(1) | N | 饮酒史 |
| is_pregnant | tinyint(1) | N | 妊娠期 |
| has_liver_disease | tinyint(1) | N | 肝病史 |
| has_kidney_disease | tinyint(1) | N | 肾病史 |
| has_allergy | tinyint(1) | N | 过敏史 |
| other_history | text | N | 其他病史 |
| **不良反应/事件信息** |
| reaction_name | varchar(200) | Y | 不良反应/事件名称 |
| reaction_time | datetime | Y | 不良反应/事件发生时间 |
| reaction_description | text | Y | 不良反应/事件过程描述（包括症状、体征、临床检验等）及处理情况 |
| **不良反应/事件结果** |
| reaction_result | varchar(20) | N | 结果：cured-痊愈, improved-好转, not_improved-未好转, unknown-不详, sequela-有后遗症, death-死亡 |
| sequela_description | text | N | 后遗症表现 |
| death_time | datetime | N | 死亡时间 |
| death_cause | text | N | 直接死因 |
| **停药或减量后情况** |
| stop_drug_reaction | varchar(20) | N | 停药或减量后反应是否消失或减轻：yes-是, no-否, unknown-不明, not_stop-未停药或未减量 |
| rechallenge_reaction | varchar(20) | N | 再次使用后是否再次出现：yes-是, no-否, unknown-不明, not_use-未再使用 |
| **对原患疾病的影响** |
| disease_impact | varchar(20) | N | 影响：none-不明显, prolonged-病程延长, aggravated-病情加重, sequela-导致后遗症, death-导致死亡 |
| **关联性评价** |
| reporter_evaluation | varchar(20) | N | 报告人评价：definite-肯定, probable-很可能, possible-可能, unlikely-可能无关, pending-待评价, unable-无法评价 |
| unit_evaluation | varchar(20) | N | 报告单位评价：同上 |
| unit_evaluator_name | varchar(50) | N | 评价人签名 |
| **报告人信息** |
| reporter_phone | varchar(20) | N | 报告人联系电话 |
| reporter_profession | varchar(20) | N | 职业：doctor-医生, pharmacist-药师, nurse-护士, other-其他 |
| reporter_email | varchar(100) | N | 电子邮箱 |
| reporter_signature | varchar(50) | N | 报告人签名 |
| **报告单位信息** |
| unit_name | varchar(200) | N | 报告单位名称 |
| unit_contact | varchar(50) | N | 联系人 |
| unit_phone | varchar(20) | N | 单位电话 |
| report_date | date | N | 报告日期 |
| **生产企业填写信息来源** |
| info_source | varchar(20) | N | 信息来源：hospital-医疗机构, business-经营企业, personal-个人, literature-文献报道, study-上市后研究, other-其他 |
| remark | text | N | 备注 |
| **系统字段** |
| status | varchar(20) | Y | 状态：draft-草稿, pending_audit-待审核, returned-已退回, pending_process-待处理, closed-已结案 |
| create_by | varchar(50) | N | 创建人 |
| create_time | datetime | N | 创建时间 |
| update_by | varchar(50) | N | 更新人 |
| update_time | datetime | N | 更新时间 |
| sys_org_code | varchar(64) | N | 所属部门编码 |
| del_flag | tinyint(1) | N | 删除标记 |

### 2.2 子表：drug_adverse_suspect_drug（怀疑药品）

| 字段名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| id | varchar(36) | Y | 主键ID |
| report_id | varchar(36) | Y | 关联报告ID |
| sort_order | int | N | 排序号 |
| approval_no | varchar(100) | N | 批准文号 |
| trade_name | varchar(200) | N | 商品名称 |
| generic_name | varchar(200) | Y | 通用名称（含剂型）|
| manufacturer | varchar(200) | N | 生产厂家 |
| batch_no | varchar(100) | N | 生产批号 |
| dose_per_time | varchar(50) | N | 单次剂量 |
| dose_unit | varchar(20) | N | 剂量单位：g, mg, ug, ml, IU等 |
| route | varchar(50) | N | 给药途径：oral-口服, iv-静脉注射, im-肌注, topical-外用, other-其他 |
| frequency | varchar(50) | N | 日次数 |
| start_date | date | N | 用药开始时间 |
| end_date | date | N | 用药截止时间 |
| indication | varchar(200) | N | 用药原因 |
| create_by | varchar(50) | N | 创建人 |
| create_time | datetime | N | 创建时间 |

### 2.3 子表：drug_adverse_concomitant_drug（并用药品）

| 字段名 | 类型 | 必填 | 说明 |
|-------|------|------|------|
| id | varchar(36) | Y | 主键ID |
| report_id | varchar(36) | Y | 关联报告ID |
| sort_order | int | N | 排序号 |
| approval_no | varchar(100) | N | 批准文号 |
| trade_name | varchar(200) | N | 商品名称 |
| generic_name | varchar(200) | Y | 通用名称（含剂型）|
| manufacturer | varchar(200) | N | 生产厂家 |
| batch_no | varchar(100) | N | 生产批号 |
| dose_per_time | varchar(50) | N | 单次剂量 |
| dose_unit | varchar(20) | N | 剂量单位 |
| route | varchar(50) | N | 给药途径 |
| frequency | varchar(50) | N | 日次数 |
| start_date | date | N | 用药开始时间 |
| end_date | date | N | 用药截止时间 |
| indication | varchar(200) | N | 用药原因 |
| create_by | varchar(50) | N | 创建人 |
| create_time | datetime | N | 创建时间 |

## 3. 功能需求

### 3.1 前端表单
- 完全按照国家标准报告表样式布局
- 分组显示：报告类型 → 患者信息 → 怀疑药品 → 并用药品 → 不良反应信息 → 结果评价 → 报告人信息
- 怀疑药品和并用药品支持动态添加多行
- 表单保存草稿和提交功能

### 3.2 后端API
- 新增/编辑/查询药品不良反应报告
- 一对多关系处理（主表+怀疑药品+并用药品）
- 自动生成报告编号

### 3.3 权限控制
- 复用现有不良事件角色体系
- 临床上报人员可新增、编辑、提交
- 科室审核人员可审核通过/退回

## 4. 技术方案

### 4.1 后端
- 基于现有 jeecg-module-adverse-event 模块扩展
- 使用 JeecgBoot 主子表模式
- Entity 使用 @TableField 注解关联子表

### 4.2 前端
- 使用 JeecgBoot 的 useListPage 和 useModal
- 表单使用 BasicForm + 动态表格组件
- 子表使用 JEditableTable 可编辑表格

## 5. MVP 范围

### Phase 1: 数据库
- [x] 创建主表 drug_adverse_report
- [x] 创建子表 drug_adverse_suspect_drug
- [x] 创建子表 drug_adverse_concomitant_drug
- [x] 配置数据字典

### Phase 2: 后端实体
- [x] 创建 Entity 类
- [x] 创建 Mapper 接口
- [x] 创建 Service 接口和实现
- [x] 创建 Controller

### Phase 3: 业务API
- [x] 保存草稿 API
- [x] 提交报告 API
- [x] 查询列表 API

### Phase 4: 前端页面
- [x] 药品不良反应列表页
- [x] 药品不良反应表单页（1:1复刻）
- [x] 表单详情页

### Phase 5: 菜单权限
- [x] 创建菜单 SQL
- [x] 配置访问权限

## 6. 验收标准

- [ ] 表单布局与国家标准报告表一致
- [ ] 怀疑药品支持多行录入
- [ ] 并用药品支持多行录入
- [ ] 可保存草稿和提交
- [ ] 数据正确存储到主表和子表

---
> 创建时间: 2025-12-19
> 作者: TC Agent
