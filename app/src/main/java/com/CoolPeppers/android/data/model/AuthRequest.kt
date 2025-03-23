package com.CoolPeppers.android.data.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    val username: String,
    val email: String,
    @SerializedName("is_active") val isActive: Boolean = true,
    @SerializedName("is_admin") val isAdmin: Boolean = false,
    val password: String
)

data class RefreshToken(
    @SerializedName("refresh_token") val refreshToken: String
)

data class LoginRequest(
    val username: String,
    val password: String
)
