package com.CoolPeppers.android.presentation.profile

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.api.remote.RetrofitClient
import com.CoolPeppers.android.data.model.User
import com.CoolPeppers.android.data.repository.ProfileRepository
import com.CoolPeppers.android.data.repository.Result.Error
import com.CoolPeppers.android.data.repository.Result.Success
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApiService(context: Context): ApiService =
        RetrofitClient(context).apiService
}

@HiltViewModel
class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _userState = mutableStateOf<User>(User(
        username = "",
        email = "",
        firstName = null,
        lastName = null,
        age = null,
        bloodType = null,
        photoUrl = null
    ))
    val userState: State<User> = _userState

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> = _errorState

    init {
        loadUser()
    }

    fun loadUser() {
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

    fun updateUser(
        firstName: String,
        lastName: String,
        age: Int?,
        bloodType: String,
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
                }
                is Error -> {
                    _errorState.value = result.message
                }
            }
            _loadingState.value = false
        }
    }
}


class ProfileViewModelFactory(
    private val controller: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(controller) as T
    }
}