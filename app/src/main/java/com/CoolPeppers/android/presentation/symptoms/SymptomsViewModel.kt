package com.CoolPeppers.android.presentation.symptoms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.CoolPeppers.android.data.model.MatchResult
import com.CoolPeppers.android.data.model.Symptom
import com.CoolPeppers.android.data.repository.SymptomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SymptomsViewModel @Inject constructor(
    private val repository: SymptomRepository
) : ViewModel() {

    private val _symptoms = MutableLiveData<List<Symptom>>()
    val symptoms: LiveData<List<Symptom>> = _symptoms

    private val _matchedServices = MutableLiveData<String>()
    val matchedServices: LiveData<String> = _matchedServices

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadSymptoms() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _symptoms.value = repository.getSymptoms()
            } catch (e: Exception) {
                _error.value = e.message ?: "Произошла ошибка при загрузке симптомов"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun matchServices(symptoms: List<String>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = repository.matchServices(symptoms)
                _matchedServices.value = result.services
            } catch (e: Exception) {
                _error.value = e.message ?: "Произошла ошибка при поиске услуг"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 