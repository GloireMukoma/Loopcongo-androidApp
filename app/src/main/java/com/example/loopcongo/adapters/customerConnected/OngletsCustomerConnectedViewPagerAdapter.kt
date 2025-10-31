package com.example.loopcongo.adapters.customerConnected

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.customerConnected.OngletArticlesCustomerAbonnesFragment
import com.example.loopcongo.fragments.customerConnected.OngletListVendeurCustomerAbonnementFragment

class OngletsCustomerConnectedViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val customerId: Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2 // nombre d'onglets

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OngletListVendeurCustomerAbonnementFragment.newInstance(customerId) // Onglet Abonnements
            1 -> OngletArticlesCustomerAbonnesFragment.newInstance(customerId) // Onglet Articles
            else -> throw IllegalStateException("Position inconnue : $position")
        }
    }
}
