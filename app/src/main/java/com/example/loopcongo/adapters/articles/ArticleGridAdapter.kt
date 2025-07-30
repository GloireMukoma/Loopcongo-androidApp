package com.example.loopcongo.adapters.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.ArticleGridView

class ArticleGridAdapter(private val articles: List<ArticleGridView>) :
    RecyclerView.Adapter<ArticleGridAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgArticle: ImageView = view.findViewById(R.id.img_article)
        val nom: TextView = view.findViewById(R.id.nom_article)
        val auteur: TextView = view.findViewById(R.id.auteur_article)
        val date: TextView = view.findViewById(R.id.date_article)
        val prix: TextView = view.findViewById(R.id.prix_article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_grid, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.imgArticle.setImageResource(article.imageResId)
        holder.nom.text = article.titre
        holder.auteur.text = article.auteur
        holder.date.text = article.date
        holder.prix.text = article.prix
    }

    override fun getItemCount(): Int = articles.size
}
