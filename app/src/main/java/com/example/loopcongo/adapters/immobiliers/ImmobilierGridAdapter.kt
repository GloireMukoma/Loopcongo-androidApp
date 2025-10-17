package com.example.loopcongo.adapters.immobiliers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailImmobilierActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Immobilier

class ImmobilierGridAdapter(private val immobiliers: List<Immobilier>) :
    RecyclerView.Adapter<ImmobilierGridAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgImmo: ImageView = view.findViewById(R.id.imgImmobilierItem)
        val titreImmo: TextView = view.findViewById(R.id.titreImmobilierItem)
        val prix: TextView = view.findViewById(R.id.prixImmobilierItem)
        val auteur: TextView = view.findViewById(R.id.auteurImmobilierItem)
        //val date: TextView = view.findViewById(R.id.datqae_article)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_immobilier_grid, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val immobilier = immobiliers[position]

        // Charger image depuis URL
        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/${immobilier.file_url}")
            .placeholder(R.drawable.loading)
            .into(holder.imgImmo)

        // Remplir les champs
        holder.titreImmo.text = immobilier.about
        holder.prix.text = "${immobilier.prix} $"
        holder.auteur.text = immobilier.quartier  // Auteur ou quartier

        // ✅ Utiliser le context de la vue pour créer l'Intent
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailImmobilierActivity::class.java)
            intent.putExtra("immoId", immobilier.id)
            intent.putExtra("userId", immobilier.account_id)
            intent.putExtra("typeImmo", immobilier.typeimmo)
            intent.putExtra("statut", immobilier.statut)
            intent.putExtra("city", immobilier.city)
            intent.putExtra("quartier", immobilier.quartier)
            intent.putExtra("prix", immobilier.prix)
            intent.putExtra("address", immobilier.address)
            intent.putExtra("description", immobilier.about)
            intent.putExtra("ImmoImage", immobilier.file_url)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = immobiliers.size
}
