package com.example.loopcongo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.Vendeur

class StatutUserProfileAdapter(
    private val profiles: List<Vendeur>,
    private val onItemClick: (Vendeur) -> Unit
) : RecyclerView.Adapter<StatutUserProfileAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val background = view.findViewById<ImageView>(R.id.statutImageBackground)
        val avatar = view.findViewById<ImageView>(R.id.statutImageProfile)
        val name = view.findViewById<TextView>(R.id.statutUserName)
        val time = view.findViewById<TextView>(R.id.statutTime)

        fun bind(profile: Vendeur) {
            background.setImageResource(profile.imageResId)
            avatar.setImageResource(profile.imageResId)
            name.text = profile.nom
            time.text = profile.description

            itemView.setOnClickListener {
                onItemClick(profile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_profile_statut, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = profiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(profiles[position])
    }
}
