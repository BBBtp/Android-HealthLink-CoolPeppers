package com.CoolPeppers.android.data.model

import com.google.gson.annotations.SerializedName

data class Appointment(
    @SerializedName("clinic_id") val clinicId: Int,
    @SerializedName("doctor_id") val doctorId: Int,
    @SerializedName("service_id") val serviceId: Int,
    val id: Int,
    val status: String,
    @SerializedName("appointment_slot_id") val appointmentSlotId: Int
)