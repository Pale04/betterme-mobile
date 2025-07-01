package com.betterdevs.betterme.data.repository

import android.content.Context
import com.betterdevs.betterme.data.data_source.UsersDataSource
import com.betterdevs.betterme.data.dto.UpdateUserEmailDTO
import com.betterdevs.betterme.data.shared.UserSession
import com.betterdevs.betterme.data.shared.toDomain
import com.betterdevs.betterme.data.shared.toCreateAccountDto
import com.betterdevs.betterme.data.shared.toUpdateAccountDto
import com.betterdevs.betterme.domain_model.Response
import com.betterdevs.betterme.domain_model.User

class UsersRepository(val context: Context, val dataSource: UsersDataSource = UsersDataSource(context)) {
    suspend fun getUser(userId: String): Response<User> {
        val result = dataSource.getUser(userId)
        return Response(result.success, result.message, result.data?.toDomain())
    }

    suspend fun addUser(user: User): Response<User> {
        val result = dataSource.addUser(user.toCreateAccountDto())
        return Response(result.success, result.message, result.data?.toDomain())
    }

    suspend fun updateUser(user: User): Response<User> {
        val result = dataSource.updateUser(UserSession.getId(), user.toUpdateAccountDto())
        return Response(result.success, result.message, result.data?.toDomain())
    }

    suspend fun updateEmail(code: String, newEmail: String) =
        dataSource.updateUserEmail(UserSession.getId(), UpdateUserEmailDTO(verificationCode = code, newEmail = newEmail))
}