package com.example.loopcongo.fragments.prestataire

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.R
import com.example.loopcongo.adapters.CarouselAnnonceAdapter
import com.example.loopcongo.adapters.prestataire.TopPrestataireProfileAdapter
import com.example.loopcongo.adapters.prestataire.TopPrestationAdapter
import com.example.loopcongo.models.*
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccueilFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())

    // Top prestataires (Profils prestataires)
    private lateinit var topPrestataireProfileRecyclerView: RecyclerView
    private lateinit var adapter: TopPrestataireProfileAdapter
    private val topPrestataires = ArrayList<Prestataire>()

    // Top prestations (Publications)
    private lateinit var topPrestationsRecyclerView: RecyclerView
    private lateinit var topPrestationsAdapter: TopPrestationAdapter
    private val prestationsList = mutableListOf<PrestationSponsorisee>()

    // Carousel d'annonces
    val imageList = listOf(
        CarouselItem(R.drawable.chaussures, "20% de réduction", "Promo exceptionnelle !"),
        CarouselItem(R.drawable.shoes, "Qualité garantie", "Chaussures pour hommes et femmes"),
        CarouselItem(R.drawable.shoes_men, "Nouveautés", "Collection été 2025")
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.onglet_acceuil_prestataire, container, false)

        viewPager2 = view.findViewById(R.id.carouselPrestataireAnnonce)
        viewPager2.adapter = CarouselAnnonceAdapter(imageList)
        viewPager2.offscreenPageLimit = 3
        viewPager2.isNestedScrollingEnabled = false


        val sliderRunnable = Runnable {
            viewPager2.currentItem = (viewPager2.currentItem + 1) % imageList.size
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000)
            }
        })

        // Top prestataire Profils (horizontal)
        topPrestataireProfileRecyclerView = view.findViewById(R.id.topPrestataireProfileRecyclerView)
        topPrestataireProfileRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // Pour regler le probleme de blocage quand on scroll (conflit avec le NestedSrollview)
        topPrestataireProfileRecyclerView.isNestedScrollingEnabled = false
        topPrestataireProfileRecyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        adapter = TopPrestataireProfileAdapter(topPrestataires)
        topPrestataireProfileRecyclerView.adapter = adapter
        loadTopPrestataires()

        // Top prestations (publications pour les prestataires sponsorisés)
        topPrestationsRecyclerView = view.findViewById(R.id.topPrestationsHomePageRecyclerView)
        topPrestationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        topPrestationsAdapter = TopPrestationAdapter(prestationsList)
        topPrestationsRecyclerView.adapter = topPrestationsAdapter
        loadTopPrestations()

        return view
    }

    private fun loadTopPrestataires() {
        ApiClient.instance.getTopPrestataires().enqueue(object : Callback<PrestataireResponse> {
            override fun onResponse(
                call: Call<PrestataireResponse>,
                response: Response<PrestataireResponse>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val prestataires = response.body()?.data ?: emptyList()
                    topPrestataires.clear()
                    topPrestataires.addAll(prestataires)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "Erreur : ${response.body()?.message ?: "Inconnue"}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PrestataireResponse>, t: Throwable) {
                Toast.makeText(context, "Échec de connexion : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadTopPrestations() {
        ApiClient.instance.getSponsorisedPublications().enqueue(object : Callback<PrestationSponsoriseesResponse> {
            override fun onResponse(
                call: Call<PrestationSponsoriseesResponse>,
                response: Response<PrestationSponsoriseesResponse>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    prestationsList.clear()
                    prestationsList.addAll(response.body()?.data ?: emptyList())
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Erreur lors du chargement", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PrestationSponsoriseesResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur réseau: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        // Redémarrer le défilement auto si nécessaire
        sliderHandler.postDelayed({
            viewPager2.currentItem = (viewPager2.currentItem + 1) % imageList.size
        }, 3000)
    }

}
