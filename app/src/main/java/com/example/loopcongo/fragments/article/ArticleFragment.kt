package com.example.loopcongo.fragments.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.R
import com.example.loopcongo.adapters.ArticleViewPagerAdapter
import com.example.loopcongo.adapters.articles.ArticleTabsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ArticleFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_article, container, false)

        viewPager = view.findViewById(R.id.fragmentArticleViewPager)
        tabLayout = view.findViewById(R.id.fragmentArticleTabLayout)

        viewPager.adapter = ArticleTabsPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Habillement"
                1 -> "Voitures"
                2 -> "Autres"
                else -> ""
            }
        }.attach()

        return view
    }
}
