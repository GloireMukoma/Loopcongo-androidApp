package com.example.loopcongo.adapters.userImmobilierConnected

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.vendeurs.SettingItem

class OngletOperationsAdapter(
    context: Context,
    private val items: List<SettingItem>
) : ArrayAdapter<SettingItem>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_operations, parent, false)

        val icon = view.findViewById<ImageView>(R.id.iconOperation)
        val title = view.findViewById<TextView>(R.id.textOperation)

        item?.let {
            icon.setImageResource(it.iconRes)
            title.text = it.title
        }

        return view
    }
}
