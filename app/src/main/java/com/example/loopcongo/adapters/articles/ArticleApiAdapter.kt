package com.example.loopcongo.adapters.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Article
import com.example.loopcongo.models.ArticleApi

class ArticleApiAdapter(private val articles: List<ArticleApi>) :
    RecyclerView.Adapter<ArticleApiAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.articleImage)
        val title: TextView = itemView.findViewById(R.id.articleTitle)
        val prix: TextView = itemView.findViewById(R.id.articlePrix)
        //val badge: TextView = itemView.findViewById(R.id.articleBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_api, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.nom
        holder.prix.text = "${article.prix} ${article.devise}"

        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/" + article.file_url)
            .into(holder.image)

        /*if (article.is_sponsored == 1) {
            holder.badge.visibility = View.VISIBLE
        } else {
            holder.badge.visibility = View.GONE
        }*/
    }

    override fun getItemCount() = articles.size
}
