package com.example.loopcongo.restApi

import com.example.loopcongo.database.LoginResponse
import com.example.loopcongo.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // Endpoint pour récupérer les articles
    @GET("articles")
    fun getArticles(): Call<ArticleResponse>

    // Connexion d'un user
    @POST("user/login")
    fun login(@Body body: Map<String, String>): Call<LoginResponse>

    //Get tout les articles par categorie ainsi que le filtrage ou la recherche
    @GET("articles/ctg/{category_id}")
    fun getArticlesByCategory(
        @Path("category_id") categoryId: Int,
        @Query("search") search: String? = null
    ): Call<List<Article>>

    // Exemple : /api/immobiliers/search?city=Kinshasa&quartier=Gombe
    @GET("immobiliers/search") // ← plus de "api/" ici
    fun searchImmobiliers(
        @Query("city") city: String?,
        @Query("quartier") quartier: String?
    ): Call<List<Immobilier>>

    @GET("commandes/vendeur/{id}")
    fun getCommandesVendeur(@Path("id") vendeurId: Int): Call<CommandeResponse>

    // Endpoint pour récupérer les profils utilisateurs (recuperer les users avec leurs dernieres articles
    @GET("users")
    fun getUsersWithLastArticle(): Call<UserResponse>

    // Récupère tous les articles d’un vendeur
    @GET("articles/vendeur/{id}")
    fun getArticlesByVendeur(@Path("id") vendeurId: Int): Call<ArticleResponse>

    // Appelle GET sur /api/vendeurs
    @GET("vendeurs")
    fun getVendeurs(): Call<VendeurResponse>

    // Get la liste des prestataires
    @GET("prestataires")
    fun getPrestataires(): Call<PrestataireListResponse>

    //Get les prestations deja publiés
    @GET("prestations")
    fun getPublications(): Call<PrestationResponse>

    // Get la liste des top prestataire (sponsorisés)
    @GET("prestataires/sponsored")
    fun getTopPrestataires(): Call<PrestataireListResponse>

    //Get la liste des publications des prestataires qui sont sponsorisés
    @GET("prestations/sponsorisees")
    fun getSponsorisedPublications(): Call<PrestationSponsoriseesResponse>

    // Get la liste des annonces des prestataires (a charger dans la caroussel home)
    @GET("prest/annonces/actives")
    fun getPrestataireAnnonces(): Call<AnnoncePrestataireResponse>

    // Get un user + ses articles par son ID
    @GET("user/{id}")
    fun getUserById(@Path("id") id: Int): Call<UserUniqueResponse>

    // Get les annonces des articles (afficher dans la caroussel)
    @GET("annonces/articles")
    fun getAnnoncesArticlesCaroussel(): Call<AnnonceResponse>

    // Recuperer les prestations d'un prestataire par son id
    // 🔹 Exemple : GET /prestations?prestataire_id=5
    @GET("prestations")
    fun getPrestationsByPrestataire(
        @Query("prestataire_id") prestataireId: Int
    ): Call<List<Prestation>>

    @GET("prestataire/get/{id}")
    fun getPrestataireById(
        @Path("id") id: Int
    ): Call<PrestataireResponse>

}
