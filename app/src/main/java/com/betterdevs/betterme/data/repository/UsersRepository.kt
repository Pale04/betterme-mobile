package com.betterdevs.betterme.data.repository

import android.content.Context
import com.betterdevs.betterme.data.data_source.UsersDataSource
import com.betterdevs.betterme.data.shared.toDomain
import com.betterdevs.betterme.data.shared.toDto
import com.betterdevs.betterme.domain_model.Response
import com.betterdevs.betterme.domain_model.User

class UsersRepository(val context: Context, val dataSource: UsersDataSource = UsersDataSource(context)) {
    suspend fun addUser(user: User): Response<User> {
        val result = dataSource.addUser(user.toDto())
        return Response(result.success, result.message, result.data?.toDomain())
    }
}