package com.CoolPeppers.android.data.repository

import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.data.model.Doctor
import javax.inject.Inject

class DoctorRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getDoctors(skip: Int, limit: Int, search: String,serviceId: Int?, clinicId: Int?): List<Doctor> {
        return apiService.getDoctors(
            skip = skip,
            limit = limit,
            search = search,
            serviceId = serviceId,
            clinicId = clinicId
        )
    }

    suspend fun getDoctorById(doctorId: Int): Doctor {
        return apiService.getDoctorById(doctorId = doctorId)
    }
}