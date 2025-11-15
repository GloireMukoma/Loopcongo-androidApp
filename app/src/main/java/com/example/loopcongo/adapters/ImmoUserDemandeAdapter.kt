package com.example.loopcongo.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.ImmoUserDemande

class ImmoUserDemandeAdapter(
    private val demandes: List<ImmoUserDemande>,
    @LayoutRes private val itemLayout: Int // 🔹 Layout à utiliser
    ) :
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
            .inflate(itemLayout, parent, false) // 🔹 Utilise le layout passé
        return DemandeViewHolder(view)
    }

    override fun onBindViewHolder(holder: DemandeViewHolder, position: Int) {
        val demande = demandes[position]

        holder.username.text = demande.username
        holder.message.text = demande.message
        holder.time.text = "• ${demande.created_at}"

        // Charger avatar avec Glide
        Glide.with(holder.itemView.context)
            .load(demande.avatar)
            .placeholder(R.drawable.user1)
            .circleCrop()
            .into(holder.avatar)

        // --- ACTION QUAND ON CLIQUE SUR L'ITEM ---
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val contactCustomer = demande.contact ?: ""

            if (contactCustomer.isEmpty()) {
                Toast.makeText(context, "Numéro WhatsApp introuvable", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val message = "Bonjour, concernant votre demande immobilière sur LoopCongo, nous avons trouvé une solution pour vous.\n\n" +
                    "Votre demande : ${demande.message}"

            val url = "https://wa.me/$contactCustomer?text=${Uri.encode(message)}"

            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "WhatsApp n’est pas installé", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun getItemCount() = demandes.size
}
