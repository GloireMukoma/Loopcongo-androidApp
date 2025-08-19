package com.example.loopcongo.database

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val data: User? // Contiendra l’utilisateur si connexion réussie
)
