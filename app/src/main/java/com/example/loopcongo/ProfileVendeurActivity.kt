package com.example.loopcongo

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.vendeurs.OngletsProfileVendeurPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
        val vendeurTypeCompte = intent.getStringExtra("vendeurTypeAccount")
        val vendeurAvatarImg = intent.getStringExtra("vendeurAvatarImg")
        val isCertifiedVendeur = intent.getIntExtra("isCertifiedVendeur", 0)

        val vendeurTotalArticles = intent.getIntExtra("vendeurTotalArticles", 0)
        val vendeurTotalLikes = intent.getIntExtra("vendeurTotalLikes", 0)
        val vendeurNbAbonner = intent.getIntExtra("vendeurNbAbonner", 0)

        findViewById<TextView>(R.id.profileVendeurNbArticlePublie).text = vendeurTotalArticles.toString()
        findViewById<TextView>(R.id.profileVendeurNbLike).text = vendeurTotalLikes.toString()
        findViewById<TextView>(R.id.profileVendeurNbAbonner).text = vendeurNbAbonner.toString()

        findViewById<TextView>(R.id.profileVendeurUsername).text = vendeurUsername
        findViewById<TextView>(R.id.profileVendeurDescription).text = vendeurDescription
        findViewById<TextView>(R.id.profileVendeurPhone).text = vendeurContact
        findViewById<TextView>(R.id.profileVendeurCity).text = vendeurCity

        val sponsorTextView = findViewById<ImageView>(R.id.profileVendeurBadge) // TextView pour le badge
        if (isCertifiedVendeur == 1) {
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

        val vendeurId = intent.getIntExtra("vendeurId", -1) // -1 = valeur par défaut si pas trouvé
        // Passe l'ID au PagerAdapter
        val pagerAdapter = OngletsProfileVendeurPagerAdapter(this, vendeurId)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Articles"
                1 -> "Annonces"
                else -> "Onglet ${position + 1}"
            }
        }.attach()
    }

}
