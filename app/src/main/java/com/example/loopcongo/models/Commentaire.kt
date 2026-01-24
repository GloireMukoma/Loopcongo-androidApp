package com.example.loopcongo.models

data class Commentaire(
    val id: Int,
    val commentaire: String,
    val time_ago: String?,
    val created_at: String?,

    val article_id: Int,

    val username: String?, // si l’API renvoie
    val user_avatar: String?,
    val user_type: String?,
    val user_id: Int,
    val contact: String?,
    val city: String?,
    val about: String?,
)

data class CommentaireResponse(
    val status: Boolean,
    val total: Int,
    val commentaires: List<Commentaire>
)

data class ApiCommentaireResponse(
    val status: Boolean,
    val message: String
)

data class PublierCommentaireRequest(
    val article_id: Int,
    val user_id: Int,
    val user_type: String, // "customer" | "vendeur"
    val commentaire: String
)




