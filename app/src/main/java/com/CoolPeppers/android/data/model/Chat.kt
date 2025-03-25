package com.CoolPeppers.android.data.model

data class Chat (
    val id: Int,
    val doctor: Doctor,
    val lastMessage: Message,
)
