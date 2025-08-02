package com.example.loopcongo.restApi

import com.example.loopcongo.models.ArticleApi
import com.example.loopcongo.models.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    // Endpoint pour récupérer les articles
    @GET("articles")
    fun getArticles(): Call<List<ArticleApi>>

    // Endpoint pour récupérer les profils utilisateurs
    @GET("users")
    fun getUsersWithLastArticle(): Call<UserResponse>

}
