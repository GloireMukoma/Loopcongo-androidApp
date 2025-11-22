package com.example.loopcongo.adapters.superAdminConnected

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Article
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemOngletAllArticlesSuperAdminConnectedAdapter(
    private val context: Context,
    private val articles: MutableList<Article>,
    private val onDeleteSuccess: (Int) -> Unit
) : RecyclerView.Adapter<ItemOngletAllArticlesSuperAdminConnectedAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.articleImageSuperAdminOnglet)
        val nomArticle: TextView = view.findViewById(R.id.articleNomSuperAdminOnglet)
        val username: TextView = view.findViewById(R.id.usernameArticleSuperAdminOnglet)
        val prix: TextView = view.findViewById(R.id.articlePrixSuperAdminOnglet)
        val btnDelete: ImageView = view.findViewById(R.id.btnDeleteArticleSuperAdminOnglet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_all_articles_superadmin_connected, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        holder.nomArticle.text = article.nom ?: "Sans titre"
        holder.username.text = article.username ?: "Utilisateur"
        holder.prix.text = "${article.prix ?: 0} ${article.devise ?: ""}"

        Glide.with(context)
            .load("https://loopcongo.com/${article.file_url}")
            .placeholder(R.drawable.loading)
            .error(R.drawable.img)
            .into(holder.image)

        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Supprimer l'article")
                .setMessage("Voulez-vous vraiment supprimer cet article ?")
                .setPositiveButton("Oui") { _, _ ->
                    deleteArticle(article.id, holder.adapterPosition)
                }
                .setNegativeButton("Non", null)
                .show()
        }
    }

    override fun getItemCount() = articles.size

    private fun deleteArticle(articleId: Int, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.deleteArticle(articleId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {

                        Toast.makeText(context, "Article supprimé", Toast.LENGTH_SHORT).show()

                        // On avertit le Fragment
                        onDeleteSuccess(position)

                    } else {
                        Toast.makeText(
                            context,
                            "Erreur suppression : ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Erreur réseau : ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
