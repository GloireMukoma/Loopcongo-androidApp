package com.example.loopcongo.adapters.servers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.ServerCategorie

class ServerCategorieAdapter(
    context: Context,
    private val categories: List<ServerCategorie>
) : ArrayAdapter<ServerCategorie>(context, 0, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_server_categorie, parent, false)

        val txtName: TextView = view.findViewById(R.id.txtName)
        val imgIcon: ImageView = view.findViewById(R.id.imgIcon)

        val categorie = categories[position]
        txtName.text = categorie.name

        Glide.with(context)
            .load(categorie.icon)
            .placeholder(R.drawable.loading)
            .circleCrop()
            .into(imgIcon)

        return view
    }
}
