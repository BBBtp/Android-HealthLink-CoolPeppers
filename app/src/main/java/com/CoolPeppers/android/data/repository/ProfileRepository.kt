package com.CoolPeppers.android.data.repository

import android.util.Log
import com.CoolPeppers.android.data.api.remote.ApiService
import com.CoolPeppers.android.data.model.User
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getUser(): Result<User> {
        return try {
            val user = apiService.getUser()
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun updateUser(
        firstName: String?,
        lastName: String?,
        age: Int?,
        bloodType: String?,
        photo: File?
    ): Result<User> {
        return try {
            Log.d("rep updateUser", "trying")
            val firstNameBody = firstName?.toRequestBody()
            val lastNameBody = lastName?.toRequestBody()
            val bloodTypeBody = bloodType?.toRequestBody()
            val ageBody = age?.toString()?.toRequestBody()
            Log.d("rep updateUser", "trying again")
            val photoPart = photo?.toMultipartBodyPart("photo")
            Log.d("rep updateUser", "trying upload photo")
            val updatedUser = apiService.updateUser(
                firstName = firstNameBody,
                lastName = lastNameBody,
                age = ageBody,
                bloodType = bloodTypeBody,
                photo = photoPart
            )
            Log.d("rep updateUser", "trying update user")
            Result.Success(updatedUser)
        } catch (e: Exception) {
            Log.d("rep updateUser", "caught exception")
            Result.Error(e.message ?: "Update failed")
        }
    }

//     Extension functions для преобразования данных
    private fun String.toRequestBody(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())

    private fun File.toMultipartBodyPart(partName: String): MultipartBody.Part =
        MultipartBody.Part.createFormData(
            partName,
            name,
            asRequestBody("image/*".toMediaTypeOrNull())
        )
}