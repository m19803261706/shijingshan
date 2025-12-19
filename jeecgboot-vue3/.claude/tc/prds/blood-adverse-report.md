# 输血使用不良事件报告 PRD

## 1. 功能概述

### 1.1 功能名称
输血使用不良事件报告（Blood Transfusion Adverse Event Report）

### 1.2 功能描述
实现医疗机构输血使用不良事件的上报、审核、处理和整改闭环管理流程，与药品/医疗器械不良事件报告模块保持一致的工作流程。

### 1.3 业务流程
```
临床上报 → 输血科审核 → 输血科处理(直接结案/要求整改) → 临床整改 → 输血科确认 → 结案
```

### 1.4 角色权限
| 角色 | 权限 |
|------|------|
| 临床人员 | 创建报告、提交报告、查看退回记录、提交整改 |
| 输血科 | 审核报告（通过/退回）、处理报告（直接结案/要求整改）、确认整改 |
| 管理员 | 全量数据查看 |

---

## 2. 表单设计（基于输血使用不良事件报告表）

### 2.1 A. 患者资料
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| 姓名 | 文本 | 是 | 患者姓名 |
| 性别 | 单选 | 是 | 男/女 |
| 年龄 | 数字 | 是 | 患者年龄 |
| 临床诊断 | 文本域 | 是 | 临床诊断描述 |
| 病案号 | 文本 | 是 | 住院/门诊病案号 |
| 涉及科室 | 文本 | 是 | 发生事件的科室 |

### 2.2 B. 不良事件情况
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| 事件发生场所 | 多选 | 是 | 门诊/急诊/病区/手术室/血透室/ICU/其他 |
| 事件发生场所(其他) | 文本 | 条件 | 选择"其他"时必填 |
| 事件描述 | 文本域 | 是 | 事件详细描述 |

### 2.3 C. 不良事件内容
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| 不良事件名称 | 下拉 | 是 | 输血反应/溶血反应/过敏反应/发热反应/细菌污染/TRALI/TA-GVHD/其他 |
| 不良事件名称(其他) | 文本 | 条件 | 选择"其他"时必填 |
| 输血事件 | 下拉 | 是 | 输错血型/输错患者/输错血液成分/输注过期血液/其他 |
| 输血事件(其他) | 文本 | 条件 | 选择"其他"时必填 |
| 涉及人员 | 文本 | 否 | 事件涉及的相关人员 |

### 2.4 D. 不良事件等级
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| 严重程度 | 单选 | 是 | A级(轻微)/B级(一般)/C级(严重)/D级(灾难) |
| 事件等级 | 单选 | 是 | I级/II级/III级/IV级 |

### 2.5 E. 事件发生后及时处理与分析
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| 事件处理措施 | 文本域 | 是 | 描述采取的处理措施 |
| 导致事件的可能原因 | 文本域 | 是 | 分析可能的原因 |

### 2.6 F. 改进措施
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| 改进措施 | 文本域 | 是 | 拟采取的改进措施 |

### 2.7 G. 选填项目
| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| 报告人 | 多选 | 否 | 患者/患者家属/护士/医生/其他职员/其他 |
| 当事人类别 | 多选 | 否 | 患者/患者家属/护士/医生/其他职员/其他 |
| 职称 | 单选 | 否 | 医师/护师/技师/其他 |
| 报告人签名 | 文本 | 是 | 报告人签名 |
| 科室名称 | 文本 | 是 | 报告科室 |
| 联系电话 | 文本 | 否 | 联系电话 |
| Email | 文本 | 否 | 电子邮箱 |

---

## 3. 数据库设计

### 3.1 主表：blood_adverse_report（输血不良事件报告表）

```sql
CREATE TABLE `blood_adverse_report` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `report_code` varchar(32) NOT NULL COMMENT '报告编号（自动生成）',

  -- A. 患者资料
  `patient_name` varchar(50) DEFAULT NULL COMMENT '患者姓名',
  `patient_gender` varchar(10) DEFAULT NULL COMMENT '性别(M/F)',
  `patient_age` int DEFAULT NULL COMMENT '年龄',
  `clinical_diagnosis` text COMMENT '临床诊断',
  `medical_record_no` varchar(50) DEFAULT NULL COMMENT '病案号',
  `involved_dept` varchar(100) DEFAULT NULL COMMENT '涉及科室',

  -- B. 不良事件情况
  `event_place` varchar(200) DEFAULT NULL COMMENT '事件发生场所（多选，逗号分隔）',
  `event_place_other` varchar(100) DEFAULT NULL COMMENT '其他场所说明',
  `event_description` text COMMENT '事件描述',

  -- C. 不良事件内容
  `event_name` varchar(50) DEFAULT NULL COMMENT '不良事件名称',
  `event_name_other` varchar(100) DEFAULT NULL COMMENT '其他事件名称说明',
  `transfusion_event` varchar(50) DEFAULT NULL COMMENT '输血事件类型',
  `transfusion_event_other` varchar(100) DEFAULT NULL COMMENT '其他输血事件说明',
  `involved_staff` varchar(200) DEFAULT NULL COMMENT '涉及人员',

  -- D. 不良事件等级
  `severity_level` varchar(10) DEFAULT NULL COMMENT '严重程度(A/B/C/D)',
  `event_level` varchar(10) DEFAULT NULL COMMENT '事件等级(I/II/III/IV)',

  -- E. 事件处理与分析
  `handling_measures` text COMMENT '事件处理措施',
  `possible_causes` text COMMENT '导致事件的可能原因',

  -- F. 改进措施
  `improvement_measures` text COMMENT '改进措施',

  -- G. 选填项目
  `reporter_type` varchar(100) DEFAULT NULL COMMENT '报告人类型（多选，逗号分隔）',
  `party_type` varchar(100) DEFAULT NULL COMMENT '当事人类别（多选，逗号分隔）',
  `professional_title` varchar(20) DEFAULT NULL COMMENT '职称',
  `reporter_signature` varchar(50) DEFAULT NULL COMMENT '报告人签名',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '科室名称',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT 'Email',
  `report_date` date DEFAULT NULL COMMENT '报告日期',

  -- 流程状态
  `status` varchar(20) DEFAULT 'draft' COMMENT '状态(draft/pending_audit/returned/pending_process/pending_rectify/rectifying/closed)',
  `close_type` varchar(20) DEFAULT NULL COMMENT '结案方式(direct/rectify)',
  `close_user_id` varchar(36) DEFAULT NULL COMMENT '结案人ID',
  `close_user_name` varchar(50) DEFAULT NULL COMMENT '结案人姓名',
  `close_time` datetime DEFAULT NULL COMMENT '结案时间',
  `close_comment` text COMMENT '结案意见',

  -- 审核信息
  `audit_user_id` varchar(36) DEFAULT NULL COMMENT '审核人ID',
  `audit_user_name` varchar(50) DEFAULT NULL COMMENT '审核人姓名',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_comment` text COMMENT '审核意见',

  -- JeecgBoot标准字段
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '所属部门',
  `del_flag` int DEFAULT 0 COMMENT '删除标志(0-正常,1-已删除)',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_report_code` (`report_code`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='输血不良事件报告表';
```

### 3.2 流转记录表：blood_adverse_flow

```sql
CREATE TABLE `blood_adverse_flow` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `report_id` varchar(36) NOT NULL COMMENT '报告ID',
  `action` varchar(20) NOT NULL COMMENT '操作类型(submit/audit_pass/audit_reject/close_direct/require_rectify/submit_rectify/confirm_approve/confirm_reject)',
  `from_status` varchar(20) DEFAULT NULL COMMENT '原状态',
  `to_status` varchar(20) DEFAULT NULL COMMENT '新状态',
  `operator_id` varchar(36) DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `comment` text COMMENT '操作意见',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',

  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='输血不良事件流转记录表';
```

### 3.3 整改记录表：blood_adverse_rectify

```sql
CREATE TABLE `blood_adverse_rectify` (
  `id` varchar(36) NOT NULL COMMENT '主键',
  `report_id` varchar(36) NOT NULL COMMENT '报告ID',
  `round` int DEFAULT 1 COMMENT '整改轮次',

  -- 整改要求（输血科下发）
  `requirement` text COMMENT '整改要求',
  `deadline` date DEFAULT NULL COMMENT '整改期限',
  `require_user_id` varchar(36) DEFAULT NULL COMMENT '下发人ID',
  `require_user_name` varchar(50) DEFAULT NULL COMMENT '下发人姓名',
  `require_time` datetime DEFAULT NULL COMMENT '下发时间',

  -- 整改提交（临床提交）
  `measures` text COMMENT '整改措施',
  `completion` text COMMENT '完成情况',
  `submit_user_id` varchar(36) DEFAULT NULL COMMENT '提交人ID',
  `submit_user_name` varchar(50) DEFAULT NULL COMMENT '提交人姓名',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',

  -- 确认结果（输血科确认）
  `status` varchar(20) DEFAULT 'pending' COMMENT '状态(pending/submitted/approved/rejected)',
  `confirm_user_id` varchar(36) DEFAULT NULL COMMENT '确认人ID',
  `confirm_user_name` varchar(50) DEFAULT NULL COMMENT '确认人姓名',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `confirm_comment` text COMMENT '确认意见',

  -- 标准字段
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',

  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='输血不良事件整改记录表';
```

---

## 4. API 设计

### 4.1 临床上报 API

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 新增报告 | POST | /adverse/blood/add | 创建草稿 |
| 编辑报告 | PUT | /adverse/blood/edit | 编辑草稿 |
| 删除报告 | DELETE | /adverse/blood/delete | 删除草稿 |
| 报告详情 | GET | /adverse/blood/queryById | 查询详情 |
| 报告列表 | GET | /adverse/blood/list | 分页查询 |
| 提交报告 | POST | /adverse/blood/submit | 提交审核 |

### 4.2 输血科审核 API

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 待审核列表 | GET | /adverse/blood/audit/pending | 待审核报告 |
| 审核通过 | POST | /adverse/blood/audit/pass | 通过审核 |
| 审核退回 | POST | /adverse/blood/audit/reject | 退回修改 |
| 已审核列表 | GET | /adverse/blood/audit/passed | 已审核报告 |

### 4.3 输血科处理 API

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 待处理列表 | GET | /adverse/blood/process/pending | 待处理报告 |
| 直接结案 | POST | /adverse/blood/process/closeDirect | 直接结案 |
| 要求整改 | POST | /adverse/blood/process/requireRectify | 下发整改 |
| 待确认列表 | GET | /adverse/blood/process/confirming | 待确认整改 |
| 确认通过 | POST | /adverse/blood/process/confirmApprove | 整改通过 |
| 退回整改 | POST | /adverse/blood/process/confirmReject | 退回重改 |
| 已结案列表 | GET | /adverse/blood/process/closed | 已结案报告 |
| 整改记录 | GET | /adverse/blood/process/rectifyHistory | 整改历史 |
| 流转历史 | GET | /adverse/blood/process/flowHistory | 流转历史 |

### 4.4 临床整改 API

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 待整改列表 | GET | /adverse/blood/rectify/pending | 待整改报告 |
| 提交整改 | POST | /adverse/blood/rectify/submit | 提交整改 |
| 已提交列表 | GET | /adverse/blood/rectify/submitted | 已提交整改 |

---

## 5. 菜单设计

```
不良事件管理
├── 药品不良反应          # 已完成
├── 医疗器械不良事件       # 已完成
└── 输血不良事件
    ├── 输血不良事件上报    # /adverse/blood/report
    ├── 输血科审核         # /adverse/clinic-audit/blood
    ├── 输血科处理         # /adverse/blood-process
    └── 临床整改           # /adverse/clinic-rectify/blood
```

---

## 6. 字典配置

### 6.1 事件发生场所 (blood_event_place)
| 编码 | 名称 |
|------|------|
| outpatient | 门诊 |
| emergency | 急诊 |
| ward | 病区 |
| operating_room | 手术室 |
| dialysis_room | 血透室 |
| icu | ICU |
| other | 其他 |

### 6.2 不良事件名称 (blood_event_name)
| 编码 | 名称 |
|------|------|
| transfusion_reaction | 输血反应 |
| hemolysis | 溶血反应 |
| allergy | 过敏反应 |
| fever | 发热反应 |
| contamination | 细菌污染 |
| trali | TRALI(输血相关急性肺损伤) |
| ta_gvhd | TA-GVHD(输血相关移植物抗宿主病) |
| other | 其他 |

### 6.3 输血事件类型 (blood_transfusion_event)
| 编码 | 名称 |
|------|------|
| wrong_blood_type | 输错血型 |
| wrong_patient | 输错患者 |
| wrong_component | 输错血液成分 |
| expired_blood | 输注过期血液 |
| other | 其他 |

### 6.4 严重程度 (blood_severity_level)
| 编码 | 名称 | 说明 |
|------|------|------|
| A | A级 | 轻微 |
| B | B级 | 一般 |
| C | C级 | 严重 |
| D | D级 | 灾难 |

### 6.5 事件等级 (blood_event_level)
| 编码 | 名称 |
|------|------|
| I | I级 |
| II | II级 |
| III | III级 |
| IV | IV级 |

### 6.6 报告人/当事人类型 (blood_person_type)
| 编码 | 名称 |
|------|------|
| patient | 患者 |
| family | 患者家属 |
| nurse | 护士 |
| doctor | 医生 |
| staff | 其他职员 |
| other | 其他 |

### 6.7 职称 (blood_professional_title)
| 编码 | 名称 |
|------|------|
| doctor | 医师 |
| nurse | 护师 |
| technician | 技师 |
| other | 其他 |

---

## 7. 实现任务拆解

### 7.1 数据库层 [database]
- [ ] Task 1: 创建数据库迁移脚本 V1029__blood_adverse_report.sql
- [ ] Task 2: 创建字典数据迁移脚本 V1030__blood_adverse_dict.sql
- [ ] Task 3: 创建菜单权限迁移脚本 V1031__blood_adverse_menu.sql

### 7.2 后端层 [backend]
- [ ] Task 4: 创建 Entity 实体类（BloodAdverseReport, BloodAdverseFlow, BloodAdverseRectify）
- [ ] Task 5: 创建 Mapper 接口和 XML
- [ ] Task 6: 创建 Service 层（临床上报、审核、处理、整改）
- [ ] Task 7: 创建 Controller（BloodAdverseController, BloodAuditController, BloodProcessController, BloodRectifyController）

### 7.3 前端层 [frontend]
- [ ] Task 8: 创建 API 接口文件（blood.ts, bloodAudit.ts, bloodProcess.ts, bloodRectify.ts）
- [ ] Task 9: 创建数据配置文件 blood.data.ts
- [ ] Task 10: 创建上报表单页面 BloodReportForm.vue
- [ ] Task 11: 创建上报列表页面 BloodReportList.vue
- [ ] Task 12: 创建审核页面（BloodAuditList.vue, BloodReportDetailModal.vue）
- [ ] Task 13: 创建处理页面（BloodProcessPending.vue, BloodProcessConfirming.vue, BloodProcessClosed.vue）
- [ ] Task 14: 创建整改页面（BloodRectifyPending.vue, BloodRectifySubmitted.vue）
- [ ] Task 15: 配置路由

---

## 8. 验收标准

1. ✅ 临床人员可以创建、编辑、提交输血不良事件报告
2. ✅ 输血科可以审核报告（通过/退回）
3. ✅ 输血科可以处理报告（直接结案/要求整改）
4. ✅ 临床人员可以提交整改
5. ✅ 输血科可以确认整改（通过/退回）
6. ✅ 整改支持多轮次
7. ✅ 完整的流转历史记录
8. ✅ 表单字段与实际报告表一致

---

## 9. 参考文档

- 药品不良反应报告模块：`src/views/adverse/drug/`
- 医疗器械不良事件报告模块：`src/views/adverse/device/`
- 工作流程与药品/器械模块保持一致

---

> 创建时间：2025-12-19
> 创建人：TC Agent
> 版本：1.0
