package com.example.loopcongo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.userImmobilierConnected.OngletsProfileUserImmobilierPagerAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.User
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class UserImmobilierConnectedActivity : AppCompatActivity() {

    private lateinit var profileImage: ShapeableImageView
    private lateinit var nameUserConnected: TextView
    private lateinit var telephoneUserConnected: TextView
    private lateinit var descriptionUserConnected: TextView

    private lateinit var nbBienPublieUserImmoConnected: TextView
    private lateinit var nbCommandUserImmoConnected: TextView
    private lateinit var nbAbonnerUserImmoConnected: TextView

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_user_immobilier_connected)

        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)

        profileImage = findViewById(R.id.profileImageUserImmoConnected)
        nameUserConnected = findViewById(R.id.userNameImmoConnected)
        telephoneUserConnected = findViewById(R.id.telephoneUserImmoConnected)
        descriptionUserConnected = findViewById(R.id.descriptionUserImmoConnected)

        nbBienPublieUserImmoConnected = findViewById(R.id.nbBienPublieUserImmoConnected)
        nbCommandUserImmoConnected = findViewById(R.id.nbCommandUserImmoConnected)
        nbAbonnerUserImmoConnected = findViewById(R.id.nbAbonnerUserImmoConnected)

        tabLayout = findViewById(R.id.userImmoConnectedProfiletabLayout)
        viewPager = findViewById(R.id.userImmoConnectedProfileviewPager)

        // Récupérer la DB et le DAO
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        // Statistique de l'utilisateur: nb article + nb commande
        lifecycleScope.launch {
            val user = userDao.getUser() // méthode à adapter selon ta DAO
            user?.let {
                val userId = it.id

                try {
                    // Requet a l'api pour nous retourne le nb d'article et de commandes
                    // faire sur les articles publiés par un utilisateur
                    val response = ApiClient.instance.getUserImmoStats(userId)

                    // Mettre à jour les TextView avec les données
                    nbBienPublieUserImmoConnected.text = response.nb_articles.toString()
                    nbCommandUserImmoConnected.text = response.nb_commandes.toString()
                    nbAbonnerUserImmoConnected.text = "0" // à compléter plus tard
                } catch (e: Exception) {
                    e.printStackTrace()
                    nbBienPublieUserImmoConnected.text = "--"
                    nbCommandUserImmoConnected.text = "--"
                    nbAbonnerUserImmoConnected.text = "--"
                }
            }
        }

        // on se deconnecte lorsqu'on clique sur l'icon de deconnexion sur l'activité profile user connecté
        val logoutBtnUserConnected = findViewById<ImageView>(R.id.logoutBtnUserImmoConnected)

        logoutBtnUserConnected.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Voulez-vous vraiment vous déconnecter ?")
                .setPositiveButton("Oui") { dialog, _ ->
                    // Coroutine pour supprimer l'utilisateur de la base
                    lifecycleScope.launch {
                        userDao.clearUsers()

                        // Redirection vers MainActivity
                        val intent = Intent(this@UserImmobilierConnectedActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Non") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
                .apply {
                    // Modification des couleurs des boutons
                    getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                    getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                }
        }

        // Charger l'utilisateur depuis Room
        lifecycleScope.launch {
            val user: User? = userDao.getUser()
            user?.let {
                // Afficher les données dans la vue
                nameUserConnected.text = it.nom ?: "Utilisateur"
                telephoneUserConnected.text = it.contact ?: "N/A"
                descriptionUserConnected.text = it.about ?: ""

                // Passer l'id de l'utilisateur afin d'afficher les articles dans le tablayout
                setupViewPager(it.id)

                // Charger l'image de profil avec Glide
                if (!it.file_url.isNullOrEmpty()) {
                    Glide.with(this@UserImmobilierConnectedActivity)
                        .load(it.file_url)
                        .placeholder(R.drawable.ic_person) // ton image par défaut
                        .into(profileImage)
                } else {
                    profileImage.setImageResource(R.drawable.ic_person)
                }
            }
        }
    }
    private fun setupViewPager(userId: Int) {
        val adapter = OngletsProfileUserImmobilierPagerAdapter(this, userId)
        viewPager.adapter = adapter

        // Noms des onglets (personnalisables)
        val tabTitles = arrayOf("Immobiliers", "Annonces", "Options")

        // Sécurise l’accès au tableau (évite crash si le nombre d’onglets change)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position in tabTitles.indices) {
                tab.text = tabTitles[position]
            } else {
                tab.text = "Onglet ${position + 1}"
            }
        }.attach()
    }

}
