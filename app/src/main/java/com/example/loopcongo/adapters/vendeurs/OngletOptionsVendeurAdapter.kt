package com.example.loopcongo.adapters.vendeurs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.loopcongo.R

data class SettingItem(val iconRes: Int, val title: String)

class OngletOptionsVendeurAdapter(context: Context, private val items: List<SettingItem>) :
    ArrayAdapter<SettingItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_options, parent, false)

        val icon = view.findViewById<ImageView>(R.id.iconSetting)
        val title = view.findViewById<TextView>(R.id.textSetting)

        val item = items[position]
        icon.setImageResource(item.iconRes)
        title.text = item.title

        return view
    }
}
