package com.betterdevs.betterme.data.data_source

import MultimediaService.Multimedia.FileChunk
import MultimediaService.Multimedia.Post
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.service.MultimediaService
import com.betterdevs.betterme.domain_model.Response
import com.google.protobuf.ByteString
import io.grpc.StatusException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.InputStream

class PostDataSource(val context: Context) {
    private val multimediaService = MultimediaService()

    suspend fun createPost(post: Post, multimediaUri: Uri? = null): Response<Post> {
        val response: Response<Post>

        if (multimediaUri == null) {
            val grpcResponse: Post
            try {
                grpcResponse = multimediaService.createPost(post)
            } catch (error: StatusException) {
                error.printStackTrace()
                return Response(false, context.getString(R.string.post_creation_create_post_error))
            }
            response = Response(true, context.getString(R.string.post_creation_created_successfully), grpcResponse)
        } else {
            response = createPostWithMultimedia(post, multimediaUri)
        }

        return response
    }

    private suspend fun createPostWithMultimedia(post: Post, multimediaUri: Uri): Response<Post> {
        val fileExtension = getFileExtension(multimediaUri)
        if (fileExtension.isEmpty()) return Response(false, context.getString(R.string.post_creation_multimedia_access_error))

        val createdPost: Post
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(multimediaUri)
            if (inputStream == null) {
                return Response(false, context.getString(R.string.post_creation_multimedia_access_error))
            }
            createdPost = multimediaService.createPost(post)
            val flow: Flow<FileChunk> = generateMultimediaChunks(inputStream, createdPost.id, fileExtension)
            multimediaService.uploadPostMultimedia(flow)
        } catch (error: FileNotFoundException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.post_creation_multimedia_access_error))
        } catch (error: StatusException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.post_creation_create_post_error))
        } finally {
            withContext(Dispatchers.IO) {
                inputStream?.close()
            }
        }

        return Response(true, context.getString(R.string.post_creation_created_successfully), createdPost)
    }

    private fun generateMultimediaChunks(inputStream: InputStream, postId: String, fileExtension: String): Flow<FileChunk> = flow {
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            emit(
                FileChunk.newBuilder()
                    .setChunk(ByteString.copyFrom(buffer, 0, length))
                    .setExt(fileExtension)
                    .setResourceId(postId)
                    .build()
            )
        }
    }.flowOn(Dispatchers.IO)

    private fun getFileExtension(uri: Uri): String {
        var extension = ""
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    val displayName = cursor.getString(displayNameIndex)
                    if (!displayName.isNullOrEmpty() && displayName.contains(".")) {
                        extension = displayName.substring(displayName.lastIndexOf(".") + 1)
                    }
                }
            }
        }
        return extension
    }
}