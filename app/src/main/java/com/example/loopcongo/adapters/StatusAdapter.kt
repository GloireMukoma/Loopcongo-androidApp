package com.example.loopcongo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R

/*class StatusAdapter(private val items: List<StatusItem>) :
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    inner class StatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageBackground: ImageView = view.findViewById(R.id.imageBackground)
        val imageProfile: ImageView = view.findViewById(R.id.imageProfile)
        val textUserName: TextView = view.findViewById(R.id.textUserName)
        val textTime: TextView = view.findViewById(R.id.textTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.imageBackground)
        Glide.with(holder.itemView.context).load(item.profileUrl).into(holder.imageProfile)
        holder.textUserName.text = item.userName
        holder.textTime.text = "Il y a ${item.timeAgo}"
    }

    override fun getItemCount(): Int = items.size
}*/
