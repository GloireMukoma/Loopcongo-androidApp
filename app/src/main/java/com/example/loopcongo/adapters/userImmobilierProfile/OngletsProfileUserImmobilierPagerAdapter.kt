package com.example.loopcongo.adapters.userImmobilierProfile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.profilevendeursImmobilier.OngletAnnonceFragment
import com.example.loopcongo.fragments.profilevendeursImmobilier.OngletImmobilierFragment


class OngletsProfileUserImmobilierPagerAdapter(
    activity: FragmentActivity,
    private val userId: Int
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            // Onglet 1 : les biens immobiliers publiés par l'utilisateur
            0 -> OngletImmobilierFragment.newInstance(userId)

            // Onglet 2 : les annonces immobilières de l'utilisateur (par ex. en location ou vente)
            1 -> OngletAnnonceFragment.newInstance(userId)

            else -> throw IllegalArgumentException("Position d'onglet invalide")
        }
    }
}
