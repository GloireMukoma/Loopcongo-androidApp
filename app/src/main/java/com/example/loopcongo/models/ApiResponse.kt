package com.example.loopcongo.models

data class ApiResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T?
)
