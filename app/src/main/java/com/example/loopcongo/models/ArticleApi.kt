package com.example.loopcongo.models
data class ArticleApi(
    val id: Int,
    val account_id: Int,
    val categorie_id: Int,
    val nom: String,
    val is_sponsored: Int,
    val boost_type: String?,
    val sponsored_until: String?,
    val prix: String,
    val devise: String,
    val nb_like: Int,
    val about: String,
    val file_url: String,
    val created_at: String
)
