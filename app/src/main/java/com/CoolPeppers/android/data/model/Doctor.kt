package com.CoolPeppers.android.data.model

data class Doctor(
    val firstName: String,
    val lastName: String,
    val specialization: String,
    val photoUrl: String?,
    val rating: Float,
    val id: Int,
    val description: String?,
    val experience: Int?,
    val customerCount: Int?,
    val reviewsCount: Int?
)