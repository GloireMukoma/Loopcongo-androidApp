package com.example.loopcongo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.Prestation

class PrestationAdapter(private val prestations: List<Prestation>) :
    RecyclerView.Adapter<PrestationAdapter.PrestationViewHolder>() {

    class PrestationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagePrestation: ImageView = itemView.findViewById(R.id.imagePrestation)
        //val titre: TextView = itemView.findViewById(R.id.titrePrestation)
        val description: TextView = itemView.findViewById(R.id.descriptionPrestation)
        //val prix: TextView = itemView.findViewById(R.id.prixPrestation)
        val nomPrestataire: TextView = itemView.findViewById(R.id.nomPrestataire)
        val domaine: TextView = itemView.findViewById(R.id.domainePrestataire)
        val localisation: TextView = itemView.findViewById(R.id.localisationPrestation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrestationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prestation, parent, false)
        return PrestationViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrestationViewHolder, position: Int) {
        val prestation = prestations[position]
        holder.imagePrestation.setImageResource(prestation.imageResId)
        //holder.titre.text = prestation.titre
        holder.description.text = prestation.description
        //holder.prix.text = "${prestation.prix} FC"
        holder.nomPrestataire.text = prestation.nomPrestataire
        holder.domaine.text = prestation.domaine
        holder.localisation.text = prestation.localisation
    }

    override fun getItemCount(): Int = prestations.size
}
