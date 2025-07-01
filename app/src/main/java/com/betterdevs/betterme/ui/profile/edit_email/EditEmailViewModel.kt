package com.betterdevs.betterme.ui.profile.edit_email

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betterdevs.betterme.data.repository.EmailRepository
import com.betterdevs.betterme.data.repository.UsersRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class EditEmailState(
    val code: String = "",
    val newEmail: String = "",

    val isCodeFieldWrong: Boolean = false,
    val isNewEmailWrong: Boolean = false,

    val isLoading: Boolean = false,
    val isCodeVerified: Boolean = false
)

class EditEmailViewModel(val context: Context): ViewModel() {
    private val emailRepository = EmailRepository(context)
    private val usersRepository = UsersRepository(context)

    private val _state = mutableStateOf(EditEmailState())
    val state: State<EditEmailState> = _state

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    fun onCodeChanged(code: String) {
        if (code.length <= 6) {
            _state.value = _state.value.copy(code = code)
        }
    }

    fun onNewEmailChanged(newEmail: String) {
        _state.value = _state.value.copy(newEmail = newEmail)
    }

    private fun validateFields(): Boolean {
        val codeCorrect = _state.value.code.length == 6
        val newEmailCorrect = android.util.Patterns.EMAIL_ADDRESS.matcher(_state.value.newEmail).matches()

        _state.value = _state.value.copy(
            isCodeFieldWrong = !codeCorrect,
            isNewEmailWrong = !newEmailCorrect,
        )

        return codeCorrect && newEmailCorrect
    }

    fun sendCode() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )

            val result = emailRepository.sendCodeForEmailUpdate()
            _snackbarMessage.emit(result.message)

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }

    fun updateEmail() {
        if (!validateFields()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )

            val result = usersRepository.updateEmail(_state.value.code, _state.value.newEmail)
            if (result.success) {
                _state.value = _state.value.copy(
                    code = "",
                    newEmail = "",
                    isCodeVerified = true
                )
            }

            _snackbarMessage.emit(result.message)

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }
}