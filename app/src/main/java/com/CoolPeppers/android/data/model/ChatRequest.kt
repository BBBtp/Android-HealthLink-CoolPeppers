package com.CoolPeppers.android.data.model

import com.google.gson.annotations.SerializedName

data class CreateChatRequest(
    @SerializedName("doctor_id")
    val doctorId: Int,

    @SerializedName("user_id")
    val userId: Int,

)