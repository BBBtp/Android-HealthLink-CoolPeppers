package com.CoolPeppers.android.data.api.remote

import android.content.Context
import com.CoolPeppers.android.data.api.local.TokenManager
import com.CoolPeppers.android.data.api.network.AuthInterceptor
import com.CoolPeppers.android.data.repository.AuthRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    context: Context,
    tokenManager: TokenManager,
    authRepository: AuthRepository
) {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenManager, authRepository))
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
