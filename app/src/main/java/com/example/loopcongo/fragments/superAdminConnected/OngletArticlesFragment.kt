package com.example.loopcongo.fragments.superAdminConnected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.UserConnectedOngletArticleAdapter
import com.example.loopcongo.adapters.superAdminConnected.ItemOngletAllArticlesSuperAdminConnectedAdapter
import com.example.loopcongo.models.Article
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.Response

// L'onglet article de tablayout de l'utilisateur connecté
class OngletArticlesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var txtEmpty: TextView
    private lateinit var searchBar: EditText

    private lateinit var adapter: ItemOngletAllArticlesSuperAdminConnectedAdapter

    private var allArticles = mutableListOf<Article>()
    private var filteredArticles = mutableListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.onglet_all_articles_superadmin_connected,
            container,
            false
        )

        recyclerView = view.findViewById(R.id.ongletAllArticlesSuperAdminConnectedRecyclerView)
        progressBar = view.findViewById(R.id.progressBarArticlesSuperAdmin)
        txtEmpty = view.findViewById(R.id.txtEmptyArticlesSuperAdmin)
        searchBar = view.findViewById(R.id.searchBarArticlesSuperAdmin)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = ItemOngletAllArticlesSuperAdminConnectedAdapter(
            requireContext(),
            filteredArticles,
            ::onDeleteArticle
        )

        recyclerView.adapter = adapter

        fetchAllArticles()
        setupSearch()

        return view
    }

    private fun setupSearch() {
        searchBar.addTextChangedListener { text ->
            val query = text.toString().trim().lowercase()

            filteredArticles.clear()

            if (query.isEmpty()) {
                filteredArticles.addAll(allArticles)
            } else {
                filteredArticles.addAll(
                    allArticles.filter {
                        (it.nom ?: "").lowercase().contains(query) ||
                                (it.username ?: "").lowercase().contains(query)
                    }
                )
            }

            adapter.notifyDataSetChanged()

            txtEmpty.visibility =
                if (filteredArticles.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun fetchAllArticles() {
        progressBar.visibility = View.VISIBLE
        txtEmpty.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.instance.getAllProductsForAdmin()

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    if (response.status) {
                        allArticles = response.data.toMutableList()
                        filteredArticles.clear()
                        filteredArticles.addAll(allArticles)
                        adapter.notifyDataSetChanged()
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

    /** Suppression : mettre à jour les deux listes */
    private fun onDeleteArticle(position: Int) {
        if (position in filteredArticles.indices) {
            val article = filteredArticles[position]

            // Supprimer aussi dans la liste complète
            allArticles.remove(article)

            filteredArticles.removeAt(position)
            adapter.notifyItemRemoved(position)

            if (filteredArticles.isEmpty())
                txtEmpty.visibility = View.VISIBLE
        }
    }
}

