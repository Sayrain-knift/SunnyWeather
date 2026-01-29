package com.example.sunnyweather.logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    private const val TAG = "SunnyWeatherNetwork"
    private const val MIN_INTERVAL_MS = 1500L
    private val rateMutex = Mutex()
    private var lastRequestAt = 0L
    private val placeService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String) = rateLimitAndCall {
        placeService.searchPlaces(query).await()
    }

    private val weatherService = ServiceCreator.create<WeatherService>()

    suspend fun getDailyWeather(lng:String,lat:String) = rateLimitAndCall {
        weatherService.getDailyWeather(
            SunnyWeatherApplication.TOKEN.trim(),
            "${lng.trim()},${lat.trim()}"
        ).await()
    }

    suspend fun getRealtimeWeather(lng: String,lat: String) = rateLimitAndCall {
        weatherService.getRealtimeWeather(
            SunnyWeatherApplication.TOKEN.trim(),
            "${lng.trim()},${lat.trim()}"
        ).await()
    }

    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {

                override fun onResponse(
                    call: Call<T?>,
                    response: Response<T?>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) continuation.resume(body)
                        else continuation.resumeWithException(
                            RuntimeException("response body is null")
                        )
                    } else {
                        val errorDetail = response.errorBody()?.string() ?: "no error detail"
                        val rawUrl = call.request().url().toString()
                        val safeUrl = rawUrl.replace(SunnyWeatherApplication.TOKEN.trim(), "***")
                        Log.e(TAG, "HTTP ${response.code()} ${response.message()} url=$safeUrl body=$errorDetail")
                        continuation.resumeWithException(
                            RuntimeException("request failed with status: ${response.code()} ${response.message()}. Info: $errorDetail")
                        )
                    }
                }

                override fun onFailure(call: Call<T?>, t: Throwable) {
                    val rawUrl = call.request().url().toString()
                    val safeUrl = rawUrl.replace(SunnyWeatherApplication.TOKEN.trim(), "***")
                    Log.e(TAG, "request failed: ${t.message} url=$safeUrl", t)
                    continuation.resumeWithException(t)
                }

            })
        }
    }

    private suspend fun <T> rateLimitAndCall(block: suspend () -> T): T {
        return rateMutex.withLock {
            val now = System.currentTimeMillis()
            val waitMs = MIN_INTERVAL_MS - (now - lastRequestAt)
            if (waitMs > 0) delay(waitMs)
            val result = block()
            lastRequestAt = System.currentTimeMillis()
            result
        }
    }
}
