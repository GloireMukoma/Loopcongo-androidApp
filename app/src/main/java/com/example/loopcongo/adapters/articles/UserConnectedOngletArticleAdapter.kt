package com.example.loopcongo.adapters.articles

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Article
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserConnectedOngletArticleAdapter(
    private val context: Context,
    private val articles: MutableList<Article> // <- mutable pour removeAt()
) : RecyclerView.Adapter<UserConnectedOngletArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.articleImageUserConnectedOnglet)
        val nomArticle: TextView = view.findViewById(R.id.articleNomUserConnectedOnglet)
        val prix: TextView = view.findViewById(R.id.articlePrixUserConnectedOnglet)
        val description: TextView = view.findViewById(R.id.articleDescUserConnectedOnglet)
        val btnDelete: ImageView = view.findViewById(R.id.btnDeleteArticleUserConnectedOnglet)
        val btnUpdate: ImageView = view.findViewById(R.id.btnUpdateArticleUserConnectedOnglet)
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

        holder.btnUpdate.setOnClickListener {
            val url = "https://loopcongo.com/product/edit/${article.id}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
                .setTitle("Supprimer l'article")
                .setMessage("Êtes-vous sûr de vouloir supprimer cet article ?")
                .setPositiveButton("Oui") { _, _ ->
                    val pos = holder.adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        deleteArticle(article.id, pos)
                    }
                }
                .setNegativeButton("Non", null)
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE)
            }

            dialog.show()
        }
    }

    override fun getItemCount() = articles.size

    private fun deleteArticle(articleId: Int, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.deleteArticle(articleId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (position in 0 until articles.size) {
                            articles.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, articles.size - position)
                        }
                        Toast.makeText(context, "Article supprimé", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Erreur suppression: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erreur réseau: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
