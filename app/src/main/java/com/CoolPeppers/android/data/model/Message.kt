package com.CoolPeppers.android.data.model

data class Message(
    val id: Int,
    val chat_id: Int,
    val sender_id: Int,
    val text: String,
    val created_at: String,
    val status: String,
)
