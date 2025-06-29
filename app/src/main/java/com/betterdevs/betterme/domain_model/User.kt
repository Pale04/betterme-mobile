package com.betterdevs.betterme.domain_model

import java.time.Instant

class User (
    val id: String = "",
    val account: Account,
    val email: String,
    val name: String,
    val birthday: Instant,
    val description: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val verified: Boolean = false,
    val active: Boolean = true,
    val role: UserRole = UserRole.MEMBER
)