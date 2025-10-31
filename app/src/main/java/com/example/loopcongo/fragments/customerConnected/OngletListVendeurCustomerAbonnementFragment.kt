package com.example.loopcongo.fragments.customerConnected

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.ProfileVendeurActivity
import com.example.loopcongo.ProfileVendeurImmobilierActivity
import com.example.loopcongo.R
import com.example.loopcongo.adapters.customerConnected.OngletAbonnementListVendeurAdapter
import com.example.loopcongo.models.User
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class OngletListVendeurCustomerAbonnementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var txtEmpty: TextView
    private lateinit var adapter: OngletAbonnementListVendeurAdapter

    private var customerId: Int = 0

    companion object {
        private const val ARG_CUSTOMER_ID = "customer_id"

        fun newInstance(customerId: Int): OngletListVendeurCustomerAbonnementFragment {
            val fragment = OngletListVendeurCustomerAbonnementFragment()
            val args = Bundle()
            args.putInt(ARG_CUSTOMER_ID, customerId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            customerId = it.getInt(ARG_CUSTOMER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.onglet_abonnements_customer_connected_list_vendeur,
            container,
            false
        )

        recyclerView = view.findViewById(R.id.ongletAbonnementsCustomerListVendeurRecyclerView)
        progressBar = view.findViewById(R.id.progressBarAbonnementsCustomer)
        txtEmpty = view.findViewById(R.id.txtEmptyAbonnementsCustomer)

        // 🔹 Définir layoutManager et décoration
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        )

        // 🔹 Définir l’adapter avec listener
        adapter = OngletAbonnementListVendeurAdapter(emptyList()) { vendeur ->
            val nextActivity = when (vendeur.type_account?.lowercase()) {
                "vendeur" -> ProfileVendeurActivity::class.java
                "immobilier" -> ProfileVendeurImmobilierActivity::class.java
                else -> ProfileVendeurActivity::class.java
            }

            val intent = Intent(requireContext(), nextActivity).apply {
                putExtra("vendeurId", vendeur.id)
                putExtra("vendeurUsername", vendeur.username)
                putExtra("vendeurContact", vendeur.contact)
                putExtra("vendeurCity", vendeur.city)
                putExtra("vendeurDescription", vendeur.about)
                putExtra("vendeurTypeAccount", vendeur.type_account)
                putExtra("vendeurAvatarImg", vendeur.file_url)
                putExtra("isCertifiedVendeur", vendeur.is_certified)
            }

            startActivity(intent)
        }

        recyclerView.adapter = adapter

        // 🔹 Charger les abonnements depuis l’API
        fetchAbonnements()

        return view
    }

    private fun fetchAbonnements() {
        progressBar.visibility = View.VISIBLE
        txtEmpty.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.instance.getAbonnementsByCustomer(customerId)
                if (response.isSuccessful) {
                    val abonnements = response.body() ?: emptyList<User>()
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        if (abonnements.isNotEmpty()) {
                            adapter.updateData(abonnements)
                        } else {
                            txtEmpty.visibility = View.VISIBLE
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        txtEmpty.visibility = View.VISIBLE
                        txtEmpty.text = "Erreur API : ${response.code()}"
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    txtEmpty.visibility = View.VISIBLE
                    txtEmpty.text = "Erreur réseau : ${e.message}"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    txtEmpty.visibility = View.VISIBLE
                    txtEmpty.text = "Erreur : ${e.message}"
                }
            }
        }
    }
}
