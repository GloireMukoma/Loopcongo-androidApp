package com.example.loopcongo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.adapters.prestataire.PrestatairePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PrestataireActivity : AppCompatActivity() {

    private val tabTitles = arrayOf("Accueil", "Fil d'actualité", "Autre")
    private val tabIcons = arrayOf(
        R.drawable.ic_home,
        R.drawable.ic_alert,
        R.drawable.ic_more
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prestataire)

        val tabLayout = findViewById<TabLayout>(R.id.prestataireTabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.prestataireViewPager)

        val pagerAdapter = PrestatairePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
            tab.setIcon(tabIcons[position])
        }.attach()
    }
}
