# SpringCache 示例项目

本项目演示了如何在 Spring Boot 中使用注解式缓存（Spring Cache）进行基本的数据缓存操作。使用 `@Cacheable`、`@CachePut` 和 `@CacheEvict` 等注解，实现对 `User` 实体的缓存读写控制。

## 技术栈

- Spring Boot
- Spring Cache
- MyBatis（使用 Mapper 直接操作数据库）
- JDK 版本：建议 JDK 8 ~ 17

---

## 控制器：UserController

### 📌 功能说明

| 请求方式 | 路径         | 功能说明         | 缓存注解                     |
|----------|--------------|------------------|------------------------------|
| POST     | /user        | 添加用户         | `@CachePut` 将新增结果缓存   |
| GET      | /user?id=1   | 根据 ID 查询用户 | `@Cacheable` 缓存查找结果   |
| DELETE   | /user?id=1   | 删除指定用户     | `@CacheEvict` 删除对应缓存项 |
| DELETE   | /user/delAll | 清空所有用户     | `@CacheEvict(allEntries=true)` |

---

## 缓存策略说明

- **@Cacheable**：查询前先检查缓存，命中即返回，不再执行方法体。
- **@CachePut**：执行方法体并将返回结果缓存，适用于新增或更新操作。
- **@CacheEvict**：用于删除缓存。支持按 key 删除或清空整个缓存空间。

---

## 缓存命名规范

- `cacheNames` 为缓存空间名称，示例中为 `userCache`。
- `key` 表达式用 SpEL 编写，例如：`#id` 表示方法参数中的 `id`。
- 缓存 Key 格式：`userCache::id值`

---

## 示例

### 添加用户

```http
POST /user
Content-Type: application/json

{
  "id": 1,
  "name": "Tom",
  "age": 20
}