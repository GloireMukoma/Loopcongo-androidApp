package com.example.loopcongo.models
data class UserProfile(
    val id: Int,
    val nom: String,
    val is_sponsored: Int,
    val is_boosted: Int,
    val boosted_until: String?,
    val type_account: String,
    val contact: String,
    val city: String,
    val file_url: String,
    val about: String,
)

data class UserProfileResponse(
    val status: Boolean,
    val message: String,
    val data: List<UserProfile>
)

data class UserData(
    val user: User,
    val articles: List<Article>
)
