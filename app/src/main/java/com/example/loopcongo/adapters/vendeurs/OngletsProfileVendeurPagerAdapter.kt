package com.example.loopcongo.adapters.vendeurs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.profilevendeurs.ProfilVendeursOngletArticleFragment


class OngletsProfileVendeurPagerAdapter(
    activity: FragmentActivity,
    private val vendeurId: Int
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfilVendeursOngletArticleFragment.newInstance(vendeurId)
            1 -> ProfilVendeursOngletArticleFragment.newInstance(vendeurId)
            else -> throw IllegalArgumentException("Position d'onglet invalide")
        }
    }
}
