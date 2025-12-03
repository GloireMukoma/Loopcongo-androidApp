package com.example.loopcongo.fragments.superAdminConnected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.UserConnectedOngletArticleAdapter
import com.example.loopcongo.adapters.superAdminConnected.ItemOngletAllArticlesSuperAdminConnectedAdapter
import com.example.loopcongo.adapters.superAdminConnected.ItemOngletAllImmobiliersSuperAdminConnectedAdapter
import com.example.loopcongo.models.Article
import com.example.loopcongo.models.Immobilier
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.Response

// L'onglet article de tablayout de l'utilisateur connecté
class OngletImmobiliersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var txtEmpty: TextView
    private lateinit var searchBar: EditText
    private lateinit var txtTotalImmobiliers: TextView

    private lateinit var adapter: ItemOngletAllImmobiliersSuperAdminConnectedAdapter

    private var allImmobiliers = mutableListOf<Immobilier>()
    private var filteredImmobiliers = mutableListOf<Immobilier>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(
            R.layout.onglet_all_immobiliers_superadmin_connected,
            container,
            false
        )

        recyclerView = view.findViewById(R.id.ongletAllImmobiliersSuperAdminConnectedRecyclerView)
        progressBar = view.findViewById(R.id.progressBarImmobiliersSuperAdmin)
        txtEmpty = view.findViewById(R.id.txtEmptyImmobiliersSuperAdmin)
        searchBar = view.findViewById(R.id.searchBarImmobiliersSuperAdmin)
        txtTotalImmobiliers = view.findViewById(R.id.txtTotalImmobiliers)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = ItemOngletAllImmobiliersSuperAdminConnectedAdapter(
            requireContext(),
            filteredImmobiliers
        ) { total ->
            txtTotalImmobiliers.text = "Total : $total"
        }

        recyclerView.adapter = adapter

        fetchAllImmobiliers()
        setupSearch()

        return view
    }

    private fun setupSearch() {
        searchBar.addTextChangedListener { text ->
            val query = text.toString().trim().lowercase()

            filteredImmobiliers.clear()
            if (query.isEmpty()) {
                filteredImmobiliers.addAll(allImmobiliers)
            } else {
                filteredImmobiliers.addAll(
                    allImmobiliers.filter {
                        (it.about ?: "").lowercase().contains(query) ||
                                (it.username ?: "").lowercase().contains(query)
                    }
                )
            }

            adapter.notifyDataSetChanged()
            txtEmpty.visibility = if (filteredImmobiliers.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun fetchAllImmobiliers() {
        progressBar.visibility = View.VISIBLE
        txtEmpty.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.instance.getAllImmobiliers()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.status) {
                        allImmobiliers.clear()
                        allImmobiliers.addAll(response.data)

                        filteredImmobiliers.clear()
                        filteredImmobiliers.addAll(allImmobiliers)

                        adapter.notifyDataSetChanged()
                        txtTotalImmobiliers.text = "Total : ${allImmobiliers.size}"
                    } else {
                        txtEmpty.visibility = View.VISIBLE
                        txtEmpty.text = response.message
                    }
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
