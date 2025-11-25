package com.example.loopcongo.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.*
import com.example.loopcongo.adapters.*
import com.example.loopcongo.adapters.articles.ArticleForPopupRandomUserPremiumAdapter
import com.example.loopcongo.adapters.articles.TopArticleAdapter
import com.example.loopcongo.adapters.articles.CarouselUserAnnonceAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.CustomerDao
import com.example.loopcongo.database.UserDao
import com.example.loopcongo.models.*
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.imageview.ShapeableImageView
import com.example.loopcongo.database.User as DbUser
import com.example.loopcongo.database.Customer as DbCustomer
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var userDao: UserDao
    private lateinit var customerDao: CustomerDao

    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())

    private lateinit var gridView: GridView
    private lateinit var carouselView: ViewPager2
    private lateinit var statutUserrecyclerView: RecyclerView
    private lateinit var topArticlesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Instance de la BD Room
        val db = AppDatabase.getDatabase(requireContext())
        userDao = db.userDao()
        customerDao = db.customerDao()

        // Récupère l'ImageView de l'avatar
        val avatarIconUserConnected = view.findViewById<ImageView>(R.id.avatarImgProfileUserConnected)

        // Met à jour l'avatar et configure le clic
        lifecycleScope.launch {
            updateAvatarAndListener(avatarIconUserConnected)
        }

        // Icon (etoile) d'abonnement
        val iconSubscription = view.findViewById<ImageView>(R.id.iconSubscription)
        iconSubscription.setOnClickListener {
            val intent = Intent(requireContext(), SubscriptionActivity::class.java)
            startActivity(intent)
        }

        // Charger les données des profils utilisateurs de l'API REST (Top vendeur)
        val recyclerViewUsers = view.findViewById<RecyclerView>(R.id.statutUserProfileHomePageRecycler)
        recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        ApiClient.instance.getUsersWithLastArticle().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val users = response.body()?.data ?: emptyList()
                    val userAdapter = StatutUserProfileAdapter(users)
                    recyclerViewUsers.adapter = userAdapter
                } else {
                    Log.d("API_DEBUG", "Réponse complète: ${response.body()}")

                    Toast.makeText(requireContext(), "Échec de chargement des utilisateurs", Toast.LENGTH_SHORT).show()
                }
                Log.e("API_DEBUG", "Erreur serveur: ${response.errorBody()?.string()}")

            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur : ${t.message}", Toast.LENGTH_LONG).show()

            }
        })

        // Charger les données provenant de l'API REST (pour les top articles)
        val topArticlesListView = view.findViewById<ListView>(R.id.topArticlesHomePageRecyclerView)

        ApiClient.instance.getArticles().enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful && response.body() != null && response.body()!!.status) {
                    val articles = response.body()!!.data  // Liste des articles
                    val adapter = TopArticleAdapter(requireContext(), articles)
                    topArticlesListView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Erreur serveur", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur : ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        // Gere la caroussel des annonces
        viewPager2 = view.findViewById(R.id.carouselArticleAnnnonce)
        viewPager2.offscreenPageLimit = 5
        viewPager2.isNestedScrollingEnabled = false

        ApiClient.instance.getUserAnnoncesCaroussel().enqueue(object : Callback<AnnonceResponse> {
            override fun onResponse(call: Call<AnnonceResponse>, response: Response<AnnonceResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val annonces = response.body()!!.data
                    //viewPager2.adapter = CarouselUserAnnonceAdapter(requireContext(), annonces)
                    viewPager2.adapter = CarouselUserAnnonceAdapter(requireContext(), annonces) { annonce ->
                        val intent = Intent(requireContext(), DetailAnnonceActivity::class.java)
                        intent.putExtra("id", annonce.id)
                        intent.putExtra("user_id", annonce.user_id)
                        intent.putExtra("titre", annonce.titre)
                        intent.putExtra("description", annonce.description)
                        intent.putExtra("image", annonce.image)
                        intent.putExtra("username", annonce.username)
                        intent.putExtra("city", annonce.city)
                        intent.putExtra("contact", annonce.contact)
                        intent.putExtra("file_url", annonce.file_url)

                        // données de l'utilisateur
                        intent.putExtra("vendeurId", annonce.user_id)
                        intent.putExtra("vendeurUsername", annonce.username)
                        intent.putExtra("vendeurContact", annonce.contact)
                        intent.putExtra("vendeurCity", annonce.city)
                        intent.putExtra("vendeurDescription", annonce.about)
                        intent.putExtra("vendeurTypeAccount", annonce.type_account)
                        intent.putExtra("vendeurAvatarImg", annonce.file_url)
                        intent.putExtra("isCertifiedVendeur", annonce.is_certified)
                        intent.putExtra("vendeurNbAbonner", annonce.nb_abonner)
                        startActivity(intent)
                    }


                    val sliderRunnable = Runnable {
                        viewPager2.currentItem = (viewPager2.currentItem + 1) % annonces.size
                    }

                    viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            sliderHandler.removeCallbacks(sliderRunnable)
                            sliderHandler.postDelayed(sliderRunnable, 3000)
                        }
                    })
                    // Lance la première fois le défilement auto
                    sliderHandler.postDelayed(sliderRunnable, 3000)

                } else {
                    Toast.makeText(requireContext(), "Réponse vide ou invalide", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AnnonceResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Affichage de la popup des users premium
        showRandomPremiumUserPopup(requireContext())

        return view
    }
    // Affiche la popup des users premiums et leurs articles
    fun showRandomPremiumUserPopup(context: Context) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.popup_premium_user_and_articles, null)

        val recyclerView: RecyclerView = view.findViewById(R.id.popupPremiumUserRecyclerView)
        val viewProfileBtn: Button = view.findViewById(R.id.popupPremiumUserViewProfilBtn)

        val userName: TextView = view.findViewById(R.id.popupPremiumUsername)
        val numero: TextView = view.findViewById(R.id.popupPremiumUserNumero)

        val city: TextView = view.findViewById(R.id.popupPremiumUserCity)
        val userImage: ShapeableImageView = view.findViewById(R.id.popupPremiumUserAvatarImg)

        val adapter = ArticleForPopupRandomUserPremiumAdapter(context, listOf())

        // LayoutManager pour une grille de 2 colonnes, défilable verticalement
        recyclerView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        // defilement horizontal (caroussel)
        //recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter


        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .create()

        // Récupère le bouton "close"
        val closeIcon = view.findViewById<ImageView>(R.id.closeIconPremiumPopup)
        closeIcon.setOnClickListener {
            dialog.dismiss() // ✅ Ferme la popup
        }

        dialog.show()

        // Appel API
        ApiClient.instance.getRandomPremiumUser().enqueue(object : Callback<RandomPremiumUserResponse> {
            override fun onResponse(
                call: Call<RandomPremiumUserResponse>,
                response: Response<RandomPremiumUserResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.status && body.user != null) {

                        // Rediriger vers le profil du vendeur lorsqu'on clique sur le bouton voir profil
                        viewProfileBtn.setOnClickListener {
                            // Ici tu peux rediriger vers l'activité profil complet si nécessaire
                            val intent = Intent(context, ProfileVendeurActivity::class.java)

                            // Passer les données nécessaires (tu peux en passer plus)
                            intent.putExtra("vendeurId", body.user.id)
                            intent.putExtra("vendeurUsername", body.user.username)
                            intent.putExtra("vendeurContact", body.user.contact)
                            intent.putExtra("vendeurCity", body.user.city)
                            intent.putExtra("vendeurDescription", body.user.about)
                            intent.putExtra("vendeurTypeAccount", body.user.type_account)
                            intent.putExtra("vendeurAvatarImg", body.user.file_url)
                            intent.putExtra("isCertifiedVendeur", body.user.is_certified)
                            intent.putExtra("vendeurTotalArticles", body.user.total_articles)
                            intent.putExtra("vendeurTotalLikes", body.user.total_likes)
                            intent.putExtra("vendeurNbAbonner", body.user.nb_abonner)

                            context.startActivity(intent)
                        }

                        userName.text = body.user.username
                        numero.text = body.user.contact
                        city.text = body.user.city

                        // verifier si l'user est premium pour avoir l'icon de certification
                        if (body.user.subscription_type == "premium" && body.user.subscription_status == "active") {
                            view.findViewById<ImageView>(R.id.popupPremiumUserVerifiedIcon).visibility = View.VISIBLE
                        } else {
                            view.findViewById<ImageView>(R.id.popupPremiumUserVerifiedIcon).visibility = View.GONE
                        }

                        Glide.with(context)
                            .load("https://loopcongo.com/${body.user.file_url}")
                            .placeholder(R.drawable.loading)
                            .into(userImage)

                        adapter.updateData(body.articles ?: emptyList())
                    } else {
                        Toast.makeText(context, "Aucun utilisateur premium trouvé", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                } else {
                    Toast.makeText(context, "Erreur API", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }
            override fun onFailure(call: Call<RandomPremiumUserResponse>, t: Throwable) {
                Toast.makeText(context, "Erreur: ${t.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
    }
    private suspend fun updateAvatarAndListener(avatar: ImageView) {
        // Récupère les comptes connectés
        val user = userDao.getUser()       // vendeur ou immobilier
        val customer = customerDao.getCustomer() // client
        val currentAccount: Any? = user ?: customer

        // Met à jour l'image de l'avatar
        val imageUrl = when (currentAccount) {
            is DbUser -> currentAccount.file_url
            is DbCustomer -> currentAccount.file_url
            else -> null
        }

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_login)
                .error(R.drawable.ic_login)
                .circleCrop()
                .into(avatar)
        } else {
            avatar.setImageResource(R.drawable.ic_login)
        }

        // Configure le listener sur l'avatar
        avatar.setOnClickListener {
            lifecycleScope.launch {
                val latestUser = userDao.getUser()
                val latestCustomer = customerDao.getCustomer()
                val account = latestUser ?: latestCustomer

                val nextActivity = when (account) {
                    is DbUser -> {
                        if (account.id == 1) {
                            // Redirection vers l'admin si ID = 1
                            SuperAdminConnectedActivity::class.java
                        } else {
                            when (account.type_account?.lowercase()) {
                                "vendeur" -> ProfileUserConnectedActivity::class.java
                                "immobilier" -> UserImmobilierConnectedActivity::class.java
                                else -> ProfileUserConnectedActivity::class.java
                            }
                        }
                    }
                    is DbCustomer -> CustomerConnectedActivity::class.java
                    else -> LoginActivity::class.java
                }

                startActivity(Intent(requireContext(), nextActivity))
            }
        }
    }

}
