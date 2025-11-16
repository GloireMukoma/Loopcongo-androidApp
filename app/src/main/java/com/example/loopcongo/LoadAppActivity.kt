package com.example.loopcongo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LoadAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Couleur de la status bar (en haut)
        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        // Couleur de la navigation bar (en bas)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)

        // Délai de 2 secondes
        Handler(Looper.getMainLooper()).postDelayed({
            // Redirection vers MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Empêche de revenir à la SplashScreen
        }, 2000)
    }
}
