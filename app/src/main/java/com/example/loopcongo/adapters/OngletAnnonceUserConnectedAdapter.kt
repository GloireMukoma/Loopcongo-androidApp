package com.example.loopcongo.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailAnnonceActivity
import com.example.loopcongo.DetailArticleActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Article
import com.example.loopcongo.models.UserAnnonce
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.*

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OngletAnnonceUserConnectedAdapter(
    private val context: Context,
    private var annonces: MutableList<UserAnnonce> // mutable pour suppression
) : RecyclerView.Adapter<OngletAnnonceUserConnectedAdapter.ViewHolder>(), CoroutineScope by MainScope() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgUserAnnonce)
        val titre: TextView = view.findViewById(R.id.titreUserAnnonce)
        val description: TextView = view.findViewById(R.id.userAnnonceDescription)
        val date: TextView = view.findViewById(R.id.userAnnonceCreated)
        val deletedIcon: ImageView = view.findViewById(R.id.btnDeleteAnnonceUserConnected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_annonce_user_connected, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val annonce = annonces[position]

        holder.titre.text = annonce.titre
        holder.description.text = annonce.description
        holder.date.text = annonce.created_at

        Glide.with(context)
            .load("https://loopcongo.com/${annonce.image}") // ⚠️ adapter selon ton JSON
            .centerCrop()
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .into(holder.img)

        // Gestion du clic sur l'icône suppression
        holder.deletedIcon.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
                .setTitle("Supprimer l'annonce")
                .setMessage("Êtes-vous sûr de vouloir supprimer cette annonce ?")
                .setPositiveButton("Oui") { _, _ ->
                    deleteAnnonce(annonce.id, position)
                }
                .setNegativeButton("Non", null)
                .create()

            // Définir le fond blanc
            dialog.window?.setBackgroundDrawableResource(android.R.color.white)

            dialog.show()
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailAnnonceActivity::class.java).apply {
                putExtra("id", annonce.id)
                putExtra("user_id", annonce.user_id)
                putExtra("titre", annonce.titre)
                putExtra("description", annonce.description)
                putExtra("image", annonce.image)
                putExtra("username", annonce.username)
                putExtra("city", annonce.city)
                putExtra("contact", annonce.contact)
                putExtra("file_url", annonce.file_url)
            }
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = annonces.size

    fun updateData(newData: List<UserAnnonce>) {
        annonces = newData.toMutableList()
        notifyDataSetChanged()
    }

    private fun deleteAnnonce(annonceId: Int, position: Int) {
        launch(Dispatchers.IO) {
            try {
                val response = ApiClient.instance.deleteAnnonce(annonceId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Supprime l’élément localement
                        if (position in annonces.indices) {
                            annonces.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, annonces.size - position)
                        }
                        Toast.makeText(context, "Annonce supprimée avec succès", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, "Erreur réseau : ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    // Annule les coroutines si besoin
    fun clear() {
        cancel()
    }
}

