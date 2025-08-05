package com.example.loopcongo.adapters.prestataire

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Prestataire

class PrestataireListAdapter(
    private val prestataires: List<Prestataire>
) : RecyclerView.Adapter<PrestataireListAdapter.PrestataireViewHolder>() {

    inner class PrestataireViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.usernamePrestataire)
        val profession: TextView = itemView.findViewById(R.id.professionPrestataire)
        val ville: TextView = itemView.findViewById(R.id.localisationPrestataire)
        val photoProfil: ImageView = itemView.findViewById(R.id.photoProfilPrestataire)
        val about: TextView = itemView.findViewById(R.id.aboutPrestataire)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestataireViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prestataire, parent, false)
        return PrestataireViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrestataireViewHolder, position: Int) {
        val prestataire = prestataires[position]
        holder.username.text = prestataire.username
        holder.profession.text ="${prestataire.profession}"
        holder.ville.text = prestataire.ville ?: "N/A"
        holder.about.text = prestataire.description

        // Exemple avec Glide pour charger la photo
        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/" +prestataire.photo_profil)
            .placeholder(R.drawable.avatar)
            .into(holder.photoProfil)
    }

    override fun getItemCount() = prestataires.size
}
