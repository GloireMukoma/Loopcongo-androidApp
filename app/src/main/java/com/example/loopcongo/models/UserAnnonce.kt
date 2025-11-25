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

    // Infos de l'user
    val type_account: String?,
    val nom: String?,
    val username: String?,
    val nb_abonner: Int?,
    val subscription_type: String?,
    val subscription_prix: Double?,
    val subscription_status: String?,
    val subscription_start: String?,
    val subscription_end: String?,
    val is_certified: String?,
    val password: String?,
    val contact: String?,
    val city: String?,
    val file_url: String?,
    val about: String?,
    val created_at: String?,
)

data class AnnonceResponse(
    val status: Boolean,
    val message: String,
    val data: List<UserAnnonce>
)
