package com.example.loopcongo.models

data class Customer(
    val id: Int,
    val username: String,
    val contact: String?,
    val city: String?,
    val interets: String?,
    val file_url: String?
)
// Reponse du compte client
data class CustomerResponse(
    val status: Boolean,
    val message: String,
    val data: Customer?
)