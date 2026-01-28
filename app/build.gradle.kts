plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    buildFeatures{
        viewBinding = true
    }

    namespace = "com.example.sunnyweather"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.sunnyweather"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    //项目需要的依赖库
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")
    // implementation("com.google.android.material:material:1.13.0") // 移除重复项，下方 libs.material 已包含
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.6.1")
    implementation("com.squareup.retrofit2:converter-gson:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // 升级以兼容 Kotlin 2.0
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // 升级以兼容 Kotlin 2.0

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.places)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}