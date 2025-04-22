package com.CoolPeppers.android.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    val age: Int?,
    @SerializedName("blood_type") val bloodType: String?,
    @SerializedName("photo_url") val photoUrl: String?
)
