package com.example.loopcongo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class DetailAnnonceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_annonce)
        supportActionBar?.hide()

        // Récupération des vues
        val userAvatar = findViewById<ShapeableImageView>(R.id.userAvatarAnnonceDetail)
        val username = findViewById<TextView>(R.id.usernameAnnonceDetail)
        val userContact = findViewById<TextView>(R.id.userContactAnnonceDetail)
        val titreAnnonce = findViewById<TextView>(R.id.titreAnnonceDetail)
        val descriptionAnnonce = findViewById<TextView>(R.id.descriptionAnnonceDetail)
        val imageAnnonce = findViewById<ImageView>(R.id.imageAnnonceDetail)

        // Récupération des données depuis Intent
        val titre = intent.getStringExtra("titre")
        val description = intent.getStringExtra("description")
        val image = intent.getStringExtra("image")
        val nomVendeur = intent.getStringExtra("username")
        val contact = intent.getStringExtra("contact")
        val city = intent.getStringExtra("city")
        val fileUrl = intent.getStringExtra("file_url")

        // Remplir les vues
        titreAnnonce.text = titre
        descriptionAnnonce.text = description
        username.text = nomVendeur
        userContact.text = contact
        Glide.with(this).load("https://loopcongo.com/$fileUrl").into(userAvatar)
        Glide.with(this).load("https://loopcongo.com/$image").into(imageAnnonce)

        val userContactText = userContact.text.toString()
        val discuterBtn = findViewById<LinearLayout>(R.id.btnWhatsappAnnonceDetail)

        discuterBtn.setOnClickListener {
            if (userContactText.isNotEmpty()) {
                val message = "Bonjour, je suis intéressé par votre article sur LoopCongo."
                val url = "https://wa.me/$userContactText?text=${Uri.encode(message)}"
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
