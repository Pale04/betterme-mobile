package com.betterdevs.betterme.ui.create_account

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betterdevs.betterme.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CreateAccountScreen(
    onIHaveAnAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.10f to Color(0xFFDEF9C4),
                        0.40f to Color(0xFF9CDBA6),
                        0.70f to Color(0xFF50B498),
                        1.0f to Color(0xFF468585)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.create_account_title),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = {nameInput = it},
                    label = { Text(stringResource(R.string.create_account_name_text_field_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = {emailInput = it},
                    label = { Text(stringResource(R.string.create_account_email_text_field_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
                OutlinedTextField(
                    value = usernameInput,
                    onValueChange = {usernameInput = it},
                    label = { Text(stringResource(R.string.create_account_username_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
                DatePickerModal(
                    label = R.string.create_account_birthday_label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = {passwordInput = it},
                    label = { Text(stringResource(R.string.create_account_password_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
                OutlinedTextField(
                    value = confirmPasswordInput,
                    onValueChange = {confirmPasswordInput = it},
                    label = { Text(stringResource(R.string.create_account_confirm_password_label)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
            }

            Column (
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {  }
                ) {
                    Text(text = stringResource(R.string.create_account_register_button_text))
                }
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onIHaveAnAccountClick
                ) {
                    Text(text = stringResource(R.string.create_account_secondary_button_text))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(@StringRes label: Int, modifier: Modifier = Modifier) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let { convertMillisToDate(it) } ?: ""

    OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        label = { Text(stringResource(label))},
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePickerDialog = !showDatePickerDialog }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
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

fun convertMillisToDate(millis: Long): String {
    val localDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    return localDate.format(formatter)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateAccountPreview() {
    CreateAccountScreen({})
}