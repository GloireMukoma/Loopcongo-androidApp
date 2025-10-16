package com.example.loopcongo.adapters.commande

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailCommandeActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Commande

class CommandesAdapter(context: Context, commandes: List<Commande>) :
    ArrayAdapter<Commande>(context, 0, commandes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_commande, parent, false)

        val commande = getItem(position)

        val articleNom = view.findViewById<TextView>(R.id.nomArticleItemCommande)
        //val articlePrix = view.findViewById<TextView>(R.id.article_prix)
        val acheteurNom = view.findViewById<TextView>(R.id.usernameAcheteurItemCommande)
        //val acheteurTel = view.findViewById<TextView>(R.id.acheteur_telephone)
        //val status = view.findViewById<TextView>(R.id.commande_status)
        //val date = view.findViewById<TextView>(R.id.commande_date)
        val message = view.findViewById<TextView>(R.id.messageAcheteurItemCommande)
        val imgArticle = view.findViewById<ImageView>(R.id.articleImageItemCommande)

        commande?.let {
            articleNom.text = it.article_nom
            //articlePrix.text = "${it.article_prix} ${it.article_devise}"
            acheteurNom.text = it.acheteur_nom
            //acheteurTel.text = it.acheteur_telephone
            //status.text = it.status
            //date.text = it.created_at
            message.text = it.message

            Glide.with(context)
                .load("https://loopcongo.com/${it.article_image}")
                .placeholder(R.drawable.loading)
                .into(imgArticle)
        }

        view.setOnClickListener {
            val intent = Intent(context, DetailCommandeActivity::class.java)
            intent.putExtra("article_nom", commande?.article_nom)
            intent.putExtra("article_prix", commande?.article_prix)
            intent.putExtra("article_devise", commande?.article_devise)
            intent.putExtra("article_description", commande?.article_devise)
            context.startActivity(intent)
        }

        return view
    }
}

