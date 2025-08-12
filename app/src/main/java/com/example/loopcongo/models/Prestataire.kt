package com.example.loopcongo.models

import java.io.Serializable

data class Prestataire(
    val id: Int,
    val username: String,
    val is_sponsored: Int,
    val sponsored_until: String?,
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
    val photo_profil: String?,
    val publications: List<Prestation> = emptyList()  // ajout des publications
) : Serializable

data class PrestataireResponse(
    val status: Boolean,
    val message: String,
    val data: Prestataire
)

data class PrestataireListResponse(
    val status: Boolean,
    val message: String,
    val data: List<Prestataire>
)


