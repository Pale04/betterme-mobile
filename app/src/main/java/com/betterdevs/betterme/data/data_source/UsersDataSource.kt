package com.betterdevs.betterme.data.data_source

import android.content.Context
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.dto.CreateAccountBodyDTO
import com.betterdevs.betterme.data.dto.CreateAccountResponseDTO
import com.betterdevs.betterme.data.dto.UpdateUserBodyDTO
import com.betterdevs.betterme.data.dto.UpdateUserEmailDTO
import com.betterdevs.betterme.data.dto.UserDTO
import com.betterdevs.betterme.data.service.UsersService
import com.betterdevs.betterme.domain_model.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class UsersDataSource(val context: Context) {
    private val usersService = UsersService.create(context)

    suspend fun getUser(id: String): Response<UserDTO> {
        val response: retrofit2.Response<UserDTO>

        try {
            response = usersService.getUser(id)
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
            200 -> Response(true, "User retrieved successfully", response.body())
            401 -> Response(false, context.getString(R.string.general_expired_token_error))
            404 -> Response(false, context.getString(R.string.profile_user_not_found))
            else -> Response(false, context.getString(R.string.general_server_error))
        }
    }

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

    suspend fun updateUser(id: String, user: UpdateUserBodyDTO): Response<UserDTO> {
        val response: retrofit2.Response<CreateAccountResponseDTO>

        try {
            response = usersService.updateUser(id, user)
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
            200 -> Response(true, context.getString(R.string.profile_edit_update_successful), response.body()?.user)
            401 -> Response(false, context.getString(R.string.general_expired_token_error))
            else -> Response(false, context.getString(R.string.general_server_error))
        }
    }

    suspend fun updateUserEmail(id: String, request: UpdateUserEmailDTO): Response<String> {
        val response: retrofit2.Response<UpdateUserEmailDTO>

        try {
            response = usersService.updateUserEmail(id, request)
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
            204 -> Response(true, context.getString(R.string.profile_edit_update_successful), response.body()?.msg)
            400 -> Response(false, context.getString(R.string.profile_edit_email_incorrect_code))
            else -> Response(false, context.getString(R.string.general_server_error))
        }
    }
}