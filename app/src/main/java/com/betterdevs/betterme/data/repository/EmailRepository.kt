package com.betterdevs.betterme.data.repository

import android.content.Context
import com.betterdevs.betterme.data.data_source.EmailDataSource
import com.betterdevs.betterme.data.shared.UserSession
import com.betterdevs.betterme.domain_model.Response

class EmailRepository(val context: Context, val dataSource: EmailDataSource = EmailDataSource(context)) {
    suspend fun sendCodeForEmailUpdate(): Response<String> = dataSource.sendCodeForEmailUpdate(UserSession.getEmail())
}