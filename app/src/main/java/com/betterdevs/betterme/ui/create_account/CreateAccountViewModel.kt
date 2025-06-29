package com.betterdevs.betterme.ui.create_account

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betterdevs.betterme.data.repository.UsersRepository
import com.betterdevs.betterme.domain_model.Account
import com.betterdevs.betterme.domain_model.User
import com.betterdevs.betterme.ui.shared.Validator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate

data class CreateAccountState (
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val birthday: Instant? = null,
    val password: String = "",
    val confirmPassword: String = "",

    val isNameWrong: Boolean = false,
    val isEmailWrong: Boolean = false,
    val isUsernameWrong: Boolean = false,
    val isBirthdayWrong: Boolean = false,
    val isPasswordWrong: Boolean = false,
    val isConfirmPasswordWrong: Boolean = false,

    val isLoading: Boolean = false
)

class CreateAccountViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val repository: UsersRepository = UsersRepository(context)

    private val _state = mutableStateOf(CreateAccountState())
    val state: State<CreateAccountState> = _state

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    fun onNameChanged(name: String) {
        _state.value = _state.value.copy(
            name = name
        )
        if (_state.value.isNameWrong) {
            _state.value = _state.value.copy(
                isNameWrong = false
            )
        }
    }

    fun onEmailChanged(email: String) {
        _state.value = _state.value.copy(
            email = email
        )
        if (_state.value.isEmailWrong) {
            _state.value = _state.value.copy(
                isEmailWrong = false
            )
        }
    }

    fun onUsernameChanged(username: String) {
        _state.value = _state.value.copy(
            username = username
        )
        if (_state.value.isUsernameWrong) {
            _state.value = _state.value.copy(
                isUsernameWrong = false
            )
        }
    }

    fun onBirthdayChanged(birthday: Instant) {
        _state.value = _state.value.copy(
            birthday = birthday
        )
        if (_state.value.isBirthdayWrong) {
            _state.value = _state.value.copy(
                isBirthdayWrong = false
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _state.value = _state.value.copy(
            password = password
        )
        if (_state.value.isPasswordWrong) {
            _state.value = _state.value.copy(
                isPasswordWrong = false
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _state.value = _state.value.copy(
            confirmPassword = confirmPassword,
            isConfirmPasswordWrong = confirmPassword != _state.value.password
        )
    }

    private fun validateFields(): Boolean {
        val nameValid = _state.value.name.isNotBlank()
        val emailValid = Validator.isEmailCorrect(_state.value.email)
        val usernameValid = _state.value.username.isNotBlank()
        val birthdayValid = _state.value.birthday != null
        val passwordValid = _state.value.password.isNotBlank() && Validator.isPasswordCorrect(_state.value.password)
        val confirmPasswordValid = _state.value.confirmPassword == _state.value.password

        _state.value = _state.value.copy(
            isNameWrong = !nameValid,
            isEmailWrong = !emailValid,
            isUsernameWrong = !usernameValid,
            isBirthdayWrong =  !birthdayValid,
            isPasswordWrong = !passwordValid,
            isConfirmPasswordWrong = !confirmPasswordValid
        )

        return nameValid && emailValid && usernameValid && birthdayValid && passwordValid && confirmPasswordValid
    }

    fun createAccount() {
        if (!validateFields()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )

            val newUser = User(
                account = Account(
                    username = _state.value.username,
                    password = _state.value.password
                ),
                email = _state.value.email,
                name = _state.value.name,
                birthday = _state.value.birthday!!
            )
            val response = repository.addUser(newUser)

            if (response.success) {
                _state.value = _state.value.copy(
                    name = "",
                    email = "",
                    username = "",
                    birthday = null,
                    password = "",
                    confirmPassword = ""
                )
            }
            _snackbarMessage.emit(response.message)

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }
}