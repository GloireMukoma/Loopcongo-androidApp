package com.example.loopcongo.fragments.article.onglets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.ArticleGridAdapter
import com.example.loopcongo.models.Article
import com.example.loopcongo.models.ArticleResponse
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Ce fragment est réutilisable pour tout les categories
// On l'appelle en specifiant la categorie d'articles a afficher
class ArticleForAllCategoriesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleGridAdapter
    private val articles = mutableListOf<Article>()
    private var categoryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getInt("categorie_id") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Ici on gonfle ton layout fragment_article_for_all_categories.xml
        return inflater.inflate(R.layout.fragment_article_for_all_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.articlesForAllCategoriesRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ArticleGridAdapter(articles)
        recyclerView.adapter = adapter
        loadArticles()
    }
    fun loadArticles(search: String? = null) {
        ApiClient.instance.getArticlesByCategory(categoryId, search)
            .enqueue(object : Callback<List<Article>> {
                override fun onResponse(
                    call: Call<List<Article>>,
                    response: Response<List<Article>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body() ?: emptyList()
                        articles.clear()
                        articles.addAll(result)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                    Toast.makeText(requireContext(), "Erreur de chargement", Toast.LENGTH_SHORT).show()
                }
            })
    }

}