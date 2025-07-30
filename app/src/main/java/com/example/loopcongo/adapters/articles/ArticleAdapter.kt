package com.example.loopcongo.adapters.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.Article

class ArticleAdapter(private val articles: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.articleTitle)
        val views = view.findViewById<TextView>(R.id.views)
        val prix = view.findViewById<TextView>(R.id.articlePrix)
        val auteur = view.findViewById<TextView>(R.id.articleAuteur)
        /*val timeAgo = view.findViewById<TextView>(R.id.articleTimeAgo)
        val likes = view.findViewById<TextView>(R.id.likes)
        val comments = view.findViewById<TextView>(R.id.comments)*/
        val image = view.findViewById<ImageView>(R.id.articleImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.nom
        holder.prix.text = article.prix
        holder.auteur.text = article.auteur
        /*holder.timeAgo.text = article.articleTimeAgo
        holder.views.text = ""
        holder.likes.text = ""
        holder.comments.text = ""*/
        holder.image.setImageResource(article.imageResId)

        /*holder.views.text = "👁 ${article.views}"
        holder.likes.text = "👍 ${article.likes}"
        holder.comments.text = "💬 ${article.comments}"*/
    }

    override fun getItemCount() = articles.size
}
