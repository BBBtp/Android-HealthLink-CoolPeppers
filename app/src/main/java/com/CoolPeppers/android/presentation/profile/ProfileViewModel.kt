package com.CoolPeppers.android.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Profile
import com.CoolPeppers.android.data.model.ProfileController
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileController: ProfileController
) : ViewModel() {
    private val _profileState = mutableStateOf(
        Profile(
            id = 0,
            first_name = "",
            last_name = "",
            email = "",
            phone = "",
            avatarUrl = ""
        )
    )
    val profileState: State<Profile> = _profileState

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    init {
        viewModelScope.launch {
            _loadingState.value = true
            _profileState.value = profileController.getProfile()
            _loadingState.value = false
        }
    }

    fun updateFirstName(newFirstName: String) {
        _profileState.value = _profileState.value.copy(first_name = newFirstName)
    }

    fun updateLastName(newLastName: String) {
        _profileState.value = _profileState.value.copy(last_name = newLastName)
    }

    fun updateEmail(newEmail: String) {
        _profileState.value = _profileState.value.copy(email = newEmail)
    }

    fun updatePhone(newPhone: String) {
        _profileState.value = _profileState.value.copy(phone = newPhone)
    }

    fun updateAvatar(url: String) {
        viewModelScope.launch {
            _loadingState.value = true
            profileController.updateAvatar(url)
            _profileState.value = _profileState.value.copy(avatarUrl = url)
            _loadingState.value = false
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            _loadingState.value = true
            profileController.updateProfile(_profileState.value)
            _loadingState.value = false
        }
    }
}


class ProfileViewModelFactory(
    private val controller: ProfileController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(controller) as T
    }
}