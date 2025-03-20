package com.CoolPeppers.android.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.CoolPeppers.android.data.model.AuthController

class AuthViewModel(
    private val authController: AuthController
) : ViewModel() {

    // Состояние для экрана регистрации
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // Состояние для экрана входа
    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")

    // Сообщения об ошибках
    var errorMessage by mutableStateOf<String?>(null)

    // Токен авторизации
    var authToken by mutableStateOf<String?>(null)

    // Метод для регистрации
    suspend fun register() {
        errorMessage = null
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorMessage = "Все поля должны быть заполнены"
            return
        }

        val success = authController.register(username, password)
        if (!success) {
            errorMessage = "Ошибка регистрации"
        } else {
            errorMessage = "Регистрация успешна"
        }
    }

    // Метод для авторизации
    suspend fun login() {
        errorMessage = null
        if (loginEmail.isEmpty() || loginPassword.isEmpty()) {
            errorMessage = "Все поля должны быть заполнены"
            return
        }

        val token = authController.login(loginEmail, loginPassword)
        if (token != null) {
            authToken = token
            errorMessage = "Авторизация успешна"
        } else {
            errorMessage = "Ошибка авторизации"
        }
    }

    // Метод для сброса ошибок
    fun resetError() {
        errorMessage = null
    }
}

// Фабрика для AuthViewModel
class AuthViewModelFactory(
    private val authController: AuthController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}