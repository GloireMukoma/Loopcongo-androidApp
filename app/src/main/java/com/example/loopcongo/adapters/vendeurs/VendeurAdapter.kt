package com.example.loopcongo.adapters.vendeurs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailOffrePrestationActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.OffrePrestation
import com.example.loopcongo.models.Vendeur

class VendeurAdapter(private val vendeurs: List<Vendeur>) :
    RecyclerView.Adapter<VendeurAdapter.VendeurViewHolder>() {

    inner class VendeurViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfileVendeur)
        val nom: TextView = itemView.findViewById(R.id.usernameVendeur)
        val boost: TextView = itemView.findViewById(R.id.nbArticlePublier)
        val about: TextView = itemView.findViewById(R.id.aboutVendeur)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendeurViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vendeur, parent, false)
        return VendeurViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendeurViewHolder, position: Int) {
        val vendeur = vendeurs[position]

        // Affichage nom et description
        holder.nom.text = vendeur.nom
        holder.about.text = vendeur.about ?: "Aucune description"

        // Affiche boost_type ou texte par défaut
        holder.boost.text = when {
            vendeur.is_sponsored == 1 && !vendeur.boost_type.isNullOrBlank() ->
                "• ${vendeur.boost_type.uppercase()}"
            vendeur.is_sponsored == 1 ->
                "• Sponsorisé"
            else ->
                "• Non sponsorisé"
        }

        // Chargement de l'image
        val imageUrl = if (!vendeur.file_url.isNullOrEmpty()) {
            "https://loopcongo.com/${vendeur.file_url}"
        } else {
            "https://loopcongo.com/default-avatar.jpg"
        }
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.avatar) // Optionnel : image temporaire
            .error(R.drawable.avatar)       // Optionnel : image si erreur
            .into(holder.imgProfile)
    }

    override fun getItemCount(): Int = vendeurs.size
}
