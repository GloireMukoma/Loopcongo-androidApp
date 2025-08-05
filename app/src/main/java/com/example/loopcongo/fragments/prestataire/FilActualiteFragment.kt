package com.example.loopcongo.fragments.prestataire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loopcongo.R
import com.example.loopcongo.adapters.prestataire.FilActualitePrestataireAdapter
import com.example.loopcongo.models.Prestation
import com.example.loopcongo.models.PrestationResponse
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilActualiteFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: FilActualitePrestataireAdapter
    private val publicationList = ArrayList<Prestation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_fil_actualite_prestataire, container, false)

        listView = view.findViewById(R.id.PrestationsListFilActualiteRecyclerView)
        adapter = FilActualitePrestataireAdapter(requireContext(), publicationList)
        listView.adapter = adapter

        fetchPublications()

        return view
    }

    private fun fetchPublications() {
        ApiClient.instance.getPublications().enqueue(object : Callback<PrestationResponse> {
            override fun onResponse(
                call: Call<PrestationResponse>,
                response: Response<PrestationResponse>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val publications = response.body()?.data ?: emptyList()
                    publicationList.clear()
                    publicationList.addAll(publications)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Erreur lors du chargement", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PrestationResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
