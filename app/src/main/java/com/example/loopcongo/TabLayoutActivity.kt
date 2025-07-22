package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)
        supportActionBar?.hide()

        /*val ar = supportActionBar
        if (ar != null) {
            ar.title = "Articles"
        }*/

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        // Configurer l'adaptateur du ViewPager
        //val adapter = ViewPagerAdapter(this)
        //viewPager.adapter = adapter

        // Lier le TabLayout avec le ViewPager
        /*TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTitle(position) // Définir le titre
        }.attach()*/
    }
}