package com.betterdevs.betterme.data.data_source

import android.content.Context
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.dto.CreateAccountBodyDTO
import com.betterdevs.betterme.data.dto.CreateAccountResponseDTO
import com.betterdevs.betterme.data.dto.UserDTO
import com.betterdevs.betterme.data.service.UsersService
import com.betterdevs.betterme.domain_model.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class UsersDataSource(val context: Context) {
    private val usersService = UsersService.create(context)

    suspend fun addUser(user: CreateAccountBodyDTO): Response<UserDTO> {
        val response: retrofit2.Response<CreateAccountResponseDTO>

        try {
            response = usersService.addUser(user)
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
            201 -> Response(true, context.getString(R.string.create_account_created_successfully), response.body()?.user)
            401 -> Response(false, context.getString(R.string.general_expired_token_error))
            else -> Response(false, context.getString(R.string.general_server_error))
        }
    }
}