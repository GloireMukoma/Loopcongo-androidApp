package com.example.loopcongo.fragments.immobiliers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.ArticleGridAdapter
import com.example.loopcongo.adapters.immobiliers.ImmobilierGridAdapter
import com.example.loopcongo.models.Article
import com.example.loopcongo.models.ArticleResponse
import com.example.loopcongo.models.Immobilier
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Ce fragment est réutilisable pour tout les biens immobiliers immobiliers de chaque ville
// On l'appelle en specifiant la ville afin d'afficher les biens immobiliers
class ImmobilierForAllCityFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImmobilierGridAdapter
    private val immobiliers = mutableListOf<Immobilier>()
    private var cityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityName = arguments?.getString("city_name") // on récupère le nom de la ville
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_immobilier_for_all_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.immobilierForAllCityRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ImmobilierGridAdapter(immobiliers)
        recyclerView.adapter = adapter

        loadImmos()
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
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        Log.e("ImmobilierAPI", "Erreur: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<Immobilier>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Erreur de chargement", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
