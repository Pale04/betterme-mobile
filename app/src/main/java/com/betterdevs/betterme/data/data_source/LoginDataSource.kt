package com.betterdevs.betterme.data.data_source

import android.content.Context
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.dto.AccountCredentialsDTO
import com.betterdevs.betterme.data.dto.LoginResponseDTO
import com.betterdevs.betterme.data.service.LoginService
import com.betterdevs.betterme.data.shared.SharedPreferencesHelper
import com.betterdevs.betterme.domain_model.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class LoginDataSource (val context: Context) {
    private val loginService = LoginService.create()

    suspend fun login(account: AccountCredentialsDTO): Response<LoginResponseDTO> {
        val response: retrofit2.Response<LoginResponseDTO>

        try {
            response = loginService.login(account)
        } catch (error: UnknownHostException) {
            println(error.stackTrace)
            return Response(false, context.getString(R.string.general_no_internet_error))
        } catch (error: SocketTimeoutException) {
            println(error.stackTrace)
            return Response(false, context.getString(R.string.general_no_conection_error_text))
        } catch (error: ConnectException) {
            println(error.stackTrace)
            return Response(false, context.getString(R.string.general_no_conection_error_text))
        } catch (error: Exception) {
            println(error.stackTrace)
            return Response(false, context.getString(R.string.general_server_error))
        }

        return when (response.code()) {
            200 -> {
                SharedPreferencesHelper.saveAuthToken(context, response.body()?.accessToken ?: "")
                Response(true, "Login Successful", response.body())
            }
            401 -> Response(false, context.getString(R.string.login_bad_credentials_text))
            else -> Response(false, context.getString(R.string.general_server_error))
        }
    }
}