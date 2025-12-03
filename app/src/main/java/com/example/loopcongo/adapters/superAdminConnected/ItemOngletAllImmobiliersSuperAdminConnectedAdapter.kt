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
import com.example.loopcongo.models.Immobilier
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemOngletAllImmobiliersSuperAdminConnectedAdapter(
    private val context: Context,
    private val immobiliers: MutableList<Immobilier>,
    private val onTotalUpdated: (Int) -> Unit  // callback pour mettre à jour le total
) : RecyclerView.Adapter<ItemOngletAllImmobiliersSuperAdminConnectedAdapter.ImmoViewHolder>() {

    inner class ImmoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.immobilierImageSuperAdminOnglet)
        val description: TextView = view.findViewById(R.id.descImmobilierSuperAdminOnglet)
        val username: TextView = view.findViewById(R.id.usernameImmobilierSuperAdminOnglet)
        val prix: TextView = view.findViewById(R.id.immobilierPrixSuperAdminOnglet)
        val btnDelete: ImageView = view.findViewById(R.id.btnDeleteImmobilierSuperAdminOnglet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImmoViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_all_immobiliers_superadmin_connected, parent, false)
        return ImmoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImmoViewHolder, position: Int) {
        val immo = immobiliers[position]

        holder.description.text = immo.about ?: "Sans titre"
        holder.username.text = immo.username ?: "Utilisateur"
        holder.prix.text = "${immo.statut} • ${immo.prix} $"

        Glide.with(context)
            .load("https://loopcongo.com/${immo.file_url}")
            .placeholder(R.drawable.loading)
            .error(R.drawable.img)
            .into(holder.image)

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

    override fun getItemCount() = immobiliers.size

    private fun deleteImmo(immoId: Int, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.deleteImmobilier(immoId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (position in 0 until immobiliers.size) {
                            immobiliers.removeAt(position)
                            notifyItemRemoved(position)
                        }
                        Toast.makeText(context, "Immobilier supprimé", Toast.LENGTH_SHORT).show()
                        onTotalUpdated(immobiliers.size) // met à jour le total
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

}

