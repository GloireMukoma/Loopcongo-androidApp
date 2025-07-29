package com.example.loopcongo.adapters.prestataire

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.Prestataire

class StatutPrestataireProfileAdapter(private val profiles: List<Prestataire>) :
    RecyclerView.Adapter<StatutPrestataireProfileAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val background: ImageView = view.findViewById(R.id.statutPrestataireImage)
        //val avatar: ImageView = view.findViewById(R.id.statutPrestataireImageProfile)
        val name: TextView = view.findViewById(R.id.statutPrestataireName)
        //val time: TextView = view.findViewById(R.id.statutPrestataireTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prestataire_profile_statut, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = profiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.background.setImageResource(profile.imageResId)
        //holder.avatar.setImageResource(profile.imageResId)
        holder.name.text = profile.username
        //holder.time.text = profile.description
    }
}

