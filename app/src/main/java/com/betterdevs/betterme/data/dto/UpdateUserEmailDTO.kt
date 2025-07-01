package com.betterdevs.betterme.data.dto

class UpdateUserEmailDTO (
    val verificationCode: String = "",
    val newEmail: String = "",
    val msg: String = ""
)