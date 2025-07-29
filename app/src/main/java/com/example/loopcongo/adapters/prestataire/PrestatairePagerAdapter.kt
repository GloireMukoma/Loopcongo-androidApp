package com.example.loopcongo.adapters.prestataire

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.prestataire.AccueilFragment
import com.example.loopcongo.fragments.prestataire.FilActualiteFragment
import com.example.loopcongo.fragments.prestataire.AutreFragment

class PrestatairePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AccueilFragment()
            1 -> FilActualiteFragment()
            2 -> AutreFragment()
            else -> AccueilFragment()
        }
    }
}
