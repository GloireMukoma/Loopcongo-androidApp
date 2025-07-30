package com.example.loopcongo.fragments.profilevendeurs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.ArticleGridAdapter
import com.example.loopcongo.models.Article

class ProfilVendeursOngletArticleFragment : Fragment() {

    private lateinit var gridView: GridView
    private lateinit var adapter: ArticleGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profil_vendeurs_onglet_article, container, false)

        gridView = view.findViewById(R.id.gridViewArticles)

        // Tu peux remplacer cet appel plus tard par les articles du vendeur
        val articles = articlesList()

        /*adapter = ArticleGridAdapter(requireContext(), articles)
        gridView.adapter = adapter*/

        return view
    }

    private fun articlesList(): List<Article> {
        return listOf(
            Article("Créer des interfaces animées...", "10 $", "Tatiana moda", "Il y a 1 min", 1736, 48, 5, R.drawable.shoes_men),
            Article("Pourquoi j’ai choisi Flutter...", "21000 CDF", "Gloire mukoma", "Il y a 19 min", 872, 82, 9, R.drawable.shoes),
            Article("8 astuces cachées en CSS", "85 $", "Tatiana moda", "Il y a 1 min", 734, 69, 2, R.drawable.chaussures),
            Article("Créer des interfaces animées...", "120 $", "Etienne mulenda", "Il y a 2 h", 1736, 48, 5, R.drawable.shoes_men),
            Article("Pourquoi j’ai choisi Flutter...", "8500 CDF", "Tatiana moda", "Il y a 1 min", 872, 82, 9, R.drawable.chaussures),
            Article("8 astuces cachées en CSS", "45 $", "Elisée kab", "Il y a 1 min", 734, 69, 2, R.drawable.shoes),
            Article("Créer des interfaces animées...", "1500 CDF", "Tatiana moda", "Il y a 1 h", 1736, 48, 5, R.drawable.chaussures),
            Article("Pourquoi j’ai choisi Flutter...", "40 $", "Tatiana moda", "Il y a 1 min", 872, 82, 9, R.drawable.shoes_men),
            Article("8 astuces cachées en CSS", "22500 CDF", "Tatiana moda", "Il y a 3 h", 734, 69, 2, R.drawable.shoes),
        )
    }
}
