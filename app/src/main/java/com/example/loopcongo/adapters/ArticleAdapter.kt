package com.example.loopcongo.adapters

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
        val likes = view.findViewById<TextView>(R.id.likes)
        val comments = view.findViewById<TextView>(R.id.comments)
        val image = view.findViewById<ImageView>(R.id.articleImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.title
        holder.views.text = "👁 ${article.views}"
        holder.likes.text = "👍 ${article.likes}"
        holder.comments.text = "💬 ${article.comments}"
        holder.image.setImageResource(article.imageResId)
    }

    override fun getItemCount() = articles.size
}
