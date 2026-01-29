## 修复步骤

### 1. 修复 WeatherViewModel.kt 的“爆红”错误
*   **问题**：IDE 无法识别 `Transformations` 或 `switchMap` 扩展函数，导致 lambda 参数类型推断失败。
*   **方案**：
    *   清理并重新组织 `androidx.lifecycle` 的导入。
    *   使用 `locationLiveData.switchMap { ... }` 扩展函数写法，这是目前 Android 开发的推荐做法。
    *   确保 `com.example.sunnyweather.logic.model.Location` 被正确导入。

### 2. 修复无法获取天气信息的问题
*   **问题**：点击地点后未能成功展示天气，可能是请求触发失败或结果处理异常。
*   **方案**：
    *   在 `WeatherActivity.kt` 中检查 `Intent` 传参的 Key 是否与 `PlaceAdapter.kt` 一致（目前代码中 `location_lng` 和 `location_lat` 是匹配的）。
    *   在 `WeatherActivity` 的观察者回调中增加详细的错误日志打印，以便在 Logcat 中查看具体的 API 报错信息。
    *   确保 `refreshWeather` 调用时传入的经纬度不为空。

### 3. 验证数据模型
*   确认 `PlaceResponse.kt` 中的 `Location` 类属性名为 `lng` 和 `lat`（经核实，当前代码中定义正确）。

## 预期效果
*   `WeatherViewModel.kt` 文件不再有红色报错。
*   点击搜索结果后，`WeatherActivity` 能够正常发起网络请求并展示天气数据。
