package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ListView
import androidx.core.widget.addTextChangedListener
import com.example.loopcongo.adapters.CitizenAdapter
import com.example.loopcongo.models.Citizen

class CitizenActivity : AppCompatActivity() {
    private lateinit var adapter: CitizenAdapter
    private lateinit var citizenList: List<Citizen>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citizen)

        val searchInput = findViewById<EditText>(R.id.searchInput)
        val listView = findViewById<ListView>(R.id.citizenListView)

        // Exemple de données
        citizenList = listOf(
            Citizen("Jean Mulumba", "Electricité", "Compétent en installation et dépannage électrique.", R.drawable.images),
            Citizen("Marie Lufungula", "Plomberie", "Réparations rapides de tuyauterie et sanitaires.", R.drawable.img_1),
            Citizen("Paul Mbemba", "Maçonnerie", "Maçon avec 10 ans d'expérience en construction.", R.drawable.images)
        )

        adapter = CitizenAdapter(this, citizenList)
        listView.adapter = adapter

        // Filtrage lors de la saisie
        searchInput.addTextChangedListener { text ->
            val query = text.toString()
            val filtered = citizenList.filter {
                it.domaine.contains(query, ignoreCase = true) ||
                        it.nom.contains(query, ignoreCase = true)
            }
            adapter.filterList(filtered)
        }

    }
}
