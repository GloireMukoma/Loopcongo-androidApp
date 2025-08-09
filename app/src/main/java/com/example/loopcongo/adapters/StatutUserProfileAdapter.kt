package com.example.loopcongo.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.User
import com.example.loopcongo.models.UserProfile
import com.example.loopcongo.models.Vendeur
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.*

//StatutUserProfileAdapter

class StatutUserProfileAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<StatutUserProfileAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val articleImage: ImageView = itemView.findViewById(R.id.topVendeursImageArticle)
        val avatarImage: ShapeableImageView = itemView.findViewById(R.id.topVendeurAvatarImage)
        val userName: TextView = itemView.findViewById(R.id.topVendeurUserName)
        val totalArticle: TextView = itemView.findViewById(R.id.topVendeurNbArticle)
        //val badgeText: TextView = itemView.findViewById(R.id.profileBadgeText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_profile_statut2, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.userName.text = user.username
        holder.totalArticle.text = "${user.total_articles} articles"


        //CHARGEMENT DES IMAGES (profil et derniere article publié)
        val baseUrl = "https://loopcongo.com/"

        val profileUrl = user.profile_image?.let {
            if (it.startsWith("http")) it else baseUrl + it
        }
        val articleUrl = user.article_image?.let {
            if (it.startsWith("http")) it else baseUrl + it
        }
        // Charger l'image de profil (avatar)
        Glide.with(holder.itemView.context)
            .load(profileUrl)
            .placeholder(R.drawable.avatar)
            .circleCrop()
            .into(holder.avatarImage)

        // Charger l'image de fond (dernière image article ou image utilisateur par défaut)
        Glide.with(holder.itemView.context)
            .load(articleUrl ?: profileUrl)
            .placeholder(R.drawable.avatar)
            .centerCrop()
            .into(holder.articleImage)

        // Affiche "VIP" uniquement si is_sponsored == 1
        /*if (user.is_sponsored == 1) {
            holder.badgeText.visibility = View.VISIBLE
            holder.badgeText.text = user.boost_type?.uppercase() ?: "VIP"
        } else {
            holder.badgeText.visibility = View.GONE
        }*/


    }

    // Convertir la date en "Il y a..."
    private fun getTimeAgo(dateStr: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val past = sdf.parse(dateStr) ?: return "Inconnu"
            val now = Date()
            val seconds = (now.time - past.time) / 1000

            when {
                seconds < 60 -> "À l'instant"
                seconds < 3600 -> "Il y a ${seconds / 60} min"
                seconds < 86400 -> "Il y a ${seconds / 3600} h"
                else -> "Il y a ${seconds / 86400} j"
            }
        } catch (e: Exception) {
            "Date invalide"
        }
    }
}
