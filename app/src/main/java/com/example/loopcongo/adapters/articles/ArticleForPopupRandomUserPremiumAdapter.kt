package com.example.loopcongo.adapters.articles

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailArticleActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Article

class ArticleForPopupRandomUserPremiumAdapter(
    private val context: Context,
    private var products: List<Article>
) : RecyclerView.Adapter<ArticleForPopupRandomUserPremiumAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.productImage)
        val name: TextView = view.findViewById(R.id.productName)
        val price: TextView = view.findViewById(R.id.productPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.nom
        holder.price.text = product.prix
        Glide.with(context)
            .load("https://loopcongo.com/${product.image}")
            .placeholder(R.drawable.loading)
            .into(holder.img)
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newData: List<Product>) {
        products = newData
        notifyDataSetChanged()
    }
}
