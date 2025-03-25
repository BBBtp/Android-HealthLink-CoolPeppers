package com.CoolPeppers.android.data.model

import com.google.gson.annotations.SerializedName

data class SlotResponse(
    @SerializedName("doctor_id") val doctorId: Int,
    @SerializedName("slot_time") val slotTime: String,
    val id: Int
)