package com.example.loopcongo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.Vendeur

class StatutUserProfileAdapter(private val profiles: List<Vendeur>) :
    RecyclerView.Adapter<StatutUserProfileAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val background = view.findViewById<ImageView>(R.id.statutImageBackground)
        val avatar = view.findViewById<ImageView>(R.id.statutImageProfile)
        val name = view.findViewById<TextView>(R.id.statutUserName)
        val time = view.findViewById<TextView>(R.id.statutTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_profile_statut, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = profiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profiles[position]
        holder.background.setImageResource(profile.imageResId)
        holder.avatar.setImageResource(profile.imageResId)
        holder.name.text = profile.nom
        holder.time.text = profile.description
    }
}
