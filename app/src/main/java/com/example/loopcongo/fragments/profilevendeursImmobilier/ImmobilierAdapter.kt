package com.example.loopcongo.fragments.profilevendeursImmobilier

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Immobilier


class ImmobilierAdapter(
    private val context: Context,
    private val immobiliers: MutableList<Immobilier>
) : RecyclerView.Adapter<ImmobilierAdapter.ImmoViewHolder>() {

    inner class ImmoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.imageImmobilierUserConnectedOnglet)
        val description = itemView.findViewById<TextView>(R.id.descImmobilierUserConnectedOnglet)
        val prix = itemView.findViewById<TextView>(R.id.prixImmobilierUserConnectedOnglet)
        val quartier = itemView.findViewById<TextView>(R.id.quartierImmobilierUserConnectedOnglet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImmoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_immobilier_user_connected, parent, false)
        return ImmoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImmoViewHolder, position: Int) {
        val immo = immobiliers[position]

        holder.description.text = immo.about
        holder.prix.text = "${immo.prix} $"
        holder.quartier.text = immo.quartier

        Glide.with(context)
            .load("https://loopcongo.com/${immo.file_url}")
            .placeholder(R.drawable.loading)
            .into(holder.image)
    }

    override fun getItemCount(): Int = immobiliers.size
}
