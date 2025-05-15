package com.CoolPeppers.android.presentation.chat

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.Message
import com.CoolPeppers.android.data.repository.ChatRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatDialogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val webSocketService: WebSocketService,
    private val chatRepository: ChatRepository
) : ViewModel() {
    private val _messages = mutableStateListOf<Message>()

    private val _messagesFlow = MutableStateFlow<List<Message>>(emptyList())
    val messagesFlow: StateFlow<List<Message>> = _messagesFlow.asStateFlow()

    private val _connectionState = mutableStateOf(false)
    val connectionState: State<Boolean> get() = _connectionState

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> get() = _loadingState

    private var webSocket: WebSocket? = null
    private val chatId: Int = savedStateHandle.get<String>("chatId")?.toIntOrNull() ?: 0
    private val _currentUserId = mutableStateOf<Int>(0)
    val currentUserId: Int get() = _currentUserId.value

    init {
        loadChatParticipants()
        connectWebSocket()
        loadInitialMessages()
    }

    fun loadChatParticipants() {
        viewModelScope.launch {
            Log.d("ChatViewModel", "Loading participants for chat $chatId")
            try {
                val chat = chatRepository.getChatById(chatId)
                _currentUserId.value = chat.user1.id
                Log.d("ChatViewModel", "Loaded chat: $chat")
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Error loading chat $chatId", e)
            }
        }
    }
    fun connectWebSocket() {
        _loadingState.value = true
        viewModelScope.launch {
            try {
                webSocket = webSocketService.connect(
                    userId = currentUserId,
                    onMessageReceived = { message ->
                        Log.d("WebSocket", "Received: $message")
                        handleReceivedMessage(message)
                    },
                    onConnectionClosed = {
                        Log.d("WebSocket", "Connection closed")
                        _connectionState.value = false
                    }
                )
                _connectionState.value = true
                Log.d("WebSocket", "Connected successfully")
            } catch (e: Exception) {
                Log.e("WebSocket", "Connection failed", e)
            } finally {
                _loadingState.value = false
            }
        }
    }

    private fun loadInitialMessages() {
        viewModelScope.launch {
            try {
                val initialMessages = webSocketService.getChatHistory(chatId) ?: emptyList()
                _messages.clear()
                _messages.addAll(initialMessages)
                _messagesFlow.value = _messages.toList()
            } catch (e: Exception) {
                Log.e("ChatViewModel", "Failed to load messages", e)
                _messages.clear()
                _messagesFlow.value = emptyList()
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val tempId = System.currentTimeMillis().toInt()
        val currentTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            .format(Date())

        val tempMessage = Message(
            id = tempId,
            chat_id = chatId,
            sender_id = currentUserId,
            text = text,
            created_at = currentTime,
            status = "sending"
        )
        _messages.add(tempMessage)
        _messagesFlow.value = _messages.toList()

        viewModelScope.launch {
            try {
                if (webSocket == null) {
                    connectWebSocket()
                }

                val messageRequest = JsonObject().apply {
                    addProperty("action", "send_message")
                    addProperty("chat_id", chatId)
                    addProperty("sender_id", currentUserId)
                    addProperty("text", text)
                }.toString()

                val isSent = webSocket?.send(messageRequest) ?: false

                if (isSent) {

                    launch {
                        delay(1000)
                        updateMessageStatus(tempId, "delivered")
                    }
                } else {
                    updateMessageStatus(tempId, "failed")
                }
            } catch (e: Exception) {
                updateMessageStatus(tempId, "failed")
            }
        }
    }
    private fun handleReceivedMessage(jsonMessage: String) {
        Log.d("WebSocket", "Получено сырое сообщение: $jsonMessage")
    }


    fun updateMessageStatus(messageId: Int, status: String) {
        val index = _messages.indexOfFirst { it.id == messageId }
        Log.i("ChatViewModel","$index  $status")
        if (index != -1) {
            val message = _messages[index]
            _messages[index] = message.copy(status = status)
            Log.i("ChatViewModel","$message  $status")
            _messagesFlow.value = _messages.toList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocket?.close(1000, "Activity destroyed")
    }
}
class WebSocketService @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val chatApi: ApiService
) {
    suspend fun connect(
        userId: Int,
        onMessageReceived: (String) -> Unit,
        onConnectionClosed: () -> Unit
    ): WebSocket = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("ws://79.137.192.44:8080/api/v1/ws/ws/$userId")
            .build()

        val webSocketListener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                onMessageReceived(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                onConnectionClosed()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                onConnectionClosed()
            }
        }

        okHttpClient.newWebSocket(request, webSocketListener)
    }

    suspend fun getChatHistory(chatId: Int): List<Message> {
        return try {
            chatApi.getChatById(chatId).messages ?: emptyList()
        } catch (e: Exception) {
            Log.e("WebSocketService", "Error loading chat history", e)
            emptyList()
        }
    }
}