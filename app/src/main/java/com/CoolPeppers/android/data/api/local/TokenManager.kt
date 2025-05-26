package com.CoolPeppers.android.data.api.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class TokenManager @Inject constructor(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val ACCESS_TOKEN_TIMESTAMP_KEY = "access_token_timestamp"
        private const val TOKEN_EXPIRATION_TIME = 15 * 60 * 1000L // 15 минут
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        val currentTime = System.currentTimeMillis()
        prefs.edit()
            .putString(ACCESS_TOKEN_KEY, accessToken)
            .putString(REFRESH_TOKEN_KEY, refreshToken)
            .putLong(ACCESS_TOKEN_TIMESTAMP_KEY, currentTime)
            .apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN_KEY, null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN_KEY, null)
    }

    fun isTokenExpired(): Boolean {
        val tokenTimestamp = getAccessTokenTimestamp()
        if (tokenTimestamp == 0L) return true
        val currentTime = System.currentTimeMillis()
        return (currentTime - tokenTimestamp) > TOKEN_EXPIRATION_TIME
    }

    fun shouldRefreshToken(): Boolean {
        val tokenTimestamp = getAccessTokenTimestamp()
        if (tokenTimestamp == 0L) return true
        val currentTime = System.currentTimeMillis()
        // Обновляем токен за 5 минут до истечения срока действия
        return (currentTime - tokenTimestamp) > (TOKEN_EXPIRATION_TIME - 5 * 60 * 1000)
    }

    private fun getAccessTokenTimestamp(): Long {
        return prefs.getLong(ACCESS_TOKEN_TIMESTAMP_KEY, 0)
    }

    fun clearTokens() {
        prefs.edit()
            .remove(ACCESS_TOKEN_KEY)
            .remove(REFRESH_TOKEN_KEY)
            .remove(ACCESS_TOKEN_TIMESTAMP_KEY)
            .apply()
    }
}