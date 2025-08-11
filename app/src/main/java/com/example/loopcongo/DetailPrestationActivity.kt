package com.example.loopcongo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

class DetailPrestationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_prestation)
        supportActionBar?.title = "Prestation"

        val nom = intent.getStringExtra("prestataire_nom")
        val profession = intent.getStringExtra("prestataire_profession")
        val description = intent.getStringExtra("description")
        val date = intent.getStringExtra("date_publication")
        val photoProfil = intent.getStringExtra("photo_profil")
        val image = intent.getStringExtra("image")

        // Ici tu peux afficher ces données dans tes vues

        findViewById<TextView>(R.id.usernameAuteurDetailPrestation).text = nom
        findViewById<TextView>(R.id.professionDetailPrestation).text = profession
        findViewById<TextView>(R.id.contenuTextDetailPrestation).text = description
        val avatar = findViewById<ImageView>(R.id.auteurDetailPrestationProfilImage)
        val imagePrestation = findViewById<ImageView>(R.id.imagePrestation)

        val whatsappButton = findViewById<LinearLayout>(R.id.contactButtonWhatsappDetailPrestation)
        val numeroWhatsapp = "243971737160"

        // Image profil utilisateur
        Glide.with(this)
            .load("https://loopcongo.com/$photoProfil")
            .placeholder(R.drawable.shoes)
            .into(avatar)

        // Image prestation
        Glide.with(this)
            .load("https://loopcongo.com/$image")
            .placeholder(R.drawable.avatar)
            .into(imagePrestation)

        whatsappButton.setOnClickListener {
            if (!numeroWhatsapp.isEmpty()) {
                val message = "Bonjour, je suis intéressé par votre article sur LoopCongo."
                val url = "https://wa.me/$numeroWhatsapp?text=${Uri.encode(message)}"

                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "WhatsApp n’est pas installé", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Numéro WhatsApp introuvable", Toast.LENGTH_SHORT).show()
            }
        }
}
}