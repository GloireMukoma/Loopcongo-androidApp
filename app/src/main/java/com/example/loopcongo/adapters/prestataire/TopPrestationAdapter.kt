package com.example.loopcongo.adapters.prestataire

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailPrestationActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.PrestationSponsorisee

// Affiche la liste des publications des prestataires sponsorisés (onglet accueil)
class TopPrestationAdapter(
    private val publications: List<PrestationSponsorisee>
) : RecyclerView.Adapter<TopPrestationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomPrestataire: TextView = itemView.findViewById(R.id.usernameTopPrestation)
        val profession: TextView = itemView.findViewById(R.id.professionTopPrestation)
        val imagePublication: ImageView = itemView.findViewById(R.id.imageTopPrestation)
        val textContenu: TextView = itemView.findViewById(R.id.textContenuTopPrestation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prestation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pub = publications[position]
        holder.nomPrestataire.text = pub.prestataire_nom
        holder.profession.text = pub.prestataire_profession
        holder.textContenu.text = pub.description

        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/" + (pub.image ?: "prestataires/prestations/job_default.jpg"))
            .placeholder(R.drawable.loading)
            .into(holder.imagePublication)

        // ✅ Gestion du clic pour ouvrir l'activité de détail
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailPrestationActivity::class.java).apply {
                putExtra("prestataire_nom", pub.prestataire_nom)
                putExtra("prestataire_profession", pub.prestataire_profession)
                putExtra("description", pub.description)
                putExtra("date_publication", pub.date_publication)
                putExtra("photo_profil", pub.photo_profil)
                putExtra("image", pub.image)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = publications.size
}
