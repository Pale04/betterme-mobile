package com.betterdevs.betterme.data.repository

import android.content.Context
import com.betterdevs.betterme.data.data_source.StatisticsDataSource
import com.betterdevs.betterme.data.shared.UserSession
import com.betterdevs.betterme.data.shared.toDomain
import com.betterdevs.betterme.data.shared.toDto
import com.betterdevs.betterme.domain_model.Response
import com.betterdevs.betterme.domain_model.Statistic
import com.betterdevs.betterme.domain_model.StatisticCategory

class StatisticsRepository (val context: Context, val dataSource: StatisticsDataSource = StatisticsDataSource(context)) {
    suspend fun getStatistics(): Response<List<Statistic>> {
        val result = dataSource.getStatistics(UserSession.getId())
        return Response(result.success, result.message, result.data?.map { it.toDomain() })
    }

    suspend fun saveStatistic(category: StatisticCategory, value: Double): Response<Unit> {
        val statistic = when (category) {
            StatisticCategory.ARMS -> Statistic(arms = value)
            StatisticCategory.WAIST -> Statistic(waist = value)
            StatisticCategory.WEIGHT -> Statistic(weight = value)
            StatisticCategory.MOOD -> Statistic(mood = value.toInt())
            StatisticCategory.SLEEP_HOURS -> Statistic(sleepHours = value.toInt())
            StatisticCategory.WATER_INTAKE -> Statistic(waterIntake = value.toInt())
        }

        val result = dataSource.saveStatistic(statistic.toDto())
        return Response(result.success, result.message, result.data)
    }
}