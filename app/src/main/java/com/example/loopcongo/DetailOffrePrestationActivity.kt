package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class DetailOffrePrestationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_offre_prestation)

        // Récupération des données passées depuis l'adapter
        val username = intent.getStringExtra("username")
        val date = intent.getStringExtra("date")
        val titre = intent.getStringExtra("titre")
        val description = intent.getStringExtra("description")
        val avatarResId = intent.getIntExtra("avatarResId", R.drawable.avatar)

        // Liaison avec les vues
        findViewById<TextView>(R.id.usernameAnnonceurDetail).text = username
        findViewById<TextView>(R.id.datePublicationDetail).text = date
        findViewById<TextView>(R.id.titreOffreDetail).text = titre
        findViewById<TextView>(R.id.descriptionOffreDetail).text = description
        findViewById<ImageView>(R.id.imgAnnonceurDetail).setImageResource(avatarResId)

        findViewById<Button>(R.id.btnContacterAnnonceur).setOnClickListener {
            Toast.makeText(this, "Contact en cours avec $username", Toast.LENGTH_SHORT).show()
            // ici tu peux lancer une messagerie ou un appel
        }
    }
}