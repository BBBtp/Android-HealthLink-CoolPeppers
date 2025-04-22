package com.CoolPeppers.android.data.repository

import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.Chat
import com.CoolPeppers.android.data.model.CreateChatRequest
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getChatById(chatId: Int): Chat {
        return apiService.getChatById(chatId = chatId)
    }

    suspend fun getUserChats(
    ): List<Chat> {
        return apiService.getUserChats(
        )
    }

    // Создание нового чата
    suspend fun createChat(
        doctorId: Int,
        userId: Int
    ): Chat {
        val request = CreateChatRequest(
            doctorId = doctorId,
            userId = userId
        )
        return apiService.createChat(request)
    }
}
