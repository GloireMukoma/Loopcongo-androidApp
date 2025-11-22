package com.example.loopcongo.adapters.superAdminConnected

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.User

class SubscribedUsersAdapter(
    private val list: List<User>
) : RecyclerView.Adapter<SubscribedUsersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username = view.findViewById<TextView>(R.id.username)
        val contact = view.findViewById<TextView>(R.id.contact)
        val type = view.findViewById<TextView>(R.id.typeSubscription)
        val image = view.findViewById<ImageView>(R.id.userImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subscribed_user, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]

        holder.username.text = user.username
        holder.contact.text = user.contact
        holder.type.text = user.subscription_type

        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/${user.file_url}")
            .placeholder(R.drawable.loading)
            .into(holder.image)
    }

    override fun getItemCount() = list.size
}
