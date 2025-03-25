package com.CoolPeppers.android.presentation.clinic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Clinic
import com.CoolPeppers.android.data.repository.ClinicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicViewModel @Inject constructor(
    private val clinicRepository: ClinicRepository,
) : ViewModel() {

    private val _clinics = MutableLiveData<List<Clinic>>()

    val clinics: LiveData<List<Clinic>> get() = _clinics


    private val _clinic =  MutableLiveData<List<Clinic>>()
    val clinic: LiveData<List<Clinic>> get() = _clinic

    fun loadClinics(skip: Int, limit: Int, search: String) {
        viewModelScope.launch {
            try {
                val result = clinicRepository.getClinics(
                    skip = skip,
                    limit = limit,
                    search = search,
                )
                _clinics.value = result
            } catch (e: Exception) {
                Log.e("ClinicViewModel", "Ошибка при загрузке клиник", e)
            }
        }
    }
    fun loadClinicByID(clinicId: Int) {
        viewModelScope.launch {
            try {
                val result = clinicRepository.getClinicsById(
                    clinicId = clinicId,
                )
                _clinic.postValue(listOf(result))
            } catch (e: Exception) {
                Log.e("ClinicViewModel", "Ошибка при загрузке клиник", e)
            }
        }
    }
}

