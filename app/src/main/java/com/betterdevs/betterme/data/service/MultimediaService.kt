package com.betterdevs.betterme.data.service

import MultimediaService.Multimedia.FileChunk
import MultimediaService.Multimedia.Post
import MultimediaService.Multimedia.PostInfo
import MultimediaService.MultimediaServiceGrpcKt.MultimediaServiceCoroutineStub
import com.betterdevs.betterme.BuildConfig
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.Flow

interface IMultimediaService {
    suspend fun getPostMultimedia(postInfo: PostInfo): Flow<FileChunk>
    suspend fun createPost(post: Post): Post
    suspend fun uploadPostMultimedia(multimedia: Flow<FileChunk>): PostInfo
}

class MultimediaService : IMultimediaService {
    private val channel: ManagedChannel = ManagedChannelBuilder
        .forAddress(BuildConfig.SERVER_BASE_IP, BuildConfig.MULTIMEDIA_API_PORT)
        .usePlaintext()
        .build()

    private val stub = MultimediaServiceCoroutineStub(channel).withWaitForReady()

    override suspend fun getPostMultimedia(postInfo: PostInfo): Flow<FileChunk> = stub.getPostMultimedia(postInfo)

    override suspend fun createPost(post: Post): Post = stub.createPost(post)

    override suspend fun uploadPostMultimedia(multimedia: Flow<FileChunk>): PostInfo = stub.uploadPostMultimedia(multimedia)
}