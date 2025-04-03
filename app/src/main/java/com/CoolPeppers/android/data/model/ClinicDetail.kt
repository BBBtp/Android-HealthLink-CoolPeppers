package com.CoolPeppers.android.data.model

data class ClinicDetail(
    val id: Int,
    val name: String,
    val city: String,
    val address: String,
    val metro: String,
    val schedule: String,
    val info: String,
    val rating: Int,
    val price: Int,
    val year: Int,
    val clients: Int,
    val reviews: Int,
    val image: Int
)