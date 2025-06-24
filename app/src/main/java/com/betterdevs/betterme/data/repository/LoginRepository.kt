package com.betterdevs.betterme.data.repository

import android.content.Context
import com.betterdevs.betterme.data.data_source.LoginDataSource
import com.betterdevs.betterme.data.shared.toDto
import com.betterdevs.betterme.domain_model.Account
import com.betterdevs.betterme.domain_model.Response
import org.json.JSONObject
import android.util.Base64
import com.betterdevs.betterme.data.shared.UserSession
import java.nio.charset.StandardCharsets

class LoginRepository (val context: Context, val dataSource: LoginDataSource = LoginDataSource(context)) {
    suspend fun login(account: Account): Response<String> {
        val result = dataSource.login(account.toDto())
        if (result.success) configUserSession(result.data?.accessToken ?: "")
        return Response(result.success, result.message, result.data?.accessToken ?: "")
    }

    private fun configUserSession(token: String) {
        val payload = token.split(".")[1]
        val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val jsonString = String(decoded, StandardCharsets.UTF_8)
        val json = JSONObject(jsonString)

        UserSession.setSession(json.getString("id"), json.getString("username"), json.getString("role"))
    }

}