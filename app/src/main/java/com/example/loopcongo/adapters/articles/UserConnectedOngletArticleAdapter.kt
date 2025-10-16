package com.example.loopcongo.adapters.articles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Article

class UserConnectedOngletArticleAdapter(
    private val context: Context,
    private val articles: List<Article>
) : RecyclerView.Adapter<UserConnectedOngletArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.articleImageUserConnectedOnglet)
        val nomArticle: TextView = view.findViewById(R.id.articleNomUserConnectedOnglet)
        val prix: TextView = view.findViewById(R.id.articlePrixUserConnectedOnglet)
        val description: TextView = view.findViewById(R.id.articleDescUserConnectedOnglet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_article_user_connected_onglet, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        holder.nomArticle.text = article.nom ?: "Nom non disponible"
        holder.prix.text = "${article.prix ?: 0} ${article.devise ?: ""}"
        holder.description.text = article.about ?: ""

        Glide.with(context)
            .load("https://loopcongo.com/${article.file_url}")
            .placeholder(R.drawable.loading)
            .error(R.drawable.img)
            .into(holder.image)
    }

    override fun getItemCount() = articles.size
}
