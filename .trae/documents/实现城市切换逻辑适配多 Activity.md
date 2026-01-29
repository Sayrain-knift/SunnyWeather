## 任务目标
在 `PlaceAdapter` 中处理城市切换逻辑。根据 `PlaceFragment` 所处的 `Activity` 类型（`MainActivity` 或 `WeatherActivity`），执行不同的操作：
1. 如果在 `WeatherActivity` 中，则关闭侧滑菜单，更新经纬度和城市名，并刷新天气。
2. 如果在 `MainActivity` 中，则跳转到 `WeatherActivity`。

## 实施方案

### 1. 修改 WeatherActivity.kt
- 将 `wBinding` 变量的修饰符从 `private` 改为默认（`public`），以便在 `PlaceAdapter` 中能够访问 `drawerLayout`。

### 2. 修改 PlaceAdapter.kt
- 在 `onCreateViewHolder` 的点击事件监听器中：
    - 获取当前选中的 `place` 数据。
    - 判断 `fragment.activity` 的类型。
    - **如果是 `WeatherActivity`**：
        - 调用 `activity.wBinding.drawerLayout.closeDrawers()` 关闭侧滑菜单。
        - 更新 `activity.viewModel` 中的 `locationLng`、`locationLat` 和 `placeName`。
        - 调用 `activity.refreshWeather()` 刷新天气数据。
    - **如果是其他 Activity（如 `MainActivity`）**：
        - 创建 `Intent` 跳转到 `WeatherActivity` 并携带相关数据。
        - 启动 Activity 并销毁当前 Activity。
    - **通用操作**：调用 `fragment.viewModel.savePlace(place)` 保存选中的城市。

## 待执行步骤
1. [WeatherActivity.kt](file:///d:/Android_project/SunnyWeather/app/src/main/java/com/example/sunnyweather/ui/weather/WeatherActivity.kt): 修改 `wBinding` 的访问权限。
2. [PlaceAdapter.kt](file:///d:/Android_project/SunnyWeather/app/src/main/java/com/example/sunnyweather/ui/place/PlaceAdapter.kt): 实现多 Activity 适配的城市切换逻辑。
