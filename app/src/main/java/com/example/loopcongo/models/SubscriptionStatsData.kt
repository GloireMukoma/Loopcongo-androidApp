package com.example.loopcongo.models

data class SubscriptionStatsResponse(
    val status: Boolean,
    val message: String,
    val data: SubscriptionStatsData
)

data class SubscriptionStatsData(
    val total_active: Int,
    val basic: Int,
    val pro: Int,
    val premium: Int,
    val waiting: Int
)
