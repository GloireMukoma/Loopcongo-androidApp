package com.example.loopcongo.adapters.articles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.UserAnnonce

class CarouselUserAnnonceAdapter(
    private val context: Context,
    private val annonces: List<UserAnnonce>,
    private val onItemClick: ((UserAnnonce) -> Unit)? = null  // callback clic
) : RecyclerView.Adapter<CarouselUserAnnonceAdapter.AnnonceViewHolder>() {

    inner class AnnonceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageAnnonce: ImageView = view.findViewById(R.id.carouselAnnonceArticleImage)
        val titreAnnonce: TextView = view.findViewById(R.id.carousselAnnonceArticleTitre)
        val descriptionAnnonce: TextView = view.findViewById(R.id.carousselAnnonceArticleDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnonceViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_carousel_user_annonce, parent, false)
        return AnnonceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnonceViewHolder, position: Int) {
        val annonce = annonces[position]
        holder.titreAnnonce.text = annonce.titre
        holder.descriptionAnnonce.text = annonce.description
        Glide.with(context).load("https://loopcongo.com/" + annonce.image).into(holder.imageAnnonce)

        // Gestion du clic
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(annonce)
        }
    }

    override fun getItemCount(): Int = annonces.size
}
