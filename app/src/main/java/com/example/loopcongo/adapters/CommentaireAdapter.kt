package com.example.loopcongo.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.ProfileCustomerActivity
import com.example.loopcongo.ProfileVendeurActivity
import com.example.loopcongo.ProfileVendeurImmobilierActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Commentaire

class CommentaireAdapter(
    private val list: MutableList<Commentaire>
) : RecyclerView.Adapter<CommentaireAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val user: TextView = view.findViewById(R.id.txtUser)
        val avatar: ImageView = view.findViewById(R.id.avatarUserItemCommentaire)
        val message: TextView = view.findViewById(R.id.txtMessage)
        val date: TextView = view.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_commentaire, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.user.text = item.username ?: "Utilisateur"
        holder.message.text = item.commentaire
        holder.date.text = item.time_ago ?: ""

        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/${item.user_avatar}")
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .into(holder.avatar)

        // 👉 CLIC SUR L’AVATAR
        holder.avatar.setOnClickListener {

            if (holder.adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            val context = holder.itemView.context
            val typeAccount = item.user_type?.lowercase() ?: return@setOnClickListener

            val intent = when (typeAccount) {

                "vendeur" -> Intent(context, ProfileVendeurActivity::class.java).apply {
                    putExtra("vendeurId", item.user_id)
                    putExtra("vendeurUsername", item.username)
                    putExtra("vendeurAvatarImg", item.user_avatar)
                    putExtra("vendeurContact", item.contact)
                    putExtra("vendeurCity", item.city)
                    putExtra("vendeurDescription", item.about)
                }

                "immobilier" -> Intent(context, ProfileVendeurImmobilierActivity::class.java).apply {
                    putExtra("vendeurId", item.user_id)
                    putExtra("vendeurUsername", item.username)
                    putExtra("vendeurAvatarImg", item.user_avatar)
                    putExtra("vendeurContact", item.contact)
                    putExtra("vendeurCity", item.city)
                    putExtra("vendeurDescription", item.about)
                }

                "customer" -> Intent(context, ProfileCustomerActivity::class.java).apply {
                    putExtra("userId", item.user_id)
                    putExtra("username", item.username)
                    putExtra("avatar", item.user_avatar)
                }

                else -> return@setOnClickListener
            }

            context.startActivity(intent)
        }
    }
}
