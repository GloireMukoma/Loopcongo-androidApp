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
            window.statusBarColor = ContextCompat.getColor(this, R.color.secondprimaryColor)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.secondprimaryColor)
        }

        // Récupération des données depuis l'Intent
        val vendeurUsername = intent.getStringExtra("vendeurUsername")
        val vendeurContact = intent.getStringExtra("vendeurContact")
        val vendeurCity = intent.getStringExtra("vendeurCity")
        val vendeurDescription = intent.getStringExtra("vendeurDescription")
        val vendeurTypeCompte = intent.getStringExtra("vendeurTypeAccount")
        val vendeurAvatarImg = intent.getStringExtra("vendeurAvatarImg")
        val isSponsoredVendeur = intent.getIntExtra("isSponsoredVendeur", 0)

        findViewById<TextView>(R.id.profileVendeurNbArticlePublie).text = "350"
        findViewById<TextView>(R.id.profileVendeurNbLike).text = "470"
        findViewById<TextView>(R.id.profileVendeurNbAbonner).text = "820"
        findViewById<TextView>(R.id.profileVendeurUsername).text = vendeurUsername
        //findViewById<TextView>(R.id.profileVendeurType).text = vendeurTypeCompte
        findViewById<TextView>(R.id.profileVendeurDescription).text = vendeurDescription
        findViewById<TextView>(R.id.profileVendeurPhone).text = vendeurContact

        val sponsorTextView = findViewById<ImageView>(R.id.profileVendeurBadge) // TextView pour le badge
        if (isSponsoredVendeur == 1) {
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
                1 -> "Infos"
                else -> "Onglet ${position + 1}"
            }
        }.attach()
    }

}
