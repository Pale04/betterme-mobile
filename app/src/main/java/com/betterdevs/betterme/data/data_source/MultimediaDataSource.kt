package com.betterdevs.betterme.data.data_source

import MultimediaService.Multimedia.FileChunk
import MultimediaService.Multimedia.Post
import MultimediaService.Multimedia.PostInfo
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
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
import java.io.IOException
import java.io.InputStream

class MultimediaDataSource(val context: Context) {
    private val multimediaService = MultimediaService()

    suspend fun createPost(post: Post, multimediaUri: Uri? = null): Response<Post> {
        val response: Post

        try {
            response = multimediaService.createPost(post)
            if (multimediaUri != null) {
                uploadPostMultimedia(response.id, multimediaUri)
            }
        } catch (error: StatusException) {
            error.printStackTrace()
            return Response(false, "Error al crear el post")
        } catch (error: IOException) {
            error.printStackTrace()
            return Response(false, "No es posible acceder al archivo adjunto")
        }

        return Response(true, "Post created successfully", response)
    }

    private suspend fun uploadPostMultimedia(postId: String, multimediaUri: Uri): Response<PostInfo> {
        val fileExtension = getFileExtension(multimediaUri)
        if (fileExtension.isEmpty()) return Response(false, "No es posible acceder al archivo adjunto")

        val response: PostInfo
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(multimediaUri)
            if (inputStream == null) return Response(false, "No es posible acceder al archivo adjunto")
            val flow: Flow<FileChunk> = generateMultimediaChunks(inputStream, postId, fileExtension)
            response = multimediaService.uploadPostMultimedia(flow)
        } catch (error: FileNotFoundException) {
            error.printStackTrace()
            return Response(false, "No es posible acceder al archivo adjunto")
        } catch (error: StatusException) {
            error.printStackTrace()
            return Response(false, "Error al subir la multimedia")
        } finally {
            withContext(Dispatchers.IO) {
                inputStream?.close()
            }
        }

        return Response(true, "Multimedia uploaded successfully", response)
    }

    private fun generateMultimediaChunks(inputStream: InputStream, postId: String, fileExtension: String): Flow<FileChunk> = flow {
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            emit(
                FileChunk
                    .newBuilder()
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