package com.example.loopcongo.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.vendeurs.OngletListFournisseurFragment
import com.example.loopcongo.fragments.vendeurs.OngletListImmobilierFragment
import com.example.loopcongo.fragments.vendeurs.OngletListVendeurFragment

class TabVendeurPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OngletListImmobilierFragment()
            1 -> OngletListVendeurFragment()
            2 -> OngletListFournisseurFragment()
            else -> OngletListVendeurFragment()
        }
    }
}
