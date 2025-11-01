package com.example.loopcongo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.ImmoUserDemande

class ImmoUserDemandeAdapter(private val demandes: List<ImmoUserDemande>) :
    RecyclerView.Adapter<ImmoUserDemandeAdapter.DemandeViewHolder>() {

    inner class DemandeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.avatarImgUserDemande)
        val username: TextView = view.findViewById(R.id.usernameDemande)
        val message: TextView = view.findViewById(R.id.msgUserDemande)
        val time: TextView = view.findViewById(R.id.timeDemande)
        //val btnRepondre: LinearLayout = view.findViewById(R.id.repondreDemandeImmoBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemandeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_demande, parent, false)
        return DemandeViewHolder(view)
    }

    override fun onBindViewHolder(holder: DemandeViewHolder, position: Int) {
        val demande = demandes[position]

        holder.username.text = demande.username
        holder.message.text = demande.message
        holder.time.text = demande.created_at

        // Chargement de l'image (avec Glide)
        Glide.with(holder.itemView.context)
            .load(demande.avatar)
            .placeholder(R.drawable.user1)
            .circleCrop()
            .into(holder.avatar)

        /*holder.btnRepondre.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Répondre à ${demande.username}", Toast.LENGTH_SHORT).show()
        }*/
    }

    override fun getItemCount() = demandes.size
}
