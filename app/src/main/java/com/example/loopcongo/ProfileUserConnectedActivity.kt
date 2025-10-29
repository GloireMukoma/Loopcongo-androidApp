package com.example.loopcongo


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.userVendeurConnected.OngletsUserViewPagerAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.User
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ProfileUserConnectedActivity : AppCompatActivity() {

    private lateinit var profileImage: ShapeableImageView
    private lateinit var nameUserConnected: TextView

    private lateinit var telephoneUserConnected: TextView
    private lateinit var descriptionUserConnected: TextView

    private lateinit var nbArticlesPublierUserConnected: TextView
    private lateinit var nbCommandUserConnected: TextView

    private lateinit var nbAbonnerUserConnected: TextView


    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_user_connected)

        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)

        profileImage = findViewById(R.id.profileImageUserConnected)
        nameUserConnected = findViewById(R.id.nameUserConnected)
        telephoneUserConnected = findViewById(R.id.telephoneUserConnected)
        descriptionUserConnected = findViewById(R.id.descriptionUserConnected)

        nbArticlesPublierUserConnected = findViewById(R.id.nbArticlesPublierUserConnected)
        nbCommandUserConnected = findViewById(R.id.nbCommandUserConnected)
        nbAbonnerUserConnected = findViewById(R.id.nbAbonnerUserConnected)


        tabLayout = findViewById(R.id.userConnectedProfiletabLayout)
        viewPager = findViewById(R.id.userConnectedProfileviewPager)

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
                    val response = ApiClient.instance.getUserStats(userId)

                    // Mettre à jour les TextView avec les données
                    nbArticlesPublierUserConnected.text = response.nb_articles.toString()
                    nbCommandUserConnected.text = response.nb_commandes.toString()
                    nbAbonnerUserConnected.text = "0" // à compléter plus tard
                } catch (e: Exception) {
                    e.printStackTrace()
                    nbArticlesPublierUserConnected.text = "--"
                    nbCommandUserConnected.text = "--"
                    nbAbonnerUserConnected.text = "--"
                }
            }
        }

        // on se deconnecte lorsqu'on clique sur l'icon de deconnexion sur l'activité profile user connecté
        val logoutBtnUserConnected = findViewById<ImageView>(R.id.logoutBtnUserConnected)

        logoutBtnUserConnected.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Voulez-vous vraiment vous déconnecter ?")
                .setPositiveButton("Oui") { dialog, _ ->
                    // Coroutine pour supprimer l'utilisateur de la base
                    lifecycleScope.launch {
                        userDao.clearUsers()

                        // Redirection vers MainActivity
                        val intent = Intent(this@ProfileUserConnectedActivity, MainActivity::class.java)
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
                    Glide.with(this@ProfileUserConnectedActivity)
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
        val adapter = OngletsUserViewPagerAdapter(this, userId)
        viewPager.adapter = adapter

        val tabTitles = arrayOf("Articles", "Annonces", "Options")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
