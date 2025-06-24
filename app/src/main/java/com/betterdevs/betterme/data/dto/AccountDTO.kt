package com.betterdevs.betterme.data.dto

import java.time.Instant

class AccountDTO (
    val _id: String,
    val username: String,
    val email: String,
    val name: String,
    val active: Boolean,
    val userType: String,
    val createdAt: Instant,
    val updatedAt: Instant
)