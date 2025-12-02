package com.example.loopcongo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.*
import com.example.loopcongo.adapters.vendeurs.VendeurAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.Customer
import com.example.loopcongo.database.CustomerDao
import com.example.loopcongo.database.UserDao
import com.example.loopcongo.models.User
import com.example.loopcongo.models.UserResponse
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendeurListFragment : Fragment() {

    private lateinit var recyclerVendeurs: RecyclerView
    private lateinit var vendeurAdapter: VendeurAdapter
    private val vendeurs = mutableListOf<User>()
    private var type: String? = null

    companion object {
        fun newInstance(type: String): VendeurListFragment {
            val fragment = VendeurListFragment()
            val args = Bundle()
            args.putString("type", type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getString("type")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vendeur_list, container, false)

        recyclerVendeurs = view.findViewById(R.id.vendeurRecyclerView)

        // Configuration RecyclerView
        recyclerVendeurs.layoutManager = LinearLayoutManager(context)
        vendeurAdapter = VendeurAdapter(requireContext(), vendeurs) { vendeur ->
            // Clic sur un vendeur → ouvrir profil
            val intent = when (vendeur.type_account?.lowercase()) {
                "vendeur" -> Intent(context, ProfileVendeurActivity::class.java)
                "immobilier" -> Intent(context, ProfileVendeurImmobilierActivity::class.java)
                else -> Intent(context, ProfileVendeurActivity::class.java)
            }

            intent.putExtra("vendeurId", vendeur.id)
            intent.putExtra("vendeurUsername", vendeur.username)
            intent.putExtra("vendeurContact", vendeur.contact)
            intent.putExtra("vendeurCity", vendeur.city)
            intent.putExtra("vendeurDescription", vendeur.about)
            intent.putExtra("vendeurTypeAccount", vendeur.type_account)
            intent.putExtra("vendeurAvatarImg", vendeur.file_url)
            intent.putExtra("vendeurSubscriptionType", vendeur.subscription_type)
            intent.putExtra("vendeurTotalArticles", vendeur.total_articles)
            intent.putExtra("vendeurTotalLikes", vendeur.total_likes)
            intent.putExtra("vendeurNbAbonner", vendeur.nb_abonner)

            context?.startActivity(intent)
        }
        recyclerVendeurs.adapter = vendeurAdapter

        loadVendeurs(null) // Chargement initial

        return view
    }

    // Appel depuis l’activité principale pour filtrer
    fun filterVendeursFromApi(query: String) {
        loadVendeurs(if (query.isEmpty()) null else query)
    }

    private fun loadVendeurs(username: String?) {
        ApiClient.instance.getVendeurs(username).enqueue(object : Callback<UserResponse> {

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val allVendeurs = response.body()?.data ?: emptyList()

                    // Filtrer par type
                    val filtered = allVendeurs.filter { it.type_account == type }

                    vendeurs.clear()
                    vendeurs.addAll(filtered)

                    vendeurAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Erreur de chargement.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Échec: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
