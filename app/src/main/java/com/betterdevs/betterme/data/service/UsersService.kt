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
import com.betterdevs.betterme.data.dto.UserDTO
import com.google.gson.GsonBuilder
import retrofit2.http.Path
import java.time.Instant
import com.google.gson.JsonDeserializer

interface UsersService {
    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: String): Response<UserDTO>

    companion object {
        fun create(context: Context): UsersService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(AuthInterceptor(context))
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.USERS_API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(Instant::class.java, JsonDeserializer { json, _, _ ->
                            Instant.parse(json.asString)
                        })
                        .create()
                ))
                .build()
                .create(UsersService::class.java)
        }
    }
}