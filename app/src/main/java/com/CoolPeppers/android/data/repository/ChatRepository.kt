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

    suspend fun createChat(user1Id: Int, user2Id: Int) {
        return apiService.createChat(user1Id = user1Id , user2Id = user2Id)
    }
}
