package com.example.loopcongo.adapters.prestataire

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.FilActualiteItem

class FilActualiteAdapter(private val articles: List<FilActualiteItem>) :
    RecyclerView.Adapter<FilActualiteAdapter.ActualiteViewHolder>() {

    inner class ActualiteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.filaActuImageArticle)
        val titre: TextView = view.findViewById(R.id.titreArticle)
        val vues: TextView = view.findViewById(R.id.views)
        val likes: TextView = view.findViewById(R.id.likes)
        val comments: TextView = view.findViewById(R.id.comments)
        val date: TextView = view.findViewById(R.id.filActuDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActualiteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fil_actualite_prestataire, parent, false)
        return ActualiteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActualiteViewHolder, position: Int) {
        val item = articles[position]
        holder.image.setImageResource(item.imageRes)
        holder.titre.text = item.titre
        holder.vues.text = item.vues.toString()
        holder.likes.text = item.likes.toString()
        holder.comments.text = item.commentaires.toString()
        holder.date.text = item.date
    }

    override fun getItemCount(): Int = articles.size
}
