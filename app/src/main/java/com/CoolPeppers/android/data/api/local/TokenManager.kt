package com.CoolPeppers.android.data.api.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class TokenManager @Inject constructor(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        val currentTime = System.currentTimeMillis()
        prefs.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .putLong("access_token_timestamp", currentTime)
            .apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }

    fun isTokenExpired(): Boolean {
        val tokenTimestamp = getAccessTokenTimestamp()
        if (tokenTimestamp == 0L) return true
        val currentTime = System.currentTimeMillis()
        return (currentTime - tokenTimestamp) > (15 * 60 * 1000)
    }

    private fun getAccessTokenTimestamp(): Long {
        return prefs.getLong("access_token_timestamp", 0)
    }

    fun clearTokens() {
        prefs.edit().remove("access_token").remove("refresh_token").remove("access_token_timestamp").apply()
    }
}