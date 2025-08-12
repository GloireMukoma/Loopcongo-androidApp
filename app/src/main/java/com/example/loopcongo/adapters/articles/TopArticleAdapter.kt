package com.example.loopcongo.adapters.articles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.Article

class TopArticleAdapter(context: Context, articles: List<Article>) :
    ArrayAdapter<Article>(context, 0, articles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val article = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_article_api, parent, false)

        val image = view.findViewById<ImageView>(R.id.topArticleImage)
        val user_avatar = view.findViewById<ImageView>(R.id.topArticleUserAvatar)
        val nom = view.findViewById<TextView>(R.id.topArticleNom)
        val prix = view.findViewById<TextView>(R.id.topArticlePrix)
        val auteur = view.findViewById<TextView>(R.id.topArticleAuteur)
        // val badge = view.findViewById<TextView>(R.id.articleBadge)

        nom.text = article?.nom
        prix.text = "${article?.prix} ${article?.devise}"
        auteur.text = article?.user_nom

        Glide.with(context)
            .load("https://loopcongo.com/${article?.file_url}")
            .into(image)

        Glide.with(context)
            .load("https://loopcongo.com/${article?.user_avatar}")
            .into(user_avatar)

        /*if (article?.is_sponsored == 1) {
            badge.visibility = View.VISIBLE
        } else {
            badge.visibility = View.GONE
        }*/

        return view
    }
}
