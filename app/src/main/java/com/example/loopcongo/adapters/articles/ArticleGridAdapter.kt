package com.example.loopcongo.adapters.articles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.Article

class ArticleGridAdapter(private val context: Context, private val articles: List<Article>) : BaseAdapter() {
    override fun getCount() = articles.size
    override fun getItem(position: Int) = articles[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_grid_article, parent, false)
        val article = articles[position]

        view.findViewById<ImageView>(R.id.gridimageArticle).setImageResource(article.imageResId)
        view.findViewById<TextView>(R.id.gridtitleArticle).text = article.nom
        view.findViewById<TextView>(R.id.gridpriceArticle).text = article.prix

        return view
    }
}
