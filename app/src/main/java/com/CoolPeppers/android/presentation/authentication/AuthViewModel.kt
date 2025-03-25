package com.CoolPeppers.android.presentation.authentication

import androidx.compose.material3.rememberTopAppBarState
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.AuthRequest
import com.CoolPeppers.android.data.model.LoginRequest
import com.CoolPeppers.android.data.repository.AuthRepository
import kotlinx.coroutines.launch
import com.CoolPeppers.android.data.api.local.TokenManager
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository, // Репозиторий для работы с авторизацией
    private val tokenManager: TokenManager // Менеджер для работы с токенами
) : ViewModel() {

    // Состояние для экрана регистрации
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    private val isActive = true
    private val isAdmin = false
    // Состояние для экрана входа
    var loginUsername by mutableStateOf("")
    var loginPassword by mutableStateOf("")

    // Сообщения об ошибках
    private var errorMessage by mutableStateOf<String?>(null)

    // Токен авторизации
    private var authToken by mutableStateOf<String?>(null)

    // Метод для регистрации
    fun register() {
        errorMessage = null
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorMessage = "Все поля должны быть заполнены"
            return
        }

        viewModelScope.launch {
            try {
                val authRequest = AuthRequest(username,email, isActive, isAdmin, password)
                authRepository.register(authRequest) // Вызов метода из репозитория для регистрации
                errorMessage = "Регистрация успешна"
            } catch (e: Exception) {
                errorMessage = "Ошибка регистрации: ${e.message}"
            }
        }
    }

    // Метод для авторизации
    fun login() {
        errorMessage = null

        // Логируем введённые данные
        println("Login attempt: Username = $loginUsername, Password = $loginPassword")

        if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
            errorMessage = "Все поля должны быть заполнены"
            println("Login failed: empty fields")
            return
        }

        viewModelScope.launch {
            try {
                println("Sending login request...") // Лог перед отправкой запроса

                val loginRequest = LoginRequest(loginUsername, loginPassword)
                val json = Gson().toJson(loginRequest)
                println("Login request JSON: $json")
                val response = authRepository.login(loginUsername,loginPassword) // Вызов метода из репозитория
                authToken = response.accessToken
                tokenManager.saveTokens(response.accessToken, response.refreshToken ?: "")

                errorMessage = "Авторизация успешна"
                println("Login successful! Token: ${response.accessToken}") // Лог успешного входа

            } catch (e: Exception) {
                errorMessage = "Ошибка авторизации: ${e.message}"
                println("Login error: ${e.message}") // Лог ошибки
            }
        }
    }


    /*// Метод для сброса пароля
    var forgotPasswordEmail by mutableStateOf("")
    var passwordResetSent by mutableStateOf(false)
    var passwordResetError by mutableStateOf<String?>(null)

    fun sendPasswordReset() = viewModelScope.launch {
        passwordResetError = null
        try {
            val success = authRepository.sendPasswordReset(forgotPasswordEmail)
            passwordResetSent = success
            if (!success) {
                passwordResetError = "Ошибка сброса пароля"
            }
        } catch (e: Exception) {
            passwordResetError = e.message ?: "Ошибка сброса пароля"
            passwordResetSent = false
        }
    }*/

    fun resetError() {
        errorMessage = null
    }
}
