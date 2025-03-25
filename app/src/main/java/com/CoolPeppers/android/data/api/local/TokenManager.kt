package com.CoolPeppers.android.data.api.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class TokenManager @Inject constructor(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }

    fun clearTokens() {
        prefs.edit().remove("access_token").remove("refresh_token").apply()
    }
}