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
import com.example.loopcongo.models.ArticleAnnonce

class CarouselAnnonceArticleAdapter(private val context: Context, private val annonces: List<ArticleAnnonce>) :
    RecyclerView.Adapter<CarouselAnnonceArticleAdapter.AnnonceViewHolder>() {

    inner class AnnonceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageAnnonce: ImageView = view.findViewById(R.id.carouselAnnonceArticleImage)
        val titreAnnonce: TextView = view.findViewById(R.id.carousselAnnonceArticleTitre)
        val descriptionAnnonce: TextView = view.findViewById(R.id.carousselAnnonceArticleDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnonceViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_carousel_article_annonce, parent, false)
        return AnnonceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnonceViewHolder, position: Int) {
        val annonce = annonces[position]
        holder.titreAnnonce.text = annonce.titre
        holder.descriptionAnnonce.text = annonce.titre
        Glide.with(context).load("https://loopcongo.com/" +annonce.annonce_image).into(holder.imageAnnonce)
    }

    override fun getItemCount(): Int = annonces.size
}
