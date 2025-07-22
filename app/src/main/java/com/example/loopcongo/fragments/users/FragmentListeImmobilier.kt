package com.example.loopcongo.fragments.users

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.VendeurAdapter
import com.example.loopcongo.models.Vendeur

// Page qui affiche la liste des utilisateurs de type immobilier
class FragmentListeImmobilier : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VendeurAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_liste_immobilier, container, false)
        recyclerView = view.findViewById(R.id.userImmoRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tous = getFakeVendeurs()
        val immobiliers = tous.filter { it.type == "immobilier" }

        adapter = VendeurAdapter(immobiliers)
        recyclerView.adapter = adapter

        return view
    }

    private fun getFakeVendeurs(): List<Vendeur> {
        return listOf(
            Vendeur("Congo Immo", "Maisons à vendre", 8, R.drawable.images, "immobilier"),
            Vendeur("Shabani Market", "Articles divers", 23, R.drawable.img, "article")
        )
    }
}
