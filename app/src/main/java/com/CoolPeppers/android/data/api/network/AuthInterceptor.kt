package com.CoolPeppers.android.data.api.network

import com.CoolPeppers.android.data.api.local.TokenManager
import com.CoolPeppers.android.data.model.RefreshToken
import com.CoolPeppers.android.data.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenManager.getAccessToken()

        // Если токен отсутствует, пропускаем запрос без авторизации
        if (token == null) {
            return chain.proceed(originalRequest)
        }

        // Проверяем, нужно ли обновить токен
        if (tokenManager.shouldRefreshToken()) {
            runBlocking {
                try {
                    val refreshToken = tokenManager.getRefreshToken()
                    if (refreshToken != null) {
                        val response = authRepository.refreshToken(RefreshToken(refreshToken))
                        tokenManager.saveTokens(response.accessToken, response.refreshToken)
                    }
                } catch (e: Exception) {
                    tokenManager.clearTokens()
                    return@runBlocking
                }
            }
        }

        // Добавляем токен к запросу
        val newToken = tokenManager.getAccessToken()
        val request = originalRequest.newBuilder()
        newToken?.let {
            request.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(request.build())
    }
}