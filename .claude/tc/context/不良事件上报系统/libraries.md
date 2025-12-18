# Context7 技术文档参考

> 查询时间: 2025-01-18
> 功能: 不良事件上报系统
> 项目: bj-shijingshan

## JeecgBoot 核心开发模式

### 1. Entity 实体类

继承 `JeecgEntity` 基类，自动获得 id、createBy、createTime、updateBy、updateTime 字段。

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description="不良事件")
@TableName("adverse_event")
public class AdverseEvent extends JeecgEntity implements Serializable {
    @Excel(name="事件编号", width=25)
    @Schema(description = "事件编号")
    private String eventCode;

    @Schema(description = "事件状态")
    @Dict(dicCode = "event_status")
    private String status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date occurTime;
}
```

### 2. Controller 控制器

继承 `JeecgController` 基类，自动获得导入导出功能。

```java
@Slf4j
@Tag(name = "不良事件管理")
@RestController
@RequestMapping("/adverse/event")
public class AdverseEventController extends JeecgController<AdverseEvent, IAdverseEventService> {

    @Autowired
    private IAdverseEventService adverseEventService;

    @Operation(summary = "分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> list(AdverseEvent adverseEvent,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest req) {
        QueryWrapper<AdverseEvent> queryWrapper = QueryGenerator.initQueryWrapper(adverseEvent, req.getParameterMap());
        Page<AdverseEvent> page = new Page<>(pageNo, pageSize);
        IPage<AdverseEvent> pageList = adverseEventService.page(page, queryWrapper);
        return Result.OK(pageList);
    }
}
```

### 3. Service 接口与实现

```java
// 接口
public interface IAdverseEventService extends IService<AdverseEvent> {
}

// 实现
@Service
public class AdverseEventServiceImpl extends ServiceImpl<AdverseEventMapper, AdverseEvent>
    implements IAdverseEventService {
}
```

### 4. Mapper 接口

```java
public interface AdverseEventMapper extends BaseMapper<AdverseEvent> {
}
```

## Flyway 迁移脚本

### 命名规范

- 位置: `jeecg-module-system/jeecg-system-start/src/main/resources/flyway/sql/mysql/`
- 命名: `V{版本号}_{序号}__{描述}.sql`
- 示例: `V1.0.0_1__adverse_event_init.sql`

### 配置

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:flyway/sql/mysql
    clean-disabled: true  # 生产环境必须为 true
```

## 菜单权限 SQL

```sql
-- 插入菜单
INSERT INTO sys_permission(id, parent_id, name, url, component, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, hide_tab, description, status, del_flag, rule_flag, create_by, create_time, update_by, update_time, internal_or_external)
VALUES('unique_id', 'parent_id', '菜单名称', '/路由', 'views/组件路径', NULL, NULL, 0, NULL, '1', 1.00, 0, 'icon-name', 1, 0, 0, 0, 0, NULL, '1', 0, 0, 'admin', NOW(), NULL, NULL, 0);
```

## 数据字典配置

```sql
-- 字典主表
INSERT INTO sys_dict(id, dict_name, dict_code, description, del_flag, create_by, create_time)
VALUES('dict_id', '事件状态', 'event_status', '不良事件状态字典', 0, 'admin', NOW());

-- 字典项
INSERT INTO sys_dict_item(id, dict_id, item_text, item_value, description, sort_order, status)
VALUES('item_id', 'dict_id', '草稿', 'draft', '', 1, 1);
```

## 代码复用点

### 可复用组件

1. **JeecgEntity** - 基础实体类（id、创建/更新时间等）
2. **JeecgController** - 基础控制器（导入导出Excel）
3. **QueryGenerator** - 查询条件生成器
4. **Result** - 统一响应结果封装
5. **@Dict** - 数据字典翻译注解
6. **@AutoLog** - 操作日志注解
7. **@Excel** - Excel导入导出注解

### 潜在冲突点

1. **sys_permission** - 菜单表，需要确保 ID 唯一
2. **sys_dict** - 字典表，需要确保 dict_code 唯一

## 模块结构参考

```
jeecg-module-adverse-event/
├── pom.xml
└── src/main/java/org/jeecg/modules/adverse/
    ├── controller/
    │   ├── AdverseEventController.java
    │   ├── AuditController.java
    │   └── RectifyController.java
    ├── entity/
    │   ├── AdverseEvent.java
    │   ├── AdverseEventFlow.java
    │   └── AdverseEventRectify.java
    ├── mapper/
    │   ├── AdverseEventMapper.java
    │   └── xml/
    │       └── AdverseEventMapper.xml
    ├── service/
    │   ├── IAdverseEventService.java
    │   └── impl/
    │       └── AdverseEventServiceImpl.java
    └── vo/
        └── AdverseEventVO.java
```
