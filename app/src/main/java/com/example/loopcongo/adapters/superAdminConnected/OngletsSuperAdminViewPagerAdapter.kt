package com.example.loopcongo.adapters.superAdminConnected

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.superAdminConnected.*

class OngletsSuperAdminViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val userId: Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OngletAccountsCreatedFragment()
            1 -> OngletArticlesFragment()
            2 -> OngletImmobiliersFragment()
            3 -> OngletWaitingUsersAbonnementFragment()
            else -> Fragment()
        }
    }
}
