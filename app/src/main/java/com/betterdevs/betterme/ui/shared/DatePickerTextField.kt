package com.betterdevs.betterme.ui.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.betterdevs.betterme.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    onDateSelected: (Instant) -> Unit,
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        onDateSelected(Instant.ofEpochMilli(it))
        val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        localDate.format(formatter)
    } ?: ""

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        label = { Text(stringResource(label)) },
        readOnly = true,
        isError = isError,
        supportingText = supportingText,
        trailingIcon = {
            IconButton(onClick = { showDatePickerDialog = !showDatePickerDialog }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date icon"
                )
            }
        },
        modifier = modifier
    )

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button( onClick = { showDatePickerDialog = false } ) {
                    Text(text = stringResource(R.string.general_confirm_text))
                }
            },
            dismissButton = {
                OutlinedButton( onClick = { showDatePickerDialog = false } ) {
                    Text(text = stringResource(R.string.general_cancel_text))
                }
            },
            modifier = Modifier.padding(20.dp)
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}