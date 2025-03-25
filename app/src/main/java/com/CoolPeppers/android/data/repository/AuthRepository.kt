package com.CoolPeppers.android.data.repository

import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.data.model.AuthRequest
import com.CoolPeppers.android.data.model.AuthResponse
import com.CoolPeppers.android.data.model.LoginRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(requestBody: LoginRequest): AuthResponse {
        return apiService.loginUser(request = requestBody)
    }

    suspend fun register(request: AuthRequest) {
        return apiService.regUser(request = request)
    }

}