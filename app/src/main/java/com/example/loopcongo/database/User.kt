package com.example.loopcongo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
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


