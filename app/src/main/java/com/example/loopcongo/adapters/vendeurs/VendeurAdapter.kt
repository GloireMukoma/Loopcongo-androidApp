package com.example.loopcongo.adapters.vendeurs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.ProfileVendeurActivity
import com.example.loopcongo.ProfileVendeurImmobilierActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.User

class VendeurAdapter(
    context: Context,
    private val vendeurs: List<User>
) : ArrayAdapter<User>(context, 0, vendeurs) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_vendeur2, parent, false)

        val vendeur = vendeurs[position]

        // Views
        val imgProfile = view.findViewById<ImageView>(R.id.imgProfileVendeur)
        val nom = view.findViewById<TextView>(R.id.usernameVendeur)
        val phone = view.findViewById<TextView>(R.id.phoneItemVendeur)
        val city = view.findViewById<TextView>(R.id.locationItemVendeur)
        val about = view.findViewById<TextView>(R.id.aboutVendeur)
        val badgeImage = view.findViewById<ImageView>(R.id.vendeurBadgeSponsor)

        // Texte
        nom.text = vendeur.username
        phone.text = vendeur.contact
        city.text = vendeur.city
        about.text = vendeur.about ?: "Aucune description"

        // Badge certifié
        if (vendeur.is_certified == "1") {
            badgeImage.visibility = View.VISIBLE
        } else {
            badgeImage.visibility = View.GONE
        }

        // Image de profil
        val imageUrl = if (!vendeur.file_url.isNullOrEmpty()) {
            "https://loopcongo.com/${vendeur.file_url}"
        } else {
            "https://loopcongo.com/default-avatar.jpg"
        }

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.avatar)
            .error(R.drawable.avatar)
            .into(imgProfile)

        // Clic → Profil
        view.setOnClickListener {
            val intent = when (vendeur.type_account?.lowercase()) {
                "vendeur" -> Intent(context, ProfileVendeurActivity::class.java)
                "immobilier" -> Intent(context, ProfileVendeurImmobilierActivity::class.java)
                else -> Intent(context, ProfileVendeurActivity::class.java)
            }

            intent.putExtra("vendeurId", vendeur.id)
            intent.putExtra("vendeurUsername", vendeur.username)
            intent.putExtra("vendeurContact", vendeur.contact)
            intent.putExtra("vendeurCity", vendeur.city)
            intent.putExtra("vendeurDescription", vendeur.about)
            intent.putExtra("vendeurTypeAccount", vendeur.type_account)
            intent.putExtra("vendeurAvatarImg", vendeur.file_url)
            intent.putExtra("isCertifiedVendeur", vendeur.is_certified)
            intent.putExtra("vendeurTotalArticles", vendeur.total_articles)
            intent.putExtra("vendeurTotalLikes", vendeur.total_likes)
            intent.putExtra("vendeurNbAbonner", vendeur.nb_abonner)

            context.startActivity(intent)
        }

        return view
    }
}
