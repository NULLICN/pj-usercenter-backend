# 数据库字段设计与MyBatis-Plus最佳实践

## 一、数据库字段命名规范

### 推荐方案：全小写下划线分隔符（SQL标准）

```sql
CREATE TABLE `user` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    user_account VARCHAR(50),
    avatar_url VARCHAR(255),
    gender INT,
    user_password VARCHAR(255),
    phone VARCHAR(20),
    email VARCHAR(100),
    user_status INT NOT NULL DEFAULT 0,
    create_time DATETIME,
    update_time DATETIME,
    is_delete INT NOT NULL DEFAULT 0,
    user_role INT NOT NULL DEFAULT 0,
    planet_code VARCHAR(50)
);
```

**优点：**
- ✅ 符合SQL标准和业界规范
- ✅ 易于识别多词组合字段
- ✅ 数据库厂商兼容性好
- ✅ 与ORM工具有天然适配（自动驼峰转换）

---

### 不推荐方案1：全小写无分隔（本项目情况）

```sql
CREATE TABLE `user` (
    id BIGINT,
    username VARCHAR(50),
    useraccount VARCHAR(50),      -- ❌ 难以分辨单词边界
    avatarurl VARCHAR(255),        -- ❌ 难以阅读
    userstatus INT,
    ...
);
```

**缺点：**
- ❌ 多词组合时难以区分单词边界
- ❌ 代码可读性差
- ❌ 需要明确指定字段映射

---

### 不推荐方案2：混合驼峰式（MyBatis-Plus自动映射失效）

```sql
CREATE TABLE `user` (
    id BIGINT,
    username VARCHAR(50),
    userAccount VARCHAR(50),      -- ❌ 不是SQL标准
    avatarUrl VARCHAR(255),        -- ❌ 与驼峰转下划线冲突
    userPassword VARCHAR(255),
    ...
);
```

**缺点：**
- ❌ 非SQL标准
- ❌ MyBatis-Plus的 `map-underscore-to-camel-case` 无法自动转换
- ❌ 容易与Java命名冲突

---

## 二、Java实体类设计规范

### 方案A：推荐 - 结合驼峰命名和显式映射

当数据库采用 **下划线分隔符** 时：

```java
@TableName(value = "user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 数据库列: username (与驼峰对应)
    private String username;
    
    // 数据库列: user_account
    // 配合 map-underscore-to-camel-case=true，MyBatis-Plus自动转换
    private String userAccount;
    
    // 数据库列: avatar_url
    private String avatarUrl;
    
    // 数据库列: user_password
    private String userPassword;
    
    // 可选字段标记为 @TableField(exist = false)
    private String tempField;
}
```

**application.yaml 配置：**
```yaml
mybatis:
  configuration:
    map-underscore-to-camel-case: true  # 启用自动映射
```

**优点：**
- ✅ 自动驼峰转换，无需手工指定每个字段
- ✅ 代码简洁
- ✅ 遵循Java命名规范

---

### 方案B：当前项目方案 - 显式映射每个字段

当数据库采用 **全小写无分隔** 或 **混合命名** 时，需要显式指定：

```java
@TableName(value = "user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField(value = "username")
    private String username;
    
    @TableField(value = "useraccount")  // 显式映射到数据库列
    private String userAccount;
    
    @TableField(value = "avatarurl")
    private String avatarUrl;
    
    @TableField(value = "userstatus")
    private Integer userStatus;
    
    // 不存在的字段标记为不映射
    @TableField(exist = false)
    private String tempField;
}
```

**application.yaml 配置：**
```yaml
mybatis:
  configuration:
    map-underscore-to-camel-case: false  # 禁用自动映射
```

**缺点：**
- ❌ 代码冗长，每个字段都需要指定

---

### 方案C：混合方案 - 既要驼峰映射，又要显式覆盖

```java
@TableName(value = "user")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 自动映射：usernameX -> username (驼峰转下划线)
    private String username;
    
    // 自动映射失效时显式指定
    @TableField(value = "custom_column_name")
    private String customField;
    
    // 标记为不映射
    @TableField(exist = false)
    private String transientField;
}
```

---

## 三、MyBatis-Plus最佳实践

### 1. 完整配置文件示例

```yaml
# application.yaml
mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: xyz.nullicn.projectstudyusercenter.model
  configuration:
    # 关键配置：下划线转驼峰
    map-underscore-to-camel-case: true
    # 其他配置
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # 打印SQL
    aggressive-lazy-loading: false
    default-enum-type-handler: org.apache.ibatis.type.EnumTypeHandler
  global-config:
    db-config:
      id-type: auto
      insert-strategy: not_null  # 插入时只插入非null字段
      update-strategy: not_null  # 更新时只更新非null字段
      select-strategy: not_null
```

---

### 2. 实体类完整示例

```java
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@TableName(value = "user")  // 显式指定表名
@Data
public class User implements Serializable {
    
    // 主键自增
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 简单字段（自动驼峰映射）
    private String username;
    private String phone;
    private String email;
    private Integer gender;
    
    // 复杂字段名（显式映射）
    @TableField(value = "useraccount")
    private String userAccount;
    
    @TableField(value = "userpassword")
    private String userPassword;
    
    @TableField(value = "avatarurl")
    private String avatarUrl;
    
    // 逻辑删除字段
    @TableLogic
    private Integer isDelete;
    
    // 时间字段（MyBatis-Plus自动填充）
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    
    // 非数据库字段
    @TableField(exist = false)
    private String tempData;
}
```

---

### 3. Mapper接口（推荐使用IService）

```java
// UserMapper.java
public interface UserMapper extends BaseMapper<User> {
    // 可添加自定义查询方法
}

// UserService.java
public interface UserService extends IService<User> {
    // 继承自IService的方法：
    // save(User), saveBatch(Collection<User>)
    // removeById(Long), removeByIds(Collection<Long>)
    // updateById(User), updateBatchById(Collection<User>)
    // getById(Long), listByIds(Collection<Long>)
    // page(Page<User>, QueryWrapper<User>)
}

// UserServiceImpl.java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    // 自动获得以上所有方法的实现
}
```

---

### 4. 常用查询示例

```java
// 简单插入
User user = new User();
user.setUsername("nullicn");
user.setUserAccount("account123");
userService.save(user);

// 条件查询
QueryWrapper<User> wrapper = new QueryWrapper<>();
wrapper.eq("username", "nullicn")
       .and(q -> q.eq("gender", 0).or().eq("gender", 1));
List<User> users = userService.list(wrapper);

// 分页查询
Page<User> page = new Page<>(1, 10);
userService.page(page, new QueryWrapper<User>()
    .eq("is_delete", 0)
    .orderByDesc("create_time")
);

// 批量操作
List<User> userList = Arrays.asList(...);
userService.saveBatch(userList);
```

---

## 四、本项目修复对比

### ❌ 修复前（导致错误）
- 数据库字段：`useraccount`、`userpassword`、`avatarurl` 等
- 配置：`map-underscore-to-camel-case: true`
- 实体：无 `@TableField` 注解
- **问题**：MyBatis-Plus尝试转换 `userAccount` → `user_account`，但数据库实际是 `useraccount`

### ✅ 修复后（正确解决）
- 数据库字段：`useraccount`、`userpassword`、`avatarurl` 等
- 配置：`map-underscore-to-camel-case: false`
- 实体：所有字段添加 `@TableField(value = "xxx")` 显式映射
- **结果**：完全匹配，SQL执行成功

---

## 五、设计建议总结

| 场景 | 数据库设计 | 实体配置 | 推荐度 |
|------|----------|--------|-------|
| 新项目 | 下划线分隔 `user_account` | 驼峰 + 自动映射 | ⭐⭐⭐⭐⭐ |
| 现有项目（混合） | 混合式 | 显式 `@TableField` | ⭐⭐⭐ |
| 现有项目（全小写） | 全小写 `useraccount` | 显式 `@TableField` | ⭐⭐ |
| 遗留系统 | 任意 | 显式 + `exist=false` | ⭐ |

---

## 六、避免问题的检查清单

- [ ] 数据库字段名与实体映射一致
- [ ] 所有字段都有正确的 `@TableField` 注解（如果需要）
- [ ] MyBatis配置中 `map-underscore-to-camel-case` 与实际情况相符
- [ ] 非数据库字段使用 `@TableField(exist = false)` 标记
- [ ] 主键使用 `@TableId` 注解
- [ ] 逻辑删除字段使用 `@TableLogic` 注解
- [ ] 时间字段配置 `@TableField(fill = FieldFill.INSERT/UPDATE)`
- [ ] 运行单元测试验证字段映射是否正确


