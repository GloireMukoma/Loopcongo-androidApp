package com.example.loopcongo.adapters.articles

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.article.onglets.AutresFragment
import com.example.loopcongo.fragments.article.onglets.HabillementFragment
import com.example.loopcongo.fragments.article.onglets.VoitureFragment

class ArticleTabsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HabillementFragment()
            1 -> VoitureFragment()
            2 -> AutresFragment()
            else -> HabillementFragment()
        }
    }
}
