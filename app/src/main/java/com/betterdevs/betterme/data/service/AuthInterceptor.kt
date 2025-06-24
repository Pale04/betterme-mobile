package com.betterdevs.betterme.data.service

import android.content.Context
import android.content.Intent
import com.betterdevs.betterme.MainActivity
import com.betterdevs.betterme.data.shared.SharedPreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SharedPreferencesHelper.getAuthToken(context) ?: run {
            context.startActivity(Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            throw IOException("No auth token available")
        }

        val newRequest = chain.request()
            .newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}