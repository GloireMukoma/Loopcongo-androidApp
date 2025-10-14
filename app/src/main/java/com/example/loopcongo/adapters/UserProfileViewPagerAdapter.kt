package com.example.loopcongo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.userConnectedProfilOnglets.OngletArticleFragment
import com.example.loopcongo.fragments.userConnectedProfilOnglets.OngletOptionsFragment
import com.example.loopcongo.profileUserFragments.*

class UserProfileViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val userId: Int
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        OngletArticleFragment.newInstance(userId),
        OngletOptionsFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
