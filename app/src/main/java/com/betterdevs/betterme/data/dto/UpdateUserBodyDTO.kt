package com.betterdevs.betterme.data.dto

import java.time.Instant

class UpdateUserBodyDTO (
    val username: String,
    val email: String,
    val name: String,
    val active: Boolean,
    val userType: String,
    val birthday: Instant,
    val description: String? = null,
    val phone: String? = null,
    val website: String? = null
)