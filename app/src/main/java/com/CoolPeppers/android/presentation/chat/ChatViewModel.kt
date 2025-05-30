package com.CoolPeppers.android.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Chat
import com.CoolPeppers.android.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _refreshTrigger = MutableStateFlow(0)
    init {
        loadUserAndData()
    }

    private fun loadUserAndData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                loadDoctorsAndChats()

            } catch (e: Exception) {
                _error.value = "Ошибка загрузки пользователя: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadDoctorsAndChats() {
        try {

            val chats = chatRepository.getUserChats()
            _chats.value = chats
            Log.d("ChatViewModel", "$chats")

        } catch (e: Exception) {
            _error.value = "Ошибка загрузки данных: ${e.localizedMessage}"
            Log.e("ChatViewModel", "Error loading data", e)
        }
    }

    fun refreshData() {
        _refreshTrigger.value++
        loadUserAndData()
    }
}