package com.betterdevs.betterme.ui.statistics

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.betterdevs.betterme.R
import com.betterdevs.betterme.domain_model.StatisticCategory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatisticsScreen(
    category: StatisticCategory,
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = StatisticsViewModel(LocalContext.current, category)
) {
    val state by viewModel.state
    val chartDataState by viewModel.chartDataState

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.getStatistics()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = viewModel.categorySpecification.title,
                style = MaterialTheme.typography.titleLarge,
            )

            if (chartDataState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            } else if (!chartDataState.error) {
                LineChart(
                    points = chartDataState.points,
                    measurementUnit = viewModel.categorySpecification.measurementUnit,
                    ySteps = viewModel.categorySpecification.chartYSteps,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                        .height(350.dp)
                )

                Text(
                    text = stringResource( if(viewModel.categorySpecification.todayTrackingDone) R.string.statistics_todays_tracking_done else R.string.statistics_no_todays_tracking),
                    style = MaterialTheme.typography.titleMedium
                )

                if (!viewModel.categorySpecification.todayTrackingDone) {
                    OutlinedTextField(
                        value = state.measureInput,
                        onValueChange = { viewModel.onMeasureInputChange(it) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text( text = viewModel.categorySpecification.inputPlaceholder ) },
                        label = { Text( text = viewModel.categorySpecification.inputLabel ) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp)
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = !state.inputIsLoading,
                        onClick = { viewModel.onSaveMeasureClick() }
                    ) {
                        if (state.inputIsLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Text(text = stringResource(R.string.statistics_save_measure_button))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LineChart(points: List<Point>, measurementUnit: String, ySteps: Int, modifier: Modifier = Modifier) {
    if (points.isEmpty()) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Aún no registras ningún dato",
                modifier = Modifier.fillMaxWidth()
            )
        }
        return
    }
    
    val xAxisData = AxisData.Builder()
        .axisStepSize(( 340/(if (points.isNotEmpty()) points.size else 1) ).dp)
        .backgroundColor(Color.Transparent)
        .topPadding(16.dp)
        .steps(points.size - 1)
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(Color(0xFF468585))
        .axisLabelColor(Color(0xFF468585))
        .build()

    val yAxisData = AxisData.Builder()
        .steps(ySteps)
        .labelAndAxisLinePadding(10.dp)
        .backgroundColor(Color.Transparent)
        .labelData { i ->
            val yMin = points.minOf { it.y }
            val yMax = points.maxOf { it.y }
            val yScale = (yMax - yMin) / ySteps
            "${((i * yScale) + yMin).formatToSinglePrecision()}$measurementUnit"
        }
        .axisLineColor(Color(0xFF468585))
        .axisLabelColor(Color(0xFF468585))
        .build()

    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = points,
                    LineStyle(
                        color = Color(0xFF468585),
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(
                        color = Color(0xFF468585)
                    ),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF50B498),
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp(
                        popUpLabel = { x, y -> "${generateChartDateLabel(x.toLong())}-${y}"}
                    )
                )
            )
        ),
        backgroundColor = MaterialTheme.colorScheme.surface,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant)
    )

    Column(modifier = modifier) {
        LineChart(
            modifier = Modifier,
            lineChartData = data
        )
    }

}

private fun generateChartDateLabel(epochDay: Long): String {
    val format = DateTimeFormatter.ofPattern("dd/MM")
    return LocalDate.ofEpochDay(epochDay).format(format)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StatisticsPreview() {
    StatisticsScreen(StatisticCategory.WAIST)
}