package com.betterdevs.betterme.data.dto

import java.time.Instant

class StatisticDTO (
    val id: String,
    val userId: String,

    val arms: Double? = null,
    val mood: Int? = null,
    val sleepHours: Int? = null,
    val waist: Double? = null,
    val weight: Double? = null,
    val waterIntake: Int? = null,

    val date: Instant
)