package com.example.loopcongo.models

class Server (
    val id: Int,
    val name: String,
    val subscribers_count: Int,
    val image: String // URL de l’image du serveur
)


data class ServerCategorie(
    val id: Int,
    val name: String,
    val icon: String,

    val subscribers_count: Int,
    val image: String // URL de l’image du serveur
)
