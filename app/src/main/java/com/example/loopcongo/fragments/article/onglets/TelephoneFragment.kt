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

class TelephoneFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.onglet_voiture_article, container, false)
        recyclerView = view.findViewById(R.id.ongletVoitureArticleRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        // Exemple de données

        /*adapter = ArticleGridAdapter(articles)
        recyclerView.adapter = adapter*/

        return view
    }
}
