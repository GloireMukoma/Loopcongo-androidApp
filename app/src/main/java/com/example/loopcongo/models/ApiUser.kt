package com.example.loopcongo.models

// Pour l’API
data class ApiUser(
    val id: Int,
    val type: String?,
    val type_account: String?,
    val username: String?,
    val is_certified: Int?,
    val contact: String?,
    val city: String?,
    val interets: String?, // Pour le compte client
    val file_url: String?,
    val about: String?,
    val created_at: String?
)

// LoginResponse générique
data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: ApiUser? // Un utilisateur ou un customer
)
