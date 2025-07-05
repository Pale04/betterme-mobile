package com.betterdevs.betterme.data.shared

import MultimediaService.Multimedia
import com.betterdevs.betterme.data.dto.AccountCredentialsDTO
import com.betterdevs.betterme.data.dto.CreateAccountBodyDTO
import com.betterdevs.betterme.data.dto.PostDTO
import com.betterdevs.betterme.data.dto.ReportDTO
import com.betterdevs.betterme.data.dto.StatisticDTO
import com.betterdevs.betterme.data.dto.UpdateUserBodyDTO
import com.betterdevs.betterme.data.dto.UserDTO
import com.betterdevs.betterme.domain_model.Account
import com.betterdevs.betterme.domain_model.Post
import com.betterdevs.betterme.domain_model.PostCategory
import com.betterdevs.betterme.domain_model.PostStatus
import com.betterdevs.betterme.domain_model.Report
import com.betterdevs.betterme.domain_model.Statistic
import com.betterdevs.betterme.domain_model.User
import com.betterdevs.betterme.domain_model.UserRole
import com.google.protobuf.Timestamp
import java.time.Instant
import java.time.ZoneId

fun Account.toCreateAccountDto(): AccountCredentialsDTO {
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

fun Statistic.toCreateAccountDto(): StatisticDTO {
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
    val status = when (this.status) {
        PostStatus.PUBLISHED -> "Published"
        PostStatus.REPORTED -> "Reported"
        PostStatus.DELETED -> "Deleted"
    }
    return Multimedia.Post.newBuilder()
        .setTitle(this.title)
        .setDescription(this.description)
        .setCategory(this.category.value())
        .setUserId(this.userId)
        .setTimeStamp(Timestamp.newBuilder().setSeconds(this.timeStamp.epochSecond))
        .setStatus(status)
        .build()
}

fun Multimedia.Post.toDomain(): Post {
    val category = when (this.category) {
        "Alimentación" -> PostCategory.EATING
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

fun User.toCreateAccountDto(): CreateAccountBodyDTO {
    return CreateAccountBodyDTO(
        username = this.account.username,
        password = this.account.password,
        email = this.email,
        name = this.name,
        birthday = this.birthday,
        description = this.description,
        phone = this.phone,
        website = this.website
    )
}

fun User.toUpdateAccountDto(): UpdateUserBodyDTO {
    return UpdateUserBodyDTO(
        username = this.account.username,
        email = this.email,
        name = this.name,
        active = this.active,
        userType = if (this.role == UserRole.MEMBER) "Member" else "Moderator",
        birthday = this.birthday,
        description = this.description,
        phone = this.phone,
        website = this.website
    )
}

fun UserDTO.toDomain(): User {
    return User(
        id = this._id,
        account = Account(
            username = this.account.username,
            password = ""
        ),
        email = this.account.email,
        name = this.account.name,
        birthday = this.birthday,
        description = this.description,
        phone = this.phone,
        website = this.website,
        verified = this.verified,
        active = this.account.active,
        role = if (this.account.userType.equals("Member", true)) UserRole.MEMBER else UserRole.MODERATOR
    )
}

fun PostCategory.value(): String {
    return when (this) {
        PostCategory.EATING -> "Alimentación"
        PostCategory.HEALTH -> "Salud"
        PostCategory.MEDICINE -> "Medicina"
        PostCategory.EXERCISE -> "Ejercicio"
    }
}

fun PostDTO.toDomain(author: UserDTO? = null): Post {
    return Post(
        id = this.id,
        title = this.title,
        description = this.description,
        category = when (this.category) {
            "Alimentación" -> PostCategory.EATING
            "Salud" -> PostCategory.HEALTH
            "Medicina" -> PostCategory.MEDICINE
            else -> PostCategory.EXERCISE
        },
        userId = this.userId,
        timeStamp = this.timeStamp,
        status = when (this.status) {
            "Published" -> PostStatus.PUBLISHED
            "Reported" -> PostStatus.REPORTED
            else -> PostStatus.DELETED
        },
        authorUsername = author?.account?.username ?: "",
        authorIsVerified = author?.verified ?: false
    )
}

fun Report.toDto(): ReportDTO {
    return ReportDTO(
        postId = this.postId,
        reason = this.reason
    )
}

fun ReportDTO.toDomain(): Report {
    return Report(
        postId = this.postId,
        reason = this.reason
    )
}