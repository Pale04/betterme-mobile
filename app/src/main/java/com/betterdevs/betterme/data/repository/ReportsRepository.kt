package com.betterdevs.betterme.data.repository

import android.content.Context
import com.betterdevs.betterme.data.data_source.ReportsDataSource
import com.betterdevs.betterme.data.shared.toDomain
import com.betterdevs.betterme.data.shared.toDto
import com.betterdevs.betterme.domain_model.Report
import com.betterdevs.betterme.domain_model.Response

class ReportsRepository(val context: Context) {
    private val reportsDataSource: ReportsDataSource = ReportsDataSource(context)

    suspend fun reportPost(report: Report): Response<Report> {
        val result = reportsDataSource.reportPost(report.toDto())
        return Response(result.success, result.message, result.data?.toDomain())
    }
}