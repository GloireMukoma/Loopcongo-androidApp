package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DetailCommandeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_commande)
        supportActionBar?.title = "Detail de la commande"
    }
}