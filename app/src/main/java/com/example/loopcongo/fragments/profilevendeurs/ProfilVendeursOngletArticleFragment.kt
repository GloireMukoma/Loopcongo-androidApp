package com.example.loopcongo.fragments.profilevendeurs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.vendeurs.ArticleGridForOngletProfileVendeurAdapter
import com.example.loopcongo.models.Article
import com.example.loopcongo.models.ArticleResponse
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilVendeursOngletArticleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleGridForOngletProfileVendeurAdapter

    private var vendeurId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vendeurId = arguments?.getInt(ARG_VENDEUR_ID) ?: 0
    }
    // On peut aussi l'ecrire de cette maniere
    /*private var vendeurId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vendeurId = it.getInt(ARG_VENDEUR_ID)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profil_vendeurs_onglet_article, container, false)

        recyclerView = view.findViewById(R.id.profileVendeurArticleGridview)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        articleAdapter = ArticleGridForOngletProfileVendeurAdapter(requireContext(), listOf())
        recyclerView.adapter = articleAdapter

        if (vendeurId != 0) {
            loadArticlesVendeur(vendeurId)
        } else {
            Toast.makeText(requireContext(), "ID vendeur invalide", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadArticlesVendeur(id: Int) {
        ApiClient.instance.getArticlesByVendeur(id).enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val articles: List<Article> = response.body()!!.data
                    if (articles.isNotEmpty()) {
                        articleAdapter.updateData(articles)
                    } else {
                        Toast.makeText(requireContext(), "Aucun article trouvé", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de la récupération des articles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    companion object {
        private const val ARG_VENDEUR_ID = "vendeurId"

        fun newInstance(vendeurId: Int): ProfilVendeursOngletArticleFragment {
            val fragment = ProfilVendeursOngletArticleFragment()
            val args = Bundle()
            args.putInt(ARG_VENDEUR_ID, vendeurId)
            fragment.arguments = args
            return fragment
        }
    }
    // On peut aussi l'ecrire de cette maniere
    /*companion object {
        private const val ARG_VENDEUR_ID = "vendeurId"

        fun newInstance(vendeurId: Int) = ProfilVendeursOngletArticleFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_VENDEUR_ID, vendeurId)
            }
        }
    }*/
}
