package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.adapters.PrestationAdapter
import com.example.loopcongo.models.Citizen_
import com.example.loopcongo.models.Prestation

class CitizenProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citizen_profile)

        val imageProfile = findViewById<ImageView>(R.id.imageProfile)
        val textNom = findViewById<TextView>(R.id.textNom)
        val textFormation = findViewById<TextView>(R.id.textFormation)
        val textDescription = findViewById<TextView>(R.id.textDescription)
        //val textHoraire = findViewById<TextView>(R.id.textHoraire)

        val citizen = Citizen_(
            "Dagon B",
            "Formation Covid-19",
            "Votre aide-ménagère tous les vendredis",
            "13:30 - 15:30",
            R.drawable.img
        )

        textNom.text = citizen.nom
        textFormation.text = citizen.formation
        textDescription.text = citizen.description
        //textHoraire.text = citizen.horaire
        imageProfile.setImageResource(citizen.photoResId)

        val prestations = listOf(
            Prestation("Vendredi, mai 20 2022", "13:30 - 15:30"),
            Prestation("Vendredi, juin 3 2022", "13:30 - 15:30"),
            Prestation("Vendredi, juin 10 2022", "13:30 - 15:30")
        )

        val recycler = findViewById<RecyclerView>(R.id.prestationRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = PrestationAdapter(prestations)
    }
}
