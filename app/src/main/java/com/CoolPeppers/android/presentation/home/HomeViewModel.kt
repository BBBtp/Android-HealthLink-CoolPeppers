package com.CoolPeppers.android.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Appointment
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.model.SlotResponse
import com.CoolPeppers.android.data.repository.AppointmentRepository
import com.CoolPeppers.android.data.repository.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    private val _doctors = MutableLiveData<Map<Int,Doctor>>()
    private val _appointments = MutableLiveData<List<Appointment>>()
    private val _slots = MutableLiveData<Map<Int,SlotResponse>>()
    
    // Состояния загрузки
    private val _isLoadingAppointments = MutableLiveData<Boolean>()
    private val _isLoadingDoctors = MutableLiveData<Boolean>()
    private val _isLoadingSlots = MutableLiveData<Boolean>()
    
    // Состояния ошибок
    private val _appointmentsError = MutableLiveData<String?>()
    private val _doctorsError = MutableLiveData<String?>()
    private val _slotsError = MutableLiveData<String?>()

    val slots: LiveData<Map<Int, SlotResponse>> get() = _slots
    val doctors: LiveData<Map<Int, Doctor>> get() = _doctors
    val appointments: LiveData<List<Appointment>> get() = _appointments
    
    // Публичные состояния загрузки
    val isLoadingAppointments: LiveData<Boolean> get() = _isLoadingAppointments
    val isLoadingDoctors: LiveData<Boolean> get() = _isLoadingDoctors
    val isLoadingSlots: LiveData<Boolean> get() = _isLoadingSlots
    
    // Публичные состояния ошибок
    val appointmentsError: LiveData<String?> get() = _appointmentsError
    val doctorsError: LiveData<String?> get() = _doctorsError
    val slotsError: LiveData<String?> get() = _slotsError

    fun getAppointments() {
        viewModelScope.launch {
            _isLoadingAppointments.value = true
            _appointmentsError.value = null
            
            try {
                val result = appointmentRepository.getAppointments()
                val doctorMap = mutableMapOf<Int, Doctor>()
                val slotMap = mutableMapOf<Int, SlotResponse>()
                
                // Параллельная загрузка данных
                val doctorDeferred = result.map { appointment ->
                    async { doctorRepository.getDoctorById(appointment.doctorId) }
                }
                val slotDeferred = result.map { appointment ->
                    async { appointmentRepository.getSlotById(appointment.appointmentSlotId) }
                }
                
                // Ожидаем загрузку всех данных
                val doctors = doctorDeferred.map { it.await() }
                val slots = slotDeferred.map { it.await() }
                
                // Заполняем мапы
                result.forEachIndexed { index, appointment ->
                    doctorMap[appointment.doctorId] = doctors[index]
                    slotMap[appointment.appointmentSlotId] = slots[index]
                }
                
                _appointments.value = result
                _doctors.value = doctorMap
                _slots.value = slotMap
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Ошибка при загрузке записей", e)
                _appointmentsError.value = "Не удалось загрузить записи. Попробуйте позже."
            } finally {
                _isLoadingAppointments.value = false
            }
        }
    }

    fun retryLoading() {
        getAppointments()
    }
}