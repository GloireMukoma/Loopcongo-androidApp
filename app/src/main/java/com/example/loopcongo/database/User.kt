package com.example.loopcongo.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    val type_account: String?,
    val nom: String?,
    val nb_abonner: Int?,
    val is_sponsored: Int?,
    val boost_type: String?,
    val sponsored_until: String?,
    val password: String?,
    val contact: String?,
    val city: String?,
    val file_url: String?,
    val about: String?
)


