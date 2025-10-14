package com.example.loopcongo.models
data class Vendeur(
    val id: Int,
    val type_account: String,
    val nom: String,
    val nb_abonner: Int,
    val total_articles: Int,
    val total_likes: Int,
    val is_certified: Int,
    val is_boosted: Int,
    val boosted_until: String?,
    val password: String?,        // Facultatif si jamais tu ne veux pas l'utiliser
    val contact: String?,
    val city: String?,
    val file_url: String?,
    val about: String?
)

data class VendeurResponse(
    val status: Boolean,
    val message: String,
    val data: List<Vendeur>
)

