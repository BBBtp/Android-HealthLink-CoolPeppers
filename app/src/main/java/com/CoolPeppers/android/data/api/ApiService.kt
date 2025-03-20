package com.CoolPeppers.android.data.api

import com.CoolPeppers.android.data.model.Clinic
import retrofit2.http.GET
import retrofit2.http.Query

interface  ApiService {

    //Методы для клиник
    @GET("/api/v1/clinics/")
    suspend fun getClinics(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100,
        @Query("search") search: String = ""
    ): List<Clinic>
}