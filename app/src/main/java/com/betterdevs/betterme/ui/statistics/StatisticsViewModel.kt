package com.betterdevs.betterme.ui.statistics

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.repository.StatisticsRepository
import com.betterdevs.betterme.domain_model.Statistic
import com.betterdevs.betterme.domain_model.StatisticCategory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class StatisticsState (
    val measureInput: String = "",
    val inputIsLoading: Boolean = false,
    val inputError: String? = null,
)

data class ChartState (
    val points: List<Point> = listOf(Point(0f,0f)),
    val isLoading: Boolean = false,
    val error: Boolean = false,
)

data class CategorySpecification (
    val title: String = "",
    val inputLabel: String = "",
    val inputPlaceholder: String = "",
    val chartYSteps: Int = 10,
    val measurementUnit: String = "",
    var todayTrackingDone: Boolean = false
)

class StatisticsViewModel(val context: Context, val category: StatisticCategory) : ViewModel() {
    private val repository = StatisticsRepository(context)

    val categorySpecification: CategorySpecification = when (category) {
        StatisticCategory.ARMS -> CategorySpecification(
            title = context.getString(R.string.statistics_arms_title),
            inputLabel = context.getString(R.string.statistics_arms_input_label),
            inputPlaceholder = context.getString(R.string.statistics_arms_input_placeholder),
            chartYSteps = 5,
            measurementUnit = "cm."
        )
        StatisticCategory.MOOD -> CategorySpecification(
            title = context.getString(R.string.statistics_mood_title),
            inputLabel = context.getString(R.string.statistics_mood_input_label),
            chartYSteps = 5
        )
        StatisticCategory.SLEEP_HOURS -> CategorySpecification(
            title = context.getString(R.string.statistics_sleeping_time_title),
            inputLabel = context.getString(R.string.statistics_sleeping_time_input_label),
            inputPlaceholder = context.getString(R.string.statistics_sleeping_time_input_placeholder),
            measurementUnit = "hr."
        )
        StatisticCategory.WAIST -> CategorySpecification(
            title = context.getString(R.string.statistics_waist_measure_title),
            inputLabel = context.getString(R.string.statistics_waist_input_label),
            inputPlaceholder = context.getString(R.string.statistics_waist_input_placeholder),
            measurementUnit = "cm."
        )
        StatisticCategory.WEIGHT -> CategorySpecification(
            title = context.getString(R.string.statistics_weight_measure_title),
            inputLabel = context.getString(R.string.statistics_weight_input_label),
            inputPlaceholder = context.getString(R.string.statistics_weight_input_placeholder),
            measurementUnit = "kg."
        )
        StatisticCategory.WATER_INTAKE -> CategorySpecification(
            title = context.getString(R.string.statistics_water_intake_title),
            inputLabel = context.getString(R.string.statistics_water_intake_input_label),
            inputPlaceholder = context.getString(R.string.statistics_water_intake_input_placeholder),
            measurementUnit = "vasos"
        )
    }

    private val _state = mutableStateOf(StatisticsState())
    val state: State<StatisticsState> = _state

    private val _chartDataState = mutableStateOf(ChartState())
    val chartDataState: State<ChartState> = _chartDataState

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    fun onMeasureInputChange(measure: String) {
        val regex = Regex("^\\d{0,3}(\\.\\d?)?$")
        if (regex.matches(measure)) {
            _state.value = state.value.copy(
                measureInput = measure
            )
        }
    }

    private fun validFields(): Boolean {
        return _state.value.measureInput.isNotBlank()
    }

    fun onSaveMeasureClick() {
        if (!validFields()) {
            viewModelScope.launch {
                _snackbarMessage.emit(context.getString(R.string.general_empty_fields_error))
            }
            return
        }

        viewModelScope.launch {
            _state.value = state.value.copy(
                inputIsLoading = true
            )

            val response = repository.saveStatistic(category, state.value.measureInput.toDouble())
            if (response.success) {
                getStatistics()
            } else {
                _snackbarMessage.emit(response.message)
            }

            _state.value = state.value.copy(
                inputIsLoading = false
            )
        }
    }

    fun getStatistics() {
        viewModelScope.launch {
            _chartDataState.value = chartDataState.value.copy(
                isLoading = true
            )

            val response = repository.getStatistics()
            if (response.success) {
                if (response.data.isNullOrEmpty()) {
                    _snackbarMessage.emit("Aún no registras ningún dato")
                }
                else {
                    val lastStatistic = response.data.last()
                    if (lastStatistic.date.compareTo(LocalDate.now().atStartOfDay().toLocalDate()) == 0) {
                        when (category) {
                            StatisticCategory.ARMS -> categorySpecification.todayTrackingDone = lastStatistic.arms != null
                            StatisticCategory.MOOD -> categorySpecification.todayTrackingDone = lastStatistic.mood != null
                            StatisticCategory.SLEEP_HOURS -> categorySpecification.todayTrackingDone = lastStatistic.sleepHours != null
                            StatisticCategory.WAIST -> categorySpecification.todayTrackingDone = lastStatistic.waist != null
                            StatisticCategory.WEIGHT -> categorySpecification.todayTrackingDone = lastStatistic.weight != null
                            StatisticCategory.WATER_INTAKE -> categorySpecification.todayTrackingDone = lastStatistic.waterIntake != null
                        }
                    }
                    _chartDataState.value = chartDataState.value.copy(
                        points = convertStatisticToPoint(response.data)
                    )
                }
            } else {
                _chartDataState.value = chartDataState.value.copy(
                    error = true
                )
                _snackbarMessage.emit(response.message)
            }

            _chartDataState.value = chartDataState.value.copy(
                isLoading = false
            )
        }
    }

    private fun convertStatisticToPoint(statistics: List<Statistic>): List<Point> {
        val points = mutableListOf<Point>()

        val actualDate = LocalDate.now()
        for (statistic in statistics) {
            val y = when (this.category) {
                StatisticCategory.ARMS -> statistic.arms
                StatisticCategory.MOOD -> statistic.mood
                StatisticCategory.SLEEP_HOURS -> statistic.sleepHours
                StatisticCategory.WAIST -> statistic.waist
                StatisticCategory.WEIGHT -> statistic.weight
                StatisticCategory.WATER_INTAKE -> statistic.waterIntake
            }
            if (y != null) {
                val x = actualDate.minusDays(actualDate.toEpochDay() - statistic.date.toEpochDay()).toEpochDay()
                points.add(Point(x.toFloat(), y?.toFloat() ?: 0.0f))
            }

        }

        return points
    }
}