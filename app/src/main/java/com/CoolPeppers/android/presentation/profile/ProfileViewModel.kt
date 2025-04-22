package com.CoolPeppers.android.presentation.profile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.api.local.TokenManager
import com.CoolPeppers.android.data.model.User
import com.CoolPeppers.android.data.repository.ProfileRepository
import com.CoolPeppers.android.data.repository.Result.Error
import com.CoolPeppers.android.data.repository.Result.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _userState = MutableStateFlow(User(
        id = 1,
        username = "",
        email = "",
        firstName = null,
        lastName = null,
        age = null,
        bloodType = null,
        photoUrl = null
    ))
    val userState: StateFlow<User> = _userState.asStateFlow()

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> = _errorState

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            when (val result = profileRepository.getUser()) {
                is Success -> {
                    _userState.value = result.data
                    _errorState.value = null
                }
                is Error -> {
                    _errorState.value = result.message
                }
            }
            _loadingState.value = false
        }
    }

    fun logOut() {
        viewModelScope.launch {
            tokenManager.clearTokens()
        }
    }

    fun updateUser(
        firstName: String?,
        lastName: String?,
        age: Int?,
        bloodType: String?,
        photo: File?
    ) {
        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            when (val result = profileRepository.updateUser(
                firstName,
                lastName,
                age,
                bloodType,
                photo
            )) {
                is Success -> {
                    _userState.value = result.data
                    _errorState.value = null
                    Log.d("update user", "success")
                }
                is Error -> {
                    _errorState.value = result.message
                    Log.d("update user", "fail: " + result.message)
                }
            }

            _loadingState.value = false
        }
    }
}
