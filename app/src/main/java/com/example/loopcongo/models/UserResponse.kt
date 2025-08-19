package com.example.loopcongo.models

// Ce modele gere les utilisateurs ainsi que leurs dernières articles qu'ils ont publié
data class UserResponse(
    val status: Boolean,
    val message: String,
    val data: List<User>
)

// Est utiliser quand l'utilisateur veut se connecter
// L'API renvoie l'unique user qui s'est connecter
data class UserUniqueResponse(
    val status: Boolean,
    val message: String,
    val data: User // objet unique, null si login échoue
)

data class User(
    val id: Int,
    val username: String,
    val nb_abonner: Int,
    val type_account: String,
    val contact: String,
    val city: String,
    val profile_image: String?,
    val about: String?,
    val is_sponsored: Int,
    val boost_type: String?,
    val sponsored_until: String?,
    val article_id: Int?,
    val article_nom: String?,
    val article_image: String?,
    val article_date: String?,
    val total_articles: Int,
    // champ pour les faire correspondre aux données de l'api
    // qu'il nous envoie quand l'utilisateur s'est connecter (login activité)
    val nom: String,
    val password: String,
    val file_url: String
)
