package com.example.loopcongo.adapters.immobiliers

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.article.onglets.*
import com.example.loopcongo.fragments.immobiliers.ImmobilierForAllCityFragment

class ImmobilierViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // Noms pour l’API et labels pour tabs
    val cities = listOf(
        "Lubumbashi",
        "Kolwezi",
        "Kinshasa",
        "Likasi",
        "Autres"
    )
    override fun getItemCount(): Int = cities.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ImmobilierForAllCityFragment()
        fragment.arguments = Bundle().apply {
            putString("city_name", cities[position]) // on envoie la ville (String)
        }
        return fragment
    }
}


