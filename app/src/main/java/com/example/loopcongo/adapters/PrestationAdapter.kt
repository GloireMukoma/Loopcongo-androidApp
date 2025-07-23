package com.example.loopcongo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.models.Prestation

class PrestationAdapter(private val prestations: List<Prestation>) :
    RecyclerView.Adapter<PrestationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date = view.findViewById<TextView>(R.id.textDate)
        val heure = view.findViewById<TextView>(R.id.textHeure)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prestation, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = prestations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = prestations[position]
        holder.date.text = p.date
        holder.heure.text = p.heure
    }
}
