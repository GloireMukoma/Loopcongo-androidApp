package com.example.loopcongo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.ArticleViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ArticleFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.fragmentArticleTabLayout)
        viewPager = view.findViewById(R.id.fragmentArticleViewPager)

        val adapter = ArticleViewPagerAdapter(this) // this = ArticleFragment

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Habillement"
                1 -> "Voiture"
                2 -> "Telephone"
                3 -> "Electronique"
                4 -> "Chaussures"
                5 -> "Sac"
                6 -> "Autres"
                else -> ""
            }
        }.attach()


    }
}
