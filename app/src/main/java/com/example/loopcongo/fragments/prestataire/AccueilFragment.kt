package com.example.loopcongo.fragments.prestataire

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.R
import com.example.loopcongo.adapters.CarouselAnnonceAdapter
import com.example.loopcongo.adapters.prestataire.StatutPrestataireProfileAdapter
import com.example.loopcongo.models.CarouselItem
import com.example.loopcongo.models.Prestataire
import com.example.loopcongo.models.Prestation

class AccueilFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.onglet_acceuil_prestataire, container, false)

        // Carousel d'annonces
        val imageList = listOf(
            CarouselItem(R.drawable.chaussures, "20% de réduction", "Promo exceptionnelle !"),
            CarouselItem(R.drawable.shoes, "Qualité garantie", "Chaussures pour hommes et femmes"),
            CarouselItem(R.drawable.shoes_men, "Nouveautés", "Collection été 2025")
        )

        viewPager2 = view.findViewById(R.id.carouselPrestataireAnnonce)
        viewPager2.adapter = CarouselAnnonceAdapter(imageList)
        viewPager2.offscreenPageLimit = 3

        val sliderRunnable = Runnable {
            viewPager2.currentItem = (viewPager2.currentItem + 1) % imageList.size
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        // Profils prestataires (horizontal)
        val statutPrestataireRecyclerView = view.findViewById<RecyclerView>(R.id.statutPrestataireProfileRecyclerView)
        statutPrestataireRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val prestataires = listOf(
            Prestataire(1, "Glody", "Plombier", "Disponible 24h/24", R.drawable.avatar_1),
            Prestataire(2, "Sarah", "Coiffeuse", "Salon moderne", R.drawable.avatar_2),
            Prestataire(3, "John", "Mécanicien", "Spécialiste 4x4", R.drawable.chaussures)
        )

        statutPrestataireRecyclerView.adapter = StatutPrestataireProfileAdapter(prestataires)

        // Top prestations (vertical)
        val prestations = listOf(
            Prestation(1, "Coupe de cheveux", "Coiffure pour homme tendance", 5000.0, R.drawable.img, "David", "Coiffeur", "Kinshasa"),
            Prestation(2, "Manucure", "Manucure et soins des ongles", 8000.0, R.drawable.chaussures, "Jessica", "Esthéticienne", "Gombe"),
            Prestation(3, "Nettoyage", "Service de nettoyage à domicile", 12000.0, R.drawable.shoes, "Jean", "Agent d'entretien", "Limete")
        )

        /*val prestationRecyclerView = view.findViewById<RecyclerView>(R.id.topPrestationRecyclerView)
        prestationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        prestationRecyclerView.adapter = PrestationAdapter(prestations)*/

        return view
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacksAndMessages(null)
    }
}
