package com.example.loopcongo.adapters.superAdminConnected

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.User
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountCreatedAdapter(
    context: Context,
    private var users: MutableList<User>
) : ArrayAdapter<User>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_account, parent, false)

        val user = users[position]

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfileUser)
        val txtUsername = view.findViewById<TextView>(R.id.username)
        val txtPhone = view.findViewById<TextView>(R.id.phone)
        val menu = view.findViewById<ImageView>(R.id.btnMenu)
        val txtCity = view.findViewById<TextView>(R.id.city)
        val txtStatus = view.findViewById<TextView>(R.id.status)
        val badge = view.findViewById<ImageView>(R.id.vendeurBadgeSponsor)

        // Remplissage
        txtUsername.text = user.username
        txtPhone.text = user.contact
        txtCity.text = user.city
        txtStatus.text = user.status

        // Couleur selon le status
        when (user.status) {
            "Non abonné" -> txtStatus.setTextColor(ContextCompat.getColor(view.context, R.color.red))
            "Abonné"  -> txtStatus.setTextColor(ContextCompat.getColor(view.context, android.R.color.holo_blue_dark))
            else -> txtStatus.setTextColor(Color.GRAY) // couleur par défaut
        }

        // Badge SPONSOR
        //badge.visibility = if (user.is_certified == "1") View.VISIBLE else View.GONE

        // Badge selon le type d'abonnement
        when (user.subscription_type) {
            "Premium" -> {
                badge.visibility = View.VISIBLE
                badge.setColorFilter(
                    ContextCompat.getColor(context, android.R.color.holo_blue_dark),
                    PorterDuff.Mode.SRC_IN
                )
            }
            "Pro" -> {
                badge.visibility = View.VISIBLE
                badge.setColorFilter(
                    ContextCompat.getColor(context, R.color.gray),
                    PorterDuff.Mode.SRC_IN
                )
            }
            else -> {
                badge.visibility = View.GONE
            }
        }


        // Image
        Glide.with(context)
            .load("https://loopcongo.com/${user.file_url}")
            .placeholder(R.drawable.avatar)
            .centerCrop()
            .into(imgProfile)

        // Menu (Valider / Rejeter)
        menu.setOnClickListener {
            val popup = PopupMenu(context, menu)
            popup.menuInflater.inflate(R.menu.menu_users_abonnement_options2, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.optionActiver -> {
                        activateUserSubscription(user.id)
                        true
                    }
                    R.id.optionDesactiver -> {
                        deactivateUserSubscription(user.id)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }


        return view
    }
    fun update(newList: List<User>) {
        users.clear()       // ✅ fonctionne maintenant
        users.addAll(newList)
        notifyDataSetChanged()
    }


    private fun activateUserSubscription(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.activateSubscription(userId)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Abonnement activé", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deactivateUserSubscription(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.deactivateSubscription(userId)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Abonnement désactivé", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
