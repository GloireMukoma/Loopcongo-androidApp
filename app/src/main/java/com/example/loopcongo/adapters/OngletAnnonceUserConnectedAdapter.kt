package com.example.loopcongo.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

class OngletAnnonceUserConnectedAdapter(
    context: Context,
    private var annonces: MutableList<UserAnnonce>
) : ArrayAdapter<UserAnnonce>(context, 0, annonces) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_annonce_user_connected, parent, false)

        val annonce = annonces[position]

        val img: ImageView = view.findViewById(R.id.imgUserAnnonce)
        val titre: TextView = view.findViewById(R.id.titreUserAnnonce)
        val description: TextView = view.findViewById(R.id.userAnnonceDescription)
        val date: TextView = view.findViewById(R.id.userAnnonceCreated)
        val deleteIcon: ImageView = view.findViewById(R.id.btnDeleteAnnonceUserConnected)

        titre.text = annonce.titre
        description.text = annonce.description
        date.text = annonce.created_at

        Glide.with(context)
            .load("https://loopcongo.com/${annonce.image}")
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .centerCrop()
            .into(img)

        // 🔥 Gestion suppression
        deleteIcon.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
                .setTitle("Supprimer l’annonce")
                .setMessage("Êtes-vous sûr de vouloir supprimer cette annonce ?")
                .setPositiveButton("Oui") { _, _ ->
                    deleteAnnonce(annonce.id, position)
                }
                .setNegativeButton("Non", null)
                .create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.white)
            dialog.show()
        }

        // 🔥 Clic pour afficher le détail
        view.setOnClickListener {
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

        return view
    }

    private fun deleteAnnonce(annonceId: Int, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.deleteAnnonce(annonceId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        annonces.removeAt(position)
                        notifyDataSetChanged()
                        Toast.makeText(context, "Annonce supprimée", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Erreur serveur : ${response.code()}",
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

    fun updateData(newList: List<UserAnnonce>) {
        annonces.clear()
        annonces.addAll(newList)
        notifyDataSetChanged()
    }
}


