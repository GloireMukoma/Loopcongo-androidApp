package com.example.loopcongo.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.users.FragmentListeImmobilier
import com.example.loopcongo.fragments.users.FragmentListeVendeur

class TabVendeurPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentListeVendeur()
            1 -> FragmentListeImmobilier()
            else -> FragmentListeVendeur()
        }
    }
}
