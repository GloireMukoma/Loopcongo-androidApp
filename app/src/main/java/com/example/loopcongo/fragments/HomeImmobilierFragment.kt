package com.example.loopcongo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.ImmoUserDemandeAdapter
import com.example.loopcongo.adapters.immobiliers.ItemCityImmobilierAdapter
import com.example.loopcongo.models.ApiResponseDemande
import com.example.loopcongo.models.ImmoUserDemande
import com.example.loopcongo.models.ItemCityImmo
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeImmobilierFragment : Fragment() {

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

        // Caroussel des items des demandes
        demandeRecycler = view.findViewById(R.id.immoUserDemandeRecyclerView)
        demandeRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        demandeAdapter = ImmoUserDemandeAdapter(demandes)
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
