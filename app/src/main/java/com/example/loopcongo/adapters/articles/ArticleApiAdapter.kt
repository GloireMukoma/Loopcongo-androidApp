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

class ArticleApiAdapter(context: Context, articles: List<ArticleApi>) :
    ArrayAdapter<ArticleApi>(context, 0, articles) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val article = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_article_api, parent, false)

        val image = view.findViewById<ImageView>(R.id.articleImage)
        val title = view.findViewById<TextView>(R.id.articleTitle)
        val prix = view.findViewById<TextView>(R.id.articlePrix)
        val timeAgo = view.findViewById<TextView>(R.id.articleTimeAgo)
        // val badge = view.findViewById<TextView>(R.id.articleBadge)

        title.text = article?.nom
        prix.text = "${article?.prix} ${article?.devise}"
        timeAgo.text = article?.created_at

        Glide.with(context)
            .load("https://loopcongo.com/${article?.file_url}")
            .into(image)

        /*if (article?.is_sponsored == 1) {
            badge.visibility = View.VISIBLE
        } else {
            badge.visibility = View.GONE
        }*/

        return view
    }
}
