package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class BoostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boost)

        val btnContinue: Button = findViewById(R.id.boostBtnContinue)

        btnContinue.setOnClickListener {
            Toast.makeText(this, "Redirection vers le paiement...", Toast.LENGTH_SHORT).show()
            // TODO: rediriger vers la page de paiement ou confirmation
        }
    }
}