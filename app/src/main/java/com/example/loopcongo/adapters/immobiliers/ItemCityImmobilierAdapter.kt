package com.example.loopcongo.adapters.immobiliers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.ItemCityImmo

class ItemCityImmobilierAdapter(
    private val items: List<ItemCityImmo>
) : RecyclerView.Adapter<ItemCityImmobilierAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageVille: ImageView = view.findViewById(R.id.imageCityItemImmo)
        val nomVille: TextView = view.findViewById(R.id.cityNameItemImmo)
        val nombreBiens: TextView = view.findViewById(R.id.nombreBiensItemImmo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city_immobilier, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nomVille.text = item.cityName
        holder.nombreBiens.text = item.nbImmoPublish

        Glide.with(holder.itemView.context)
            .load(item.imgUrl)
            .into(holder.imageVille)
    }

    override fun getItemCount() = items.size
}
