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
import com.example.loopcongo.ArticleDetailActivity
import com.example.loopcongo.DetailOffrePrestationActivity
import com.example.loopcongo.ProfileVendeurActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.OffrePrestation
import com.example.loopcongo.models.Vendeur

class VendeurAdapter(private val vendeurs: List<Vendeur>) :
    RecyclerView.Adapter<VendeurAdapter.VendeurViewHolder>() {

    inner class VendeurViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProfile: ImageView = itemView.findViewById(R.id.imgProfileVendeur)
        val nom: TextView = itemView.findViewById(R.id.usernameVendeur)
        val phone: TextView = itemView.findViewById(R.id.phoneItemVendeur)
        val city: TextView = itemView.findViewById(R.id.locationItemVendeur)

        //val boost: TextView = itemView.findViewById(R.id.statutSponsoredOrNot)
        val about: TextView = itemView.findViewById(R.id.aboutVendeur)
        val badgeImage: ImageView = itemView.findViewById(R.id.vendeurBadgeSponsor)
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
        holder.phone.text = vendeur.contact
        holder.city.text = vendeur.city
        holder.about.text = vendeur.about ?: "Aucune description"

        // Affiche boost_type ou texte par défaut
        /*holder.boost.text = when {
            vendeur.is_sponsored == 1 && !vendeur.boost_type.isNullOrBlank() ->
                "• ${vendeur.boost_type.uppercase()}"
            vendeur.is_sponsored == 1 ->
                "• Sponsorisé"
            else ->
                "• Non sponsorisé"
        }*/
        if (vendeur.is_sponsored == 1) {
            holder.badgeImage.visibility = View.VISIBLE
        } else {
            holder.badgeImage.visibility = View.GONE
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

        // 👉 CLIC : Redirection vers Détail
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProfileVendeurActivity::class.java)

            // Passer les données nécessaires (tu peux en passer plus)
            intent.putExtra("vendeurId", vendeur.id)
            intent.putExtra("vendeurUsername", vendeur.nom)
            intent.putExtra("vendeurContact", vendeur.contact)
            intent.putExtra("vendeurCity", vendeur.city)
            intent.putExtra("vendeurDescription", vendeur.about)
            intent.putExtra("vendeurTypeAccount", vendeur.type_account)
            intent.putExtra("vendeurAvatarImg", vendeur.file_url)
            intent.putExtra("isSponsoredVendeur", vendeur.is_sponsored)
            intent.putExtra("vendeurTotalArticles", vendeur.total_articles)
            intent.putExtra("vendeurTotalLikes", vendeur.total_likes)
            intent.putExtra("vendeurNbAbonner", vendeur.nb_abonner)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = vendeurs.size
}
