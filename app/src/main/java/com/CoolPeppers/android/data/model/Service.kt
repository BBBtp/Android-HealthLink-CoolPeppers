package com.CoolPeppers.android.data.model

import com.google.gson.annotations.SerializedName

data class Service(
    val name: String,
    val description: String?,
    val price: Int?,
    val duration: Int?,
    @SerializedName("logo_url") val logoUrl: String?,
    val id: Int
)