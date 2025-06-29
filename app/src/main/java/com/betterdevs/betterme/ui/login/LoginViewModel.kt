package com.betterdevs.betterme.ui.login

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.repository.LoginRepository
import com.betterdevs.betterme.data.shared.UserSession
import com.betterdevs.betterme.domain_model.Account
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class LoginState (
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val repository = LoginRepository(context)

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    private val _loginSuccessful = MutableSharedFlow<Unit>()
    val loginSuccessful: SharedFlow<Unit> = _loginSuccessful

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    fun onUserChanged(username: String) {
        _state.value = _state.value.copy(
            username = username,
        )
    }

    fun onPasswordChanged(password: String) {
        _state.value = _state.value.copy(
            password = password
        )
    }

    private fun validFields(): Boolean {
        return _state.value.username.isNotBlank() && _state.value.password.isNotBlank()
    }

    fun login() {
        if (!validFields()) {
            viewModelScope.launch {
                _snackbarMessage.emit(context.getString(R.string.general_empty_fields_error))
            }
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )

            val response = repository.login(Account(_state.value.username, _state.value.password))
            if (response.success) {
                if (!UserSession.isMember()) {
                    _snackbarMessage.emit(context.getString(R.string.login_maderator_account_error))
                } else {
                    _state.value = _state.value.copy(
                        username = "",
                        password = ""
                    )
                    _loginSuccessful.emit(Unit)
                }

            } else {
                _snackbarMessage.emit(response.message)
            }

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }
}

