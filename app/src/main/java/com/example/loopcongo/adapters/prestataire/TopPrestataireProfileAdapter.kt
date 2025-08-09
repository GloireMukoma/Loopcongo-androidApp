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

class TopPrestataireProfileAdapter(
    private val prestataires: List<Prestataire>
) : RecyclerView.Adapter<TopPrestataireProfileAdapter.PrestataireViewHolder>() {


    class PrestataireViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoProfil: ImageView = itemView.findViewById(R.id.topPrestataireProfilImg)
        val username: TextView = itemView.findViewById(R.id.topPrestataireUserName)
        val profession: TextView = itemView.findViewById(R.id.topPrestataireProfession)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestataireViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_top_prestataire, parent, false)
        return PrestataireViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrestataireViewHolder, position: Int) {
        val prestataire = prestataires[position]

        holder.username.text = prestataire.username
        holder.profession.text = prestataire.profession

        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/" + prestataire.photo_profil)
            .placeholder(R.drawable.avatar)
            .into(holder.photoProfil)
    }

    override fun getItemCount(): Int = prestataires.size
}

