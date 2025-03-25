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
    private val _doctor = MutableLiveData<Doctor>()
    private val _appointments = MutableLiveData<List<Appointment>>()
    private val _slot = MutableLiveData<SlotResponse>()
    val slot: LiveData<SlotResponse> get() = _slot
    val doctor: LiveData<Doctor> get() = _doctor
    val appointments: LiveData<List<Appointment>> get() = _appointments

    fun getAppointments() {
        viewModelScope.launch {
            try{
                val result = appointmentRepository.getAppointments()

                result.map { appointment ->
                    val doctor = doctorRepository.getDoctorById(appointment.doctorId)
                    val slot = appointmentRepository.getSlotById(appointment.appointmentSlotId)
                    _doctor.value = doctor
                    _slot.value = slot
                }
                _appointments.value = result
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Ошибка при загрузке записей", e)
            }

        }
    }


}