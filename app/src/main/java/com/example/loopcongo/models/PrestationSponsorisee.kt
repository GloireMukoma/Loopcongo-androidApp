package com.example.loopcongo.models

data class PrestationSponsorisee(
    val id: Int,
    val prestataire_id: Int,
    val titre: String,
    val description: String,
    val image: String?,
    val date_publication: String,
    val prestataire_nom: String,
    val photo_profil: String?,
    val prestataire_profession: String
)

data class PrestationSponsoriseesResponse(
    val status: Boolean,
    val message: String,
    val data: List<PrestationSponsorisee>
)
