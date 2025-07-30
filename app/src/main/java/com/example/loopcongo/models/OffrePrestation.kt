package com.example.loopcongo.models
data class OffrePrestation(
    val nomAnnonceur: String,
    val heure: String,
    val texteContenu: String,
    val avatarResId: Int,
    val titre: String? = null // Optionnel, utilisé pour le détail
)
