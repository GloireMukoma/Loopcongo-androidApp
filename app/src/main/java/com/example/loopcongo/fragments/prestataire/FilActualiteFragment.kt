package com.example.loopcongo.fragments.prestataire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.loopcongo.R
import com.example.loopcongo.adapters.prestataire.FilActualitePrestataireAdapter
import com.example.loopcongo.models.FilActualitePrestataire

class FilActualiteFragment : Fragment() {

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_fil_actualite_prestataire, container, false)

        listView = view.findViewById(R.id.listViewFilActuPrestataire)

        val articles = listOf(
            FilActualitePrestataire(R.drawable.avatar, "Jules livingstone", "• 1 h",
                "En direct, François Bayrou premier ministre : Bruno Retailleau a été reçu à Matignon vendredi soir",
                R.drawable.chaussures),
            FilActualitePrestataire(R.drawable.avatar, "Gloire mukoma", "• 2 h",
                "Climat : les prévisions montrent un pic de chaleur inédit ce week-end.",
                R.drawable.shoes),
            FilActualitePrestataire(R.drawable.avatar, "Etienne mulenda", "• 1 h",
                "En direct, François Bayrou premier ministre : Bruno Retailleau a été reçu à Matignon vendredi soir",
                R.drawable.chaussures),
            FilActualitePrestataire(R.drawable.avatar, "Elisée kabamba", "• 2 h",
                "Climat : les prévisions montrent un pic de chaleur inédit ce week-end.",
                R.drawable.shoes),
                    FilActualitePrestataire(R.drawable.avatar, "John lorem", "• 1 h",
            "En direct, François Bayrou premier ministre : Bruno Retailleau a été reçu à Matignon vendredi soir",
            R.drawable.chaussures),
        FilActualitePrestataire(R.drawable.avatar, "Becky mukoma", "• 2 h",
            "Climat : les prévisions montrent un pic de chaleur inédit ce week-end.",
            R.drawable.shoes)
        )

        val adapter = FilActualitePrestataireAdapter(requireContext(), articles)
        listView.adapter = adapter

        return view
    }
}
