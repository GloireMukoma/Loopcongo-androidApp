package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.models.BasicResponse
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileServeurActivity : AppCompatActivity() {

    private var serverId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_serveur)
        supportActionBar?.hide()

        // Récupération des données de l'intent
        serverId = intent.getIntExtra("server_id", 0)
        val serverName = intent.getStringExtra("serverName") ?: ""
        val nbMembres = intent.getIntExtra("nbMembres", 0)
        val serverIcon = intent.getStringExtra("icon") ?: ""

        // Références des vues
        val txtName = findViewById<TextView>(R.id.txtProfileServeurName)
        val txtAuteur = findViewById<TextView>(R.id.txtProfileServeurAuteur)
        val txtAbout = findViewById<TextView>(R.id.txtAboutProfileServeur)
        val imgProfileServeur = findViewById<ShapeableImageView>(R.id.imgProfileServeur)
        val btnRejoindre = findViewById<Button>(R.id.btnRejoindreServeur)
        val backButton = findViewById<ImageView>(R.id.backButton)

        // Affichage des données
        txtName.text = serverName
        txtAuteur.text = "$nbMembres membre.s"
        txtAbout.text = "Bienvenue sur le serveur $serverName"

        // Charger l'image du serveur avec Glide
        if (serverIcon.isNotEmpty()) {
            Glide.with(this)
                .load("https://loopcongo.com/$serverIcon")
                //.placeholder(R.drawable) // image par défaut si aucun serveur
                .into(imgProfileServeur)
        }

        // Bouton retour
        backButton.setOnClickListener { finish() }

        // Vérifier si l'utilisateur est déjà membre
        checkServerMembership(serverId) { isMember ->
            if (isMember) {
                btnRejoindre.visibility = View.GONE
            } else {
                btnRejoindre.visibility = View.VISIBLE
                btnRejoindre.setOnClickListener {
                    joinServeur(serverId)
                }
            }
        }
    }

    // ---------------- Méthode pour rejoindre un serveur ----------------
    private fun joinServeur(serverId: Int) {
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()
        val customerDao = db.customerDao()

        lifecycleScope.launch {
            val vendeur = userDao.getUser()
            val client = customerDao.getCustomer()

            var userId = 0
            var userType = ""

            if (vendeur != null) {
                userId = vendeur.id
                userType = "vendeur"
            } else if (client != null) {
                userId = client.id
                userType = "client"
            } else {
                Toast.makeText(
                    this@ProfileServeurActivity,
                    "Aucun utilisateur trouvé",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }

            ApiClient.instance.joinServer(serverId, userId, userType)
                .enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val apiResponse = response.body()!!
                            if (apiResponse.status) {
                                Toast.makeText(
                                    this@ProfileServeurActivity,
                                    "Serveur rejoint avec succès",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Masquer le bouton après rejoindre
                                findViewById<Button>(R.id.btnRejoindreServeur).visibility = View.GONE
                            } else {
                                Toast.makeText(
                                    this@ProfileServeurActivity,
                                    apiResponse.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        Toast.makeText(
                            this@ProfileServeurActivity,
                            "Erreur connexion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    // ---------------- Vérifier si l'utilisateur est membre ----------------
    private fun checkServerMembership(serverId: Int, callback: (Boolean) -> Unit) {
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()
        val customerDao = db.customerDao()

        lifecycleScope.launch {
            val vendeur = userDao.getUser()
            val client = customerDao.getCustomer()

            var userId = 0
            var userType = ""

            if (vendeur != null) {
                userId = vendeur.id
                userType = "vendeur"
            } else if (client != null) {
                userId = client.id
                userType = "client"
            } else {
                callback(false)
                return@launch
            }

            // Appel API pour vérifier la membership
            ApiClient.instance.checkServerMembership(serverId, userId, userType)
                .enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val apiResponse = response.body()!!
                            callback(apiResponse.status) // true = déjà membre
                        } else {
                            callback(false)
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        callback(false)
                    }
                })
        }
    }
}