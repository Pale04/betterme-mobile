package com.betterdevs.betterme.data.repository

import android.content.Context
import com.betterdevs.betterme.data.data_source.PostDataSource
import com.betterdevs.betterme.data.shared.toDomain
import com.betterdevs.betterme.data.shared.toProto
import com.betterdevs.betterme.domain_model.Response
import com.betterdevs.betterme.domain_model.Post

class PostRepository(val context: Context, val dataSource: PostDataSource = PostDataSource(context)) {
    suspend fun createPost(post: Post): Response<Post> {
        val result = dataSource.createPost(post.toProto(), post.multimediaUri)
        return Response(result.success, result.message, result.data?.toDomain())
    }
}