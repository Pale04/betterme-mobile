package com.betterdevs.betterme.data.repository

import android.content.Context
import com.betterdevs.betterme.data.data_source.PostDataSource
import com.betterdevs.betterme.data.data_source.UsersDataSource
import com.betterdevs.betterme.data.shared.toDomain
import com.betterdevs.betterme.data.shared.toProto
import com.betterdevs.betterme.data.shared.value
import com.betterdevs.betterme.domain_model.Response
import com.betterdevs.betterme.domain_model.Post
import com.betterdevs.betterme.domain_model.PostCategory

class PostRepository(val context: Context) {
    private val postsDataSource: PostDataSource = PostDataSource(context)
    private val usersDataSource: UsersDataSource = UsersDataSource(context)

    suspend fun getPosts(category: PostCategory): Response<List<Post>> {
        val postsListResult = postsDataSource.getPosts(category.value())
        val result: Response<List<Post>>

        if (postsListResult.success) {
            val postsWithAuthors = mutableListOf<Post>()
            for (post in postsListResult.data!!) {
                val author = usersDataSource.getUser(post.userId)
                postsWithAuthors.add(post.toDomain(author.data))
            }
            result = Response(true, postsListResult.message, postsWithAuthors)
        } else {
            result = Response(false, postsListResult.message)
        }

        return result
    }

    suspend fun getPostMultimedia(postId: String): Response<ByteArray> {
        val result = postsDataSource.getPostMultimedia(postId)
        return Response(result.success, result.message, result.data)
    }

    suspend fun createPost(post: Post): Response<Post> {
        val result = postsDataSource.createPost(post.toProto(), post.multimediaUri)
        return Response(result.success, result.message, result.data?.toDomain())
    }
}