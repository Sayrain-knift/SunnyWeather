## 429 Too Many Requests 错误原因分析

即便您的总次数没有超标，依然出现 429 错误，通常是因为 **QPS（每秒请求数）限制**。彩云天气的免费 API 通常对并发请求有严格限制。

在当前代码中，[Repository.kt](file:///d:/Android_project/SunnyWeather/app/src/main/java/com/example/sunnyweather/logic/Repository.kt) 使用了 `async` 同时发出了“实时天气”和“每日天气”两个请求。这在瞬间产生了 2 个并发请求，极易触发免费版 API 的频率限制。

此外，[PlaceFragment.kt](file:///d:/Android_project/SunnyWeather/app/src/main/java/com/example/sunnyweather/ui/place/PlaceFragment.kt) 中的搜索功能在用户每输入一个字符时都会发送请求，这也会导致短时间内请求过多。

## 解决方案

### 1. 串行化天气请求

修改 [Repository.kt](file:///d:/Android_project/SunnyWeather/app/src/main/java/com/example/sunnyweather/logic/Repository.kt)，将并发请求改为串行请求，确保同一时间只有一个请求在发送。虽然加载速度会稍慢几百毫秒，但能有效避免频率超限。

### 2. 搜索框防抖优化

在 [PlaceFragment.kt](file:///d:/Android_project/SunnyWeather/app/src/main/java/com/example/sunnyweather/ui/place/PlaceFragment.kt) 中为搜索输入添加简单的逻辑（或建议用户），避免在输入过程中产生大量无效请求。

### 3. 检查 Token 来源

确保 [SunnyWeatherApplication.kt](file:///d:/Android_project/SunnyWeather/app/src/main/java/com/example/sunnyweather/SunnyWeatherApplication.kt) 中的 `TOKEN` 是您自己在彩云天气官网上申请的私有 Token。如果是使用了教程中提供的公用 Token，由于成千上万的人在同时使用，必然会频繁触发 429 错误。

## 待执行步骤

1. 修改 `Repository.kt` 中的 `refreshWeather` 方法，移除 `async` 和 `await`，改为直接调用挂起函数。
2. 建议在 `PlaceFragment` 中对搜索请求进行频率控制。
3. 验证修改后的程序是否能正常获取数据。

