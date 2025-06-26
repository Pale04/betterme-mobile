package com.betterdevs.betterme.data.shared

import MultimediaService.Multimedia
import com.betterdevs.betterme.data.dto.AccountCredentialsDTO
import com.betterdevs.betterme.data.dto.StatisticDTO
import com.betterdevs.betterme.domain_model.Account
import com.betterdevs.betterme.domain_model.Post
import com.betterdevs.betterme.domain_model.PostCategory
import com.betterdevs.betterme.domain_model.PostStatus
import com.betterdevs.betterme.domain_model.Statistic
import com.google.protobuf.Timestamp
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

fun Post.toProto(): Multimedia.Post {
    val category = when (this.category) {
        PostCategory.EATiNG -> "Alimentacion"
        PostCategory.HEALTH -> "Salud"
        PostCategory.MEDICINE -> "Medicina"
        PostCategory.EXERCISE -> "Ejercicio"
    }
    val status = when (this.status) {
        PostStatus.PUBLISHED -> "Published"
        PostStatus.REPORTED -> "Reported"
        PostStatus.DELETED -> "Deleted"
    }
    return Multimedia.Post.newBuilder()
        .setTitle(this.title)
        .setDescription(this.description)
        .setCategory(category)
        .setUserId(this.userId)
        .setTimeStamp(Timestamp.newBuilder().setSeconds(this.timeStamp.epochSecond))
        .setStatus(status)
        .build()
}

fun Multimedia.Post.toDomain(): Post {
    val category = when (this.category) {
        "Alimentacion" -> PostCategory.EATiNG
        "Salud" -> PostCategory.HEALTH
        "Medicina" -> PostCategory.MEDICINE
        else -> PostCategory.EXERCISE
    }
    val status = when (this.status) {
        "Published" -> PostStatus.PUBLISHED
        "Reported" -> PostStatus.REPORTED
        else -> PostStatus.DELETED
    }

    return Post(
        id = this.id,
        title = this.title,
        description = this.description,
        category = category,
        userId = this.userId,
        timeStamp = Instant.ofEpochSecond(this.timeStamp.seconds),
        status = status
    )
}