package com.example.loopcongo.models

data class Immobilier(
    val id: Int,
    val account_id: Int,
    val typeimmo: String,
    val statut: String,
    val city: String,
    val quartier: String,
    val prix: String,
    val address: String,
    val about: String,
    val file_url: String,

    // Utiliser pour afficher les items des villes sur la page d'accueil immobiliers
    val cityName: String,
    val nbImmoPublish: String,
    val imgUrl: String,

    val username: String,
    val userImage: String,
    val user_avatar: String,
    val contact: String
)

data class ImmobilierResponse(
    val status: Boolean,
    val message: String,
    val data: List<Immobilier>
)

data class ImmobilierResponse2(
    val status: Int,
    val city: String,
    val count: Int,
    val data: List<Immobilier>
)

data class ImmoUserDemande(
    val demande_id: Int,
    val user_id: Int,
    val username: String,
    val contact: String,
    val message: String,
    val avatar: String?,
    val created_at: String
)

data class ApiResponseDemande(
    val status: String,
    val total: Int,
    val demandes: List<ImmoUserDemande>
)

data class ItemCityImmo(
    val int: Int,
    val cityName: String,
    val nbImmoPublish: String,
    val imgUrl: Int
)

