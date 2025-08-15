package com.example.loopcongo.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.ArticleViewPagerAdapter
import com.example.loopcongo.fragments.article.onglets.ArticleForAllCategoriesFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ArticleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val searchEditText = view.findViewById<EditText>(R.id.searchArticleEditText)
        val tabLayout = view.findViewById<TabLayout>(R.id.fragmentArticleTabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.fragmentArticleViewPager)

        val adapter = ArticleViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.categories[position].second
        }.attach()

        searchEditText.doOnTextChanged { text, start, before, count ->
            val currentFragment = childFragmentManager.findFragmentByTag("f${viewPager.currentItem}")
            if (currentFragment is ArticleForAllCategoriesFragment) {
                currentFragment.loadArticles(text?.toString())
            }
        }

    }
}
