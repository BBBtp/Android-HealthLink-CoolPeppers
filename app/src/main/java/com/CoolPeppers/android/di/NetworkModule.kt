package com.CoolPeppers.android.di

import android.content.Context
import com.CoolPeppers.android.data.api.local.TokenManager
import com.CoolPeppers.android.data.api.network.AuthInterceptor
import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.api.remote.ApiConstants
import com.CoolPeppers.android.data.repository.AppointmentRepository
import com.CoolPeppers.android.data.repository.AuthRepository
import com.CoolPeppers.android.data.repository.ChatRepository
import com.CoolPeppers.android.data.repository.ClinicRepository
import com.CoolPeppers.android.data.repository.DoctorRepository
import com.CoolPeppers.android.data.repository.ProfileRepository
import com.CoolPeppers.android.data.repository.ServiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(tokenManager: TokenManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val token = tokenManager.getAccessToken()
                
                if (token == null) {
                    return@addInterceptor chain.proceed(originalRequest)
                }

                val request = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDoctorRepository(apiService: ApiService): DoctorRepository {
        return DoctorRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideClinicRepository(apiService: ApiService): ClinicRepository {
        return ClinicRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideServiceRepository(apiService: ApiService): ServiceRepository {
        return ServiceRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideAppointmentRepository(apiService: ApiService): AppointmentRepository {
        return AppointmentRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(apiService: ApiService): ProfileRepository {
        return ProfileRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(apiService: ApiService): AuthRepository {
        return AuthRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideChatRepository(apiService: ApiService): ChatRepository {
        return ChatRepository(apiService)
    }
}
