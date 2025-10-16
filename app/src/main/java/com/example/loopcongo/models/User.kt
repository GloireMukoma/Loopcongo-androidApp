package com.example.loopcongo.models

// NB:  User = Vendeur
data class User(
    val id: Int,
    val type_account: String?,
    val nom: String?,
    val username: String?,
    val nb_abonner: Int?,
    val subscription_type: String?,
    val subscription_prix: Double?,
    val subscription_status: String?,
    val subscription_start: String?,
    val subscription_end: String?,
    val is_certified: Int?,
    val password: String?,
    val contact: String?,
    val city: String?,
    val file_url: String?,
    val about: String?,
    val created_at: String?,

    // Champs supplémentaires renvoyés par les jointures
    val profile_image: String?,
    val article_id: Int?,
    val article_nom: String?,
    val article_image: String?,
    val article_date: String?,
    val total_articles: Int?,
    val total_likes: Int?
)

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

data class UserData(
    val user: User,
    val articles: List<Article>
)

// Utilisé par la popup qui affiche un user premium et ses articles
data class RandomPremiumUserResponse(
    val status: Boolean,
    val message: String,
    val user: User?,
    val articles: List<Article>?
)



