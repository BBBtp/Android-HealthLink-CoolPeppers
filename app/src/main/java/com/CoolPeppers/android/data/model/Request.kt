package com.CoolPeppers.android.data.model

data class Request(
    val id: Int,
    val doctor: Doctor,
    val date: String,
    val timeRange: String
)