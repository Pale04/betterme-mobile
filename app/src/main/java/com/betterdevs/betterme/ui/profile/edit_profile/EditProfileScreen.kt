package com.betterdevs.betterme.ui.profile.edit_profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betterdevs.betterme.R
import com.betterdevs.betterme.ui.shared.DatePickerTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfileScreen(
    onChangeEmailClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = viewModel()
) {
    val state by viewModel.state
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect (key1 = Unit) {
        viewModel.getUserDetails()
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
        modifier = modifier
    ) {
        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.profile_edit_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            )
            SecondaryTabRow(
                selectedTabIndex = state.selectedTab,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.onTabSelected(0) }
                ) {
                    Text(
                        text = stringResource(R.string.profile_edit_personal_info),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.onTabSelected(1) }
                ) {
                    Text(
                        text = stringResource(R.string.profile_edit_email_title),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                Tab(
                    selected = state.selectedTab == 2,
                    onClick = { viewModel.onTabSelected(2) }
                ) {
                    Text(
                        text = stringResource(R.string.profile_edit_password_title),
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }

            Column (
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (state.selectedTab) {
                    0 -> PersonalInfoEdition(
                        state = state,
                        onNameChanged = { viewModel.onNameChanged(it) },
                        onBirthdayChanged = { viewModel.onBirthdayChanged(it) },
                        onDescriptionChanged = { viewModel.onDescriptionChanged(it) },
                        onWebsiteChanged = { viewModel.onWebsiteChanged(it) },
                        onPhoneChanged = { viewModel.onPhoneChanged(it) },
                        onSaveChangesButtonClick = { viewModel.saveChanges() }
                    )
                    1 -> EmailEdition(
                        state = state,
                        onChangeEmailClick = onChangeEmailClick
                    )
                    2 -> PasswordEdition()
                }
            }
        }
    }
}

@Composable
fun PersonalInfoEdition(
    state: EditProfileState,
    onNameChanged: (String) -> Unit,
    onBirthdayChanged: (Instant) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onWebsiteChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onSaveChangesButtonClick: () -> Unit,
) {
    OutlinedTextField(
        value = state.name,
        onValueChange = { onNameChanged(it) },
        label = { Text(stringResource(R.string.create_account_name_text_field_label)) },
        isError = state.isNameWrong,
        supportingText = {
            if (state.isNameWrong) {
                Text(text = stringResource(R.string.create_account_empty_name_error))
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
    DatePickerTextField(
        initialValue = state.birthday,
        onDateSelected = { onBirthdayChanged(it) },
        label = R.string.create_account_birthday_label,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
    OutlinedTextField(
        value = state.website,
        onValueChange = { onWebsiteChanged(it) },
        label = { Text(stringResource(R.string.profile_edit_website_label)) },
        placeholder = { Text(stringResource(R.string.profile_edit_website_placeholder)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
    OutlinedTextField(
        value = state.phone,
        onValueChange = { onPhoneChanged(it) },
        label = { Text(stringResource(R.string.profile_edit_phone_label)) },
        placeholder = { Text(stringResource(R.string.profile_edit_phone_placeholder)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
    OutlinedTextField(
        value = state.description,
        onValueChange = { onDescriptionChanged(it) },
        label = { Text(stringResource(R.string.profile_edit_description_label)) },
        supportingText = { Text(stringResource(R.string.profile_edit_description_placeholder)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .height(240.dp)
    )
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSaveChangesButtonClick
    ) {
        Text(text = stringResource(R.string.profile_edit_save_changes))
    }
}

@Composable
fun EmailEdition(
    state: EditProfileState,
    onChangeEmailClick: () -> Unit
) {
    OutlinedTextField(
        value = state.email,
        onValueChange = {  },
        label = { Text(stringResource(R.string.create_account_email_text_field_label)) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
    Button (
        onClick = onChangeEmailClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.profile_edit_change_email_button))
    }
}

@Composable
fun PasswordEdition() {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen({})
}