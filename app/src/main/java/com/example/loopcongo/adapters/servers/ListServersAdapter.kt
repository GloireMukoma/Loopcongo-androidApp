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
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.ServerDiscussionActivity
import com.example.loopcongo.models.Server

class ListServersAdapter(
    context: Context,
    private val servers: List<Server>
) : ArrayAdapter<Server>(context, 0, servers) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_server_all, parent, false)

        val server = servers[position]

        val txtName = view.findViewById<TextView>(R.id.txtServerName)
        val txtNbMembres = view.findViewById<TextView>(R.id.txtNbMembres)
        val imgServer = view.findViewById<ImageView>(R.id.imgServer)

        txtName.text = server.name
        //txtSubs.text = "${server.subscribers_count} abonnés"
        txtNbMembres.text = server.membres_count.toString()

        Glide.with(context)
            .load(server.image)
            .placeholder(R.drawable.loading)
            .circleCrop()
            .into(imgServer)

        // 🔥 LOGIQUE DE CLIC DANS L'ADAPTER
        view.setOnClickListener {
            Toast.makeText(context, "Serveur : ${server.name}", Toast.LENGTH_SHORT).show()

            // TODO: navigation vers détails serveur
            val intent = Intent(context, ServerDiscussionActivity::class.java)
            intent.putExtra("server_id", server.id)
            intent.putExtra("serverName", server.name)
            context.startActivity(intent)
        }

        return view
    }
}
