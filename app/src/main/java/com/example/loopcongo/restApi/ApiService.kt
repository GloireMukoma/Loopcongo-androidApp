package com.example.loopcongo.restApi

import com.example.loopcongo.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Endpoint pour récupérer les articles
    @GET("articles")
    fun getArticles(): Call<ArticleResponse>

    // Get les articles des vendeurs auxquels un client s'est abonnés
    // les afficher dans l'onglet des articles des abonnements
    @GET("products/customer/abonnes/{customerId}")
    suspend fun getArticlesByAbonnements(
        @Path("customerId") customerId: Int
    ): Response<ArticleResponse>

    // Connexion d'un user
    @POST("user/login")
    fun login(@Body body: Map<String, String>): Call<LoginResponse>

    //Get tout les articles par categorie ainsi que le filtrage ou la recherche
    @GET("articles/ctg/{category_id}")
    fun getArticlesByCategory(
        @Path("category_id") categoryId: Int,
        @Query("search") search: String? = null
    ): Call<List<Article>>

    @DELETE("product/delete/{id}")
    suspend fun deleteArticle(@Path("id") id: Int): Response<Unit>

    @DELETE("immobilier/delete/{id}")
    suspend fun deleteImmobilier(@Path("id") id: Int): Response<Unit>

    @DELETE("user/annonce/delete/{id}")
    suspend fun deleteAnnonce(@Path("id") id: Int): Response<Unit>

    @GET("article/detail/imgs/{id}")
    suspend fun getArticleDetailImages(@Path("id") id: Int): Response<List<DetailImage>>

    // Est utiliser sur le bouton voir profil sur l'activité detailArticle pour voir le profile user
    @GET("user/userById/{id}")
    suspend fun userById(@Path("id") id: Int): Response<UserUniqueResponse>

    // Recuperer les infos du compte client qui s'est connecté
    @GET("user/customer/get/{id}")
    suspend fun getCustomerById(@Path("id") id: Int): Response<CustomerResponse>

    @GET("abonnements/customer/get/{customerId}")
    suspend fun getAbonnementsByCustomer(
        @Path("customerId") customerId: Int
    ): Response<List<User>>

    // Récupère les articles des vendeurs auxquels le client est abonné
    @GET("products/customer/abonnements/get/{customerId}")
    suspend fun getArticlesByCustomerAbonnements(
        @Path("customerId") customerId: Int
    ): Response<List<Article>>

    // Exemple : /api/immobiliers/search?city=Kinshasa&quartier=Gombe
    @GET("immobiliers/search") // ← plus de "api/" ici
    fun searchImmobiliers(
        @Query("city") city: String?,
        @Query("quartier") quartier: String?
    ): Call<List<Immobilier>>

    @GET("immobiliers/similar/images/{immoId}")
    suspend fun getImmobiliersDetailImages(
        @Path("immoId") immoId: Int
    ): Response<List<DetailImage>>

    // Get tout les immobiliers publiés par un vendeur par son id
    @GET("user/immobiliers/get/{user_id}")
    fun getImmobiliersByUser(@Path("user_id") userId: Int): Call<ImmobilierResponse>


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
    fun getVendeurs(): Call<UserResponse>

    //Get les prestations deja publiés
    @GET("prestations")
    fun getPublications(): Call<PrestationResponse>

    //Get la liste des publications des prestataires qui sont sponsorisés
    @GET("prestations/sponsorisees")
    fun getSponsorisedPublications(): Call<PrestationSponsoriseesResponse>

    // Get un user + ses articles par son ID
    @GET("user/{id}")
    fun getUserById(@Path("id") id: Int): Call<UserUniqueResponse>

    // get les statistiques d'un user: nb d'articles publiés+ nb de command
    @GET("user/statistics/get/{id}")
    suspend fun getUserStats(@Path("id") userId: Int): UserStatsResponse

    @GET("user/immo/statistics/get/{id}")
    suspend fun getUserImmoStats(@Path("id") userId: Int): UserStatsResponse

    // Get un user premium pour l'afficher dans la popup des utilisateurs premium
    @GET("user/random-premium")
    fun getRandomPremiumUser(): Call<RandomPremiumUserResponse>

    // Get les annonces des articles (afficher dans la caroussel)
    @GET("user/annonces")
    fun getUserAnnoncesCaroussel(): Call<AnnonceResponse>

    // Get les annonces du vendeur est les afficher dans l'onglet annonces du tablayout
    //de l'activité profile user
    @GET("user/annonces/get/{id}")
    fun getAnnoncesByVendeur(@Path("id") vendeurId: Int): Call<AnnonceResponse>

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

    // Get la liste des top prestataire (sponsorisés)
    @GET("prestataires/sponsored")
    fun getTopPrestataires(): Call<PrestataireListResponse>

    // Get la liste des annonces des prestataires (a charger dans la caroussel home)
    @GET("prest/annonces/actives")
    fun getPrestataireAnnonces(): Call<AnnoncePrestataireResponse>

    // Get la liste des prestataires
    @GET("prestataires")
    fun getPrestataires(): Call<PrestataireListResponse>

}
