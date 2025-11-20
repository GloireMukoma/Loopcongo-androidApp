package com.example.loopcongo.models

class SuperAdmin {
}

data class SuperAdminStatsResponse(
    val status: Boolean,
    val nbAccounts: Int,
    val nbArticles: Int,
    val nbAbonnements: Int
)
