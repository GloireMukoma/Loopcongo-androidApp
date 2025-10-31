package com.example.loopcongo.models
data class Article(
    val id: Int,
    val username: String?,
    val account_id: Int,
    val user_nom: String,
    val categorie_id: Int,
    val nom: String,
    val is_sponsored: Int,
    val is_boosted: Int,
    val boosted_until: String?,
    val prix: String,
    val devise: String,
    val nb_like: String,
    val about: String,
    val file_url: String,
    val user_avatar: String,
    val user_contact: String,
    val created_at: String
)

data class ArticleResponse(
    val status: Boolean,
    val message: String,
    val data: List<Article>
)
// Classe qui represente les images de detail de l'article (API REST)
data class DetailImage(
    val id: Int,
    val product_id: Int,
    val file_url: String
)


