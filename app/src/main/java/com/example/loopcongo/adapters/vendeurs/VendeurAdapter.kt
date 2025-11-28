package com.example.loopcongo.adapters.vendeurs

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.ProfileVendeurActivity
import com.example.loopcongo.ProfileVendeurImmobilierActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.User

class VendeurAdapter(
    private val context: Context,
    private val vendeurs: List<User>,
    private val onItemClick: ((User) -> Unit)? = null // Callback clic
) : RecyclerView.Adapter<VendeurAdapter.VendeurViewHolder>() {

    inner class VendeurViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfile: ImageView = view.findViewById(R.id.imgProfileVendeur)
        val nom: TextView = view.findViewById(R.id.usernameVendeur)
        val phone: TextView = view.findViewById(R.id.phoneItemVendeur)
        val city: TextView = view.findViewById(R.id.locationItemVendeur)
        val about: TextView = view.findViewById(R.id.aboutVendeur)
        val badgeImage: ImageView = view.findViewById(R.id.vendeurBadgeSponsor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendeurViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_vendeur, parent, false)
        return VendeurViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendeurViewHolder, position: Int) {
        val vendeur = vendeurs[position]

        holder.nom.text = vendeur.username
        holder.phone.text = vendeur.contact
        holder.city.text = vendeur.city
        holder.about.text = vendeur.about ?: "Aucune description"

        // Badge selon le type d'abonnement
        when (vendeur.subscription_type) {
            "Premium" -> {
                holder.badgeImage.visibility = View.VISIBLE
                holder.badgeImage.setColorFilter(
                    ContextCompat.getColor(context, android.R.color.holo_blue_dark),
                    PorterDuff.Mode.SRC_IN
                )
            }
            "Pro" -> {
                holder.badgeImage.visibility = View.VISIBLE
                holder.badgeImage.setColorFilter(
                    ContextCompat.getColor(context, R.color.gray),
                    PorterDuff.Mode.SRC_IN
                )
            }
            else -> {
                holder.badgeImage.visibility = View.GONE
            }
        }

        // Image de profil
        val imageUrl = if (!vendeur.file_url.isNullOrEmpty()) {
            "https://loopcongo.com/${vendeur.file_url}"
        } else {
            "https://loopcongo.com/default-avatar.jpg"
        }

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(holder.imgProfile)

        // Clic → callback
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(vendeur)
        }
    }

    override fun getItemCount(): Int = vendeurs.size
}
