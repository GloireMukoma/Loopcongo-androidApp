package com.example.loopcongo.adapters.prestataire

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.loopcongo.DetailOffrePrestationActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.OffrePrestation

class OffrePrestationAdapter(context: Context, private val offres: List<OffrePrestation>) :
    ArrayAdapter<OffrePrestation>(context, 0, offres) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_offre_prestation, parent, false)

        val offre = offres[position]

        val img = view.findViewById<ImageView>(R.id.imgAnnonceurOffre)
        val nom = view.findViewById<TextView>(R.id.usernameAnnonceurOffre)
        val heure = view.findViewById<TextView>(R.id.createdAtOffre)
        val contenu = view.findViewById<TextView>(R.id.texteContenuOffre)

        img.setImageResource(offre.avatarResId)
        nom.text = offre.nomAnnonceur
        heure.text = "• ${offre.heure}"
        contenu.text = offre.texteContenu

        // 🔗 Ajout de la redirection vers DetailOffreActivity
        view.setOnClickListener {
            val intent = Intent(context, DetailOffrePrestationActivity::class.java).apply {
                putExtra("username", offre.nomAnnonceur)
                putExtra("date", offre.heure)
                putExtra("titre", offre.titre ?: "Offre de prestation")
                putExtra("description", offre.texteContenu)
                putExtra("avatarResId", offre.avatarResId)
            }
            context.startActivity(intent)
        }

        return view
    }
}

