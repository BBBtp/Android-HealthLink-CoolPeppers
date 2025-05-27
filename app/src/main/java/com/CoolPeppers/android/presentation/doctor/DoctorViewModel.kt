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

    private val _doctor = MutableLiveData<List<Doctor>>()
    val doctor: LiveData<List<Doctor>> get() = _doctor

    // Состояния пагинации
    private var currentPage = 0
    private val pageSize = 20
    private var isLastPage = false
    private var isPageLoading = false

    // Состояния загрузки и ошибок
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadDoctors(skip: Int, limit: Int, search: String, serviceId: Int?, clinicId: Int?) {
        if (isPageLoading || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            isPageLoading = true

            try {
                val result = doctorRepository.getDoctors(
                    skip = skip,
                    limit = limit,
                    search = search,
                    serviceId = serviceId,
                    clinicId = clinicId
                )
                
                if (result.isEmpty()) {
                    isLastPage = true
                } else {
                    val currentList = if (skip == 0) {
                        result
                    } else {
                        (_doctors.value ?: emptyList()) + result
                    }
                    _doctors.value = currentList
                    currentPage++
                }
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Ошибка при загрузке врачей", e)
                _error.value = "Не удалось загрузить врачей. Попробуйте позже."
            } finally {
                _isLoading.value = false
                isPageLoading = false
            }
        }
    }

    fun loadMoreDoctors(search: String, serviceId: Int?, clinicId: Int?) {
        loadDoctors(
            skip = currentPage * pageSize,
            limit = pageSize,
            search = search,
            serviceId = serviceId,
            clinicId = clinicId
        )
    }

    fun resetPagination() {
        currentPage = 0
        isLastPage = false
        isPageLoading = false
        _doctors.value = emptyList()
        _error.value = null
    }

    fun loadDoctorByID(doctorId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val result = doctorRepository.getDoctorById(doctorId = doctorId)
                _doctor.value = listOf(result)
            } catch (e: Exception) {
                Log.e("DoctorViewModel", "Ошибка при загрузке информации о докторе", e)
                _error.value = "Не удалось загрузить информацию о докторе"
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        resetPagination()
    }
}