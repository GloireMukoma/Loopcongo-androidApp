package com.example.loopcongo.adapters.immobiliers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailImmobilierActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Immobilier

class ImmobilierAdapter(
    context: Context,
    private val items: MutableList<Immobilier>
) : ArrayAdapter<Immobilier>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_immobilier, parent, false)

        val item = items[position]

        val img = view.findViewById<ImageView>(R.id.imgImmobilierItem)
        val adresse = view.findViewById<TextView>(R.id.titreImmobilierItem)
        val desc = view.findViewById<TextView>(R.id.descriptionImmobilierItem)
        val prix = view.findViewById<TextView>(R.id.prixImmobilierItem)
        val status = view.findViewById<TextView>(R.id.statusImmobilierItem)
        val auteur = view.findViewById<TextView>(R.id.usernameItemImmobilier)

        adresse.text = item.address
        desc.text = item.about
        prix.text = "${item.prix} $"
        status.text = "${item.typeimmo} • ${item.statut} "
        auteur.text = item.username

        Glide.with(context)
            .load("https://loopcongo.com/${item.file_url}")
            .placeholder(R.drawable.loading)
            .into(img)

        // ✅ Click sur un item
        view.setOnClickListener {
            val intent = Intent(context, DetailImmobilierActivity::class.java)
            //intent.putExtra("immobilier_id", item.id)
            intent.putExtra("immoId", item.id)
            intent.putExtra("userId", item.account_id)
            intent.putExtra("typeImmo", item.typeimmo)
            intent.putExtra("statut", item.statut)
            intent.putExtra("city", item.city)
            intent.putExtra("quartier", item.quartier)
            intent.putExtra("prix", item.prix)
            intent.putExtra("address", item.address)
            intent.putExtra("description", item.about)
            intent.putExtra("ImmoImage", item.file_url)
            intent.putExtra("vendeurSubscriptionType", item.subscription_type)

            intent.putExtra("username", item.username)
            intent.putExtra("userImage", item.user_avatar)
            intent.putExtra("userContact", item.contact)
            context.startActivity(intent)
        }

        return view
    }

    // ✅ Mise à jour de la liste
    fun updateList(newList: List<Immobilier>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
