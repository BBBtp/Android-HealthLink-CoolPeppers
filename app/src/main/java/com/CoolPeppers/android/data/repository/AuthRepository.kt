package com.CoolPeppers.android.data.repository

import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.data.model.AuthRequest
import com.CoolPeppers.android.data.model.AuthResponse
import com.CoolPeppers.android.data.model.LoginRequest
import com.CoolPeppers.android.data.model.RefreshToken
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(username: String, password: String): AuthResponse {
        return apiService.loginUser(username = username, password = password)
    }

    suspend fun register(request: AuthRequest) {
        return apiService.regUser(request = request)
    }

    suspend fun refreshToken(request: RefreshToken): AuthResponse {
        return apiService.refreshToken(request = request)
    }

}