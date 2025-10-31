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
import com.example.loopcongo.adapters.immobiliers.ImmobilierGridAdapter
import com.example.loopcongo.adapters.immobiliers.ImmobilierViewPagerAdapter
import com.example.loopcongo.database.*
import com.example.loopcongo.fragments.article.onglets.ArticleForAllCategoriesFragment
import com.example.loopcongo.fragments.immobiliers.ImmobilierForAllCityFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ImmobilierFragment : Fragment() {
    private lateinit var userDao: UserDao
    private lateinit var customerDao: CustomerDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_immobilier, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchEditText = view.findViewById<EditText>(R.id.searchArticleEditText)
        val tabLayout = view.findViewById<TabLayout>(R.id.fragmentImmobilierTabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.fragmentImmobilierViewPager)

        // Instance de la BD Room
        val db = AppDatabase.getDatabase(requireContext())
        userDao = db.userDao()
        customerDao = db.customerDao()

        val pagerAdapter = ImmobilierViewPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // Récupère l'ImageView de l'avatar
        val avatarIconUserConnected = view.findViewById<ImageView>(R.id.avatarImgUserConnectedFrgImmo)

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

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = pagerAdapter.cities[position]
        }.attach()

        // 🔹 Recherche par quartier corrigée
        searchEditText.doOnTextChanged { text, _, _, _ ->
            val currentFragment = childFragmentManager.fragments
                .firstOrNull { it is ImmobilierForAllCityFragment } as? ImmobilierForAllCityFragment
            currentFragment?.loadImmos(text?.toString())
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
