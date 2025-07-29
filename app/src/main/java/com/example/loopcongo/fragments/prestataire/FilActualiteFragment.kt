package com.example.loopcongo.fragments.prestataire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.prestataire.FilActualiteAdapter
import com.example.loopcongo.models.FilActualiteItem

class FilActualiteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FilActualiteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_fil_actualite_prestataire, container, false)
        recyclerView = view.findViewById(R.id.recyclerFilActualite)

        val data = listOf(
            FilActualiteItem(R.drawable.shoes, "Top Langages de Programmation – Juillet 2025", 67, 6, 2, "2j"),
            FilActualiteItem(R.drawable.shoes_men, "Nuxt 4 est là ! Quelles sont les nouveautés ?", 26, 3, 6, "2j"),
            FilActualiteItem(R.drawable.chaussures, "Majorana 1 : le transistor du futur est né…", 37, 2, 3, "2j"),
            FilActualiteItem(R.drawable.shoes_men, "Django 6.0 : Ce que la prochaine version nous réserve", 36, 6, 3, "3j"),
            FilActualiteItem(R.drawable.shoes, "Top Langages de Programmation – Juillet 2025", 67, 6, 2, "2j"),
            FilActualiteItem(R.drawable.shoes_men, "Nuxt 4 est là ! Quelles sont les nouveautés ?", 26, 3, 6, "2j"),
            FilActualiteItem(R.drawable.chaussures, "Majorana 1 : le transistor du futur est né…", 37, 2, 3, "2j"),
            FilActualiteItem(R.drawable.shoes_men, "Django 6.0 : Ce que la prochaine version nous réserve", 36, 6, 3, "3j")
        )

        adapter = FilActualiteAdapter(data)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        return view
    }
}
