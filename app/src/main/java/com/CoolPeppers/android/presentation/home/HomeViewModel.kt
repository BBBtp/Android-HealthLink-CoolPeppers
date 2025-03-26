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
    val slots: LiveData<Map<Int, SlotResponse>> get() = _slots
    val doctors: LiveData<Map<Int, Doctor>> get() = _doctors
    val appointments: LiveData<List<Appointment>> get() = _appointments

    fun getAppointments() {
        viewModelScope.launch {
            try{
                val result = appointmentRepository.getAppointments()
                val doctorMap = mutableMapOf<Int, Doctor>()
                val slotMap = mutableMapOf<Int, SlotResponse>()
                result.forEach { appointment ->
                    val doctor = doctorRepository.getDoctorById(appointment.doctorId)
                    val slot = appointmentRepository.getSlotById(appointment.appointmentSlotId)
                    doctorMap[appointment.doctorId] = doctor
                    slotMap[appointment.appointmentSlotId] = slot
                }
                _appointments.value = result
                _doctors.value = doctorMap
                _slots.value = slotMap
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Ошибка при загрузке записей", e)
            }

        }
    }


}