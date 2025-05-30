package com.CoolPeppers.android.data.repository

import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.MatchResult
import com.CoolPeppers.android.data.model.Symptom
import com.CoolPeppers.android.data.model.SymptomInput
import javax.inject.Inject

class SymptomRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getSymptoms(): List<Symptom> {
        return apiService.getSymptoms()
    }

    suspend fun matchServices(symptoms: List<String>): MatchResult {
        return apiService.matchServices(SymptomInput(symptoms))
    }
} 