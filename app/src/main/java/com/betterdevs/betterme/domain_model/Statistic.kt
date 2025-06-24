package com.betterdevs.betterme.domain_model

import com.betterdevs.betterme.data.shared.UserSession
import java.time.LocalDate

class Statistic (
    val id: String = "",
    val userId: String = UserSession.getId(),

    val arms: Double? = null,
    val waist: Double? = null,
    val weight: Double? = null,

    val waterIntake: Int? = null,
    val mood: Int? = null,
    val sleepHours: Int? = null,

    val date: LocalDate = LocalDate.now()
)
