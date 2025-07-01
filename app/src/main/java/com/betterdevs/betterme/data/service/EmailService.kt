package com.betterdevs.betterme.data.service

import android.content.Context
import com.betterdevs.betterme.BuildConfig
import com.betterdevs.betterme.data.dto.EmailServiceRequestDTO
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface EmailService {
    @POST("/api/verify/existent/initiate")
    suspend fun sendCodeForEmailUpdate(@Body email: EmailServiceRequestDTO): Response<EmailServiceRequestDTO>

    companion object {
        fun create(context: Context): EmailService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(AuthInterceptor(context))
                .build()

            val baseUrl: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(BuildConfig.SERVER_BASE_IP)
                .port(BuildConfig.EMAIL_API_PORT)
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmailService::class.java)
        }
    }
}