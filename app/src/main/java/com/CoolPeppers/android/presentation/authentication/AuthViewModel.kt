package com.CoolPeppers.android.presentation.authentication

import androidx.compose.material3.rememberTopAppBarState
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.AuthRequest
import com.CoolPeppers.android.data.model.LoginRequest
import com.CoolPeppers.android.data.model.RefreshToken
import com.CoolPeppers.android.data.repository.AuthRepository
import kotlinx.coroutines.launch
import com.CoolPeppers.android.data.api.local.TokenManager
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicInteger

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository, // Репозиторий для работы с авторизацией
    private val tokenManager: TokenManager // Менеджер для работы с токенами
) : ViewModel() {

    // Состояния для экрана регистрации
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    private val isActive = true
    private val isAdmin = false

    // Состояния для экрана входа
    var loginUsername by mutableStateOf("")
    var loginPassword by mutableStateOf("")

    // Сообщения об ошибках
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Состояние загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Состояние авторизации
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    // Счетчик неудачных попыток входа
    private val loginAttempts = AtomicInteger(0)
    private val MAX_LOGIN_ATTEMPTS = 5
    private val LOCKOUT_DURATION = 15 * 60 * 1000L // 15 минут
    private var lastFailedAttemptTime = 0L

    // Валидация пароля
    private fun validatePassword(password: String): Boolean {
        return password.length >= 8 && // Минимальная длина 8 символов
               password.any { it.isDigit() } && // Минимум одна цифра
               password.any { it.isUpperCase() } && // Минимум одна заглавная буква
               password.any { it.isLowerCase() } && // Минимум одна строчная буква
               password.any { !it.isLetterOrDigit() } // Минимум один специальный символ
    }

    // Метод для регистрации
    fun register() {
        _errorMessage.value = null
        
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Все поля должны быть заполнены"
            return
        }

        if (!validatePassword(password)) {
            _errorMessage.value = "Пароль должен содержать минимум 8 символов, включая цифры, заглавные и строчные буквы, и специальные символы"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val authRequest = AuthRequest(username, email, isActive, isAdmin, password)
                authRepository.register(authRequest)
                _errorMessage.value = "Регистрация успешна"
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка регистрации: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Метод для авторизации
    fun login() {
        _errorMessage.value = null

        if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
            _errorMessage.value = "Все поля должны быть заполнены"
            return
        }

        // Проверка блокировки после неудачных попыток
        if (loginAttempts.get() >= MAX_LOGIN_ATTEMPTS) {
            val timeSinceLastAttempt = System.currentTimeMillis() - lastFailedAttemptTime
            if (timeSinceLastAttempt < LOCKOUT_DURATION) {
                val remainingTime = (LOCKOUT_DURATION - timeSinceLastAttempt) / 1000 / 60
                _errorMessage.value = "Слишком много неудачных попыток. Попробуйте через $remainingTime минут"
                return
            } else {
                loginAttempts.set(0)
            }
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = authRepository.login(loginUsername, loginPassword)
                tokenManager.saveTokens(response.accessToken, response.refreshToken)
                _isAuthenticated.value = true
                loginAttempts.set(0)
                _errorMessage.value = "Авторизация успешна"
            } catch (e: Exception) {
                loginAttempts.incrementAndGet()
                lastFailedAttemptTime = System.currentTimeMillis()
                _errorMessage.value = "Ошибка авторизации: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Метод для обновления токена
    fun refreshToken() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken != null) {
                    val response = authRepository.refreshToken(RefreshToken(refreshToken))
                    tokenManager.saveTokens(response.accessToken, response.refreshToken)
                } else {
                    _isAuthenticated.value = false
                }
            } catch (e: Exception) {
                _isAuthenticated.value = false
                tokenManager.clearTokens()
            } finally {
                _isLoading.value = false
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
        _errorMessage.value = null
    }
}
