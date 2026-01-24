package com.example.loopcongo

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.userImmobilierProfile.OngletsProfileUserImmobilierPagerAdapter
import com.example.loopcongo.adapters.vendeurs.OngletsProfileVendeurPagerAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileVendeurImmobilierActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_profile_vendeur_immobilier)
        supportActionBar?.title = "Profil"

        // Forcer la couleur de la status bar et de la navigation bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        }

        val btnClose = findViewById<ImageView>(R.id.flecheBack)
        btnClose.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // ferme l'activité actuelle pour ne pas revenir avec le bouton "Retour"
        }

        // Récupération des données depuis l'Intent
        val vendeurId = intent.getIntExtra("vendeurId", -1)

        val vendeurUsername = intent.getStringExtra("vendeurUsername")
        val vendeurContact = intent.getStringExtra("vendeurContact")
        val vendeurCity = intent.getStringExtra("vendeurCity")

        val vendeurDescription = intent.getStringExtra("vendeurDescription")
        val vendeurTypeCompte = intent.getStringExtra("vendeurTypeAccount")
        val vendeurAvatarImg = intent.getStringExtra("vendeurAvatarImg")

        // Statistique de l'utilisateur: nb article + nb commande
        val nbImmoPublierProfileVendeurImmo = findViewById<TextView>(R.id.nbImmoPublierProfileVendeurImmo)
        val nbAnnonceProfileVendeurImmo = findViewById<TextView>(R.id.nbAnnonceProfileVendeurImmo)
        val nbAbonnerProfileVendeurImmo = findViewById<TextView>(R.id.nbAbonnerProfileVendeurImmo)

        val btnSubscribe = findViewById<TextView>(R.id.btnSubscribeProfileVendeurImmo)
        btnSubscribe.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("S'abonner")
                .setMessage("Voulez-vous vous abonner à cet utilisateur ?")
                .setPositiveButton("Oui") { dialog, _ ->
                    lifecycleScope.launch {
                        val db = AppDatabase.getDatabase(this@ProfileVendeurImmobilierActivity)
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
                                        this@ProfileVendeurImmobilierActivity,
                                        response.body()?.message ?: "Abonnement réussi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@ProfileVendeurImmobilierActivity,
                                        "Erreur : ${response.code()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@ProfileVendeurImmobilierActivity,
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
                                        this@ProfileVendeurImmobilierActivity,
                                        response.body()?.message ?: "Abonnement réussi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@ProfileVendeurImmobilierActivity,
                                        "Erreur : ${response.code()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@ProfileVendeurImmobilierActivity,
                                    "Erreur : ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // 👇 Aucun compte connecté
                            Toast.makeText(
                                this@ProfileVendeurImmobilierActivity,
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

        if (vendeurId != -1) {
            lifecycleScope.launch {
                try {
                    // ⚙️ Appel réseau sur thread IO
                    val response = withContext(Dispatchers.IO) {
                        ApiClient.instance.getUserImmoStats(vendeurId)
                    }

                    // 🧠 Mise à jour des TextView sur le thread principal
                    nbImmoPublierProfileVendeurImmo.text = response.nb_articles.toString()
                    nbAnnonceProfileVendeurImmo.text = response.nb_annonces.toString()
                    nbAbonnerProfileVendeurImmo.text = response.nb_abonnes.toString() // à compléter plus tard
                } catch (e: Exception) {
                    e.printStackTrace()
                    nbImmoPublierProfileVendeurImmo.text = "--"
                    nbAnnonceProfileVendeurImmo.text = "--"
                    nbAbonnerProfileVendeurImmo.text = "--"
                }
            }
        } else {
            // 🚨 Aucun ID trouvé dans l’intent
            nbImmoPublierProfileVendeurImmo.text = "--"
            nbAnnonceProfileVendeurImmo.text = "--"
            nbAbonnerProfileVendeurImmo.text = "--"
        }


        findViewById<TextView>(R.id.profileVendeurUsername).text = vendeurUsername
        findViewById<TextView>(R.id.profileVendeurDescription).text = vendeurDescription
        findViewById<TextView>(R.id.profileVendeurPhone).text = vendeurContact
        findViewById<TextView>(R.id.profileVendeurCity).text = vendeurCity

        val sponsorImageView = findViewById<ImageView>(R.id.profileVendeurBadge)

        when (intent.getStringExtra("vendeurSubscriptionType")) {
            "Premium" -> {
                sponsorImageView.visibility = View.VISIBLE
                sponsorImageView.setColorFilter(
                    ContextCompat.getColor(this, android.R.color.holo_blue_dark),
                    PorterDuff.Mode.SRC_IN
                )
            }
            "Pro" -> {
                sponsorImageView.visibility = View.VISIBLE
                sponsorImageView.setColorFilter(
                    ContextCompat.getColor(this, R.color.gray),
                    PorterDuff.Mode.SRC_IN
                )
            }
            else -> {
                sponsorImageView.visibility = View.GONE
            }
        }

        val vendeurAvatar = findViewById<ImageView>(R.id.profileVendeurAvatarImage)

        Glide.with(this)
            .load("https://loopcongo.com/$vendeurAvatarImg")
            .placeholder(R.drawable.avatar)
            .into(vendeurAvatar)

        val viewPager = findViewById<ViewPager2>(R.id.profileVendeursviewPager)
        val tabLayout = findViewById<TabLayout>(R.id.profileVendeurstabLayout)


        // Passe l'ID au PagerAdapter
        val pagerAdapter = OngletsProfileUserImmobilierPagerAdapter(this, vendeurId)
        viewPager.adapter = pagerAdapter

        val tabIcons = arrayOf(
            R.drawable.ic_home,    // icône pour Immobiliers
            R.drawable.ic_annonce, // icône pour Annonces
            R.drawable.ic_settings // icône pour Options / Onglet 3
        )

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position in tabIcons.indices) {
                tab.setIcon(tabIcons[position])
            } else {
                tab.text = "Onglet ${position + 1}" // fallback si pas d'icône
            }
        }.attach()

    }

}
