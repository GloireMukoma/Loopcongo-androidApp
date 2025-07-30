package com.example.loopcongo.fragments.article.onglets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.ArticleGridAdapter
import com.example.loopcongo.models.ArticleGridView

class HabillementFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.onglet_habillement_article, container, false)
        recyclerView = view.findViewById(R.id.ongletHabillementArticleRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        // Exemple de données
        val articles = listOf(
            ArticleGridView(R.drawable.shoes, "Robe d'été", "Awa Boutique", "il y a 3h", "25.000 FC"),
            ArticleGridView(R.drawable.shoes_men, "Costume Homme", "ModaLux", "hier", "150.000 FC"),
            ArticleGridView(R.drawable.chaussures, "T-Shirt Graphique", "UrbanStyle", "il y a 5h", "12.000 FC"),
            ArticleGridView(R.drawable.shoes, "Robe d'été", "Awa Boutique", "il y a 3h", "25.000 FC"),
            ArticleGridView(R.drawable.shoes_men, "Costume Homme", "ModaLux", "hier", "150.000 FC"),
            ArticleGridView(R.drawable.chaussures, "T-Shirt Graphique", "UrbanStyle", "il y a 5h", "12.000 FC"),
            ArticleGridView(R.drawable.chaussures, "T-Shirt Graphique", "UrbanStyle", "il y a 5h", "12.000 FC"),
            ArticleGridView(R.drawable.shoes, "Robe d'été", "Awa Boutique", "il y a 3h", "25.000 FC"),
            ArticleGridView(R.drawable.shoes_men, "Costume Homme", "ModaLux", "hier", "150.000 FC"),
            ArticleGridView(R.drawable.chaussures, "T-Shirt Graphique", "UrbanStyle", "il y a 5h", "12.000 FC")
        )

        adapter = ArticleGridAdapter(articles)
        recyclerView.adapter = adapter

        return view
    }
}
