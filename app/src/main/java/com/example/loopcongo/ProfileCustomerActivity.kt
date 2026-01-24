package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ProfileCustomerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_customer)

    }
}