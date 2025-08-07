package com.example.loopcongo.models

data class PrestataireAnnonce(
    val id: Int,
    val prestataire_id: Int,
    val titre: String,
    val description: String,
    val image: String,
    val date_publication: String,
    val is_active: Int,
    val prestataire_nom: String,
    val annonce_image: String,
    val photo_profil: String
)

data class AnnoncePrestataireResponse(
    val status: String,
    val data: List<PrestataireAnnonce>
)
