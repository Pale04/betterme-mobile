package com.betterdevs.betterme.data.data_source

import android.content.Context
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.dto.EmailServiceRequestDTO
import com.betterdevs.betterme.data.service.EmailService
import com.betterdevs.betterme.domain_model.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class EmailDataSource(val context: Context) {
    private val emailService = EmailService.create(context)

    suspend fun sendCodeForEmailUpdate(email: String): Response<String> {
        val response: retrofit2.Response<EmailServiceRequestDTO>

        try {
            response = emailService.sendCodeForEmailUpdate(EmailServiceRequestDTO(email))
        } catch (error: UnknownHostException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_no_internet_error))
        } catch (error: SocketTimeoutException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_no_conection_error_text))
        } catch (error: ConnectException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_no_conection_error_text))
        } catch (error: Exception) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_server_error))
        }

        return when (response.code()) {
            200 -> Response(true, context.getString(R.string.profile_edit_email_code_sent), response.body()?.email)
            else -> Response(false, context.getString(R.string.general_server_error))
        }
    }
}