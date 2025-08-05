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
import com.example.loopcongo.models.ArticleApi
import com.example.loopcongo.models.ArticleGridView
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HabillementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.onglet_habillement_article, container, false)

        // Charger les données provenant de l'API REST (pour les top articles)
        recyclerView = view.findViewById(R.id.ongletHabillementArticleRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        ApiClient.instance.getArticles().enqueue(object : Callback<List<ArticleApi>> {
            override fun onResponse(
                call: Call<List<ArticleApi>>,
                response: Response<List<ArticleApi>>
            ) {
                if (response.isSuccessful) {
                    val articles = response.body()!!
                    val adapter = ArticleGridAdapter(articles)
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<ArticleApi>>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur : ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
        // End données API REST

        return view
    }
}
