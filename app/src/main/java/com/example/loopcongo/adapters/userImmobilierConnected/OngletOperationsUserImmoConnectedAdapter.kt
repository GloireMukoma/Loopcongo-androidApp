package com.example.loopcongo.adapters.userImmobilierConnected

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R

class OngletOperationsUserImmoConnectedAdapter(
    private val context: Context,
    private val iconResList: List<Int>,
    private val operationNames: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<OngletOperationsUserImmoConnectedAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon: ImageView = view.findViewById(R.id.iconOperation)
        private val title: TextView = view.findViewById(R.id.textOperation)
        private val arrow: ImageView = view.findViewById(R.id.arrowSetting)

        fun bind(iconRes: Int, titleText: String) {
            icon.setImageResource(iconRes)
            title.text = titleText
            itemView.setOnClickListener { onItemClick(titleText) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_operations, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(iconResList[position], operationNames[position])
    }

    override fun getItemCount(): Int = operationNames.size
}
