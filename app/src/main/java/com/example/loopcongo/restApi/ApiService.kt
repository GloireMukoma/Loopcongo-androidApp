package com.example.loopcongo.restApi
import com.example.loopcongo.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Endpoint pour récupérer les articles
    @GET("articles")
    fun getArticles(): Call<ArticleResponse>
    // Endpoint pour récupérer les profils utilisateurs
    @GET("users")
    fun getUsersWithLastArticle(): Call<UserResponse>

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
    fun getUserById(@Path("id") id: Int): Call<ApiResponse<UserData>>

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
