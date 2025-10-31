package com.example.loopcongo.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.*
import com.example.loopcongo.adapters.articles.ArticleViewPagerAdapter
import com.example.loopcongo.database.*
import com.example.loopcongo.database.Customer
import com.example.loopcongo.database.User
import com.example.loopcongo.fragments.article.onglets.ArticleForAllCategoriesFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import com.example.loopcongo.database.User as DbUser
import com.example.loopcongo.database.Customer as DbCustomer

class ArticleFragment : Fragment() {
    private lateinit var userDao: UserDao
    private lateinit var customerDao: CustomerDao

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

        // Instance de la BD Room
        val db = AppDatabase.getDatabase(requireContext())
        userDao = db.userDao()
        customerDao = db.customerDao()

        // Récupère l'ImageView de l'avatar
        val avatarIconUserConnected = view.findViewById<ImageView>(R.id.avatarImgUserConnectedFrgArticle)

        // Met à jour l'avatar et configure le clic
        lifecycleScope.launch {
            updateAvatarAndListener(avatarIconUserConnected)
        }

        // Icon abonnements
        val iconSubscription = view.findViewById<ImageView>(R.id.subscriptionIcon)
        iconSubscription.setOnClickListener {
            val intent = Intent(requireContext(), SubscriptionActivity::class.java)
            startActivity(intent)
        }

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
    private suspend fun updateAvatarAndListener(avatar: ImageView) {
        // Récupère les comptes connectés
        val user = userDao.getUser()       // vendeur ou immobilier
        val customer = customerDao.getCustomer() // client
        val currentAccount: Any? = user ?: customer

        // Met à jour l'image de l'avatar
        val imageUrl = when (currentAccount) {
            is User -> currentAccount.file_url
            is Customer -> currentAccount.file_url
            else -> null
        }

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .circleCrop()
                .into(avatar)
        } else {
            avatar.setImageResource(R.drawable.ic_person)
        }

        // Configure le listener sur l'avatar
        avatar.setOnClickListener {
            lifecycleScope.launch {
                val latestUser = userDao.getUser()
                val latestCustomer = customerDao.getCustomer()
                val account = latestUser ?: latestCustomer

                val nextActivity = when (account) {
                    is User -> when (account.type_account?.lowercase()) {
                        "vendeur" -> ProfileUserConnectedActivity::class.java
                        "immobilier" -> UserImmobilierConnectedActivity::class.java
                        else -> ProfileUserConnectedActivity::class.java
                    }
                    is Customer -> CustomerConnectedActivity::class.java
                    else -> LoginActivity::class.java
                }

                startActivity(Intent(requireContext(), nextActivity))
            }
        }
    }
}
