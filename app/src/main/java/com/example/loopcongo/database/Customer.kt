package com.example.loopcongo.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey val id: Int,
    val username: String = "",
    val contact: String? = "",
    val city: String? = "",
    val interets: String? = "",
    val file_url: String? = "",
    val type_account: String? = "customer" // toujours customer
)


