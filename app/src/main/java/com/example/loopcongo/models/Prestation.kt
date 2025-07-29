package com.example.loopcongo.models

data class Prestation(
    val id: Int,
    val titre: String,
    val description: String,
    val prix: Double,
    val imageResId: Int,
    val nomPrestataire: String,
    val domaine: String,
    val localisation: String
)
