package com.example.loopcongo.fragments.prestataire

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.prestataire.PrestataireListAdapter
import com.example.loopcongo.models.Prestataire
import com.example.loopcongo.models.PrestataireListResponse
import com.example.loopcongo.models.PrestataireResponse
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrestataireListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PrestataireListAdapter
    private val prestataires = mutableListOf<Prestataire>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.onglet_list_prestataire, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.ongletListPrestatairesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PrestataireListAdapter(prestataires)
        recyclerView.adapter = adapter

        fetchPrestataires()
    }

    private fun fetchPrestataires() {
        ApiClient.instance.getPrestataires().enqueue(object : Callback<PrestataireListResponse> {
            override fun onResponse(
                call: Call<PrestataireListResponse>,
                response: Response<PrestataireListResponse>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val data = response.body()?.data ?: emptyList()
                    prestataires.clear()
                    prestataires.addAll(data)
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("API_DEBUG", "Réponse API non OK : ${response.body()}")
                    Toast.makeText(requireContext(), "Échec de chargement des prestataires", Toast.LENGTH_SHORT).show()
                }

                if (!response.isSuccessful) {
                    Log.e("API_DEBUG", "Erreur serveur: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<PrestataireListResponse>, t: Throwable) {
                Log.e("API_DEBUG", "Erreur réseau: ${t.localizedMessage}")
                Toast.makeText(requireContext(), "Erreur : ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
