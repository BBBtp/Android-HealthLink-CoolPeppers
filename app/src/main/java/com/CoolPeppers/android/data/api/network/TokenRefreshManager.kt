package com.CoolPeppers.android.data.api.network

import com.CoolPeppers.android.data.api.local.TokenManager
import com.CoolPeppers.android.data.model.RefreshToken
import com.CoolPeppers.android.data.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository
) {
    fun refreshTokenIfNeeded(onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        if (tokenManager.shouldRefreshToken()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val refreshToken = tokenManager.getRefreshToken()
                    if (refreshToken != null) {
                        val response = authRepository.refreshToken(RefreshToken(refreshToken))
                        tokenManager.saveTokens(response.accessToken, response.refreshToken)
                        onSuccess()
                    } else {
                        onError()
                    }
                } catch (e: Exception) {
                    tokenManager.clearTokens()
                    onError()
                }
            }
        } else {
            onSuccess()
        }
    }
} 