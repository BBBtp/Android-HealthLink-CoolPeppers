package com.CoolPeppers.android.presentation.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Chat
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.model.Message
import com.CoolPeppers.android.data.repository.ClinicRepository
import com.CoolPeppers.android.data.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


val LocalBottomBarVisibility = staticCompositionLocalOf { mutableStateOf(true) }

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository
) : ViewModel() {
    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Загружаем докторов из репозитория
                _doctors.value = doctorRepository.getDoctors(
                    skip = 0,
                    limit = 20,
                    search = "",
                    serviceId = null,
                    clinicId = null
                )

                // Создаем моковые чаты на основе загруженных докторов
                _chats.value = createMockChats(_doctors.value)
            } catch (e: Exception) {
                // Обработка ошибок
                Log.e("ChatViewModel", "Error loading data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun createMockChats(doctors: List<Doctor>): List<Chat> {
        return doctors.mapIndexed { index, doctor ->
            Chat(
                id = index + 1,
                doctor = doctor,
                lastMessage = Message(
                    id = 1,
                    text = when (index % 3) {
                        0 -> "Добрый день! Как ваше самочувствие?"
                        1 -> "Результаты анализов готовы"
                        else -> "Напоминаю о записи на завтра"
                    },
                    time = when (index % 4) {
                        0 -> "10:30"
                        1 -> "Вчера"
                        2 -> "5 мая"
                        else -> "2 недели назад"
                    },
                    isFromUser = index % 2 == 0
                ),
            )
        }
    }

    fun getDoctorById(id: Int): Doctor? {
        return _doctors.value.find { it.id == id }
    }
}
@HiltViewModel
class ChatDialogViewModel @Inject constructor() : ViewModel() {
    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> get() = _messages

    fun sendMessage(text: String) {
        if (text.isNotBlank()) {
            val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            _messages.add(Message(_messages.size + 1, text, currentTime, true))
        }
    }
}