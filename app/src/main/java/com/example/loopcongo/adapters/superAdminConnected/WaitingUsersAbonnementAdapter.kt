package com.example.loopcongo.adapters.superAdminConnected

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.ProfileVendeurActivity
import com.example.loopcongo.ProfileVendeurImmobilierActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.User

class WaitingUsersAbonnementAdapter(
    context: Context,
    private val items: List<User>,
    private val listener: OnPendingActionListener
) : ArrayAdapter<User>(context, 0, items) {

    interface OnPendingActionListener {
        fun onValidate(userId: Int)
        fun onReject(userId: Int)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_waiting_abonnement, parent, false)

        val user = items[position]

        // Views
        val img = view.findViewById<ImageView>(R.id.imgUserPending)
        val name = view.findViewById<TextView>(R.id.txtNamePending)
        val phone = view.findViewById<TextView>(R.id.txtPhonePending)
        val heureEnvoie = view.findViewById<TextView>(R.id.txtHeureEnvoieArgent)
        val menu = view.findViewById<ImageView>(R.id.btnMenuPending)
        val dateDemande = view.findViewById<TextView>(R.id.dateDemande)
        val textStatus = view.findViewById<TextView>(R.id.textStatus)

        // Filling data
        name.text = user.username
        phone.text = user.numero // Numero utilisé de compte utilisé pour envoyer l'argent
        heureEnvoie.text = " • ${user.heure_paiement}"
        dateDemande.text = user.date_demande
        textStatus.text = user.status

        // Couleur selon le status
        when (user.status.lowercase()) {
            "waiting" -> textStatus.setTextColor(Color.RED)
            "active"  -> textStatus.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_blue_dark))
            else -> textStatus.setTextColor(Color.GRAY) // couleur par défaut
        }

        //CLIC : Redirection vers Détail
        view.setOnClickListener {
            val ctx = view.context

            val intent = when (user.type_account?.lowercase()) {
                "vendeur" -> Intent(ctx, ProfileVendeurActivity::class.java)
                "immobilier" -> Intent(ctx, ProfileVendeurImmobilierActivity::class.java)
                else -> Intent(ctx, ProfileVendeurActivity::class.java)
            }

            intent.putExtra("vendeurId", user.id)
            intent.putExtra("vendeurUsername", user.username)
            intent.putExtra("vendeurContact", user.contact)
            intent.putExtra("vendeurCity", user.city)
            intent.putExtra("vendeurDescription", user.about)
            intent.putExtra("vendeurTypeAccount", user.type_account)
            intent.putExtra("vendeurAvatarImg", user.file_url)
            intent.putExtra("isCertifiedVendeur", user.is_certified)
            intent.putExtra("vendeurTotalArticles", user.total_articles)
            intent.putExtra("vendeurTotalLikes", user.total_likes)
            intent.putExtra("vendeurNbAbonner", user.nb_abonner)

            ctx.startActivity(intent)
        }

        Glide.with(context)
            .load("https://loopcongo.com/${user.file_url}")
            .placeholder(R.drawable.avatar)
            .into(img)

        // Menu (Valider / Rejeter)
        menu.setOnClickListener {
            val popup = PopupMenu(context, menu)
            popup.menuInflater.inflate(R.menu.menu_users_abonnement_options, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.optionValidate -> {
                        listener.onValidate(user.id)
                        true
                    }
                    R.id.optionReject -> {
                        listener.onReject(user.id)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        return view
    }
}
