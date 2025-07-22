package com.example.loopcongo.models
data class Vendeur(
    val nom: String,
    val description: String,
    val nbArticlePublie: Int,
    val imageResId: Int,
    val type: String // "article" ou "immobilier"
)
