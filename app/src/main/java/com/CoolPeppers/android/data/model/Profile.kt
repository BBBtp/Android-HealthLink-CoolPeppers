package com.CoolPeppers.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Query

@Serializable
data class Profile(
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
    @SerialName("first_name") val first_name: String? = null,
    @SerialName("last_name") val last_name: String? = null,
    @SerialName("blood_type") val bloodType: String? = null,
    @SerialName("age") val age: Int? = null,
    @SerialName("photo_url") val avatarUrl: String? = null,
)

interface ProfileApi {
    @GET("/api/v1/users/me")
    suspend fun getProfile(): Response<Profile>
    @PUT("/api/v1/users/me")
    suspend fun putProfile(): Response<Profile>
}

interface ProfileController {
    suspend fun getProfile(): Profile
    suspend fun putProfile(data: Profile): Boolean
    suspend fun updateAvatar(url: String): Boolean
}

class MockProfileController : ProfileController {
    private var currentProfile = Profile(
        username = "coolpepper",
        first_name = "Cool",
        last_name = "Peppers",
        email = "support@coolpeppers.com",
        avatarUrl = "https://www.meme-arsenal.com/memes/5bfd716225affd016f78d5b2630c67e0.jpg"
    )

    override suspend fun getProfile(): Profile {
        return currentProfile
    }

    override suspend fun putProfile(data: Profile): Boolean {
        currentProfile = data
        return true
    }

    override suspend fun updateAvatar(url: String): Boolean {
        currentProfile = currentProfile.copy(avatarUrl = url)
        return true
    }
}