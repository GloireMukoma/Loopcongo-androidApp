package com.example.loopcongo.adapters.vendeurs

import android.content.Context
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

class ArticleGridForOngletProfileVendeurAdapter(
    private val context: Context,
    private var articles: List<Article>
) : RecyclerView.Adapter<ArticleGridForOngletProfileVendeurAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleImage: ImageView = itemView.findViewById(R.id.imageArticleForOngletProfilVendeur)
        val articleNom: TextView = itemView.findViewById(R.id.nomArticleForOngletProfileVendeur)
        val articlePrix: TextView = itemView.findViewById(R.id.prixArticleForOngletProfileVendeur)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_article_grid_for_onglet_profil_vendeur, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.articleNom.text = article.nom
        //holder.articlePrix.text = article.prix.toString() + article.devise // conversion en String
        val prix = article.prix ?: 0      // Si prix est null, on met 0
        val devise = article.devise ?: "" // Si devise est null, on met une chaîne vide

        holder.articlePrix.text = if (devise.isNotEmpty()) {
            "$prix $devise"
        } else {
            "$prix"
        }

        Glide.with(context)
            .load("https://loopcongo.com/" +article.file_url)
            .placeholder(R.drawable.shoes)
            .into(holder.articleImage)

        // 👉 CLIC : Redirection vers Détail
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailArticleActivity::class.java)

            // Passer les données nécessaires (tu peux en passer plus)
            intent.putExtra("article_nom", article.nom)
            intent.putExtra("article_prix", article.prix)
            intent.putExtra("article_devise", article.devise)
            intent.putExtra("article_description", article.about)
            intent.putExtra("article_nbLike", article.nb_like ?: "0")
            intent.putExtra("article_auteur", article.user_nom)
            intent.putExtra("article_photo", article.file_url)
            intent.putExtra("user_avatar", article.user_avatar)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articles.size

    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}
