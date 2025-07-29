package com.example.loopcongo

import OngletsProfileVendeurPagerAdapter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.databinding.ActivityProfileVendeurBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileVendeurActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileVendeurBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_vendeur2)
        supportActionBar?.title = "Profil"

        //forcer pour que la barre de notification et d'en bas prenne un couleur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Couleur de la status bar (en haut)
            window.statusBarColor = ContextCompat.getColor(this, R.color.ThreePrimaryColor)
            // Couleur de la navigation bar (en bas)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.primaryColor)
        }

        val viewPager = findViewById<ViewPager2>(R.id.profileVendeursviewPager)
        val tabLayout = findViewById<TabLayout>(R.id.profileVendeurstabLayout)

        val pagerAdapter = OngletsProfileVendeurPagerAdapter(this)
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
