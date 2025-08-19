package com.example.loopcongo.models

data class Commande(
    val commande_id: Int,
    val status: String,
    val message: String,
    val created_at: String,
    val acheteur_nom: String,
    val acheteur_telephone: String?,
    val article_nom: String,
    val article_prix: Double,
    val article_devise: String,
    val article_image: String
)


data class CommandeResponse(
    val status: Boolean,
    val vendeur_id: Int,
    val commandes: List<Commande>
)
