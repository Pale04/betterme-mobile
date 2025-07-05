package com.betterdevs.betterme.domain_model

import android.net.Uri
import com.betterdevs.betterme.data.shared.UserSession
import java.time.Instant

class Post (
    val id: String = "",
    val title: String,
    val description: String = "",
    val category: PostCategory,
    val userId: String = UserSession.getId(),
    val timeStamp: Instant = Instant.now(),
    val status: PostStatus = PostStatus.PUBLISHED,
    val multimediaUri: Uri? = null,

    val authorUsername: String = "",
    val authorIsVerified: Boolean = false,
    var multimedia: ByteArray? = null
)