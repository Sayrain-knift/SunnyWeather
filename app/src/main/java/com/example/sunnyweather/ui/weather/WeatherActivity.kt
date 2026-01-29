package com.example.sunnyweather.ui.weather

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.ActivityWeatherBinding
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import java.util.Locale
import java.util.Date

class WeatherActivity : AppCompatActivity() {
    lateinit var wBinding: ActivityWeatherBinding

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //将背景图和状态栏融合
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE


        wBinding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(wBinding.root)

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
             val weather = result.getOrNull()
             if (weather != null) {
                 showWeatherInfo(weather)
             } else {
                 val exception = result.exceptionOrNull()
                 val msg = if (exception?.message?.contains("429") == true) {
                     "请求过于频繁，请稍后再试"
                 } else {
                     "无法成功获取天气信息: ${exception?.message}"
                 }
                 Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                 exception?.printStackTrace()
             }
             wBinding.swipeRefresh.isRefreshing = false
         })
        wBinding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        wBinding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        wBinding.includNow.navBtn.setOnClickListener {
            wBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
        wBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        wBinding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        wBinding.swipeRefresh.isRefreshing = false
        wBinding.includNow.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        //填充now.xml布局中的数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        wBinding.includNow.currentTemp.text = currentTempText
        wBinding.includNow.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        wBinding.includNow.currentAQI.text = currentPM25Text
        wBinding.includNow.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        // 填充forecast.xml布局中的数据
        wBinding.includForecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this)
                .inflate(R.layout.forecast_item, wBinding.includForecast.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            // 彩云 API 返回的日期格式可能带 T 和时区，这里做简单的截取或解析
            val date = try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                sdf.parse(skycon.date) ?: Date()
            } catch (e: Exception) {
                Date()
            }
            dateInfo.text = simpleDateFormat.format(date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            wBinding.includForecast.forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        if (lifeIndex.coldRisk.isNotEmpty()) wBinding.includLifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        if (lifeIndex.dressing.isNotEmpty()) wBinding.includLifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        if (lifeIndex.ultraviolet.isNotEmpty()) wBinding.includLifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        if (lifeIndex.carWashing.isNotEmpty()) wBinding.includLifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        wBinding.weatherLayout.visibility = View.VISIBLE
    }
}
