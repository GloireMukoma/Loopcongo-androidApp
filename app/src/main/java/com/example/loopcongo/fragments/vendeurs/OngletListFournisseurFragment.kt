package com.example.loopcongo.fragments.vendeurs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.VendeurAdapter
import com.example.loopcongo.models.Vendeur

class OngletListFournisseurFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VendeurAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_liste_fournisseur, container, false)
        recyclerView = view.findViewById(R.id.listFourniseurRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val tous = getFakeVendeurs()
        val vendeurs = tous.filter { it.type == "article" }

        adapter = VendeurAdapter(vendeurs)
        recyclerView.adapter = adapter

        return view
    }

    private fun getFakeVendeurs(): List<Vendeur> {
        return listOf(
            Vendeur("Shabani Market", "Articles divers", 23, R.drawable.img, "article"),
            Vendeur("Congo Immo", "Maisons à vendre", 8, R.drawable.images, "immobilier")
        )
    }
}
