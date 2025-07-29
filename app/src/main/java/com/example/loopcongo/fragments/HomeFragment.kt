package com.example.loopcongo.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.ProfileVendeurActivity
import com.example.loopcongo.R
import com.example.loopcongo.adapters.*
import com.example.loopcongo.adapters.articles.ArticleAdapter
import com.example.loopcongo.models.Article
import com.example.loopcongo.models.CarouselItem
import com.example.loopcongo.models.Vendeur
import com.google.android.material.tabs.TabLayoutMediator

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

        statutUserrecyclerView = view.findViewById(R.id.statutUserProfileRecycler)
        statutUserrecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val users = listOf(
            Vendeur("Shabani Market", "Articles divers", 23, R.drawable.avatar, "article"),
            Vendeur("Congo Immo", "Maisons à vendre", 8, R.drawable.avatar_1, "immobilier"),
            Vendeur("Shabani Market", "Articles divers", 23, R.drawable.avatar_2, "article"),
            Vendeur("Congo Immo", "Maisons à vendre", 8, R.drawable.avatar_4, "immobilier"),
            Vendeur("Shabani Market", "Articles divers", 23, R.drawable.avatar, "article"),
            Vendeur("Congo Immo", "Maisons à vendre", 8, R.drawable.avatar_1, "immobilier")
        )

        statutUserrecyclerView.adapter = StatutUserProfileAdapter(users) { clickedUser ->
            val intent = Intent(requireContext(), ProfileVendeurActivity::class.java)
            intent.putExtra("user_name", clickedUser.nom)
            intent.putExtra("user_description", clickedUser.description)
            intent.putExtra("user_type", clickedUser.type)
            intent.putExtra("user_image", clickedUser.imageResId)
            startActivity(intent)
        }

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


        // gere la carouselle des annonces (home page)
        /*carouselView = view.findViewById(R.id.carouselView)
        val banners = listOf(R.drawable.chaussures, R.drawable.shoes, R.drawable.shoes_men)
        carouselView.adapter = CarouselAnnonceAdapter(banners)*/

        topArticlesRecyclerView = view.findViewById<RecyclerView>(R.id.gridArticles)
        topArticlesRecyclerView.layoutManager = LinearLayoutManager(context)
        topArticlesRecyclerView.adapter = ArticleAdapter(mockData())

        /*gridView = view.findViewById(R.id.gridArticles)
        val articles = listOf(
            Article("Créer des interfaces animées...", "10 $", "Tatiana moda", "Il y a 1min",1736, 48, 5, R.drawable.shoes),
            Article("Pourquoi j’ai choisi Flutter...", "21000 CDF", "Gloire mukoma","Il y a 19min",872, 82, 9, R.drawable.shoes_men),
            Article("8 astuces cachées en CSS", "85 $", "Tatiana moda","Il y a 1min",734, 69, 2, R.drawable.chaussures),
            Article("Créer des interfaces animées...", "120 $", "Etienne mulenda","Il y a 2h",1736, 48, 5, R.drawable.avatar),
            Article("Pourquoi j’ai choisi Flutter...","8500 CDF", "Tatiana moda", "Il y a 1min",872, 82, 9, R.drawable.avatar_4),
            Article("8 astuces cachées en CSS", "45 $", "Elisée kab","Il y a 1min",734, 69, 2, R.drawable.chaussures),
            Article("Créer des interfaces animées...","1500 CDF", "Tatiana moda", "Il y a 1h",1736, 48, 5, R.drawable.shoes),
            Article("Pourquoi j’ai choisi Flutter...", "40 $", "Tatiana moda","Il y a 1min",872, 82, 9, R.drawable.shoes_men),
            Article("8 astuces cachées en CSS", "22500 CDF", "Tatiana moda","Il y a 3h",734, 69, 2, R.drawable.chaussures),
        )
        gridView.adapter = ArticleGridAdapter(requireContext(), articles)*/

        return view
    }
    private fun mockData(): List<Article> {
        return listOf(
            Article("Créer des interfaces animées...", "10 $", "Tatiana moda", "Il y a 1min",1736, 48, 5, R.drawable.shoes_men),
            Article("Pourquoi j’ai choisi Flutter...", "21000 CDF", "Gloire mukoma","Il y a 19min",872, 82, 9, R.drawable.shoes),
            Article("8 astuces cachées en CSS", "85 $", "Tatiana moda","Il y a 1min",734, 69, 2, R.drawable.chaussures),
            Article("Créer des interfaces animées...", "120 $", "Etienne mulenda","Il y a 2h",1736, 48, 5, R.drawable.shoes_men),
            Article("Pourquoi j’ai choisi Flutter...","8500 CDF", "Tatiana moda", "Il y a 1min",872, 82, 9, R.drawable.chaussures),
            Article("8 astuces cachées en CSS", "45 $", "Elisée kab","Il y a 1min",734, 69, 2, R.drawable.shoes),
            Article("Créer des interfaces animées...","1500 CDF", "Tatiana moda", "Il y a 1h",1736, 48, 5, R.drawable.chaussures),
            Article("Pourquoi j’ai choisi Flutter...", "40 $", "Tatiana moda","Il y a 1min",872, 82, 9, R.drawable.shoes_men),
            Article("8 astuces cachées en CSS", "22500 CDF", "Tatiana moda","Il y a 3h",734, 69, 2, R.drawable.shoes),
        )
    }
}
