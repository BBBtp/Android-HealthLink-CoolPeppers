package com.CoolPeppers.android.presentation.doctor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Doctor
import com.CoolPeppers.android.data.repository.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val doctorRepository: DoctorRepository,
) : ViewModel() {

    private val _doctors = MutableLiveData<List<Doctor>>()

    val doctors: LiveData<List<Doctor>> get() = _doctors

    private val _doctor =  MutableLiveData<List<Doctor>>()
    val doctor: LiveData<List<Doctor>> get() = _doctor

    fun loadDoctors(skip: Int, limit: Int, search: String, serviceId: Int?, clinicId: Int?) {
        viewModelScope.launch {
            try {
                val result = doctorRepository.getDoctors(
                    skip = skip,
                    limit = limit,
                    search = search,
                    serviceId = serviceId,
                    clinicId = clinicId
                )
                _doctors.value = result
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Ошибка при загрузке врачей", e)
            }
        }
    }
    fun loadDoctorByID(doctorId: Int) {
        viewModelScope.launch {
            try {
                val result = doctorRepository.getDoctorById(
                    doctorId = doctorId,
                )
                _doctor.postValue(listOf(result))
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Ошибка при загрузке информации о докторе", e)
            }
        }
    }
}