书中所说的 `AppTheme` 在你的项目中对应的名字是 `Theme.SunnyWeather`。现在的 Android Studio 默认使用 Material3 主题，而书中使用的是 MaterialComponents 主题。

为了按照书中的要求修改，我们需要把 `themes.xml` 文件中的父主题 (parent) 进行替换。

**修改计划：**

1. **修改** **`res/values/themes.xml`** **(日间/默认模式)**

   * 将 `parent="Theme.Material3.DayNight.NoActionBar"`

   * 改为 `parent="Theme.MaterialComponents.DayNight.NoActionBar"`

2. **修改** **`res/values-night/themes.xml`** **(夜间模式)**

   * 同样将 `parent="Theme.Material3.DayNight.NoActionBar"`

   * 改为 `parent="Theme.MaterialComponents.DayNight.NoActionBar"`

这样做既符合书中的 MaterialComponents 模式要求，又能保留 Android 的深色模式支持。

**修改后的代码预览 (以** **`values/themes.xml`** **为例)：**

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <!-- 修改了 parent 属性 -->
    <style name="Base.Theme.SunnyWeather" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <!-- Customize your light theme here. -->
        <!-- <item name="colorPrimary">@color/my_light_primary</item> -->
    </style>

    <style name="Theme.SunnyWeather" parent="Base.Theme.SunnyWeather" />
</resources>
```

