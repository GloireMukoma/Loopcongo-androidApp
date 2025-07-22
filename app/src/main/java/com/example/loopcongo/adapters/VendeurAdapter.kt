package com.example.loopcongo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.Vendeur

class VendeurAdapter(private val vendeurs: List<Vendeur>) :
    RecyclerView.Adapter<VendeurAdapter.VendeurViewHolder>() {

    inner class VendeurViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageVendeur: ImageView = itemView.findViewById(R.id.imageVendeur)
        val nomVendeur: TextView = itemView.findViewById(R.id.nomVendeur)
        val descriptionVendeur: TextView = itemView.findViewById(R.id.descriptionVendeur)
        val nbArticles: TextView = itemView.findViewById(R.id.nbArticles)
        val typeLabel: TextView = itemView.findViewById(R.id.typeLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendeurViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vendeur, parent, false)
        return VendeurViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendeurViewHolder, position: Int) {
        val vendeur = vendeurs[position]
        holder.imageVendeur.setImageResource(vendeur.imageResId)
        holder.nomVendeur.text = vendeur.nom
        holder.descriptionVendeur.text = vendeur.description
        holder.nbArticles.text = "${vendeur.nbArticlePublie} articles"
        holder.typeLabel.text = if (vendeur.type == "immobilier") "Immobilier" else "Vendeur"
    }

    override fun getItemCount(): Int = vendeurs.size
}
