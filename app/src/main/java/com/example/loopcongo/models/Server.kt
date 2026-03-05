package com.example.loopcongo.models

class Server (
    val id: Int,
    val name: String,
    val subscribers_count: Int,
    val image: String, // URL de l’image du serveur
    val membres_count: Int
)

data class BasicResponse(
    val status: Boolean,
    val message: String
)

data class ServerResponse(
    val status: Int,
    val data: List<Server>
)

data class ServerCategorie(
    val id: Int,
    val name: String,
    val icon: String,

    val subscribers_count: Int,
    val image: String // URL de l’image du serveur
)

data class ServerMessage(
    val id: Int,
    val message: String,
    val created_at: String,
    val user_id: Int,
    val username: String,
    // photo de profil de l'utilisateur
    val file_url: String?
)

