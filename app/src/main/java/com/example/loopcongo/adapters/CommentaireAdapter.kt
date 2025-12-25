package com.example.loopcongo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Commentaire

class CommentaireAdapter(
    private val list: MutableList<Commentaire>
) : RecyclerView.Adapter<CommentaireAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val user = view.findViewById<TextView>(R.id.txtUser)
        val avatar = view.findViewById<ImageView>(R.id.avatarUserItemCommentaire)

        val message = view.findViewById<TextView>(R.id.txtMessage)
        val date = view.findViewById<TextView>(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_commentaire, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.user.text = item.username
        holder.message.text = item.commentaire
        holder.date.text = item.time_ago

        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/${item.user_avatar}")
            .placeholder(R.drawable.loading)
            .into(holder.avatar)
    }

}
