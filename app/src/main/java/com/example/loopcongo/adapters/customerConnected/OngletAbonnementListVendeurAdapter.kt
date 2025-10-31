package com.example.loopcongo.adapters.customerConnected

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.User

class OngletAbonnementListVendeurAdapter(
    private var vendeurs: List<User>,
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<OngletAbonnementListVendeurAdapter.VendeurViewHolder>() {

    inner class VendeurViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAvatar: ImageView = itemView.findViewById(R.id.itemImgAvatarVendeurAbonnementCustomer)
        val txtNom: TextView = itemView.findViewById(R.id.itemUsernameVendeurAbonnementCustomer)
        val txtType: TextView = itemView.findViewById(R.id.itemTypeVendeurAbonnementCustomer)
        val txtAbout: TextView = itemView.findViewById(R.id.itemAboutVendeurAbonnementCustomer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendeurViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vendeur_onglet_abonnement_customer_connected, parent, false)
        return VendeurViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendeurViewHolder, position: Int) {
        val vendeur = vendeurs[position]

        // Chargement de l'image du vendeur (ou image par défaut)
        Glide.with(holder.itemView.context)
            .load(vendeur.file_url ?: "")
            .placeholder(R.drawable.avatar)
            .into(holder.imgAvatar)

        // Affichage des informations
        holder.txtNom.text = vendeur.username
        holder.txtType.text = vendeur.type_account
        holder.txtAbout.text = vendeur.about ?: "Aucune description"

        // Action au clic
        holder.itemView.setOnClickListener {
            onItemClick(vendeur)
        }
    }

    override fun getItemCount(): Int = vendeurs.size

    fun updateData(newList: List<User>) {
        vendeurs = newList
        notifyDataSetChanged()
    }
}
