package com.example.loopcongo.fragments.customerConnected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.ArticleGridAdapter
import com.example.loopcongo.models.Article
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OngletArticlesCustomerAbonnesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var txtEmpty: TextView
    private lateinit var adapter: ArticleGridAdapter

    private var customerId: Int = 0

    companion object {
        private const val ARG_CUSTOMER_ID = "customer_id"

        fun newInstance(customerId: Int): OngletArticlesCustomerAbonnesFragment {
            val fragment = OngletArticlesCustomerAbonnesFragment()
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
        val view = inflater.inflate(R.layout.onglet_article_customer_abonnement_connected, container, false)

        recyclerView = view.findViewById(R.id.ongletArticlesAbonnementsCustomerRecyclerView)
        progressBar = view.findViewById(R.id.progressBarArticlesAbonnementsCustomer)
        txtEmpty = view.findViewById(R.id.txtEmptyArticlesAbonnementsCustomer)

        // LayoutManager pour 2 colonnes
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Adapter
        adapter = ArticleGridAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchArticles()

        return view
    }

    private fun fetchArticles() {
        progressBar.visibility = View.VISIBLE
        txtEmpty.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.instance.getArticlesByCustomerAbonnements(customerId)
                if (response.isSuccessful) {
                    val articles = response.body() ?: emptyList<Article>()
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        if (articles.isNotEmpty()) {
                            adapter.updateData(articles)
                        } else {
                            txtEmpty.visibility = View.VISIBLE
                            txtEmpty.text = "Aucun article disponible"
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        txtEmpty.visibility = View.VISIBLE
                        txtEmpty.text = "Erreur API : ${response.code()}"
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
