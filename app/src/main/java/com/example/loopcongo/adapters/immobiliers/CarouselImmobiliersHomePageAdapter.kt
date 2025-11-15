package com.example.loopcongo.adapters.immobiliers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailImmobilierActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Immobilier
import com.example.loopcongo.models.UserAnnonce

class CarouselImmobiliersHomePageAdapter(
    private val context: Context,
    private val immobilier: List<Immobilier>
) : RecyclerView.Adapter<CarouselImmobiliersHomePageAdapter.AnnonceViewHolder>() {

    inner class AnnonceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageImmo: ImageView = view.findViewById(R.id.carouselImageImmo)
        val statusImmo: TextView = view.findViewById(R.id.carousselStatutImmo)
        val city: TextView = view.findViewById(R.id.carousselCityImmo)

        val descriptionImmo: TextView = view.findViewById(R.id.carousselDescriptionImmo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnonceViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_carousel_immobilier_home_page, parent, false)
        return AnnonceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnnonceViewHolder, position: Int) {
        val immo = immobilier[position]

        holder.statusImmo.text = "${immo.typeimmo?.replaceFirstChar { it.uppercaseChar() }} • ${immo.statut}"
        holder.city.text = immo.city
        holder.descriptionImmo.text = immo.about

        Glide.with(context)
            .load("https://loopcongo.com/${immo.file_url}")
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .centerCrop()
            .into(holder.imageImmo)

        // Gestion du clic
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailImmobilierActivity::class.java).apply {
                putExtra("immoId", immo.id)
                putExtra("userId", immo.account_id)
                putExtra("typeImmo", immo.typeimmo)
                putExtra("statut", immo.statut)
                putExtra("city", immo.city)
                putExtra("quartier", immo.quartier)
                putExtra("prix", immo.prix)
                putExtra("address", immo.address)
                putExtra("description", immo.about)
                putExtra("ImmoImage", immo.file_url)
                putExtra("username", immo.username)
                putExtra("userImage", immo.userImage)
                putExtra("userContact", immo.contact)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = immobilier.size
}
