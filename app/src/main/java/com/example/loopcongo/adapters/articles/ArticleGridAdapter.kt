package com.example.loopcongo.adapters.articles

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.ArticleDetailActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Article

class ArticleGridAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<ArticleGridAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgArticle: ImageView = view.findViewById(R.id.img_article)
        val article_nom: TextView = view.findViewById(R.id.nom_article)
        val prix: TextView = view.findViewById(R.id.prix_article)
        val auteur: TextView = view.findViewById(R.id.auteur_article)
        //val date: TextView = view.findViewById(R.id.datqae_article)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_grid, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        // Charger image depuis URL
        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/" +article.file_url)
            .placeholder(R.drawable.loading)
            .into(holder.imgArticle)

        // Remplir les champs
        holder.article_nom.text = article.nom
        holder.prix.text = "${article.prix} ${article.devise}"

        // Auteur : non fourni dans l’API, on affiche "Inconnu" ou tu peux afficher l’ID ou rien
        holder.auteur.text = article.user_nom

        // Date brute ou formatée
        //holder.date.text = formatRelativeDate(article.created_at)

        // 👉 CLIC : Redirection vers Détail
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ArticleDetailActivity::class.java)

            // Passer les données nécessaires (tu peux en passer plus)

            intent.putExtra("article_nom", article.nom)
            intent.putExtra("article_prix", article.prix)
            intent.putExtra("article_devise", article.devise)
            intent.putExtra("article_description", article.about)
            intent.putExtra("article_nbLike", article.nb_like ?: "0")
            intent.putExtra("article_auteur", article.user_nom)
            intent.putExtra("article_photo", article.file_url)
            intent.putExtra("user_avatar", article.user_avatar)
            intent.putExtra("user_contact", article.user_contact)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articles.size

    // Utilitaire simple pour afficher "il y a X jours/heures"
    private fun formatRelativeDate(createdAt: String): String {
        // Exemple basique, à remplacer par un vrai calcul de date si besoin
        return createdAt.take(10) // juste pour montrer la date yyyy-MM-dd
    }
}
