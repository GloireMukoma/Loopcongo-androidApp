package com.example.loopcongo

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.vendeurs.OngletsProfileVendeurPagerAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileVendeurActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_vendeur)
        supportActionBar?.title = "Profil"

        // Forcer la couleur de la status bar et de la navigation bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        }

        // Récupération des données depuis l'Intent
        val vendeurUsername = intent.getStringExtra("vendeurUsername")
        val vendeurContact = intent.getStringExtra("vendeurContact")
        val vendeurCity = intent.getStringExtra("vendeurCity")

        val vendeurDescription = intent.getStringExtra("vendeurDescription")
        val vendeurAvatarImg = intent.getStringExtra("vendeurAvatarImg")
        val isCertifiedVendeur = intent.getStringExtra("isCertifiedVendeur")

        val vendeurId = intent.getIntExtra("vendeurId", -1)
        val vendeurTypeCompte = intent.getStringExtra("vendeurTypeAccount")

        val subscribeUserBtn = findViewById<Button>(R.id.btnSubscribeProfileVendeur)

        subscribeUserBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("S'abonner")
                .setMessage("Voulez-vous vous abonner à cet utilisateur ?")
                .setPositiveButton("Oui") { dialog, _ ->
                    lifecycleScope.launch {
                        val db = AppDatabase.getDatabase(this@ProfileVendeurActivity)
                        val userDao = db.userDao()
                        val customerDao = db.customerDao()

                        val user = userDao.getUser()        // vendeur connecté ?
                        val customer = customerDao.getCustomer() // client connecté ?

                        if (user != null) {
                            // 👇 Cas : utilisateur vendeur connecté
                            val userType = "vendeur"

                            try {
                                val response = ApiClient.instance.saveUserAbonnement(
                                    userType,
                                    user.id,
                                    vendeurId,
                                    vendeurTypeCompte ?: ""
                                )

                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        this@ProfileVendeurActivity,
                                        response.body()?.message ?: "Abonnement réussi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@ProfileVendeurActivity,
                                        "Erreur : ${response.code()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@ProfileVendeurActivity,
                                    "Erreur : ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else if (customer != null) {
                            // 👇 Cas : client connecté
                            val userType = "customer"

                            try {
                                val response = ApiClient.instance.saveUserAbonnement(
                                    userType,
                                    customer.id,
                                    vendeurId,
                                    vendeurTypeCompte ?: ""
                                )

                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        this@ProfileVendeurActivity,
                                        response.body()?.message ?: "Abonnement réussi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@ProfileVendeurActivity,
                                        "Erreur : ${response.code()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@ProfileVendeurActivity,
                                    "Erreur : ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // 👇 Aucun compte connecté
                            Toast.makeText(
                                this@ProfileVendeurActivity,
                                "Aucun compte connecté.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Non") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        // Statistique de l'utilisateur: nb article + nb commande
        val profileVendeurNbArticlePublie = findViewById<TextView>(R.id.profileVendeurNbArticlePublie)
        val profileVendeurNbAnnonce = findViewById<TextView>(R.id.profileVendeurNbAnnonce)
        val profileVendeurNbAbonner = findViewById<TextView>(R.id.profileVendeurNbAbonner)

        if (vendeurId != -1) {
            lifecycleScope.launch {
                try {
                    // Appel réseau sur thread IO
                    val response = withContext(Dispatchers.IO) {
                        ApiClient.instance.getUserStats(vendeurId)
                    }

                    // 🧠 Mise à jour des TextView sur le thread principal
                    profileVendeurNbArticlePublie.text = response.nb_articles.toString()
                    profileVendeurNbAnnonce.text = response.nb_annonces.toString()
                    profileVendeurNbAbonner.text = response.nb_abonnes.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                    profileVendeurNbArticlePublie.text = "--"
                    profileVendeurNbAnnonce.text = "--"
                    profileVendeurNbAbonner.text = "--"
                }
            }
        } else {
            // 🚨 Aucun ID trouvé dans l’intent
            profileVendeurNbArticlePublie.text = "--"
            profileVendeurNbAnnonce.text = "--"
            profileVendeurNbAbonner.text = "--"
        }


        findViewById<TextView>(R.id.profileVendeurUsername).text = vendeurUsername
        findViewById<TextView>(R.id.profileVendeurDescription).text = vendeurDescription
        findViewById<TextView>(R.id.profileVendeurPhone).text = vendeurContact
        findViewById<TextView>(R.id.profileVendeurCity).text = vendeurCity

        val sponsorTextView = findViewById<ImageView>(R.id.profileVendeurBadge) // TextView pour le badge
        if (isCertifiedVendeur == "1") {
            sponsorTextView.visibility = View.VISIBLE
        } else {
            sponsorTextView.visibility = View.GONE
        }

        val vendeurAvatar = findViewById<ImageView>(R.id.profileVendeurAvatarImage)

        Glide.with(this)
            .load("https://loopcongo.com/$vendeurAvatarImg")
            .placeholder(R.drawable.avatar)
            .into(vendeurAvatar)

        val viewPager = findViewById<ViewPager2>(R.id.profileVendeursviewPager)
        val tabLayout = findViewById<TabLayout>(R.id.profileVendeurstabLayout)

        // Passe l'ID au PagerAdapter
        val pagerAdapter = OngletsProfileVendeurPagerAdapter(this, vendeurId)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.ic_article)
                1 -> tab.setIcon(R.drawable.ic_annonce)
            }
        }.attach()

        //Modifier la taille des icônes après la liaison
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getTabAt(i)?.view as? ViewGroup)
            tab?.let { tabView ->
                val iconView = tabView.findViewById<ImageView>(com.google.android.material.R.id.icon)
                iconView?.layoutParams?.apply {
                    width = 60 // largeur en pixels ou utiliser des dp convertis
                    height = 60 // hauteur en pixels
                }
                iconView?.requestLayout()
            }
        }



        /*TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Articles"
                1 -> "Annonces"
                else -> "Onglet ${position + 1}"
            }
        }.attach()*/
    }

}
