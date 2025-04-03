package com.CoolPeppers.android.data.repository

import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.data.model.SlotResponse
import javax.inject.Inject

class AppointmentRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAppointments(): List<Appointment> {
        return apiService.getAppointments()
    }

    suspend fun getSlotById(slotId: Int): SlotResponse {
        return apiService.getSlotById(slotId = slotId)
    }

}