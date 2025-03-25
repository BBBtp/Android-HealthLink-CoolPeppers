package com.CoolPeppers.android.data.repository

import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.Clinic
import javax.inject.Inject

class ClinicRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getClinics(skip: Int, limit: Int, search: String): List<Clinic> {
        return apiService.getClinics(
            skip = skip,
            limit = limit,
            search = search,
        )
    }

}