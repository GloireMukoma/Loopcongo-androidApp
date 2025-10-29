package com.example.loopcongo.fragments.userConnectedProfilOnglets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.OngletAnnonceUserConnectedAdapter
import com.example.loopcongo.adapters.vendeurs.ArticleGridForOngletProfileVendeurAdapter
import com.example.loopcongo.adapters.vendeurs.OngletAnnonceForProfileVendeurAdapter
import com.example.loopcongo.models.AnnonceResponse
import com.example.loopcongo.models.Article
import com.example.loopcongo.models.ArticleResponse
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OngletAnnonceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: OngletAnnonceUserConnectedAdapter
    private var vendeurId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vendeurId = arguments?.getInt(ARG_VENDEUR_ID) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_annonce_profil_user_immobilier_connected, container, false)

        recyclerView = view.findViewById(R.id.ongletAnnonceUserImmoConnectedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Ajoute une ligne qui separe les items des annonces comme dans une listview
        val divider = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)


        articleAdapter = OngletAnnonceUserConnectedAdapter(requireContext(), mutableListOf())
        recyclerView.adapter = articleAdapter

        if (vendeurId != 0) {
            loadAnnonceVendeur(vendeurId)
        } else {
            Toast.makeText(requireContext(), "ID vendeur invalide", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadAnnonceVendeur(id: Int) {
        ApiClient.instance.getAnnoncesByVendeur(id).enqueue(object : Callback<AnnonceResponse> {
            override fun onResponse(call: Call<AnnonceResponse>, response: Response<AnnonceResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val annonces = response.body()!!.data
                    if (annonces.isNotEmpty()) {
                        articleAdapter.updateData(annonces)
                    } else {
                        Toast.makeText(requireContext(), "Aucune annonce trouvée", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la récupération des annonces", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AnnonceResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val ARG_VENDEUR_ID = "vendeurId"

        fun newInstance(vendeurId: Int): OngletAnnonceFragment {
            val fragment = OngletAnnonceFragment()
            val args = Bundle()
            args.putInt(ARG_VENDEUR_ID, vendeurId)
            fragment.arguments = args
            return fragment
        }
    }
}
