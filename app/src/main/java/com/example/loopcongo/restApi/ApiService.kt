package com.example.loopcongo.restApi

import com.example.loopcongo.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("checkServerMembership")
    fun checkServerMembership(
        @Field("server_id") serverId: Int,
        @Field("user_id") userId: Int,
        @Field("user_type") userType: String
    ): Call<BasicResponse>

    @FormUrlEncoded
    @POST("join-server")
    fun joinServer(
        @Field("server_id") serverId: Int,
        @Field("user_id") userId: Int,
        @Field("user_type") userType: String
    ): Call<BasicResponse>

    @GET("user-servers/{id}/{type}")
    fun getUserServers(
        @Path("id") userId: Int,
        @Path("type") userType: String
    ): Call<ServerResponse>

    @GET("server/{serverId}/messages/get")
    fun getServerMessages(
        @Path("serverId") serverId: Int
    ): Call<List<ServerMessage>>

    @GET("servers/get/all")
    fun getServers(): Call<List<Server>>

    @GET("servers/categories")
    fun getServerCategories(): Call<List<ServerCategorie>>

    @GET("customer/info/get/{id}")
    fun getCustomerInfo(
        @Path("id") customerId: Int
    ): Call<Customer>

    @GET("user/mes-abonnes/{id}")
    fun getMesAbonnes(
        @Path("id") userId: Int
    ): Call<ApiResponseMesAbonnes>

    @GET("articles/comments/get/{id}")
    fun getCommentaires(
        @Path("id") articleId: Int
    ): Call<CommentaireResponse>

    @POST("commentaires/register")
    fun publierCommentaire(
        @Body request: PublierCommentaireRequest
    ): Call<ApiCommentaireResponse>

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

    // Enregistrer les abonnements des utilisateurs
    @FormUrlEncoded
    @POST("user/abonnement/save")
    suspend fun saveUserAbonnement(
        @Field("user_type") userType: String,
        @Field("customer_id") customerId: Int,
        @Field("vendeur_id") vendeurId: Int,
        @Field("vendeur_type") vendeurType: String
    ): Response<AbonnementResponse>


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

    // Exemple : https://loopcongo.com/api/immobiliers?city=Kinshasa
    @GET("city/immobiliers")
    fun getImmobiliersByCity(
        @Query("city") city: String
    ): Call<ImmobilierResponse2>


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
    fun getVendeurs(@Query("username") username: String? = null): Call<UserResponse>


    //Get les prestations deja publiés
    @GET("prestations")
    fun getPublications(): Call<PrestationResponse>

    //Get la liste des publications des prestataires qui sont sponsorisés
    @GET("prestations/sponsorisees")
    fun getSponsorisedPublications(): Call<PrestationSponsoriseesResponse>

    // Get un user + ses articles par son ID
    @GET("user/{id}")
    fun getUserById(@Path("id") id: Int): Call<UserUniqueResponse>

    // Activer l'abonnement d'un utilisateur (ACCOUNT CREATED)
    @FormUrlEncoded
    @POST("admin/subscription/activate/{id}")
    suspend fun activateSubscription(
        @Path("id") userId: Int,
        @Field("type") type: String
    ): ApiResponse2

    // Desactiver l'abonnement d'un utilisateur (ACCOUNT CREATED)
    @POST("admin/subscription/desactivate/{id}")
    suspend fun deactivateSubscription(@Path("id") userId: Int): ApiResponse2


    // get les statistiques d'un user: nb d'articles publiés+ nb de command
    @GET("user/statistics/get/{id}")
    suspend fun getUserStats(@Path("id") userId: Int): UserStatsResponse

    // Get les stats pour le super admin(nb de compte crée, nb article, nb des comptes abonnés)
    @GET("admin/stats/get")
    suspend fun getSuperAdminStats(): SuperAdminStatsResponse

    // Stats des abonnements (nb de basic, pro, premium)
    @GET("admin/subscriptions/stats")
    fun getSubscriptionStats(): Call<SubscriptionStatsResponse>

    // Get les comptes deja abonnés
    @GET("admin/subscribedUsers/get")
    fun getSubscribedUsers(): Call<List<User>>

    // Get la liste des utilisateurs qui ont deja envoyés leur abonnement (en attente de validation)
    @GET("admin/abonnement/users/waiting")
    suspend fun getWaitingUsersAbonnements(): List<User>

    // Valider un abonnement
    @POST("admin/abonnements/validate/{userId}")
    suspend fun validateAbonnement(
        @Path("userId") userId: Int
    ): Response<Unit>

    // Rejeter un abonnement
    @POST("admin/abonnements/reject/{userId}")
    suspend fun rejectAbonnement(
        @Path("userId") userId: Int
    ): Response<Unit>

    // utiliser par l'onglet immobilier du super admin
    @GET("admin/immobiliers")
    suspend fun getAllImmobiliers(): ImmobilierResponse3


    // get la liste des comptes crées (onglets compte super admin)
    @GET("admin/accounts/all")
    suspend fun getAllUsers(): List<User>

    // Get la liste de toutes les articles publiés par les utilisateurs (for super admin)
    @GET("admin/products/all")
    suspend fun getAllProductsForAdmin(): ArticleResponse


    // Get le nb d'abonnement d'un utilisateur
    @GET("customer/abonnements/count/{customerId}")
    suspend fun getNbAbonnementCustomer(
        @Path("customerId") customerId: Int
    ): NbAbonnementResponse

    @GET("user/immo/statistics/get/{id}")
    suspend fun getUserImmoStats(@Path("id") userId: Int): UserStatsResponse

    // Get les demandes immobiliers laissés par les utilisateurs
    @GET("user/immo/demandes")
    fun getUserImmoDemandes(): Call<ApiResponseDemande>

    // Liste des biens des users abonnés (caroussel immobilier)
    @GET("immo/subscribed")
    fun getImmosSubscribeUsers(): Call<List<Immobilier>>

    // Retourne le nombre des biens publiés pour chaque
    @GET("immobilier/nb/city")
    fun getCities(): Call<ImmobilierResponse>

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
