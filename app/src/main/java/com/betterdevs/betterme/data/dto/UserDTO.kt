package com.betterdevs.betterme.data.dto

import java.time.Instant

class UserDTO (
    val _id: String,
    val account: AccountDTO,
    val birthday: Instant,
    val description: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val verified: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant
)