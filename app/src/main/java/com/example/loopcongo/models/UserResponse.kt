package com.example.loopcongo.models

// Ce modele gere les utilisateurs ainsi que leurs dernières articles qu'ils ont publié
data class UserResponse(
    val status: Boolean,
    val message: String,
    val data: List<User>
)

data class User(
    val id: Int,
    val username: String,
    val nb_abonner: Int,
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
    val article_date: String?,
    val total_articles: Int
)
