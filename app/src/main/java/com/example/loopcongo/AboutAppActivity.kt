package com.example.loopcongo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat

class AboutAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuClairPrimaryColor)

        val whatsappButton = findViewById<LinearLayout>(R.id.whatsappButton) // Mets l'id de ton LinearLayout
        val phoneNumber = "+243977718960"

        whatsappButton.setOnClickListener {
            try {
                // Crée l'URL pour WhatsApp
                val url = "https://wa.me/${phoneNumber.replace("+", "")}"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "WhatsApp n'est pas installé sur ce téléphone", Toast.LENGTH_SHORT).show()
            }
        }
    }
}