package com.example.loopcongo.models
data class Article(
    val nom: String,
    val prix: String,
    val auteur: String,
    val articleTimeAgo: String,
    val views: Int,
    val likes: Int,
    val comments: Int,
    val imageResId: Int
)
