package com.example.loopcongo.adapters.vendeurs

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.ProfileVendeurActivity
import com.example.loopcongo.ProfileVendeurImmobilierActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.User

class VendeurAdapter(private val vendeurs: List<User>) :
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
        holder.nom.text = vendeur.username
        holder.phone.text = vendeur.contact
        holder.city.text = vendeur.city
        holder.about.text = vendeur.about ?: "Aucune description"

        if (vendeur.is_certified == "1") {
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

            // Vérifie le type de compte du vendeur
            val intent = when (vendeur.type_account?.lowercase()) {
                "vendeur" -> Intent(context, ProfileVendeurActivity::class.java)
                "immobilier" -> Intent(context, ProfileVendeurImmobilierActivity::class.java)
                else -> {
                    // Par défaut, redirige vers ProfileVendeurActivity
                    Intent(context, ProfileVendeurActivity::class.java)
                }
            }

            // Passer les données nécessaires
            intent.putExtra("vendeurId", vendeur.id)
            intent.putExtra("vendeurUsername", vendeur.username)
            intent.putExtra("vendeurContact", vendeur.contact)
            intent.putExtra("vendeurCity", vendeur.city)
            intent.putExtra("vendeurDescription", vendeur.about)
            intent.putExtra("vendeurTypeAccount", vendeur.type_account)
            intent.putExtra("vendeurAvatarImg", vendeur.file_url)
            intent.putExtra("isCertifiedVendeur", vendeur.is_certified)
            intent.putExtra("vendeurTotalArticles", vendeur.total_articles)
            intent.putExtra("vendeurTotalLikes", vendeur.total_likes)
            intent.putExtra("vendeurNbAbonner", vendeur.nb_abonner)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = vendeurs.size
}
