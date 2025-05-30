package com.CoolPeppers.android.data.remote

import com.CoolPeppers.android.data.model.*
import retrofit2.http.*

interface ApiService {
    @GET("symptoms/")
    suspend fun getSymptoms(): List<Symptom>

    @POST("match-services")
    suspend fun matchServices(@Body payload: SymptomInput): MatchResult
} 