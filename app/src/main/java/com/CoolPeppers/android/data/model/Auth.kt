package com.CoolPeppers.android.data.model

// Интерфейс API для авторизации и регистрации
interface AuthApi {}

// Контроллер для работы с авторизацией
interface AuthController {
    suspend fun register(username: String, password: String): Boolean
    suspend fun login(email: String, password: String): String?
    suspend fun sendPasswordReset(email: String): Boolean
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

    override suspend fun sendPasswordReset(email: String): Boolean {
        return true // false в случае ошибки
    }
}