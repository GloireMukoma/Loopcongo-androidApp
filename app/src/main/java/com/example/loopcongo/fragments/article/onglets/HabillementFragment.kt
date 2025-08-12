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

class HabillementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.onglet_habillement_article, container, false)

        // Charger les données provenant de l'API REST (pour les top articles)
        recyclerView = view.findViewById(R.id.ongletHabillementArticleRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        ApiClient.instance.getArticles().enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful && response.body() != null && response.body()!!.status) {
                    val articles = response.body()!!.data  // Récupère la liste d'articles
                    val adapter = ArticleGridAdapter(articles)  // Passe uniquement la liste au constructeur
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Erreur serveur", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur : ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        // End données API REST

        return view
    }
}
