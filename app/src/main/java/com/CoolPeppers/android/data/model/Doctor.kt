package com.CoolPeppers.android.data.model

import com.google.gson.annotations.SerializedName

data class Doctor(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    val specialization: String,
    @SerializedName("photo_url") val photoUrl: String?,
    val rating: Float,
    val id: Int,
    val description: String?,
    val experience: Int?,
    @SerializedName("customer_count") val customerCount: Int?,
    @SerializedName("reviews_count") val reviewsCount: Int?
)