package com.example.loopcongo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class DetailAnnonceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_annonce)
        supportActionBar?.hide()
        window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuClairPrimaryColor)

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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

        // Recuperer les données utilisateur
        val vendeurId = intent.getIntExtra("vendeurId", 0)
        val vendeurUsername = intent.getStringExtra("vendeurUsername")
        val vendeurContact = intent.getStringExtra("vendeurContact")
        val vendeurCity = intent.getStringExtra("vendeurCity")
        val vendeurDescription = intent.getStringExtra("vendeurDescription")
        val vendeurTypeAccount = intent.getStringExtra("vendeurTypeAccount") // vendeur ou immobilier
        val vendeurAvatarImg = intent.getStringExtra("vendeurAvatarImg")
        val vendeurIsCertified = intent.getBooleanExtra("isCertifiedVendeur", false)
        val vendeurNbAbonner = intent.getIntExtra("vendeurNbAbonner", 0)

        // Redirection vers le profil utilisateur qui a publié l'annonce
        val btnVoirProfil = findViewById<Button>(R.id.btnVoirProfilAnnonceDetail)
        btnVoirProfil.setOnClickListener {

            // Vérifier le type de compte de l'utilisateur
            val type = vendeurTypeAccount?.lowercase()

            val profileIntent = when (type) {
                "vendeur" -> Intent(this, ProfileVendeurActivity::class.java)
                "immobilier" -> Intent(this, ProfileVendeurImmobilierActivity::class.java)
                else -> {
                    Toast.makeText(this, "Type de compte inconnu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // 🔥 Envoyer toutes les infos du vendeur
            profileIntent.putExtra("vendeurId", vendeurId)
            profileIntent.putExtra("vendeurUsername", vendeurUsername)
            profileIntent.putExtra("vendeurContact", vendeurContact)
            profileIntent.putExtra("vendeurCity", vendeurCity)
            profileIntent.putExtra("vendeurDescription", vendeurDescription)
            profileIntent.putExtra("vendeurTypeAccount", vendeurTypeAccount)
            profileIntent.putExtra("vendeurAvatarImg", vendeurAvatarImg)
            profileIntent.putExtra("isCertifiedVendeur", vendeurIsCertified)
            profileIntent.putExtra("vendeurNbAbonner", vendeurNbAbonner)

            startActivity(profileIntent)
        }

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
