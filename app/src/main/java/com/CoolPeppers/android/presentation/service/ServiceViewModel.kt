package com.CoolPeppers.android.presentation.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.Service
import com.CoolPeppers.android.data.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
) : ViewModel() {

    private val _services = MutableLiveData<List<Service>>()

    val services: LiveData<List<Service>> get() = _services




    fun loadService(skip: Int, limit: Int, search: String,clinicId: Int?) {
        viewModelScope.launch {
            try {
                val result = serviceRepository.getServices(
                    skip = skip,
                    limit = limit,
                    search = search,
                    clinicId = clinicId,
                )
                _services.value = result
            } catch (e: Exception) {
                Log.e("ClinicViewModel", "Ошибка при загрузке услуг", e)
            }
        }
    }
}
