package com.example.loopcongo.models

data class Immobilier(
    val id: Int,
    val account_id: Int,
    val typeimmo: String,
    val statut: String,
    val city: String,
    val quartier: String,
    val prix: String,
    val address: String,
    val about: String,
    val file_url: String
)
