package com.example.loopcongo.adapters.vendeurs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailAnnonceActivity
import com.example.loopcongo.DetailArticleActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Article
import com.example.loopcongo.models.UserAnnonce

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OngletAnnonceForProfileVendeurAdapter(
    private val context: Context,
    private var annonces: List<UserAnnonce>
) : RecyclerView.Adapter<OngletAnnonceForProfileVendeurAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgUserAnnonce)
        val titre: TextView = view.findViewById(R.id.titreUserAnnonce)
        val description: TextView = view.findViewById(R.id.userAnnonceDescription)
        val date: TextView = view.findViewById(R.id.userAnnonceCreated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_user_annonce, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val annonce = annonces[position]

        holder.titre.text = annonce.titre
        holder.description.text = annonce.description
        holder.date.text = annonce.created_at

        // ✅ Glide pour charger l’image
        Glide.with(context)
            .load("https://loopcongo.com/${annonce.image}") // ⚠️ nom du champ à adapter selon ton modèle JSON
            .centerCrop()
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailAnnonceActivity::class.java).apply {
                putExtra("id", annonce.id)
                putExtra("user_id", annonce.user_id)
                putExtra("titre", annonce.titre)
                putExtra("description", annonce.description)
                putExtra("image", annonce.image)
                putExtra("username", annonce.username)
                putExtra("city", annonce.city)
                putExtra("contact", annonce.contact)
                putExtra("file_url", annonce.file_url)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = annonces.size

    fun updateData(newData: List<UserAnnonce>) {
        annonces = newData
        notifyDataSetChanged()
    }

}

