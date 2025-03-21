package com.CoolPeppers.android.data.model

data class Message(
    val id: Int,
    val text: String,
    val time: String,
    val isFromUser: Boolean
)