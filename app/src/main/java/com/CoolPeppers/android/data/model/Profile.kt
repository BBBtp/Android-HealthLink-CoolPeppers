package com.CoolPeppers.android.data.model

data class Profile(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val avatarUrl: String,
//    val password: String
)

interface ProfileController {
    suspend fun getProfile(): Profile
    suspend fun updateProfile(data: Profile): Boolean
    suspend fun updateAvatar(url: String): Boolean
}

class MockProfileController : ProfileController {
    private var currentProfile = Profile(
        id = 1,
        first_name = "Cool",
        last_name = "Peppers",
        email = "support@coolpeppers.com",
        phone = "+7 900 000-00-00",
        avatarUrl = "https://www.meme-arsenal.com/memes/5bfd716225affd016f78d5b2630c67e0.jpg"
    )

    override suspend fun getProfile(): Profile {
        return currentProfile
    }

    override suspend fun updateProfile(data: Profile): Boolean {
        currentProfile = data
        return true
    }

    override suspend fun updateAvatar(url: String): Boolean {
        currentProfile = currentProfile.copy(avatarUrl = url)
        return true
    }
}