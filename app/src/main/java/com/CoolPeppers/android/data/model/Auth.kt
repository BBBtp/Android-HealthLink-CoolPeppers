package com.CoolPeppers.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Модель данных для запроса авторизации/регистрации
@Serializable
data class AuthRequest(
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)

// Модель данных для ответа авторизации
@Serializable
data class AuthResponse(
    @SerialName("token") val token: String
)

// Интерфейс API для авторизации и регистрации
interface AuthApi {
    @POST("/api/v1/auth/register")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/api/v1/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}

// Контроллер для работы с авторизацией
interface AuthController {
    suspend fun register(username: String, password: String): Boolean
    suspend fun login(email: String, password: String): String?
}

// Моковая реализация контроллера (для тестов)
class MockAuthController : AuthController {
    private val mockToken = "mock_token"

    override suspend fun register(username: String, password: String): Boolean {
        // Логика регистрации (в моковой реализации всегда успешно)
        return true
    }

    override suspend fun login(email: String, password: String): String? {
        // Логика авторизации (в моковой реализации возвращает токен)
        return if (email.isNotEmpty() && password.isNotEmpty()) mockToken else null
    }
}