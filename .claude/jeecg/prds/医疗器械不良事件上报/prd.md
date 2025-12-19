# PRD: 可疑医疗器械不良事件上报系统

## 基本信息

- **项目**: shijingshan
- **功能**: 可疑医疗器械不良事件上报
- **创建时间**: 2025-12-19
- **框架**: JeecgBoot 3.8.x
- **参考**: 药品不良反应上报模块 (1:1 复刻流程)
- **表单标准**: 国家食品药品监督管理局制《可疑医疗器械不良事件报告表》

## 功能概述

按照国家食品药品监督管理局《可疑医疗器械不良事件报告表》标准，1:1 复刻药品不良反应上报系统的完整业务流程，实现医疗器械不良事件的上报、审核、处理、整改、结案全流程管理。

### 与药品不良反应系统的关系

| 项目 | 药品不良反应 | 医疗器械不良事件 |
|------|-------------|-----------------|
| 表前缀 | `drug_adverse_*` | `device_adverse_*` |
| 报告编号 | ADR+日期+序号 | MDE+日期+序号 |
| 业务流程 | ✓ | 完全一致 |
| 角色权限 | ✓ | 完全一致 |
| 表单内容 | 药品专属字段 | 器械专属字段 |

## 业务流程（与药品模块一致）

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  临床上报    │ ──> │  科室审核    │ ──> │  器械科处理  │
│  (草稿/提交) │     │  (通过/退回) │     │            │
└─────────────┘     └─────────────┘     └─────────────┘
                                              │
                    ┌───────────────┬─────────┴─────────┐
                    │               │                   │
                    ▼               ▼                   ▼
              ┌───────────┐  ┌───────────────┐  ┌───────────┐
              │ 直接结案   │  │ 下发整改要求  │  │ 结案      │
              └───────────┘  └───────────────┘  └───────────┘
                                    │
                                    ▼
                            ┌───────────────┐
                            │ 临床填写整改   │
                            └───────────────┘
                                    │
                    ┌───────────────┴───────────────┐
                    ▼                               ▼
              ┌───────────┐                  ┌───────────┐
              │ 整改通过   │                  │ 整改退回   │
              │ (结案)    │                  │ (重新整改) │
              └───────────┘                  └───────────┘
```

### 状态流转

| 状态 | 值 | 说明 |
|------|-----|------|
| 草稿 | draft | 临床人员填写中 |
| 待审核 | pending_audit | 已提交，等待科室审核 |
| 已退回 | returned | 科室审核退回 |
| 待处理 | pending_process | 审核通过，等待器械科处理 |
| 待整改 | pending_rectify | 器械科下发整改要求 |
| 整改中 | rectifying | 临床人员填写整改中 |
| 已结案 | closed | 处理完成 |

## 角色权限（与药品模块一致）

| 角色 | 说明 | 权限范围 |
|------|------|----------|
| 临床科室人员 | 上报人员 | 上报、查看自己的报告、填写整改 |
| 临床科室审核员 | 科室负责人 | 审核本科室报告 |
| 器械科人员 | 职能科室 | 处理、下发整改、确认整改、结案 |
| 系统管理员 | 管理员 | 全部权限 |

## 数据库设计

### 表结构设计

#### 表1: device_adverse_report（医疗器械不良事件报告主表）

按照《可疑医疗器械不良事件报告表》字段设计：

| 字段名 | 类型 | 说明 | 备注 |
|--------|------|------|------|
| id | varchar(36) | 主键 | UUID |
| report_code | varchar(50) | 报告编号 | MDE+年月日+4位序号 |
| report_date | date | 报告日期 | |
| **A. 患者资料** | | | |
| patient_name | varchar(50) | 姓名 | |
| patient_age | int | 年龄 | |
| patient_gender | varchar(10) | 性别 | M-男, F-女 |
| patient_disease | text | 预期治疗疾病或作用 | |
| **B. 不良事件情况** | | | |
| event_manifestation | text | 5. 事件主要表现 | |
| event_occur_date | date | 6. 事件发生日期 | |
| event_discover_date | date | 7. 发现或者知悉时间 | |
| device_use_place | varchar(20) | 8. 医疗器械实际使用场所 | hospital-医疗机构, home-家庭, other-其它 |
| device_use_place_other | varchar(200) | 其它场所说明 | |
| **9. 事件后果（多选）** | | | |
| result_death | tinyint(1) | 死亡 | |
| result_death_time | datetime | 死亡时间 | |
| result_life_risk | tinyint(1) | 危及生命 | |
| result_hospitalization | tinyint(1) | 导致或延长住院 | |
| result_permanent_damage | tinyint(1) | 机体功能结构永久性损伤 | |
| result_intervention_needed | tinyint(1) | 可能导致机体功能机构永久性损伤 | |
| result_surgical_avoid | tinyint(1) | 需要内、外科治疗避免上述永久损伤 | |
| result_other | tinyint(1) | 其它 | |
| result_other_desc | text | 其它后果描述 | |
| **10. 事件陈述** | | | |
| event_statement | text | 事件陈述 | 至少包括器械使用时间、使用目的、不良事件情况、对受害者影响、采取的治疗措施、器械联合使用情况 |
| **C. 医疗器械情况** | | | |
| product_name | varchar(200) | 11. 产品名称 | |
| trade_name | varchar(200) | 12. 商品名称 | |
| registration_no | varchar(100) | 13. 注册证号 | |
| manufacturer_name | varchar(200) | 14. 生产企业名称 | |
| manufacturer_address | varchar(500) | 生产企业地址 | |
| manufacturer_contact | varchar(100) | 企业联系方式 | |
| model_spec | varchar(200) | 15. 型号规格 | |
| product_code | varchar(100) | 产品编号 | |
| product_batch | varchar(100) | 产品批号 | |
| **16. 操作人** | | | |
| operator_type | varchar(20) | 操作人类型 | professional-专业人员, non_professional-非专业人员, patient-患者, other-其它 |
| operator_type_other | varchar(100) | 其它操作人说明 | |
| valid_period_to | date | 17. 有效期至 | |
| production_date | date | 生产日期 | |
| deactivate_date | date | 19. 停用日期 | |
| implant_date | date | 20. 植入日期（若植入） | |
| **21. 事件发生初步原因分析** | | | |
| cause_analysis | text | 初步原因分析 | |
| **22. 事件初步处理情况** | | | |
| initial_handling | text | 初步处理情况 | |
| **23. 事件报告状态** | | | |
| report_status_use_unit | tinyint(1) | 已通知使用单位 | |
| report_status_business | tinyint(1) | 已通知经营企业 | |
| report_status_manufacturer | tinyint(1) | 已通知生产企业 | |
| report_status_fda | tinyint(1) | 已通知药监部门 | |
| **D. 不良事件评价** | | | |
| eval_time_sequence | varchar(20) | *1. 时间先后顺序是否合理 | yes-是, no-否 |
| eval_harm_type | varchar(20) | *2. 伤害事件是否属于器械可能导致的伤害类型 | yes-是, no-否, unknown-无法确定 |
| eval_exclude_other | varchar(20) | *3. 伤害事件是否可以用合并用药和/或械的作用、患者病情或其他非医疗器械因素来解释 | yes-是, no-否, unknown-无法确定 |
| **报告人信息** | | | |
| reporter_type | varchar(20) | 报告人类型 | doctor-医师, technician-技师, nurse-护士, other-其他 |
| reporter_signature | varchar(50) | 报告人签名 | |
| **系统字段** | | | |
| status | varchar(20) | 状态 | draft/pending_audit/returned/pending_process/pending_rectify/rectifying/closed |
| department_id | varchar(36) | 上报科室ID | |
| audit_user_id | varchar(36) | 审核人ID | |
| audit_user_name | varchar(50) | 审核人姓名 | |
| audit_time | datetime | 审核时间 | |
| audit_comment | text | 审核意见 | |
| process_user_id | varchar(36) | 处理人ID | |
| process_user_name | varchar(50) | 处理人姓名 | |
| process_time | datetime | 处理时间 | |
| process_comment | text | 处理意见 | |
| close_type | varchar(20) | 结案方式 | direct/rectify |
| close_user_id | varchar(36) | 结案人ID | |
| close_user_name | varchar(50) | 结案人姓名 | |
| close_time | datetime | 结案时间 | |
| close_comment | text | 结案意见 | |
| create_by | varchar(50) | 创建人 | |
| create_time | datetime | 创建时间 | |
| update_by | varchar(50) | 更新人 | |
| update_time | datetime | 更新时间 | |
| sys_org_code | varchar(64) | 部门编码 | |
| tenant_id | int | 租户ID | |
| del_flag | tinyint(1) | 删除标记 | |

#### 表2: device_adverse_flow（医疗器械不良事件流转记录表）

复刻 drug_adverse_flow 结构：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | varchar(36) | 主键 |
| report_id | varchar(36) | 关联报告ID |
| action | varchar(20) | 操作类型 |
| from_status | varchar(20) | 操作前状态 |
| to_status | varchar(20) | 操作后状态 |
| operator_id | varchar(36) | 操作人ID |
| operator_name | varchar(50) | 操作人姓名 |
| comment | text | 操作备注 |
| create_time | datetime | 创建时间 |

#### 表3: device_adverse_rectify（医疗器械不良事件整改表）

复刻 drug_adverse_rectify 结构：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | varchar(36) | 主键 |
| report_id | varchar(36) | 关联报告ID |
| rectify_round | int | 整改轮次 |
| prev_rectify_id | varchar(36) | 上一轮整改记录ID |
| requirement | text | 整改要求 |
| deadline | date | 整改期限 |
| require_user_id | varchar(36) | 下发人ID |
| require_user_name | varchar(50) | 下发人姓名 |
| require_time | datetime | 下发时间 |
| require_comment | text | 下发备注 |
| measures | text | 整改措施 |
| completion | text | 完成情况 |
| submit_user_id | varchar(36) | 提交人ID |
| submit_user_name | varchar(50) | 提交人姓名 |
| submit_time | datetime | 提交时间 |
| confirm_result | varchar(20) | 确认结果 |
| confirm_user_id | varchar(36) | 确认人ID |
| confirm_user_name | varchar(50) | 确认人姓名 |
| confirm_time | datetime | 确认时间 |
| confirm_comment | text | 确认意见 |
| status | varchar(20) | 状态 |
| create_by | varchar(50) | 创建人 |
| create_time | datetime | 创建时间 |
| update_by | varchar(50) | 更新人 |
| update_time | datetime | 更新时间 |
| del_flag | tinyint(1) | 删除标记 |
| tenant_id | int | 租户ID |

### 数据字典

| 字典编码 | 名称 | 字典项 |
|----------|------|--------|
| device_adr_status | 医疗器械不良事件状态 | draft-草稿, pending_audit-待审核, returned-已退回, pending_process-待处理, pending_rectify-待整改, rectifying-整改中, closed-已结案 |
| device_adr_use_place | 器械使用场所 | hospital-医疗机构, home-家庭, other-其它 |
| device_adr_operator_type | 操作人类型 | professional-专业人员, non_professional-非专业人员, patient-患者, other-其它 |
| device_adr_reporter_type | 报告人类型 | doctor-医师, technician-技师, nurse-护士, other-其他 |
| device_adr_eval_result | 评价结果 | yes-是, no-否, unknown-无法确定 |
| device_adr_flow_action | 流转操作类型 | submit-提交, audit_pass-审核通过, audit_reject-审核退回, resubmit-重新提交, close_direct-直接结案, require_rectify-要求整改, submit_rectify-提交整改, confirm_approve-确认整改通过, confirm_reject-退回整改, close-结案 |
| device_adr_rectify_status | 整改状态 | pending-待整改, submitted-已提交, approved-已通过, rejected-已退回 |
| device_adr_confirm_result | 确认结果 | approved-通过, rejected-退回 |
| device_adr_close_type | 结案方式 | direct-直接结案, rectify-整改结案 |

## 模块设计

### 后端模块

- **模块名**: jeecg-module-adverse-event（复用现有模块）
- **包名**: org.jeecg.modules.adverse.device

### 后端类设计

#### Entity 实体类

| 类名 | 文件 | 说明 |
|------|------|------|
| DeviceAdverseReport | entity/DeviceAdverseReport.java | 医疗器械不良事件报告主表实体 |
| DeviceAdverseFlow | entity/DeviceAdverseFlow.java | 流转记录实体 |
| DeviceAdverseRectify | entity/DeviceAdverseRectify.java | 整改记录实体 |

#### Mapper 数据访问层

| 类名 | 说明 |
|------|------|
| DeviceAdverseReportMapper | 报告主表 Mapper |
| DeviceAdverseFlowMapper | 流转记录 Mapper |
| DeviceAdverseRectifyMapper | 整改记录 Mapper |

#### Service 服务层

| 类名 | 说明 |
|------|------|
| IDeviceAdverseReportService | 报告服务接口 |
| DeviceAdverseReportServiceImpl | 报告服务实现 |
| IDeviceAdverseFlowService | 流转服务接口 |
| DeviceAdverseFlowServiceImpl | 流转服务实现 |
| IDeviceAdverseRectifyService | 整改服务接口 |
| DeviceAdverseRectifyServiceImpl | 整改服务实现 |

#### Controller 控制器

| 类名 | 路径前缀 | 说明 |
|------|----------|------|
| DeviceAdverseReportController | /adverse/device/report | 报告上报 CRUD |
| DeviceAuditController | /adverse/device/audit | 临床科室审核 |
| DeviceProcessController | /adverse/device/process | 器械科处理 |
| DeviceClinicRectifyController | /adverse/device/clinicRectify | 临床科室整改 |

### API 设计

#### 报告上报 API

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /adverse/device/report/list | 我的报告列表 | device:report:list |
| GET | /adverse/device/report/detail | 报告详情 | device:report:view |
| POST | /adverse/device/report/add | 新增报告 | device:report:add |
| PUT | /adverse/device/report/edit | 编辑报告 | device:report:edit |
| DELETE | /adverse/device/report/delete | 删除报告 | device:report:delete |
| POST | /adverse/device/report/submit | 提交报告 | device:report:submit |

#### 临床科室审核 API

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /adverse/device/audit/pending | 待审核列表 | device:audit:list |
| GET | /adverse/device/audit/completed | 已审核列表 | device:audit:list |
| POST | /adverse/device/audit/pass | 审核通过 | device:audit:pass |
| POST | /adverse/device/audit/reject | 审核退回 | device:audit:reject |
| GET | /adverse/device/audit/flowHistory | 流转历史 | device:audit:view |

#### 器械科处理 API

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /adverse/device/process/pending | 待处理列表 | device:process:list |
| GET | /adverse/device/process/confirming | 待确认列表 | device:process:list |
| GET | /adverse/device/process/closed | 已结案列表 | device:process:list |
| POST | /adverse/device/process/closeDirect | 直接结案 | device:process:close |
| POST | /adverse/device/process/requireRectify | 下发整改 | device:process:rectify |
| POST | /adverse/device/process/confirmApprove | 确认整改通过 | device:process:confirm |
| POST | /adverse/device/process/confirmReject | 退回整改 | device:process:confirm |
| GET | /adverse/device/process/rectifyHistory | 整改历史 | device:process:view |

#### 临床科室整改 API

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| GET | /adverse/device/clinicRectify/list | 待整改列表 | device:rectify:list |
| GET | /adverse/device/clinicRectify/detail | 整改详情 | device:rectify:view |
| POST | /adverse/device/clinicRectify/submit | 提交整改 | device:rectify:submit |

### 前端页面

#### 页面结构

```
src/views/adverse/device/                    # 医疗器械上报
├── index.vue                                # 我的报告列表
├── DeviceReportList.vue                     # 报告列表组件
├── DeviceReportForm.vue                     # 报告表单页（1:1复刻国标表单）
├── device.data.ts                           # 表单配置
└── components/
    └── DeviceReportDetailModal.vue          # 报告详情弹窗

src/views/adverse/device-audit/              # 临床科室审核（器械）
├── index.vue
├── PendingList.vue
├── CompletedList.vue
├── deviceAudit.data.ts
└── components/
    ├── DeviceAuditModal.vue
    ├── DeviceReportDetailModal.vue
    └── FlowHistoryModal.vue

src/views/adverse/device-process/            # 器械科处理
├── index.vue
├── PendingList.vue
├── ConfirmingList.vue
├── ClosedList.vue
├── deviceProcess.data.ts
└── components/
    ├── DeviceProcessModal.vue
    ├── ConfirmRectifyModal.vue
    └── RectifyHistoryModal.vue

src/views/adverse/device-rectify/            # 临床科室整改（器械）
├── index.vue
└── components/
    ├── DeviceRectifyList.vue
    └── RectifyFormModal.vue
```

### 菜单结构

```
不良事件管理
├── 药品不良反应                              # 现有
│   ├── 我的报告
│   └── ...
├── 医疗器械不良事件                          # 新增
│   ├── 我的报告                              # /adverse/device
│   ├── 科室审核                              # /adverse/device-audit
│   ├── 器械科处理                            # /adverse/device-process
│   └── 整改管理                              # /adverse/device-rectify
└── ...
```

## 开发任务拆分

### Phase 1: 数据库设计（3个任务）

- [ ] 创建 device_adverse_report 主表 (V1024)
- [ ] 创建 device_adverse_flow 流转表 (V1025)
- [ ] 创建 device_adverse_rectify 整改表 + 数据字典 (V1026)

### Phase 2: 后端实体开发（3个任务）

- [ ] 创建 DeviceAdverseReport 实体、Mapper、Service
- [ ] 创建 DeviceAdverseFlow 实体、Mapper、Service
- [ ] 创建 DeviceAdverseRectify 实体、Mapper、Service

### Phase 3: 后端控制器开发（4个任务）

- [ ] 创建 DeviceAdverseReportController（上报 CRUD）
- [ ] 创建 DeviceAuditController（临床科室审核）
- [ ] 创建 DeviceProcessController（器械科处理）
- [ ] 创建 DeviceClinicRectifyController（临床科室整改）

### Phase 4: 前端开发（4个任务）

- [ ] 创建医疗器械上报页面（DeviceReportForm 1:1复刻国标表单）
- [ ] 创建临床科室审核页面
- [ ] 创建器械科处理页面
- [ ] 创建临床科室整改页面

### Phase 5: 菜单权限配置（1个任务）

- [ ] 创建菜单 SQL、按钮权限、角色配置

## 验收标准

- [ ] 按照国标表单 1:1 复刻报告表单
- [ ] 完整的上报、审核、处理、整改、结案流程
- [ ] 流转历史完整记录
- [ ] 整改支持多轮退回
- [ ] 权限控制正确
- [ ] 状态流转正确
- [ ] 与药品模块流程完全一致

---
> JeecgBoot PRD 文档
> 创建时间: 2025-12-19
> 参考模块: 药品不良反应上报系统
> 使用命令: /jeecg:plan 进行任务规划
