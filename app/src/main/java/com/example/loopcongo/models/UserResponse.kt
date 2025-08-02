package com.example.loopcongo.models

data class UserResponse(
    val status: Boolean,
    val message: String,
    val data: List<User>
)

data class User(
    val id: Int,
    val nom: String,
    val type_account: String,
    val contact: String,
    val city: String,
    val profile_image: String?,
    val about: String?,
    val is_sponsored: Int,
    val boost_type: String?,
    val sponsored_until: String?,
    val article_id: Int?,
    val article_nom: String?,
    val article_image: String?,
    val article_date: String?
)
