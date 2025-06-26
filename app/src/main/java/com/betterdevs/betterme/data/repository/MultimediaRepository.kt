package com.betterdevs.betterme.data.repository

import android.content.Context
import com.betterdevs.betterme.data.data_source.MultimediaDataSource
import com.betterdevs.betterme.data.shared.toDomain
import com.betterdevs.betterme.data.shared.toProto
import com.betterdevs.betterme.domain_model.Response
import com.betterdevs.betterme.domain_model.Post

class MultimediaRepository(val context: Context, val dataSource: MultimediaDataSource = MultimediaDataSource(context)) {
    suspend fun createPost(post: Post): Response<Post> {
        val result = dataSource.createPost(post.toProto(), post.multimediaUri)
        return Response(result.success, result.message, result.data?.toDomain())
    }
}