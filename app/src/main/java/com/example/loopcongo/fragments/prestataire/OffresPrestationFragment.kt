package com.example.loopcongo.fragments.prestataire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.loopcongo.R
import com.example.loopcongo.adapters.prestataire.OffrePrestationAdapter
import com.example.loopcongo.models.OffrePrestation

class OffresPrestationFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: OffrePrestationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_offre_prestataire, container, false)

        listView = view.findViewById(R.id.listeOffresPrestationListView)

        val offres = listOf(
            OffrePrestation(
                avatarResId = R.drawable.avatar,
                nomAnnonceur = "Le Monde",
                heure = "1 h",
                texteContenu = "Nous recherchons un maçon pour la construction d’un bâtiment de très haut standing.",
                titre = "Offre pour maçon qualifié"
            ),
            OffrePrestation(
                avatarResId = R.drawable.avatar,
                nomAnnonceur = "Sarah",
                heure = "2 h",
                texteContenu = "Nous recherchons une coiffeuse expérimentée pour notre salon moderne à Kinshasa.",
                titre = "Offre pour coiffeuse pro"
            ),
            OffrePrestation(
                avatarResId = R.drawable.avatar,
                nomAnnonceur = "John",
                heure = "3 h",
                texteContenu = "Garage spécialisé recherche un mécanicien 4x4 pour entretien et réparation.",
                titre = "Offre pour mécanicien 4x4"
            )
        )

        adapter = OffrePrestationAdapter(requireContext(), offres)
        listView.adapter = adapter

        return view
    }
}
