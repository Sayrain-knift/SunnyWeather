## 修复 Repository.kt 中的错误

在 `Repository.kt` 文件中，`refreshWeather` 函数存在以下问题：

1. **作用域错误**：`async` 函数需要在 `CoroutineScope` 中运行，而 `liveData` 构建器提供的是 `LiveDataScope`。需要使用 `coroutineScope` 函数来创建一个协程作用域。
2. **拼写错误**：`realtimeResponse.Status` 中的 `Status` 首字母应为小写 `status`，以匹配 `RealtimeResponse` 模型类中的定义。
3. **缺失导入**：需要导入 `kotlinx.coroutines.async` 和 `kotlinx.coroutines.coroutineScope`。

### 实施步骤：

1. **更新导入语句**：在 `Repository.kt` 中添加 `kotlinx.coroutines.async` 和 `kotlinx.coroutines.coroutineScope` 的导入。
2. **重构 `refreshWeather` 函数**：
   - 使用 `coroutineScope` 包裹并发调用的逻辑。
   - 将 `realtimeResponse.Status` 修正为 `realtimeResponse.status`。
   - 确保 `result` 变量正确接收 `coroutineScope` 的返回值。
