package com.CoolPeppers.android.presentation.appointment

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.data.model.SlotResponse
import com.CoolPeppers.android.data.repository.ClinicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _slots = MutableLiveData<List<SlotResponse>>()
    val slots: LiveData<List<SlotResponse>> get() = _slots

    fun loadSlots(doctorId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getSlots(doctorId)
                _slots.value = response
            } catch (e: Exception) {

            }
        }
    }

    fun createAppointment(clinicId: Int,serviceId: Int, doctorId: Int, slotId: Int,) {
        viewModelScope.launch {
            try {
                apiService.createAppointment(
                    clinicId = clinicId,
                    doctorId = doctorId,
                    serviceId = serviceId,
                    slotId = slotId
                )
                // Handle success
            } catch (e: Exception) {
                Log.e("ClinicViewModel", "Ошибка при загрузке", e)
            }
        }
    }
}