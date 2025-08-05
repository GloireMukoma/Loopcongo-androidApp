package com.example.loopcongo.adapters.articles

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.article.onglets.AutresFragment
import com.example.loopcongo.fragments.article.onglets.HabillementFragment
import com.example.loopcongo.fragments.article.onglets.VoitureFragment

class ArticleViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HabillementFragment()
            1 -> VoitureFragment()
            2 -> AutresFragment()
            3 -> VoitureFragment()
            4 -> HabillementFragment()
            5 -> VoitureFragment()
            6 -> AutresFragment()
            else -> HabillementFragment()
        }
    }
}
