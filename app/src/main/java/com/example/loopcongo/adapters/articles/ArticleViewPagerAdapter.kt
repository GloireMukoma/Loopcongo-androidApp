package com.example.loopcongo.adapters.articles

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.article.onglets.*

class ArticleViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // IDs pour l’API + noms pour affichage
    val categories = listOf(
        Pair(1, "Telephones"),
        Pair(2, "Ordinateurs"),
        Pair(3, "Habits homme"),
        Pair(4, "Habits femme"),
        Pair(5, "Chaussures"),
        Pair(6, "Sac"),
        Pair(7, "Babouches"),
        Pair(8, "Vehicules"),
        Pair(9, "Electroniques"),
        Pair(10, "Autres")
    )

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ArticleForAllCategoriesFragment()
        fragment.arguments = Bundle().apply {
            putInt("categorie_id", categories[position].first) // envoie l’ID
        }
        return fragment
    }
}

/*class ArticleViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
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
}*/

