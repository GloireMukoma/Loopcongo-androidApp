package com.example.loopcongo.fragments.vendeurs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.*
import com.example.loopcongo.database.*
import com.example.loopcongo.fragments.VendeurListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class VendeurMainFragment : Fragment() {
    private lateinit var userDao: UserDao
    private lateinit var customerDao: CustomerDao

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val tabTitles = listOf("Vendeur", "Immobilier")
    private val types = listOf("vendeur", "immobilier")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vendeur_main, container, false)
        tabLayout = view.findViewById(R.id.vendeurFragmentabLayout)
        viewPager = view.findViewById(R.id.vendeurFragmentviewPager)

        val searchVendeurInput = view.findViewById<EditText>(R.id.searchVendeurInput)

        val db = AppDatabase.getDatabase(requireContext())
        userDao = db.userDao()
        customerDao = db.customerDao()

        val avatarIconUserConnected = view.findViewById<ImageView>(R.id.avatarImgProfileUserConnected)
        lifecycleScope.launch { updateAvatarAndListener(avatarIconUserConnected) }

        val iconSubscription = view.findViewById<ImageView>(R.id.subscriptionIcon)
        iconSubscription.setOnClickListener {
            startActivity(Intent(requireContext(), SubscriptionActivity::class.java))
        }

        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = types.size
            override fun createFragment(position: Int) = VendeurListFragment.newInstance(types[position])
        }

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        // 🔍 Recherche en temps réel — déclenchée à chaque changement de texte
        searchVendeurInput.doOnTextChanged { text, _, _, _ ->
            val currentFragment =
                childFragmentManager.findFragmentByTag("f${viewPager.currentItem}") as? VendeurListFragment
            currentFragment?.filterVendeursFromApi(text?.toString()?.trim().orEmpty())
        }


        return view
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
