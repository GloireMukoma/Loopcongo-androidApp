package com.example.loopcongo.adapters.prestataire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.loopcongo.R
import com.example.loopcongo.models.FilActualitePrestataire

class FilActualitePrestataireAdapter(
    context: Context,
    private val articles: List<FilActualitePrestataire>
) : ArrayAdapter<FilActualitePrestataire>(context, 0, articles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_fil_actualite, parent, false)

        val avatar: ImageView = view.findViewById(R.id.imgUserFilActu)
        val username: TextView = view.findViewById(R.id.usernameFilActu)
        val createdAt: TextView = view.findViewById(R.id.createdAtFilActu)
        val texteContenu: TextView = view.findViewById(R.id.texteContenuFilActuPrestataire)
        val imageArticle: ImageView = view.findViewById(R.id.imageFilActuPrestataire)

        val item = articles[position]

        avatar.setImageResource(item.avatarResId)
        username.text = item.username
        createdAt.text = item.createdAt
        texteContenu.text = item.texteContenu
        imageArticle.setImageResource(item.imageArticleResId)

        return view
    }
}
