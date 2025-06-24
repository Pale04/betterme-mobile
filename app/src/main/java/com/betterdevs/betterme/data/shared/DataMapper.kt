package com.betterdevs.betterme.data.shared

import com.betterdevs.betterme.data.dto.AccountCredentialsDTO
import com.betterdevs.betterme.data.dto.StatisticDTO
import com.betterdevs.betterme.domain_model.Account
import com.betterdevs.betterme.domain_model.Statistic
import java.time.Instant
import java.time.ZoneId

fun Account.toDto(): AccountCredentialsDTO {
    return AccountCredentialsDTO(
        username = this.username,
        password = this.password
    )
}

fun StatisticDTO.toDomain(): Statistic {
    return Statistic(
        id = this.id,
        userId = this.userId,
        date = this.date.atZone(ZoneId.systemDefault()).toLocalDate(),
        arms = this.arms,
        mood = this.mood,
        sleepHours = this.sleepHours,
        waist = this.waist,
        weight = this.weight,
        waterIntake = this.waterIntake
    )
}

fun Statistic.toDto(): StatisticDTO {
    return StatisticDTO(
        id = this.id,
        userId = this.userId,
        arms = this.arms,
        mood = this.mood,
        sleepHours = this.sleepHours,
        waist = this.waist,
        weight = this.weight,
        waterIntake = this.waterIntake,
        date = this.date.atStartOfDay(ZoneId.systemDefault()).toInstant()
    )
}

