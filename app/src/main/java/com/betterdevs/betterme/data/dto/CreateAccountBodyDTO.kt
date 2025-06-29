package com.betterdevs.betterme.data.dto

import java.time.Instant

class CreateAccountBodyDTO (
    val username: String,
    val password: String,
    val email: String,
    val name: String,
    val birthday: Instant,
    val description: String? = null,
    val phone: String? = null,
    val website: String? = null,
)