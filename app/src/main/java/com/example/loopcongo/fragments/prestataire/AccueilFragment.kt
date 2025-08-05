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
import com.example.loopcongo.adapters.PrestationAdapter
import com.example.loopcongo.adapters.prestataire.StatutPrestataireProfileAdapter
import com.example.loopcongo.models.CarouselItem
import com.example.loopcongo.models.Prestataire
import com.example.loopcongo.models.Prestation

class AccueilFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())

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
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        // Profils prestataires (horizontal)
        val statutPrestataireRecyclerView = view.findViewById<RecyclerView>(R.id.statutPrestataireProfileRecyclerView)
        statutPrestataireRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // Pour regler le probleme de blocage quand on scroll (conflit avec le NestedSrollview)
        statutPrestataireRecyclerView.isNestedScrollingEnabled = false
        statutPrestataireRecyclerView.overScrollMode = View.OVER_SCROLL_NEVER

        /*val prestataires = listOf(
            Prestataire(1, "Glody mukoma", "Plombier", "Disponible 24h/24", R.drawable.user1),
            Prestataire(2, "Sarah km", "Coiffeuse", "Salon moderne", R.drawable.user2),
            Prestataire(3, "John living", "Mécanicien", "Spécialiste 4x4", R.drawable.user3),
            Prestataire(4, "Glody", "Plombier", "Disponible 24h/24", R.drawable.user4),
            Prestataire(5, "Sarah mulanba", "Coiffeuse", "Salon moderne", R.drawable.user5),
            Prestataire(6, "John", "Mécanicien", "Spécialiste 4x4", R.drawable.user3),
            Prestataire(7, "Glody", "Plombier", "Disponible 24h/24", R.drawable.user4),
            Prestataire(8, "Sarah", "Coiffeuse", "Salon moderne", R.drawable.user1),
            Prestataire(9, "John", "Mécanicien", "Spécialiste 4x4", R.drawable.user5)
        )*/

        //statutPrestataireRecyclerView.adapter = StatutPrestataireProfileAdapter(prestataires)

        // Top prestations (vertical)
        /*val prestations = listOf(
            Prestation(1, "Coupe de cheveux", "Coiffure pour homme tendance", 5000.0, R.drawable.user1, "David", "Coiffeur", "Kinshasa"),
            Prestation(2, "Manucure", "Manucure et soins des ongles", 8000.0, R.drawable.user2, "Jessica", "Esthéticienne", "Gombe"),
            Prestation(3, "Nettoyage", "Service de nettoyage à domicile", 12000.0, R.drawable.user3, "Jean", "Agent d'entretien", "Limete"),
            Prestation(4, "Coupe de cheveux", "Coiffure pour homme tendance", 5000.0, R.drawable.user4, "David", "Coiffeur", "Kinshasa"),
            Prestation(5, "Manucure", "Manucure et soins des ongles", 8000.0, R.drawable.user2, "Jessica", "Esthéticienne", "Gombe"),
            Prestation(6, "Nettoyage", "Service de nettoyage à domicile", 12000.0, R.drawable.user1, "Jean", "Agent d'entretien", "Limete")
        )

        val prestationRecyclerView = view.findViewById<RecyclerView>(R.id.topPrestationPageAcceuilRecyclerView)
        prestationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        prestationRecyclerView.adapter = PrestationAdapter(prestations)*/

        return view
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
