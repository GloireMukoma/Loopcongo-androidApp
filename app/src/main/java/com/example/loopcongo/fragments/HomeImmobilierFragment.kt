package com.example.loopcongo.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.DemandesUsersActivity
import com.example.loopcongo.DetailAnnonceActivity
import com.example.loopcongo.R
import com.example.loopcongo.adapters.ImmoUserDemandeAdapter
import com.example.loopcongo.adapters.articles.CarouselUserAnnonceAdapter
import com.example.loopcongo.adapters.immobiliers.ItemCityImmobilierAdapter
import com.example.loopcongo.models.AnnonceResponse
import com.example.loopcongo.models.ApiResponseDemande
import com.example.loopcongo.models.ImmoUserDemande
import com.example.loopcongo.models.ItemCityImmo
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeImmobilierFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())

    private lateinit var recyclerInspiration: RecyclerView
    private lateinit var adapter: ItemCityImmobilierAdapter

    private lateinit var demandeRecycler: RecyclerView

    private lateinit var demandeAdapter: ImmoUserDemandeAdapter
    private val demandes = mutableListOf<ImmoUserDemande>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_immobilier, container, false)

        val btnVoirOffres = view.findViewById<Button>(R.id.btnVoirDemandesUsers)
        btnVoirOffres.setOnClickListener {
            val intent = Intent(requireContext(), DemandesUsersActivity::class.java)
            startActivity(intent)
        }


        // Gere la caroussel des annonces
        viewPager2 = view.findViewById(R.id.carouselImmobilierAnnonce)
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

        // Caroussel des items des demandes
        demandeRecycler = view.findViewById(R.id.immoUserDemandeRecyclerView)
        demandeRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        demandeAdapter = ImmoUserDemandeAdapter(demandes,  R.layout.item_demande)
        demandeRecycler.adapter = demandeAdapter

        recyclerInspiration = view.findViewById(R.id.itemCityImmobilierHomePageRecyclerView)
        recyclerInspiration.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val inspirations = listOf(
            ItemCityImmo(1,"Kinshasa", "120 biens", "https://content.r9cdn.net/rimg/dimg/78/42/61c54939-city-44780-16e3f996927.jpg?width=1200&height=630&xhint=1500&yhint=593&crop=true"),
            ItemCityImmo(2,"Lubumbashi", "85 biens", "https://www.congodurable.net/wp-content/uploads/2018/12/Violences-%C3%A0-Lubumbashi.jpg"),
            ItemCityImmo(3,"Goma", "42 biens", "https://content.r9cdn.net/rimg/dimg/78/42/61c54939-city-44780-16e3f996927.jpg?width=1200&height=630&xhint=1500&yhint=593&crop=true")
        )

        adapter = ItemCityImmobilierAdapter(inspirations)
        recyclerInspiration.adapter = adapter

        loadDemandes()

        return view
    }
    private fun loadDemandes() {
        ApiClient.instance.getUserImmoDemandes().enqueue(object : Callback<ApiResponseDemande> {
            override fun onResponse(
                call: Call<ApiResponseDemande>,
                response: Response<ApiResponseDemande>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.demandes ?: emptyList()
                    demandes.clear()
                    demandes.addAll(result)
                    demandeAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ApiResponseDemande>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur de chargement des demandes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
