package com.example.loopcongo.fragments.userConnectedProfilOnglets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.UserArticleAdapter
import com.example.loopcongo.models.ApiResponse
import com.example.loopcongo.models.ArticleResponse
import com.example.loopcongo.models.UserData
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// L'onglet article de tablayout de l'utilisateur connecté
class OngletArticleFragment : Fragment() {

    private var userId: Int = 0 // sera passé via arguments
    private lateinit var articlesListView: ListView

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: Int): OngletArticleFragment {
            val fragment = OngletArticleFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_USER_ID, userId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt(ARG_USER_ID, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_article_profil_user_connected, container, false)
        articlesListView = view.findViewById(R.id.ongletArticlesProfilUserConnected)
        fetchUserArticles()
        return view
    }

    private fun fetchUserArticles() {
        ApiClient.instance.getArticlesByVendeur(userId).enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val articles = response.body()?.data ?: emptyList()
                    val adapter = UserArticleAdapter(requireContext(), articles)
                    articlesListView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), response.body()?.message ?: "Erreur serveur", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur réseau: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
