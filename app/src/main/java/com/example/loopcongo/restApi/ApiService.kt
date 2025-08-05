package com.example.loopcongo.restApi

import com.example.loopcongo.models.*
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

    // Appelle GET sur /api/vendeurs
    @GET("vendeurs")
    fun getVendeurs(): Call<VendeurResponse>

    // Get la liste des prestataires
    @GET("prestataires")
    fun getPrestataires(): Call<PrestataireResponse>

    //Get les prestations deja publiés
    @GET("prestations")
    fun getPublications(): Call<PrestationResponse>


}
