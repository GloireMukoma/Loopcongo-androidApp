package com.example.loopcongo.models
data class Prestataire(
    val id: Int,
    val username: String,
    val email: String,
    val telephone: String?,
    val profession: String,
    val description: String?,
    val experience: Int?,
    val ville: String?,
    val adresse: String?,
    val disponibilite: String?,
    val note_moyenne: Float?,
    val nombre_avis: Int?,
    val photo_profil: String?
)

data class PrestataireResponse(
    val status: Boolean,
    val message: String,
    val data: List<Prestataire>
)

