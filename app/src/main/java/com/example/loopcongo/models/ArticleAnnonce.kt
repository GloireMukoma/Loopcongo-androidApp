package com.example.loopcongo.models

data class ArticleAnnonce(
    val id: Int,
    val userId: Int,
    val titre: String,
    val description: String,
    val annonce_image: String,  // URL ou chemin image
    val dateDebut: String,
    val dateFin: String,
    val statut: String,
    val nomUser: String,
    val typeAccount: String,
    val contact: String,
    val city: String,
    val fileUrl: String        // URL photo profil user
)

data class AnnonceResponse(
    val status: Boolean,
    val message: String,
    val data: List<ArticleAnnonce>
)
