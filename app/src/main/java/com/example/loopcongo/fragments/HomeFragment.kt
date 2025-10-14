package com.example.loopcongo.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.CommandesActivity
import com.example.loopcongo.LoginActivity
import com.example.loopcongo.ProfileUserConnectedActivity
import com.example.loopcongo.R
import com.example.loopcongo.adapters.*
import com.example.loopcongo.adapters.articles.TopArticleAdapter
import com.example.loopcongo.adapters.articles.CarouselAnnonceArticleAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.UserDao
import com.example.loopcongo.models.*
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var userDao: UserDao
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

        // Récupère l'ImageView de l'avatar
        val avatarIconUser = view.findViewById<ImageView>(R.id.avatarImgProfileUser)

        // Utiliser lifecycleScope pour les fonctions suspend
        lifecycleScope.launch {
            val user = userDao.getUser() // récupère l’utilisateur connecté (ou null)

            if (user != null) {
                // Utilisateur connecté → afficher sa photo
                if (!user.file_url.isNullOrEmpty()) {
                    Glide.with(this@HomeFragment)
                        .load(user.file_url)
                        .placeholder(R.drawable.ic_person) // image par défaut
                        .error(R.drawable.ic_person)
                        .circleCrop()
                        .into(avatarIconUser)
                } else {
                    avatarIconUser.setImageResource(R.drawable.ic_person)
                }
            } else {
                // Pas d’utilisateur connecté → garder l’icon par défaut
                avatarIconUser.setImageResource(R.drawable.ic_person)
            }

            // Clique sur l'avatar
            avatarIconUser.setOnClickListener {
                if (user == null) {
                    // Pas d’utilisateur enregistré → rediriger vers login
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                } else {
                    // Utilisateur existe → rediriger vers profil
                    startActivity(Intent(requireContext(), ProfileUserConnectedActivity::class.java))
                }
            }
        }


        // Icon de notication, redirection vers l'activité des commandes
        val iconNotification = view.findViewById<ImageView>(R.id.iconNotification)
        iconNotification.setOnClickListener {
            val intent = Intent(requireContext(), CommandesActivity::class.java)
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

        ApiClient.instance.getAnnoncesArticlesCaroussel().enqueue(object : Callback<AnnonceResponse> {
            override fun onResponse(call: Call<AnnonceResponse>, response: Response<AnnonceResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val annonces = response.body()!!.data
                    viewPager2.adapter = CarouselAnnonceArticleAdapter(requireContext(), annonces)

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

        return view
    }
}
