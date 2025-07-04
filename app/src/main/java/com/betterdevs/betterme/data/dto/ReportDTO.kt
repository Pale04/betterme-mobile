package com.betterdevs.betterme.data.dto

import java.time.Instant

class ReportDTO (
    val id: String? = null,
    val postId: String,
    val reportDate: Instant? = null,
    val reason: String,
    val evaluated: Boolean? = null
)