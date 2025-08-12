package com.example.loopcongo.adapters.prestataire

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.PrestationsFragment
import com.example.loopcongo.fragments.article.onglets.AutresFragment
import com.example.loopcongo.fragments.userConnectedProfilOnglets.OngletArticleFragment
import com.example.loopcongo.models.Prestataire
import com.example.loopcongo.profileUserFragments.*

class ProfilePrestataireViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val prestataire: Prestataire
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PrestationsFragment.newInstance(prestataire.publications)
            else -> AutresFragment()
        }
    }
}

