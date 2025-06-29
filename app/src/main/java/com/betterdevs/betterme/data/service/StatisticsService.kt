package com.betterdevs.betterme.data.service

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import com.betterdevs.betterme.BuildConfig
import com.betterdevs.betterme.data.dto.StatisticDTO
import com.google.gson.GsonBuilder
import retrofit2.http.Path
import java.time.Instant
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.JsonPrimitive
import okhttp3.HttpUrl
import retrofit2.http.POST

interface StatisticsService {
    @GET("healthstats/{userId}")
    suspend fun getStatistics(@Path("userId") userId: String): Response<List<StatisticDTO>>

    @POST("healthstats")
    suspend fun saveStatistic(@Body statistic: StatisticDTO): Response<Unit>

    companion object {
        fun create(context: Context): StatisticsService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(AuthInterceptor(context))
                .build()

            val baseUrl: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(BuildConfig.SERVER_BASE_IP)
                .port(BuildConfig.HEALTH_STATS_API_PORT)
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(Instant::class.java, JsonDeserializer { json, _, _ ->
                            Instant.parse(json.asString)
                        })
                        .registerTypeAdapter(Instant::class.java, JsonSerializer<Instant> { src, _, _ ->
                            JsonPrimitive(src.toString())
                        })
                        .create()
                ))
                .build()
                .create(StatisticsService::class.java)
        }
    }
}