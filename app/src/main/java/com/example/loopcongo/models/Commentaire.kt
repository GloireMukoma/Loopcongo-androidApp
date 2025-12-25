package com.example.loopcongo.models

data class Commentaire(
    val id: Int,
    val article_id: Int,
    val user_id: Int,
    val username: String?, // si l’API renvoie
    val user_avatar: String?,
    val commentaire: String,
    val created_at: String?,
    val time_ago: String?
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
    val commentaire: String
)



