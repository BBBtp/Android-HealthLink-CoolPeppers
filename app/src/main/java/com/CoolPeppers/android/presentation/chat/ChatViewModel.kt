package com.CoolPeppers.android.presentation.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Chat
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.model.Message
import com.CoolPeppers.android.data.model.User
import com.CoolPeppers.android.data.repository.ChatRepository
import com.CoolPeppers.android.data.repository.ProfileRepository
import com.CoolPeppers.android.data.repository.DoctorRepository
import com.CoolPeppers.android.data.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.CoolPeppers.android.presentation.profile.ProfileViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


val LocalBottomBarVisibility = staticCompositionLocalOf { mutableStateOf(true) }

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    private val chatRepository: ChatRepository,
) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState.asStateFlow()

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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
            // 2. Загружаем докторов
            _doctors.value = doctorRepository.getDoctors(
                skip = 0,
                limit = 20,
                search = "",
                serviceId = null,
                clinicId = null
            )

            // 3. Загружаем чаты пользователя
            val chats = chatRepository.getUserChats()
            _chats.value = chats

        } catch (e: Exception) {
            _error.value = "Ошибка загрузки данных: ${e.localizedMessage}"
            Log.e("ChatViewModel", "Error loading data", e)
        }
    }

    fun createChat(doctorId: Int) {
        viewModelScope.launch {
            try {
                _userState.value?.let { user ->
                    val newChat = chatRepository.createChat(
                        doctorId = doctorId,
                        userId = user.id
                    )
                    _chats.update { currentChats -> currentChats + newChat }
                } ?: run {
                    _error.value = "Пользователь не загружен"
                    Log.e("ChatViewModel", "User not loaded when creating chat")
                }
            } catch (e: Exception) {
                _error.value = "Ошибка создания чата: ${e.localizedMessage}"
                Log.e("ChatViewModel", "Error creating chat", e)
            }
        }
    }

    fun refreshData() {
        loadUserAndData()
    }
}
@HiltViewModel
class ChatDialogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> get() = _messages

    fun sendMessage(text: String) {
        if (text.isNotBlank()) {
            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            _messages.add(Message(_messages.size + 1, text, currentTime, true))
        }
    }
}