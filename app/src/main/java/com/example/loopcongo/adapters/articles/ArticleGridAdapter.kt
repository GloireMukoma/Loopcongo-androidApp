package com.example.loopcongo.adapters.articles

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailArticleActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Article

class ArticleGridAdapter(private var articles: List<Article>) :
    RecyclerView.Adapter<ArticleGridAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgArticle: ImageView = view.findViewById(R.id.img_article)
        val article_nom: TextView = view.findViewById(R.id.nom_article)
        val prix: TextView = view.findViewById(R.id.prix_article)
        val auteur: TextView = view.findViewById(R.id.auteur_article)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_grid, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/" + article.file_url)
            .placeholder(R.drawable.loading)
            .into(holder.imgArticle)

        holder.article_nom.text = article.nom
        holder.prix.text = "${article.prix} ${article.devise}"
        holder.auteur.text = article.user_nom

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailArticleActivity::class.java).apply {
                putExtra("article_id", article.id)
                putExtra("article_nom", article.nom)
                putExtra("article_prix", article.prix)
                putExtra("article_devise", article.devise)
                putExtra("article_description", article.about)
                putExtra("article_nbLike", article.nb_like ?: "0")
                putExtra("article_auteur", article.user_nom)
                putExtra("vendeurSubscriptionType", article.subscription_type)
                putExtra("article_photo", article.file_url)
                putExtra("user_avatar", article.user_avatar)
                putExtra("user_contact", article.user_contact)
                putExtra("user_id", article.account_id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articles.size

    // 🔹 Ajoute cette méthode pour mettre à jour les données
    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}
