package com.betterdevs.betterme.ui.profile.edit_email

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.betterdevs.betterme.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditEmailScreen(
    modifier: Modifier = Modifier,
    viewModel: EditEmailViewModel = EditEmailViewModel(context = LocalContext.current)
){
    val state by viewModel.state
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.sendCode()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) {
        Column (
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.profile_edit_email_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            
            Text(
                text = stringResource(R.string.profile_edit_email_instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = state.code,
                onValueChange = { viewModel.onCodeChanged(it) },
                label = { Text(stringResource(R.string.profile_edit_email_code_label)) },
                isError = state.isCodeFieldWrong,
                singleLine = true,
                readOnly = state.isCodeVerified,
                supportingText = {
                    if (state.isCodeFieldWrong) {
                        Text(text = stringResource(R.string.profile_edit_email_code_field_error))
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = state.newEmail,
                onValueChange = { viewModel.onNewEmailChanged(it) },
                label = { Text(stringResource(R.string.profile_edit_new_email_label)) },
                isError = state.isNewEmailWrong,
                singleLine = true,
                readOnly = state.isCodeVerified,
                placeholder = { Text( stringResource(R.string.create_account_email_placeholder) ) },
                supportingText = {
                    if (state.isNewEmailWrong) {
                        Text(text = stringResource(R.string.create_account_email_error))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else if (!state.isCodeVerified) {
                Button (
                    onClick = { viewModel.updateEmail() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.profile_edit_save_changes))
                }
                OutlinedButton(
                    onClick = { viewModel.sendCode() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.profile_edit_send_code_again))
                }
            }
        }
    }
}