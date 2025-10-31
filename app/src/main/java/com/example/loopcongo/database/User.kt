package com.example.loopcongo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    val type: String?, // Verifier si c'est un compte client ou vendeur(vendeur et immobilier)
    val type_account: String?,
    val username: String?,
    val is_certified: Int?,
    val contact: String?,
    val city: String?,
    val file_url: String?,
    val about: String?,
    val created_at: String?,
)


