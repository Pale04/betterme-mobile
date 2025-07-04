package com.betterdevs.betterme.data.service

import android.content.Context
import com.betterdevs.betterme.BuildConfig
import com.betterdevs.betterme.data.dto.PostDTO
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.Instant

interface PostsService {
    @GET("/posts")
    suspend fun getPosts(@Query("category") category: String): Response<List<PostDTO>>

    companion object {
        fun create(context: Context): PostsService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(AuthInterceptor(context))
                .build()

            val baseUrl: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(BuildConfig.SERVER_BASE_IP)
                .port(BuildConfig.POSTS_API_PORT)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(
                    GsonConverterFactory.create(
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
                .create(PostsService::class.java)
        }
    }
}