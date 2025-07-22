package com.example.loopcongo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.R
import com.example.loopcongo.adapters.TabVendeurPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FragmentTabVendeur : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab_vendeur, container, false)

        val tabLayout = view.findViewById<TabLayout>(R.id.vendeurTabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.vendeurViewPager)

        val adapter = TabVendeurPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Vendeurs"
                1 -> "Immobiliers"
                else -> ""
            }
        }.attach()

        return view
    }
}
