package com.example.loopcongo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.loopcongo.R
import com.example.loopcongo.models.Citizen

class CitizenAdapter(
    private val ctx: Context,  // Renommé ici
    private var citizenList: List<Citizen>
) : ArrayAdapter<Citizen>(ctx, 0, citizenList) {

    override fun getCount(): Int {
        return citizenList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (citizenList.isEmpty()) {
            // Pour éviter le crash si aucun élément
            return LayoutInflater.from(ctx).inflate(android.R.layout.simple_list_item_1, parent, false).apply {
                findViewById<TextView>(android.R.id.text1).text = "Aucun résultat trouvé"
            }
        }

        val view = convertView ?: LayoutInflater.from(ctx).inflate(R.layout.item_citizen, parent, false)

        val citizen = citizenList[position]

        val imageView = view.findViewById<ImageView>(R.id.imageCitizen)
        val nomText = view.findViewById<TextView>(R.id.textNom)
        val domaineText = view.findViewById<TextView>(R.id.textDomaine)
        val descriptionText = view.findViewById<TextView>(R.id.textDescription)

        imageView.setImageResource(citizen.imageResId)
        nomText.text = citizen.nom
        domaineText.text = citizen.domaine
        descriptionText.text = citizen.description

        return view
    }

    fun filterList(filteredList: List<Citizen>) {
        citizenList = filteredList
        notifyDataSetChanged()
    }
}
