package com.example.loopcongo.fragments.immobiliers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.ImmoUserDemandeAdapter
import com.example.loopcongo.adapters.articles.ArticleGridAdapter
import com.example.loopcongo.adapters.immobiliers.ImmobilierGridAdapter
import com.example.loopcongo.models.*
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Ce fragment est réutilisable pour tout les biens immobiliers immobiliers de chaque ville
// On l'appelle en specifiant la ville afin d'afficher les biens immobiliers
class ImmobilierForAllCityFragment : Fragment() {

    private lateinit var immoRecycler: RecyclerView
    private lateinit var demandeRecycler: RecyclerView

    private lateinit var immoAdapter: ImmobilierGridAdapter
    private lateinit var demandeAdapter: ImmoUserDemandeAdapter

    private val immobiliers = mutableListOf<Immobilier>()
    private val demandes = mutableListOf<ImmoUserDemande>()

    private var cityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityName = arguments?.getString("city_name")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_immobilier_for_all_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demandeRecycler = view.findViewById(R.id.immoUserDemandeRecyclerView)
        immoRecycler = view.findViewById(R.id.immobilierForAllCityRecyclerView)

        // 🔹 LayoutManagers
        demandeRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        immoRecycler.layoutManager = GridLayoutManager(requireContext(), 2)

        // 🔹 Adapters
        demandeAdapter = ImmoUserDemandeAdapter(demandes, R.layout.item_demande)
        immoAdapter = ImmobilierGridAdapter(immobiliers)

        demandeRecycler.adapter = demandeAdapter
        immoRecycler.adapter = immoAdapter

        // 🔹 Charger les données
        loadDemandes()
        loadImmos()
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

    fun loadImmos(quartier: String? = null) {
        ApiClient.instance.searchImmobiliers(cityName, quartier)
            .enqueue(object : Callback<List<Immobilier>> {
                override fun onResponse(
                    call: Call<List<Immobilier>>,
                    response: Response<List<Immobilier>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body() ?: emptyList()
                        immobiliers.clear()
                        immobiliers.addAll(result)
                        immoAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<Immobilier>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Erreur de chargement des biens", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
