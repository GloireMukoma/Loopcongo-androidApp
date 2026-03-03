package com.example.loopcongo.adapters.servers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.ServerMessage
import java.text.SimpleDateFormat
import java.util.*

class ServerMessagesAdapter(
    private val messages: List<ServerMessage>
) : RecyclerView.Adapter<ServerMessagesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProfil: ImageView = view.findViewById(R.id.profilUserImgDiscussionItemServer)
        val txtUsername: TextView = view.findViewById(R.id.usernameDiscussionItemServer)
        val txtDate: TextView = view.findViewById(R.id.createdAtDiscussionItemServer)
        val txtMessage: TextView = view.findViewById(R.id.txtMessageDiscussionServer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_server_msg, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = messages[position]

        holder.txtUsername.text = msg.username
        holder.txtMessage.text = msg.message
        holder.txtDate.text = formatDate(msg.created_at)

        Glide.with(holder.itemView.context)
            .load(msg.file_url)
            .placeholder(R.drawable.avatar)
            .circleCrop()
            .into(holder.imgProfil)
    }

    override fun getItemCount(): Int = messages.size

    private fun formatDate(dateString: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = sdf.parse(dateString)
            val now = Date()
            val diff = (now.time - date.time) / 60000

            when {
                diff < 1 -> "à l’instant"
                diff < 60 -> "• ${diff} min"
                diff < 1440 -> "• ${diff / 60} h"
                else -> "• ${diff / 1440} j"
            }
        } catch (e: Exception) {
            ""
        }
    }
}
