package com.betterdevs.betterme.ui.profile

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betterdevs.betterme.data.repository.UsersRepository
import com.betterdevs.betterme.data.shared.UserSession
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class ProfileState (
    val username: String = "",
    val description: String = "",
    val birthday: String = "",
    val webSite: String = "",
    val isVerified: Boolean = false
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val repository: UsersRepository = UsersRepository(context)

    private val _state = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    fun getUserDetails() {
        viewModelScope.launch {
            val result = repository.getUser(UserSession.getId())

            if (result.success) {
                _state.value = _state.value.copy(
                    username = result.data!!.account.username,
                    description = result.data.description ?: "",
                    birthday = getBirthdayFormat(result.data.birthday),
                    webSite = result.data.website ?: "",
                    isVerified = result.data.verified
                )
            } else {
                _snackbarMessage.emit(result.message)
            }
        }
    }

    private fun getBirthdayFormat(birthday: Instant): String {
        val localDate = birthday.atZone(ZoneId.of("UTC")).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        return localDate.format(formatter)
    }
}