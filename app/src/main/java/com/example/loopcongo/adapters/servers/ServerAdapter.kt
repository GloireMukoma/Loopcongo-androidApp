package com.example.loopcongo.adapters.servers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.ServerDiscussionActivity
import com.example.loopcongo.models.Server

class ServerAdapter(
    private val servers: List<Server>,
    private val onClick: (Server) -> Unit
) : RecyclerView.Adapter<ServerAdapter.ServerViewHolder>() {

    class ServerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgServer: ImageView = view.findViewById(R.id.imgServer)
        val txtServerName: TextView = view.findViewById(R.id.txtServerName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_servers_user_membre, parent, false)
        return ServerViewHolder(view)
    }

    override fun getItemCount() = servers.size

    override fun onBindViewHolder(holder: ServerViewHolder, position: Int) {
        val server = servers[position]

        holder.txtServerName.text = server.name

        Glide.with(holder.itemView.context)
            .load("https://loopcongo.com/" + server.image)
            .into(holder.imgServer)

        holder.itemView.setOnClickListener {
            onClick(server)
        }
    }
}