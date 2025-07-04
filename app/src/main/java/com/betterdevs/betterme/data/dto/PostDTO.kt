package com.betterdevs.betterme.data.dto

import com.betterdevs.betterme.data.shared.UserSession
import java.time.Instant

class PostDTO (
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val userId: String = UserSession.getId(),
    val timeStamp: Instant = Instant.now(),
    val status: String = "",
    val multimediaExtension: String = "",
    val mediaPath: String? = null
)