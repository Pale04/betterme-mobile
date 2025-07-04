package com.betterdevs.betterme.data.data_source

import android.content.Context
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.dto.ReportDTO
import com.betterdevs.betterme.data.service.ReportsService
import com.betterdevs.betterme.domain_model.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ReportsDataSource(val context: Context) {
    private val reportsService = ReportsService.create(context)

    suspend fun reportPost(report: ReportDTO): Response<ReportDTO> {
        val response: retrofit2.Response<ReportDTO>
        try {
            response = reportsService.reportPost(report)
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
            201 -> Response(true, "El reporte se ha enviado correctamente", response.body())
            else -> Response(false, context.getString(R.string.general_server_error))
        }

    }
}