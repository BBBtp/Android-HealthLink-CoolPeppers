package com.CoolPeppers.android.presentation.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Chat
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.model.Message
import com.CoolPeppers.android.data.repository.ClinicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


val LocalBottomBarVisibility = staticCompositionLocalOf { mutableStateOf(true) }

class ChatViewModel(private val repository: ClinicRepository) : ViewModel() {
    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _doctors.value = repository.fetchDoctors()
            _chats.value = repository.fetchChats(_doctors.value)
            _isLoading.value = false
        }
    }

    fun getDoctorById(id: Int): Doctor? {
        return _doctors.value.find { it.id == id }
    }
}

class ChatViewModelFactory(
    private val repository: ClinicRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            ChatViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
class ChatDialogViewModel : ViewModel() {
    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> get() = _messages

    fun sendMessage(text: String) {
        if (text.isNotBlank()) {
            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            _messages.add(Message(_messages.size + 1, text, currentTime, true))
        }
    }
}