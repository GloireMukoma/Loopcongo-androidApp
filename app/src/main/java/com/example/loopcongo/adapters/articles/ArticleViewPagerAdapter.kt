package com.example.loopcongo.adapters.articles

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.article.onglets.*

class ArticleViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 7

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HabillementFragment()
            1 -> VoitureFragment()
            2 -> TelephoneFragment()
            3 -> ElectroniqueFragment()
            4 -> ChaussureFragment()
            5 -> SacFragment()
            6 -> AutresFragment()
            else -> HabillementFragment()
        }
    }
}

