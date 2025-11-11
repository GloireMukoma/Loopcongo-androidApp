package com.example.loopcongo.models

import com.google.gson.annotations.SerializedName

class Abonnement {
}

data class AbonnementResponse(
    val status: Boolean,
    val message: String,
    val data: Any?
)

data class NbAbonnementResponse(
    @SerializedName("customer_id") val customerId: Int,
    @SerializedName("nb_abonnements") val nbAbonnement: Int
)

