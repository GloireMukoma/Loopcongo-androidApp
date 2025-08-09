package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.adapters.UserProfileViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_user_connected)
        // Couleur de la status bar (en haut)
        window.statusBarColor = ContextCompat.getColor(this, R.color.secondprimaryColor)

        val tabLayout = findViewById<TabLayout>(R.id.userProfiletabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.userProfileviewPager)

        // Nettoie les tabs précédents (si jamais)
        tabLayout.removeAllTabs()

        val adapter = UserProfileViewPagerAdapter(this)
        viewPager.adapter = adapter

        val tabTitles = arrayOf("Article", "Commande", "Operations")

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }
}