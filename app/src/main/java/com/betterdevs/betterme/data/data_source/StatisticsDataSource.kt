package com.betterdevs.betterme.data.data_source

import android.content.Context
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.dto.StatisticDTO
import com.betterdevs.betterme.data.service.StatisticsService
import com.betterdevs.betterme.domain_model.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class StatisticsDataSource(val context: Context) {
    private val statisticsService = StatisticsService.create(context)

    suspend fun getStatistics(userId: String): Response<List<StatisticDTO>> {
        val response: retrofit2.Response<List<StatisticDTO>>

        try {
            response = statisticsService.getStatistics(userId)
        } catch (error: UnknownHostException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_no_internet_error))
        } catch (error: SocketTimeoutException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_no_conection_error_text))
        } catch (error: ConnectException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_no_conection_error_text))
        } catch (error: Exception) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_server_error))
        }

        return when (response.code()) {
            200 -> Response(true, "Statistics retrieved successfully", response.body())
            404 -> Response(false, context.getString(R.string.general_resource_not_found_text))
            else -> Response(false, context.getString(R.string.general_server_error))
        }
    }

    suspend fun saveStatistic(statistic: StatisticDTO): Response<Unit> {
        val response: retrofit2.Response<Unit>

        try {
            response = statisticsService.saveStatistic(statistic)
        } catch (error: UnknownHostException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_no_internet_error))
        } catch (error: SocketTimeoutException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_no_conection_error_text))
        } catch (error: ConnectException) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_no_conection_error_text))
        } catch (error: Exception) {
            error.printStackTrace()
            return Response(false, context.getString(R.string.general_server_error))
        }

        return when (response.code()) {
            201 -> Response(true, "Statistic saved successfully")
            404 -> Response(false, context.getString(R.string.general_resource_not_found_text))
            else -> Response(false, context.getString(R.string.general_server_error))
        }
    }

}