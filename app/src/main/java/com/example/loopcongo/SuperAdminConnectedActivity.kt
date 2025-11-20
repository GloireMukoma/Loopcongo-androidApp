package com.example.loopcongo

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.superAdminConnected.OngletsSuperAdminViewPagerAdapter
import com.example.loopcongo.adapters.userVendeurConnected.OngletsUserViewPagerAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.User
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class SuperAdminConnectedActivity : AppCompatActivity() {

    private lateinit var badge: ImageView
    private lateinit var profileImage: ShapeableImageView

    private lateinit var nameUserConnected: TextView
    private lateinit var telephoneUserConnected: TextView

    private lateinit var nbAccountsAdminConnected: TextView
    private lateinit var nbArticlesAdminConnected: TextView
    private lateinit var nbAbonnementAdminConnected: TextView

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val SUPER_ADMIN_ID = 1  // <<--- ID fixé ici

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_super_admin_connected)

        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)

        // ---- INITIALISATION DES VUES ----
        profileImage = findViewById(R.id.profileImageUserConnected)
        badge = findViewById(R.id.profileVendeurBadgeUserConnected)

        nameUserConnected = findViewById(R.id.nameUserConnected)
        telephoneUserConnected = findViewById(R.id.telephoneUserConnected)

        nbAccountsAdminConnected = findViewById(R.id.nbAccountsAdminConnected)
        nbArticlesAdminConnected = findViewById(R.id.nbArticlesAdminConnected)
        nbAbonnementAdminConnected = findViewById(R.id.nbAbonnementAdminConnected)

        tabLayout = findViewById(R.id.superAdminConnectedtTabLayout)
        viewPager = findViewById(R.id.superAdminConnectedViewPager)

        // ---- ROOM ----
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        // ---- RÉCUPÉRER LES STATISTIQUES ADMIN ----
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getSuperAdminStats()

                nbAccountsAdminConnected.text = response.nbAccounts.toString()
                nbArticlesAdminConnected.text = response.nbArticles.toString()
                nbAbonnementAdminConnected.text = response.nbAbonnements.toString()

                // Affiche le badge si tu veux
                badge.visibility = View.VISIBLE

            } catch (e: Exception) {
                e.printStackTrace()
                nbAccountsAdminConnected.text = "--"
                nbArticlesAdminConnected.text = "--"
                nbAbonnementAdminConnected.text = "--"
            }
        }

        // ---- LOGOUT ----
        val logoutBtnUserConnected = findViewById<ImageView>(R.id.logoutBtnUserConnected)
        logoutBtnUserConnected.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Voulez-vous vraiment vous déconnecter ?")
                .setPositiveButton("Oui") { d, _ ->
                    lifecycleScope.launch {
                        userDao.clearUsers()

                        val intent = Intent(this@SuperAdminConnectedActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    d.dismiss()
                }
                .setNegativeButton("Non") { d, _ -> d.dismiss() }
                .create()

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(Color.WHITE)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(Color.WHITE)
            }
            dialog.show()
        }

        // ---- CHARGER LES INFOS DU SUPER ADMIN ----
        lifecycleScope.launch {
            val user = userDao.getUser()

            user?.let {
                nameUserConnected.text = it.username ?: "Super Admin"
                telephoneUserConnected.text = it.contact ?: "N/A"

                if (!it.file_url.isNullOrEmpty()) {
                    Glide.with(this@SuperAdminConnectedActivity)
                        .load(it.file_url)
                        .placeholder(R.drawable.ic_person)
                        .into(profileImage)
                } else {
                    profileImage.setImageResource(R.drawable.ic_person)
                }
            }
        }

        // ---- CONFIG TABLAYOUT ----
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = OngletsSuperAdminViewPagerAdapter(this, SUPER_ADMIN_ID)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.ic_users)
                1 -> tab.setIcon(R.drawable.ic_article)
                2 -> tab.setIcon(R.drawable.ic_subscription)
            }
        }.attach()

        tabLayout.tabIconTint = ContextCompat.getColorStateList(this, R.color.tab_icon_color)
    }
}
