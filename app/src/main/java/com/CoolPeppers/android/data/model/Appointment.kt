package com.CoolPeppers.android.data.model

data class Appointment(
    val clinicId: Int,
    val doctorId: Int,
    val serviceId: Int,
    val id: Int,
    val status: String,
    val appointmentSlotId: Int
)