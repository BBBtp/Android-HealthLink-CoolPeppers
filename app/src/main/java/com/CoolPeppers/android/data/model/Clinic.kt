package com.CoolPeppers.android.data.model

import com.google.gson.annotations.SerializedName

data class Clinic(
    val name: String,
    val address: String,
    @SerializedName("logo_url") val logoUrl: String?,
    val id: Int,
    val description: String,
    val rating: Float,
    val metro: String?,
    val price: String?,
    @SerializedName("work_time") val workTime: String?,
    @SerializedName("year_foundation") val yearFoundation: Int?,
    @SerializedName("customers_count") val customersCount: Int?,
    @SerializedName("reviews_count") val reviewsCount: Int?
)