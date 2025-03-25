package com.CoolPeppers.android.data.repository

import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.Service
import javax.inject.Inject

class ServiceRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getServices(skip: Int, limit: Int, search: String,clinicId: Int?): List<Service> {
        return apiService.getServices(
            skip = skip,
            limit = limit,
            search = search,
            clinicId = clinicId
        )
    }
}