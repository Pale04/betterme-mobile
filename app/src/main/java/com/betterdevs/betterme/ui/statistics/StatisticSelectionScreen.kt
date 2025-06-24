package com.betterdevs.betterme.ui.statistics

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betterdevs.betterme.R
import com.betterdevs.betterme.domain_model.StatisticCategory

@Composable
fun StatisticSelectionScreen(
    onCategoryClick: (StatisticCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.statistics_title),
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        Text(
            text = stringResource(R.string.statistics_selection_description),
            modifier = Modifier.padding(8.dp)
        )

        Row(modifier = Modifier.weight(1f)) {
            StatisticCard(
                category = R.string.statistics_arms_title,
                description = R.string.statistics_arms_description,
                modifier = Modifier.weight(1f),
                onClick = { onCategoryClick(StatisticCategory.ARMS) }
            )
            StatisticCard(
                category = R.string.statistics_weight_measure_title,
                description = R.string.statistics_weight_measure_description,
                modifier = Modifier.weight(1f),
                onClick = { onCategoryClick(StatisticCategory.WEIGHT) }
            )

        }

        Row(modifier = Modifier.weight(1f)) {
            StatisticCard(
                category = R.string.statistics_waist_measure_title,
                description = R.string.statistics_waist_measure_description,
                modifier = Modifier.weight(1f),
                onClick = { onCategoryClick(StatisticCategory.WAIST) }
            )
            StatisticCard(
                category = R.string.statistics_sleeping_time_title,
                description = R.string.statistics_sleeping_time_description,
                modifier = Modifier.weight(1f),
                onClick = { onCategoryClick(StatisticCategory.SLEEP_HOURS) }
            )
        }

        Row(modifier = Modifier.weight(1f)) {
            StatisticCard(
                category = R.string.statistics_water_intake_title,
                description = R.string.statistics_water_intake_description,
                modifier = Modifier.weight(1f),
                onClick = { onCategoryClick(StatisticCategory.WATER_INTAKE) }
            )
            /*StatisticCard(
                category = R.string.statistics_mood_title,
                description = R.string.statistics_mood_description,
                modifier = Modifier.weight(1f),
                onClick = { onCategoryClick(StatisticCategory.MOOD) }
            )*/
        }
    }
}

@Composable
fun StatisticCard(@StringRes category: Int, @StringRes description: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedCard (
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDEF9C4)
        ),
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(category),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(description),
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StatisticSelectionPreview() {
    StatisticSelectionScreen({})
}