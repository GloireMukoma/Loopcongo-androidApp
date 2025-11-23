package com.example.loopcongo.adapters.superAdminConnected

import android.content.Context
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
        val status = view.findViewById<TextView>(R.id.txtTypeStatus)
        val menu = view.findViewById<ImageView>(R.id.btnMenuPending)

        // Filling data
        name.text = user.username
        phone.text = user.contact
        status.text = user.status

        // Couleur selon le status
        when (user.status.lowercase()) {
            "waiting" -> status.setTextColor(Color.RED)
            "active"  -> status.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_blue_dark))
            else -> status.setTextColor(Color.GRAY) // couleur par défaut
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
