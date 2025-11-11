package com.example.loopcongo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.loopcongo.database.AppDatabase

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SubscriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boost)

        supportActionBar?.title = "Abonnement"

        val btnContinue: Button = findViewById(R.id.subscriptionBtnContinue)

        btnContinue.setOnClickListener {
            lifecycleScope.launch {
                val user = AppDatabase.getDatabase(this@SubscriptionActivity).userDao().getUser()

                if (user == null) {
                    Toast.makeText(this@SubscriptionActivity, "Utilisateur non trouvé", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val url = "https://loopcongo.com/subscription/${user.id}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }
}
