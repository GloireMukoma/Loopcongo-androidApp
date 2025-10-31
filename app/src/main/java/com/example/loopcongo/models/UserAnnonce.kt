package com.example.loopcongo.models

data class UserAnnonce(
    val id: Int,
    val user_id: Int,
    val titre: String,
    val description: String,
    val image: String,  // URL ou chemin image
    val date_debut: String,
    val date_fin: String,
    val statut: String,
    val username: String, // Nom de l'utilisateur (vendeur)
    val type_account: String,
    val contact: String,
    val city: String,
    val file_url: String,     // URL photo profil user
    val created_at: String
)

data class AnnonceResponse(
    val status: Boolean,
    val message: String,
    val data: List<UserAnnonce>
)
