package com.CoolPeppers.android.data.model

data class Chat(
    val id: Int,
    val user1: User,  // Первый участник чата
    val user2: User,  // Второй участник чата
    val createdAt: String,  // Дата создания чата
    val lastMessage: Message,  // Последнее сообщение в чате
    val messages: List<Message> = emptyList()  // Все сообщения в чате
)