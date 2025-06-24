package com.betterdevs.betterme.data.dto

import java.time.Instant

class UserDTO (
    val _id: String,
    val account: AccountDTO,
    val birhday: Instant,
    val description: String,
    val phone: String,
    val website: String,
    val verified: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant
)