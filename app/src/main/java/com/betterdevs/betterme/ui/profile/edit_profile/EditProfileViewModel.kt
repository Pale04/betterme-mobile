package com.betterdevs.betterme.ui.profile.edit_profile

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betterdevs.betterme.data.repository.UsersRepository
import com.betterdevs.betterme.data.shared.UserSession
import com.betterdevs.betterme.domain_model.Account
import com.betterdevs.betterme.domain_model.User
import com.betterdevs.betterme.domain_model.UserRole
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.Instant

data class EditProfileState(
    val name: String = "",
    val birthday: Instant? = null,
    val description: String = "",
    val website: String = "",
    val phone: String = "",
    val email: String = "",

    val isNameWrong: Boolean = false,
    val isLoading: Boolean = false,

    val selectedTab: Int = 0
)

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val repository: UsersRepository = UsersRepository(context)

    private val _state = mutableStateOf(EditProfileState())
    val state: State<EditProfileState> = _state

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    fun onNameChanged(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun onBirthdayChanged(birthday: Instant) {
        _state.value = _state.value.copy(birthday = birthday)
    }

    fun onDescriptionChanged(description: String) {
        if (description.length < 600) {
            _state.value = _state.value.copy(description = description)
        }
    }

    fun onWebsiteChanged(website: String) {
        _state.value = _state.value.copy(website = website)
    }

    fun onPhoneChanged(phone: String) {
        if (phone.length < 11) {
            _state.value = _state.value.copy(phone = phone)
        }
    }

    fun onTabSelected(tab: Int) {
        _state.value = _state.value.copy(selectedTab = tab)
    }

    fun saveChanges() {
        if (!validateFields()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )

            val updatedUser = User(
                name = _state.value.name,
                account = Account(
                    username = UserSession.getUsername(),
                    password = ""
                ),
                email = _state.value.email,
                active = true,
                role = UserRole.MEMBER,
                birthday = _state.value.birthday!!,
                description = _state.value.description.ifBlank { null },
                website = _state.value.website.ifBlank { null },
                phone = _state.value.phone.ifBlank { null },
            )
            val result = repository.updateUser(updatedUser)

            if (result.success) {
                _state.value = _state.value.copy(
                    name = result.data!!.name,
                    birthday = result.data.birthday,
                    description = result.data.description ?: "",
                    website = result.data.website ?: "",
                    phone = result.data.phone ?: "",
                    email = result.data.email
                )
            }

            _snackbarMessage.emit(result.message)

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }

    private fun validateFields(): Boolean {
        val nameIsValid = _state.value.name.isNotBlank()

        _state.value = _state.value.copy(
            isNameWrong = !nameIsValid
        )

        return nameIsValid
    }

    fun getUserDetails() {
        viewModelScope.launch {
            val result = repository.getUser(UserSession.getId())

            if (result.success) {
                _state.value = _state.value.copy(
                    name = result.data!!.name,
                    birthday = result.data.birthday,
                    description = result.data.description ?: "",
                    website = result.data.website ?: "",
                    phone = result.data.phone ?: "",
                    email = result.data.email
                )
            } else {
                _snackbarMessage.emit(result.message)
            }
        }
    }
}