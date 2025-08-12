package com.example.loopcongo.fragments.profilevendeurs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.ArticleGridAdapter

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
        //val articles = articlesList()
        /*adapter = ArticleGridAdapter(requireContext(), articles)
        gridView.adapter = adapter*/

        return view
    }

}
