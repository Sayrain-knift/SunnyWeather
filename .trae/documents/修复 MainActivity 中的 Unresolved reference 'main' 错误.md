该错误是因为 `MainActivity.kt` 中尝试通过 `findViewById(R.id.main)` 获取 ID 为 `main` 的视图，但在对应的布局文件 `activity_main.xml` 中没有定义这个 ID。这通常是由于 Android Studio 模板生成的代码与手动修改后的布局不匹配导致的。

**修复方案：**
1. **修改布局文件**：在 [activity_main.xml](file:///d:/Android_project/SunnyWeather/app/src/main/res/layout/activity_main.xml) 的根布局 `FrameLayout` 中添加 `android:id="@+id/main"`。

这样 `MainActivity.kt` 就能正确找到该视图并应用沉浸式状态栏（Edge-to-Edge）的相关设置。

**具体步骤：**
- 打开 `app/src/main/res/layout/activity_main.xml`。
- 为根节点 `FrameLayout` 添加属性 `android:id="@+id/main"`。
- 重新编译项目。