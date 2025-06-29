package com.betterdevs.betterme.data.service

import com.betterdevs.betterme.data.dto.AccountCredentialsDTO
import com.betterdevs.betterme.data.dto.LoginResponseDTO
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import com.betterdevs.betterme.BuildConfig
import okhttp3.HttpUrl

interface LoginService {
    @POST("api/authentication/login")
    suspend fun login(@Body request: AccountCredentialsDTO): Response<LoginResponseDTO>

    companion object {
        fun create(): LoginService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            val baseUrl: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(BuildConfig.SERVER_BASE_IP)
                .port(BuildConfig.AUTHENTICATION_API_PORT)
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LoginService::class.java)
        }
    }
}