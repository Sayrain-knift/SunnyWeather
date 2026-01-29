package com.example.sunnyweather.logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.DailyResponse
import com.example.sunnyweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.5/{token}/{location}/realtime.json")
    fun getRealtimeWeather(@Path("token") token: String, @Path(value = "location", encoded = true) location: String): Call<RealtimeResponse>

    @GET("v2.5/{token}/{location}/daily.json")
    fun getDailyWeather(@Path("token") token: String, @Path(value = "location", encoded = true) location: String): Call<DailyResponse>
}