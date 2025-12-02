package com.example.loopcongo.adapters.userImmobilierConnected

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Immobilier
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.*

class UserConnectedOngletImmobilierAdapter(
    private val context: Context,
    private val immobiliers: MutableList<Immobilier>
) : RecyclerView.Adapter<UserConnectedOngletImmobilierAdapter.ImmoViewHolder>(), CoroutineScope by MainScope() {

    inner class ImmoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageImmobilierUserConnectedOnglet)
        val description: TextView = itemView.findViewById(R.id.descImmobilierUserConnectedOnglet)

        val prix: TextView = itemView.findViewById(R.id.prixImmobilierUserConnectedOnglet)
        val quartier: TextView = itemView.findViewById(R.id.quartierImmobilierUserConnectedOnglet)

        val btnDelete: ImageView = itemView.findViewById(R.id.btnDeleteImmobilierUserConnectedOnglet)
        val btnUpdate: ImageView = itemView.findViewById(R.id.btnUpdateImmoUserConnectedOnglet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImmoViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_immobilier_user_connected, parent, false)
        return ImmoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImmoViewHolder, position: Int) {
        val immo = immobiliers[position]

        holder.description.text = immo.about
        holder.prix.text = "${immo.prix} $"
        holder.quartier.text = immo.quartier

        Glide.with(context)
            .load("https://loopcongo.com/${immo.file_url}")
            .placeholder(R.drawable.loading)
            .into(holder.image)

        holder.btnUpdate.setOnClickListener {
            val url = "https://loopcongo.com/immo/edit/${immo.id}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }

        // ✅ Gestion du clic sur le bouton Supprimer
        holder.btnDelete.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Supprimer l'immobilier")
                .setMessage("Êtes-vous sûr de vouloir supprimer ce bien ?")
                .setPositiveButton("Oui") { _, _ ->
                    val pos = holder.adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        deleteImmo(immo.id, pos)
                    }
                }
                .setNegativeButton("Non", null)
                .show()
        }
    }

    override fun getItemCount(): Int = immobiliers.size

    // ✅ Fonction de suppression
    private fun deleteImmo(immoId: Int, position: Int) {
        launch(Dispatchers.IO) {
            try {
                val response = ApiClient.instance.deleteImmobilier(immoId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (position in 0 until immobiliers.size) {
                            immobiliers.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, immobiliers.size - position)
                        }
                        Toast.makeText(context, "Immobilier supprimé", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context,
                            "Erreur de suppression : ${response.code()}",
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

    // ✅ Pour annuler les coroutines si besoin (ex: fragment détruit)
    fun clear() {
        cancel()
    }
}
