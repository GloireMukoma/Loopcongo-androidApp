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
import com.example.loopcongo.models.ArticleApi

class UserArticleAdapter(context: Context, articles: List<ArticleApi>) :
    ArrayAdapter<ArticleApi>(context, 0, articles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val article = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_article_user_connected_onglet, parent, false)

        val image = view.findViewById<ImageView>(R.id.articleImageUserConnectedOnglet)
        val nomArticle = view.findViewById<TextView>(R.id.articleNomUserConnectedOnglet)
        val prix = view.findViewById<TextView>(R.id.articlePrixUserConnectedOnglet)
        val description = view.findViewById<TextView>(R.id.articleDescUserConnectedOnglet)
        val timeAgo = view.findViewById<TextView>(R.id.articleTimeAgoUserConnectedOnglet)

        nomArticle.text = article?.nom ?: "Nom non disponible"
        prix.text = "${article?.prix ?: "0"} ${article?.devise ?: ""}"
        description.text = article?.about ?: ""

        val url = article?.file_url
        if (!url.isNullOrEmpty()) {
            Glide.with(context)
                .load("https://loopcongo.com/$url")
                .placeholder(R.drawable.shoes)
                .into(image)
        } else {
            image.setImageResource(R.drawable.shoes)
        }

        return view
    }
}
