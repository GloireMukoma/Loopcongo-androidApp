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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.ProfileVendeurActivity
import com.example.loopcongo.R
import com.example.loopcongo.adapters.*
import com.example.loopcongo.adapters.articles.ArticleAdapter
import com.example.loopcongo.adapters.articles.ArticleApiAdapter
import com.example.loopcongo.models.*
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
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


        // Charger les données des profils utilisateurs de l'API REST
        // Charger les données des profils utilisateurs de l'API REST
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
        val topArticlesRecyclerView = view.findViewById<RecyclerView>(R.id.topArticlesHomePageRecyclerView)
        topArticlesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        ApiClient.instance.getArticles().enqueue(object : Callback<List<ArticleApi>> {
            override fun onResponse(
                call: Call<List<ArticleApi>>,
                response: Response<List<ArticleApi>>
            ) {
                if (response.isSuccessful) {
                    val articles = response.body()!!
                    val adapter = ArticleApiAdapter(articles)
                    topArticlesRecyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<ArticleApi>>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur : ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
        // End données API REST

        /*statutUserrecyclerView = view.findViewById(R.id.statutUserProfileHomePageRecycler)
        statutUserrecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val users = listOf(
            Vendeur("Shabani Market", "Articles divers", 23, R.drawable.user5, "article"),
            Vendeur("Congo Immo", "Maisons à vendre", 8, R.drawable.user1, "immobilier"),
            Vendeur("Shabani Market", "Articles divers", 23, R.drawable.user2, "article"),
            Vendeur("Congo Immo", "Maisons à vendre", 8, R.drawable.user3, "immobilier"),
            Vendeur("Shabani Market", "Articles divers", 23, R.drawable.user4, "article"),
            Vendeur("Congo Immo", "Maisons à vendre", 8, R.drawable.user2, "immobilier")
        )

        statutUserrecyclerView.adapter = StatutUserProfileAdapter(users) { clickedUser ->
            val intent = Intent(requireContext(), ProfileVendeurActivity::class.java)
            intent.putExtra("user_name", clickedUser.nom)
            intent.putExtra("user_description", clickedUser.description)
            intent.putExtra("user_type", clickedUser.type)
            intent.putExtra("user_image", clickedUser.imageResId)
            startActivity(intent)
        }*/

        // Defilement automatique de la carousel d'images d'annonces sur la page d'accueil
        //val imageList = listOf(R.drawable.chaussures, R.drawable.shoes, R.drawable.shoes_men)
        val imageList = listOf(
            CarouselItem(R.drawable.chaussures, "20% de reduction", "Description de l’image 1"),
            CarouselItem(R.drawable.shoes, "Shoes de qualité", "Description de l’image ipsum dolor"),
            CarouselItem(R.drawable.shoes_men, "Titre 3", "Description de l’image 3")
        )
        val sliderRunnable = Runnable {
            viewPager2.currentItem = (viewPager2.currentItem + 1) % imageList.size
        }
        viewPager2 = view.findViewById(R.id.carouselView)
        viewPager2.adapter = CarouselAnnonceAdapter(imageList) // Ton adapter
        viewPager2.offscreenPageLimit = 3

        // Démarrer le défilement automatique
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Redémarre le timer à chaque changement de page
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000) // 3 secondes
            }
        })

        return view
    }
}
