package com.CoolPeppers.android.presentation.request

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.repository.AppointmentRepository
import com.CoolPeppers.android.data.repository.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
) : ViewModel() {

    private val _appointments = MutableLiveData<List<Appointment>>()

    val appointments: LiveData<List<Appointment>> get() = _appointments

    fun loadAppointments() {
        viewModelScope.launch {
            try {
                val result = appointmentRepository.getAppointments()
                _appointments.value = result
            } catch (e: Exception) {
                Log.e("AppointmentViewModel", "Ошибка при загрузке записей", e)
            }
        }
    }

    var selectedTab by mutableIntStateOf(0)
        private set

    fun selectTab(index: Int) {
        selectedTab = index
    }


}
