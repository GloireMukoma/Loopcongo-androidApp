package com.example.loopcongo.adapters.prestataire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Prestation

class PrestationAdapter(
    private val context: Context,
    private var prestations: List<Prestation>
) : RecyclerView.Adapter<PrestationAdapter.PrestationViewHolder>() {

    inner class PrestationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titre: TextView = itemView.findViewById(R.id.titrePrestationGrid)
        val description: TextView = itemView.findViewById(R.id.descriptionPrestationGrid)
        val image: ImageView = itemView.findViewById(R.id.imagePrestationGrid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_prestation_grid, parent, false)
        return PrestationViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrestationViewHolder, position: Int) {
        val prestation = prestations[position]
        holder.titre.text = prestation.titre
        holder.description.text = prestation.description
        Glide.with(context)
            .load("https://loopcongo.com/${prestation.image}")
            .placeholder(R.drawable.avatar)
            .into(holder.image)
    }

    override fun getItemCount(): Int = prestations.size

    fun updateData(newPrestations: List<Prestation>) {
        prestations = newPrestations
        notifyDataSetChanged()
    }
}
