package com.example.loopcongo


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
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

    private lateinit var cityUserConnected: TextView

    private lateinit var telephoneUserConnected: TextView
    private lateinit var descriptionUserConnected: TextView

    private lateinit var nbArticlesPublierUserConnected: TextView
    private lateinit var nbAnnoncesUserConnected: TextView

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

        cityUserConnected = findViewById(R.id.cityUserConnected)

        telephoneUserConnected = findViewById(R.id.telephoneUserConnected)
        descriptionUserConnected = findViewById(R.id.descriptionUserConnected)

        nbArticlesPublierUserConnected = findViewById(R.id.nbArticlesPublierUserConnected)
        nbAnnoncesUserConnected = findViewById(R.id.nbAnnoncesUserConnected)
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
                    nbAnnoncesUserConnected.text = response.nb_annonces.toString()
                    nbAbonnerUserConnected.text = response.nb_abonnes.toString() // à compléter plus tard
                } catch (e: Exception) {
                    e.printStackTrace()
                    nbArticlesPublierUserConnected.text = "--"
                    nbAnnoncesUserConnected.text = "--"
                    nbAbonnerUserConnected.text = "--"
                }
            }
        }

        // on se deconnecte lorsqu'on clique sur l'icon de deconnexion sur l'activité profile user connecté
        val logoutBtnUserConnected = findViewById<ImageView>(R.id.logoutBtnUserConnected)

        logoutBtnUserConnected.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Voulez-vous vraiment vous déconnecter ?")
                .setPositiveButton("Oui") { dialogInterface, _ ->
                    // Coroutine pour supprimer l'utilisateur de la base
                    lifecycleScope.launch {
                        userDao.clearUsers()

                        // Redirection vers MainActivity
                        val intent = Intent(this@ProfileUserConnectedActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    dialogInterface.dismiss()
                }
                .setNegativeButton("Non") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .create()

            // Fond blanc
            //dialog.window?.setBackgroundDrawableResource(android.R.color.white)

            // Changer la couleur du texte du titre et du message
            dialog.setOnShowListener {
                //dialog.findViewById<TextView>(android.R.id.message)?.setTextColor(Color.BLACK)
                //dialog.findViewById<TextView>(android.R.id.title)?.setTextColor(Color.BLACK)
                // Pour les boutons
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.BLUE)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.BLUE)
            }

            dialog.show()
        }

        // Charger l'utilisateur depuis Room
        lifecycleScope.launch {
            val user: User? = userDao.getUser()
            user?.let {
                // Afficher les données dans la vue
                nameUserConnected.text = it.username ?: "Utilisateur"
                telephoneUserConnected.text = it.contact ?: "N/A"

                cityUserConnected.text = it.city ?: "N/A"
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

        // Association TabLayout + ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.ic_article)  // produit ou article
                1 -> tab.setIcon(R.drawable.ic_annonce)  // annonce
                2 -> tab.setIcon(R.drawable.ic_settings) // options ou paramètres
            }
        }.attach()

        // Applique une couleur active/inactive aux icônes
        tabLayout.tabIconTint = ContextCompat.getColorStateList(this, R.color.tab_icon_color)

        // (Optionnel) Modifier la taille des icônes
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val icon = tab?.icon
                icon?.setBounds(0, 0, 60, 60) // taille active
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val icon = tab?.icon
                icon?.setBounds(0, 0, 60, 60) // taille normale
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /*private fun setupViewPager(userId: Int) {
        val adapter = OngletsUserViewPagerAdapter(this, userId)
        viewPager.adapter = adapter

        val tabTitles = arrayOf("Articles", "Annonces", "Options")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }*/
}
