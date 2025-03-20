package com.CoolPeppers.android.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitClient @Inject constructor() {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://79.137.192.44:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}