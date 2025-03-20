package com.CoolPeppers.android.data.model

data class Clinic(
    val name: String,
    val address: String,
    val logoUrl: String?,
    val id: Int,
    val description: String,
    val rating: Float,
    val metro: String?,
    val price: String?,
    val workTime: String?,
    val yearFoundation: Int?,
    val customersCount: Int?,
    val reviewsCount: Int?
)